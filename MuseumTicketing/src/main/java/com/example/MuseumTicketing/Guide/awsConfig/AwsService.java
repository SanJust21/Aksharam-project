package com.example.MuseumTicketing.Guide.awsConfig;

import org.springframework.beans.factory.annotation.Autowired;

public class AwsService {
    @Autowired
    private AwsProperties awsProperties;

    public void setValuesOfAws(){
        String accessKey = awsProperties.getAccessKey();
        String secretKey = awsProperties.getSecretKey();
        String region = awsProperties.getRegion();
        String bucketName = awsProperties.getS3BucketName();

    }
}
