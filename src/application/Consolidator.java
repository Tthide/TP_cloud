package application;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

public class Consolidator {

	public static String ReadCSV(String bucketName, String date) throws IOException {

		// Get file
		Regions region = Regions.US_EAST_1;
		S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
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

	public static void main(String[] args) throws IOException { // input : ( String bucketName, String filePath)

		String csv_data=ReadCSV(args[0],args[1]);
		System.out.print(csv_data);

	}

}
