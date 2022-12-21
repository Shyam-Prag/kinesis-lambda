package org.example.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;

public class AwsKinesisClient {

    public static final  String AWS_ACCESS_KEY = "aws.accessKeyId";
    public static final  String AWS_SECRET_KEY = "aws.secretKey";

    static {
        System.setProperty(AWS_ACCESS_KEY, ""); //insert aws access key
        System.setProperty(AWS_SECRET_KEY, ""); // insert aws secret access key
    }

    public static AmazonKinesis getKinesisClient(){
        return AmazonKinesisClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1)
                .build();
    }


}
