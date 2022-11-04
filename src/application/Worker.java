package application;

import application.GetMessage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.util.IOUtils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.sqs.model.Message;

public class Worker {

	public static String GetObjectNameFromQueue(String QueueUrl, Region region) {

		String[] args = { QueueUrl };
		List<Message> messages = GetMessage.SnsGetMessage(QueueUrl, region);
		String String_message = messages.get(0).body().toString();

		return String_message;
	}

	public static String ReadCSV(String bucketName, String date){

		// Get file
		Regions region = Regions.US_EAST_1;
		S3Object fullObject = null;
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region).build();

		
		fullObject = s3Client.getObject(new GetObjectRequest(bucketName, date));
		//

		// Convert Input into String
		try (S3ObjectInputStream data = fullObject.getObjectContent()) {
			return IOUtils.toString(data);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		//
	}

	public static void main(String[] args) { // input : none
		Region region = Region.US_EAST_1;
		String QueueUrl = "https://sqs.us-east-1.amazonaws.com/446929553961/Tp_cloud_Queue";
		

		//get object in queue
		String NextItemInQueue = GetObjectNameFromQueue(QueueUrl, region);
		
		String[] items = NextItemInQueue.split(";");
		String bucketNameWorker = items[0];
		String filename=items[1];
		System.out.println(bucketNameWorker);
		System.out.println(filename);
		
		try {
			
			String csv_data=ReadCSV(bucketNameWorker,filename);
			
			System.out.print(csv_data);
			

			}
			catch (S3Exception e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
			}

		//deleting temporary csv file in worker bucket
		
		S3deletefile.deleteFile(bucketNameWorker, filename);
		
		
		// code de steve

		// Uploading file to consolidator bucket

		String bucketNameConsolidator = "consolidatorbucket01";

		try {
			S3uploadfile.main(new String[] { bucketNameConsolidator, /* remplacer par le nom de la variable */ "15" });
			SendMessage.main(new String[] { QueueUrl, bucketNameConsolidator, "15" });

		} catch (S3Exception e) {
			// System.err.println(e.awsErrorDetails().errorMessage());
			// System.exit(1);

		}
	}

}
