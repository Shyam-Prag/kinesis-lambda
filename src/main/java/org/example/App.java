package org.example;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.aws.AwsKinesisClient;
import org.example.model.Order;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App 
{
    List<String> productList = new ArrayList<>();
    Random random = new Random();



    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app = new App();
        app.populateProductList();
        //steps

        // 1. get client
        AmazonKinesis kinesisClient = AwsKinesisClient.getKinesisClient();
        // 2. PutRecordRequest
        List<PutRecordsRequestEntry> requestEntryList = app.getRecordsRequestList();

        PutRecordsRequest recordsRequest = new PutRecordsRequest();
        recordsRequest.setStreamName("produce-to-Lambda");
        recordsRequest.setRecords(requestEntryList);
        // 3. putRecord or putRecords - 500 records
        PutRecordsResult result = kinesisClient.putRecords(recordsRequest);
        System.out.println(result);
    }

    private List<PutRecordsRequestEntry> getRecordsRequestList(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<PutRecordsRequestEntry> putRecordsRequestEntries = new ArrayList<>();
        for (Order order: getOrderList()){
            PutRecordsRequestEntry requestEntry = new PutRecordsRequestEntry();
            requestEntry.setData(ByteBuffer.wrap(gson.toJson(order).getBytes()));
            requestEntry.setPartitionKey(UUID.randomUUID().toString());
            putRecordsRequestEntries.add(requestEntry);
        };

        return putRecordsRequestEntries;
    }

    private List<Order> getOrderList(){
        List<Order> orders = new ArrayList<>();
        for(int i =0; i< 500; i++){
            Order order = new Order();
            order.setOrderId(random.nextInt());
            order.setProduct(productList.get(random.nextInt(productList.size())));
            order.setQuantity(random.nextInt(20));
            orders.add(order);
        };


        return orders;
    };

    private void populateProductList(){
        productList.add("shirt");
        productList.add("T-shirt");
        productList.add("shorts");
        productList.add("tie");
        productList.add("shoes");
        productList.add("sandals");
        productList.add("hat");
    }

}
