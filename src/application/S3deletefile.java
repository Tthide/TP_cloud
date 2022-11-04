package application;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;


public class S3deletefile {
	
	public static void deleteFile(String bucket_name, String file_name) {
		Region region = Region.US_EAST_1;

	    S3Client s3 = S3Client.builder().region(region).build();
		
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket_name)
                .key(file_name)
                .build();

        s3.deleteObject(deleteObjectRequest);
        
        System.out.println("File deleted");

	}

}
