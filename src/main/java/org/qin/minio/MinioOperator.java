package org.qin.minio;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @title: MinioTemplate
 * @decription:
 * @author: liuqin
 * @date: 2020/6/4 10:21
 */
@Component
@Slf4j
@SuppressWarnings("unchecked")
public class MinioOperator {

    @Autowired
    private MinioProperties minioProperties;

    private MinioClient minioClient;


    public MinioOperator() {

    }


    /**
     * @return
     * @descripttion 快速处理大量耗时较短的任务
     * @parms
     * @author liuqin
     * @date 2020/6/5
     */
    public ExecutorService newCachedThreadPool() {

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("minio-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        return singleThreadPool;
    }


    /**
     * Bucket Operations
     */

    public boolean createBucket(String bucketName) {
        try {
            MinioClient client = getMinioClient();
            if (!client.bucketExists(bucketName)) {
                client.makeBucket(bucketName);

            }
            return true;
        } catch (Throwable throwable) {
            log.error(throwable.getCause().toString());
            return false;
        }

    }


    public List<Bucket> getAllBuckets() throws Throwable {
        return getMinioClient().listBuckets();
    }


    public Optional<Bucket> getBucket(String bucketName) throws Throwable {
        return getMinioClient().listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }


    public boolean removeBucket(String bucketName) {
        try {
            getMinioClient().removeBucket(bucketName);
            return true;
        } catch (Throwable throwable) {
            log.error("removeBucket:" + throwable.getMessage());
            return false;
        }

    }


    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        List objectList = new ArrayList();
        Iterable<Result<Item>> objectsIterator = getMinioClient().listObjects(bucketName, prefix, recursive);
        objectsIterator.forEach(i -> {
            try {
                objectList.add(new MinioItem(i.get()));
            } catch (Throwable e) {
                log.error("getAllObjectsByPrefix:" + e.getMessage());
                new Throwable(e);
            }
        });
        return objectList;
    }


    /**
     * Object operations
     */

    public String getobjecturl(String bucketName, String objectName, Integer expires) {
        try {
            return getMinioClient().presignedGetObject(bucketName, objectName, expires);
        } catch (Throwable throwable) {
            log.error("getObjectURL:" + throwable.getMessage());
            return null;
        }

    }


    public void saveObject(String bucketName, String objectName, InputStream stream, long size, String contentType) throws Throwable {
        getMinioClient().putObject(bucketName, objectName, stream, size, contentType);
    }


    /**
     * @return
     * @descripttion
     * @parms
     * @author liuqin
     * @date 2020/6/4
     */

    public boolean putString(String bucketName, String objectName, String json) {

        try {
            long l = System.currentTimeMillis();
            this.createBucket(bucketName);
            StringBuilder builder = new StringBuilder();
            builder.append(json);
            ByteArrayInputStream bais = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));
            // 创建对象
            getMinioClient().putObject(bucketName, objectName, bais, bais.available(), "application/octet-stream");
            bais.close();
            long end = System.currentTimeMillis();
            long l1 = end - l;
            log.info("putString=>耗时:" + l1);
            return true;
        } catch (Throwable throwable) {
            log.error("putString:" + throwable.getMessage());
            return false;
        }

    }


    public String getString(String bucketName, String objectName) {
        try {
            Future<String> submit = (Future<String>) this.newCachedThreadPool().submit(() -> {
                this.getObjectString(bucketName, objectName);
            });
            return submit.get();

        } catch (Throwable throwable) {
            log.error("getString:" + throwable.getMessage());
            return null;
        }
    }


    private String getObjectString(String bucketName, String objectName) {
        try {
            long l = System.currentTimeMillis();
            InputStream inputStream = getMinioClient().getObject(bucketName, objectName);
            if (inputStream != null) {
                final int bufferSize = 1024;
                final char[] buffer = new char[bufferSize];
                final StringBuilder out = new StringBuilder();
                Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                for (; ; ) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0) {
                        break;
                    }
                    out.append(buffer, 0, rsz);
                }
                long end = System.currentTimeMillis();
                long l1 = end - l;
                log.info("getString=>耗时:" + l1);
                return out.toString();
            }
            return null;

        } catch (Throwable throwable) {
            log.error("getString:" + throwable.getMessage());
            return null;
        }
    }


    public ObjectStat getObjectInfo(String bucketName, String objectName) {
        try {
            return getMinioClient().statObject(bucketName, objectName);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            return null;
        }

    }


    public boolean removeObject(String bucketName, String objectName) {
        try {
            getMinioClient().removeObject(bucketName, objectName);
            return true;
        } catch (Throwable throwable) {
            log.error("removeObject:" + throwable.getMessage());
            return false;
        }

    }


    /**
     * Gets a Minio client
     *
     * @return an authenticated Amazon S3 client
     */
    public MinioClient getMinioClient() {
        return this.getMinioClientInstance();
    }


    private MinioClient getMinioClientInstance() {
        try {
            String http = minioProperties.getUrl();
            if (this.minioClient == null) {
                this.minioClient = new MinioClient(http, minioProperties.getAccessKey(), minioProperties.getSecretKey());
            }
            return this.minioClient;
        } catch (Throwable throwable) {
            log.error("endpoint:" + minioProperties.getUrl() + ",accessKey:" + minioProperties.getAccessKey() + ",secretKey:" + minioProperties.getAccessKey());
            log.error("getMinioClient:" + throwable.getMessage());
            return null;
        }
    }

}
