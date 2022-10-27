package application;

import application.GetMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.sqs.model.Message;

public class Worker {
	
	public static String GetObjectNameFromQueue(String QueueUrl, Region region) {
		
		String[] args =  {QueueUrl};
		List<Message> messages = GetMessage.SnsGetMessage(QueueUrl ,region);
		String String_message = messages.get(0).body().toString();
		
		
		return String_message;
	}
	
	
	
	
	public void ReadCSV(String file_name, String bucket_name) {
		
		//Get file
		Region region = Region.US_EAST_1;

	    S3Client s3 = S3Client.builder().region(region).build();

	    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket_name)
                .key(file_name)
                .build();

        s3.getObject(getObjectRequest);
        // 
		
	}
	
	public static void main(String[] args) { // input : ( String bucketName, String filePath)
		Region region = Region.US_EAST_1;
		String QueueUrl="https://sqs.us-east-1.amazonaws.com/446929553961/messaging-app-queue";
		
		String NextItemInQueue = GetObjectNameFromQueue(QueueUrl,region);
		System.out.println(NextItemInQueue);
	}


}
