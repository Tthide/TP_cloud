package emse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3filepop {
	
	  public static void listBucketObjects(S3Client s3, String bucketName ) {

	        try {
	            ListObjectsRequest listObjects = ListObjectsRequest
	                .builder()
	                .bucket(bucketName)
	                .build();

	            ListObjectsResponse res = s3.listObjects(listObjects);
	            List<S3Object> objects = res.contents();
	            for (S3Object myValue : objects) {
	                System.out.print("\n The name of the key is " + myValue.key());
	                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
	                System.out.print("\n The owner is " + myValue.owner());
	            }

	        } catch (S3Exception e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	    }

	    //convert bytes to kbs.
	    private static long calKb(Long val) {
	        return val/1024;
	    }
	    
	  
	
	
	
	public static String filepop(String bucket_name, String file_name) {
		Region region = Region.US_EAST_1;

	    S3Client s3 = S3Client.builder().region(region).build();

	    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket_name)
                .key(file_name)
                .build();

        s3.getObject(getObjectRequest);
		
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket_name)
                .key(file_name)
                .build();

        s3.deleteObject(deleteObjectRequest);

		
		
		
		return "";
	}

}
