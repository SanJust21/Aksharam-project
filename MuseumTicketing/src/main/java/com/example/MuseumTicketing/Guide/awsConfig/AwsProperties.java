package com.example.MuseumTicketing.Guide.awsConfig;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String s3BucketName;



    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    @Bean
    public AmazonS3 S3Client(){
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey,secretKey);
        AWSCredentials credentials =new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint,region))
                .build();
    }
}
