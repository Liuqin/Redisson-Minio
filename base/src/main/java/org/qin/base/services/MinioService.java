package org.qin.base.services;


import lombok.extern.slf4j.Slf4j;
import org.qin.base.minio.MinioItem;
import org.qin.base.minio.MinioOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @title: MinioService
 * @decription:
 * @author: liuqin
 * @date: 2020/6/4 10:45
 */
@Service
@Slf4j
public class MinioService {

    @Autowired
    private MinioOperator minioOperator;

    public boolean doMinioService() {
        boolean isCreated = minioOperator.createBucket("demo1");
        String json = "{ \"people\": [\n" +
                "\n" +
                "{ \"firstName\": \"Brett\", \"lastName\":\"McLaughlin\", \"email\": \"aaaa\" },\n" +
                "\n" +
                "{ \"firstName\": \"Jason\", \"lastName\":\"Hunter\", \"email\": \"bbbb\"},\n" +
                "\n" +
                "{ \"firstName\": \"Elliotte\", \"lastName\":\"Harold\", \"email\": \"cccc\" }\n" +
                "\n" +
                "]}";
        boolean save = minioOperator.putString("log", "liu/2020", json);
        if (save) {
            String str = minioOperator.getString("log", "liu/2020");

            str = minioOperator.getString("log", "liu/2020");

            str = minioOperator.getString("log", "liu/2020");

            str = minioOperator.getString("log", "liu/2020");

            str = minioOperator.getString("log", "liu/2020");

            str = minioOperator.getString("log", "liu/2020");

            List<MinioItem> logs = minioOperator.getAllObjectsByPrefix("log", "liu/2020", false);
            SimpleDateFormat formatInstance = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (MinioItem l : logs) {
                log.info("updated:=>" + formatInstance.format(l.getLastModified()));
            }

        }


        return isCreated;
    }

}
