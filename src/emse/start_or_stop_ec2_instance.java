package emse;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.model.DescribeInstanceStatusRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstanceStatusResponse;
import software.amazon.awssdk.services.ec2.model.StartInstancesRequest;
import software.amazon.awssdk.services.ec2.model.StopInstancesRequest;

public class start_or_stop_ec2_instance {
	
	public static void startInstance(Ec2Client ec2,String instanceID) {
		StartInstancesRequest request = StartInstancesRequest.builder()
                .instanceIds(instanceID)
                .build();

        ec2.startInstances(request);
        System.out.printf("Successfully started instance %s", instanceID);
	}
	
	public static void stopInstance(Ec2Client ec2, String instanceId) {

        StopInstancesRequest request = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.stopInstances(request);
        System.out.printf("Successfully stopped instance %s", instanceId);
    }
	
	
	public static String start_stop_ec2instance(Ec2Client ec2,String instanceID) {
		
		
		DescribeInstanceStatusRequest status_request = DescribeInstanceStatusRequest.builder().instanceIds(instanceID).build();
		DescribeInstanceStatusResponse  status = ec2.describeInstanceStatus(status_request);
		if (status.instanceStatuses().size() ==0 ) {
			startInstance(ec2,instanceID);
		}
		else {
			stopInstance(ec2,instanceID);
		}
		
		return "";
	}
	
	 public static void main(String[] args) {
		 
		 Region region  = Region.US_EAST_1;
		 
		 Ec2Client ec2 = Ec2Client.builder()
		            .region(region)
		            .credentialsProvider(ProfileCredentialsProvider.create())
		            .build();    
		            
		 String instanceID="i-045d61e815af6954f";
		 
		 start_stop_ec2instance(ec2,instanceID);
	 }

}
