package application;

import java.io.File;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

public class SendMessage {

	public static void main(String[] args) {
		Region region = Region.US_EAST_1;

		if (args.length < 3) {
			System.out.println("Missing the Queue URL, Bucket Name, or File Name arguments");
			System.exit(1);
		}

		String queueURL = args[0];
		String bucketName = args[1];
		String filePath = args[2];

		File f = new File(filePath);
		String fileName = f.getName();
		fileName = fileName.substring(0, 10) + "/" // replace last "-" by "/" in order to create a folder in
		// S3 bucket
				+ fileName.substring(10 + 1);

		SqsClient sqsClient = SqsClient.builder().region(region).build();

		SendMessageRequest sendRequest = SendMessageRequest.builder().queueUrl(queueURL)
				.messageBody(bucketName + ";" + fileName).build();

		SendMessageResponse sqsResponse = sqsClient.sendMessage(sendRequest);

		System.out.println(
				sqsResponse.messageId() + " Message sent. Status is " + sqsResponse.sdkHttpResponse().statusCode());
	}
}