package application;

import java.io.File;

import software.amazon.awssdk.services.s3.model.S3Exception;

public class Client {

	public static void main(String[] args) { // input : (String filePath)

		String QueueUrl = "https://sqs.us-east-1.amazonaws.com/446929553961/Tp_cloud_Queue";
		String bucketName = "workerbucket01";
		File f = new File(args[0]);
		String fileName=f.getName();
		

		try {
			S3uploadfile.main(new String[] {bucketName, args[0] });
			SendMessage.main(new String[] { QueueUrl, bucketName,fileName});

		} catch (S3Exception e) {
			// System.err.println(e.awsErrorDetails().errorMessage());
			// System.exit(1);

		}

	}
}
