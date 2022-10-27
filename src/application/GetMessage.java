package application;

import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class GetMessage {

	public static List<Message> SnsGetMessage(String QueueUrl, Region region) {

		SqsClient sqsClient = SqsClient.builder().region(region).build();

		ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder().queueUrl(QueueUrl).build();

		List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

		return messages;

	}

	public void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Missing the Queue URL");
			System.exit(1);
		}
		Region region = Region.US_EAST_1;

		List<Message> messages = SnsGetMessage(args[0], region);

		messages.forEach(System.out::println);

		String String_message = messages.get(0).body().toString();
		String[] separated = String_message.split(";");

		String bucket_name = separated[0];
		String file_name = separated[1];

		S3Client s3 = S3Client.builder().region(region).build();

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket_name).key(file_name).build();

		s3.getObject(getObjectRequest);
	}
}
