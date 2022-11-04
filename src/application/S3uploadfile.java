package application;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

public class S3uploadfile {
	
	//check whether the object already exists in the bucket
	public static int IsObjectInBucket(S3Client s3, String bucketName,String fileName ) {
		List<S3Object> objects = null;
		
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            objects = res.contents();
            
        } catch (S3Exception e) {
            //System.err.println(e.awsErrorDetails().errorMessage());
            //System.exit(1);
        	return 1;
        }
        
        
        if(objects.contains(fileName)) {
	    	return 1;
	    }
        return 0;
    }
	
	 private static ByteBuffer getRandomByteBuffer(int size) {
	        byte[] b = new byte[size];
	        new Random().nextBytes(b);
	        return ByteBuffer.wrap(b);
	    }
	
	
	// creates a bucket if it doesn't exists
	 public static void createBucket( S3Client s3Client, String bucketName) {

	        try {
	            S3Waiter s3Waiter = s3Client.waiter();
	            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
	                .bucket(bucketName)
	                .build();

	            s3Client.createBucket(bucketRequest);
	            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
	                .bucket(bucketName)
	                .build();

	            // Wait until the bucket is created and print out the response.
	            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
	            waiterResponse.matched().response().ifPresent(System.out::println);
	            System.out.println(bucketName +" is ready");

	        } catch (S3Exception e) {
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	    }
	 
	 public void S3CreateFolder(String bucketName, String folderName) {
			
         
	        S3Client client = S3Client.builder().build();
	         
	        PutObjectRequest request = PutObjectRequest.builder()
	                        .bucket(bucketName).key(folderName).build();
	         
	        client.putObject(request, RequestBody.empty());
	         
	        System.out.println("Folder " + folderName + " is ready.");     
	    }
	 

	 
	 
	 //polymorphism in case the user don't put a new name for the uploaded file
	 public static void upload_file(S3Client s3, String bucketName,String filePath) {
		 File f = new File(filePath);
		 String fileName=f.getName();
		 fileName = fileName.substring(0, 10) + "/" //replace last "-" by "/" in order to create a folder in
				 //S3 bucket
	              + fileName.substring(10 + 1);
		 
		 
		 ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
			        .build();
			    ListBucketsResponse listBucketResponse = s3.listBuckets(listBucketsRequest);
			    
			    if(!listBucketResponse.buckets().contains(bucketName)) {
			    	createBucket(s3,bucketName);
			    }
			    
			    
			    
				PutObjectRequest objectRequest = PutObjectRequest.builder()
		                .bucket(bucketName)
		                .key(fileName)
		                .build();
			    Path path = Paths.get(filePath);
			    s3.putObject(objectRequest, RequestBody.fromFile(path));
	 }
	 
	 public static void main(String[] args) {
		    Region region = Region.US_EAST_1;

		    S3Client s3 = S3Client.builder().region(region).build();
		    
		    String bucketName = args[0];
		    String path = args[1];
		    
		    
		    if (IsObjectInBucket(s3, bucketName, path) == 0) {
		    upload_file(s3,bucketName,path);
		    System.out.println("File uploaded");
		    }
		    else {
		    	
		    	System.out.println("Error file already exist in this Bucket");
		    }
	 }

}
