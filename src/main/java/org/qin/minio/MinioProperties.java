package org.qin.minio;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @title: MinioProperties
 * @description:
 * @author: liuqin
 * @date: 2020/6/4 9:28
 */
@Data
@Component
@ToString
public class MinioProperties {

    @Value("${minio.endpoint}")
    private String url;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

}