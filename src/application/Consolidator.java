package application;

import java.io.IOException;

import java.io.UncheckedIOException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import software.amazon.awssdk.services.s3.model.S3Exception;

public class Consolidator {

	public static String ReadCSV(String bucketName, String date) throws IOException {

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

	public static void main(String[] args) throws IOException { // input : ( String date)

		String bucketName = "consolidatorbucket01";
		String date=args[0];
		
		
		try {
		String csv_data=ReadCSV(bucketName,date);
		System.out.print(csv_data);

		}
		catch (S3Exception e) {
            //System.err.println(e.awsErrorDetails().errorMessage());
            //System.exit(1);
		}
		
		
	}
	

}
