package aws;

/*
* Cloud Computing
* 
* Dynamic Resource Management Tool
* using AWS Java SDK Library
* 
*/
import java.util.Iterator;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupRequest;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.IpRange;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.RevokeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.AllocateAddressRequest;
import com.amazonaws.services.ec2.model.AllocateAddressResult;
import com.amazonaws.services.ec2.model.DomainType;
import com.amazonaws.services.ec2.model.AssociateAddressResult;
import com.amazonaws.services.ec2.model.DisassociateAddressRequest;
import com.amazonaws.services.ec2.model.ReleaseAddressRequest;
import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.ec2.model.DescribeAddressesRequest;
import com.amazonaws.services.ec2.model.DescribeAddressesResult;
import com.amazonaws.services.ec2.model.DescribeVolumesRequest;
import com.amazonaws.services.ec2.model.DescribeVolumesResult;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DeleteVolumeRequest;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;

public class awsTest {

	static AmazonEC2 ec2;

	private static void init() throws Exception {

		String credentialFilePath = "C:/Users/jykim/.aws/credentials";
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(credentialFilePath, "default");
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct " +
							"location (C:/Users/jykim/.aws/credentials), and is in valid format.",
					e);
		}
		ec2 = AmazonEC2ClientBuilder.standard()
				.withCredentials(credentialsProvider)
				.withRegion("us-east-1") /* check the region at AWS console */
				.build();
	}

	public static void main(String[] args) throws Exception {

		init();

		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		int number = 0;

		while (true) {
			System.out.println("                                                            ");
			System.out.println("                                                            ");
			System.out.println("------------------------------------------------------------");
			System.out.println("           Amazon AWS Control Panel using SDK               ");
			System.out.println("------------------------------------------------------------");
			System.out.println("  1. list instance                2. available zones        ");
			System.out.println("  3. start instance               4. available regions      ");
			System.out.println("  5. stop instance                6. create instance        ");
			System.out.println("  7. reboot instance              8. list images            ");
			System.out.println("  9. condor_status                10. terminate instance    ");
			System.out.println("  11. list security groups        12. create security group ");
			System.out.println("  13. delete security group       14. list inbound rules    ");
			System.out.println("  15. add inbound rule            16. delete inbound rule   ");
			System.out.println("  17. allocate elastic IP         18. associate elastic IP  ");
			System.out.println("  19. disassociate elastic IP     20. release elastic IP    ");
			System.out.println("  21. list elastic IPs            22. list volumes          ");
			System.out.println("  23. create volume               24. delete volume         ");
			System.out.println("  25. attach volume               26. detach volume         ");
			System.out.println("                                  99. quit                  ");
			System.out.println("------------------------------------------------------------");

			System.out.print("Enter an integer: ");

			if (menu.hasNextInt()) {
				number = menu.nextInt();
			} else {
				System.out.println("concentration!");
				break;
			}

			String instance_id = "";

			switch (number) {
				case 1:
					listInstances();
					break;

				case 2:
					availableZones();
					break;

				case 3:
					System.out.print("Enter instance id: ");
					if (id_string.hasNext())
						instance_id = id_string.nextLine();

					if (!instance_id.trim().isEmpty())
						startInstance(instance_id);
					break;

				case 4:
					availableRegions();
					break;

				case 5:
					System.out.print("Enter instance id: ");
					if (id_string.hasNext())
						instance_id = id_string.nextLine();

					if (!instance_id.trim().isEmpty())
						stopInstance(instance_id);
					break;

				case 6:
					System.out.print("Enter ami id: ");
					String ami_id = "";
					if (id_string.hasNext())
						ami_id = id_string.nextLine();

					if (!ami_id.trim().isEmpty())
						createInstance(ami_id);
					break;

				case 7:
					System.out.print("Enter instance id: ");
					if (id_string.hasNext())
						instance_id = id_string.nextLine();

					if (!instance_id.trim().isEmpty())
						rebootInstance(instance_id);
					break;

				case 8:
					listImages();
					break;

				case 9:
					condorStatus();
					break;

				case 10:
					System.out.print("Enter instance id: ");
					if (id_string.hasNext())
						instance_id = id_string.nextLine();
					if (!instance_id.trim().isEmpty())
						terminateInstance(instance_id);
					break;

				case 11:
					listSecurityGroups();
					break;

				case 12:
					System.out.print("Enter new security group name: ");
					String sgName = "";
					if (id_string.hasNext())
						sgName = id_string.nextLine();
					System.out.print("Enter description: ");
					String sgDesc = "";
					if (id_string.hasNext())
						sgDesc = id_string.nextLine();
					if (!sgName.trim().isEmpty() && !sgDesc.trim().isEmpty())
						createSecurityGroup(sgName, sgDesc);
					break;

				case 13:
					System.out.print("Enter security group ID to delete: ");
					String sgIdToDelete = "";
					if (id_string.hasNext())
						sgIdToDelete = id_string.nextLine();
					if (!sgIdToDelete.trim().isEmpty())
						deleteSecurityGroup(sgIdToDelete);
					break;

				case 14:
					System.out.print("Enter security group ID to describe inbound rules: ");
					String sgIdToDescribe = "";
					if (id_string.hasNext())
						sgIdToDescribe = id_string.nextLine();

					if (!sgIdToDescribe.trim().isEmpty()) {
						describeSecurityGroupIngress(sgIdToDescribe);
					} else {
						System.out.println("Invalid input. Please enter a valid security group ID.");
					}
					break;

				case 15:
					System.out.print("Enter security group ID: ");
					String sgIdToAuthorize = "";
					if (id_string.hasNext())
						sgIdToAuthorize = id_string.nextLine();

					System.out.print("Enter protocol (e.g., tcp): ");
					String authorizeProtocol = "";
					if (id_string.hasNext())
						authorizeProtocol = id_string.nextLine();

					System.out.print("Enter port: ");
					int authorizePort = 22;
					if (id_string.hasNextInt())
						authorizePort = id_string.nextInt();
					id_string.nextLine(); // Consume the newline character

					System.out.print("Enter CIDR (e.g., 0.0.0.0/0): ");
					String authorizeCidr = "";
					if (id_string.hasNext())
						authorizeCidr = id_string.nextLine();

					if (!sgIdToAuthorize.trim().isEmpty() && !authorizeProtocol.trim().isEmpty()
							&& !authorizeCidr.trim().isEmpty()) {
						authorizeSecurityGroupIngress(sgIdToAuthorize, authorizeProtocol, authorizePort, authorizeCidr);
					} else {
						System.out.println("Invalid input. Please make sure all fields are filled.");
					}
					break;

				case 16:
					System.out.print("Enter security group ID: ");
					String sgIdToRevoke = "";
					if (id_string.hasNext())
						sgIdToRevoke = id_string.nextLine();

					System.out.print("Enter protocol (e.g., tcp): ");
					String revokeProtocol = "";
					if (id_string.hasNext())
						revokeProtocol = id_string.nextLine();

					System.out.print("Enter port: ");
					int revokePort = 22;
					if (id_string.hasNextInt())
						revokePort = id_string.nextInt();
					id_string.nextLine(); // Consume the newline character

					System.out.print("Enter CIDR (e.g., 0.0.0.0/0): ");
					String revokeCidr = "";
					if (id_string.hasNext())
						revokeCidr = id_string.nextLine();

					if (!sgIdToRevoke.trim().isEmpty() && !revokeProtocol.trim().isEmpty()
							&& !revokeCidr.trim().isEmpty()) {
						revokeSecurityGroupIngress(sgIdToRevoke, revokeProtocol, revokePort, revokeCidr);
					} else {
						System.out.println("Invalid input. Please make sure all fields are filled.");
					}
					break;

				case 17:
					allocateElasticIP();
					break;

				case 18:
					System.out.print("Enter instance ID: ");
					String instanceId = "";
					if (id_string.hasNext())
						instanceId = id_string.nextLine();

					System.out.print("Enter allocation ID: ");
					String allocationId = "";
					if (id_string.hasNext())
						allocationId = id_string.nextLine();

					if (!instanceId.trim().isEmpty() && !allocationId.trim().isEmpty()) {
						associateElasticIP(instanceId, allocationId);
					} else {
						System.out.println("Invalid input. Please enter valid instance ID and allocation ID.");
					}
					break;

				case 19:
					System.out.print("Enter association ID: ");
					String associationId = "";
					if (id_string.hasNext())
						associationId = id_string.nextLine();

					if (!associationId.trim().isEmpty()) {
						disassociateElasticIP(associationId);
					} else {
						System.out.println("Invalid input. Please enter a valid association ID.");
					}
					break;

				case 20:
					System.out.print("Enter allocation ID: ");
					String allocIdToRelease = "";
					if (id_string.hasNext())
						allocIdToRelease = id_string.nextLine();

					if (!allocIdToRelease.trim().isEmpty()) {
						releaseElasticIP(allocIdToRelease);
					} else {
						System.out.println("Invalid input. Please enter a valid allocation ID.");
					}
					break;

				case 21:
					listElasticIPs();
					break;

				case 22:
					listVolumes();
					break;

				case 23:
					System.out.print("Enter availability zone (e.g., us-east-1a): ");
					String az = "";
					if (id_string.hasNext())
						az = id_string.nextLine();

					System.out.print("Enter volume size (GB): ");
					int size = 0;
					if (id_string.hasNextInt())
						size = id_string.nextInt();
					id_string.nextLine(); // Consume the newline character

					if (!az.trim().isEmpty() && size > 0) {
						createVolume(az, size);
					} else {
						System.out.println("Invalid input. Please enter a valid availability zone and size.");
					}
					break;

				case 24:
					System.out.print("Enter volume ID to delete: ");
					String volumeIdToDelete = "";
					if (id_string.hasNext())
						volumeIdToDelete = id_string.nextLine();

					if (!volumeIdToDelete.trim().isEmpty()) {
						deleteVolume(volumeIdToDelete);
					} else {
						System.out.println("Invalid input. Please enter a valid volume ID.");
					}
					break;

				case 25:
					System.out.print("Enter volume ID: ");
					String volumeIdToAttach = "";
					if (id_string.hasNext())
						volumeIdToAttach = id_string.nextLine();

					System.out.print("Enter instance ID: ");
					String instanceIdToAttach = "";
					if (id_string.hasNext())
						instanceIdToAttach = id_string.nextLine();

					System.out.print("Enter device name (e.g., /dev/sdf): ");
					String device = "";
					if (id_string.hasNext())
						device = id_string.nextLine();

					if (!volumeIdToAttach.trim().isEmpty() && !instanceIdToAttach.trim().isEmpty()
							&& !device.trim().isEmpty()) {
						attachVolume(volumeIdToAttach, instanceIdToAttach, device);
					} else {
						System.out
								.println("Invalid input. Please enter valid volume ID, instance ID, and device name.");
					}
					break;

				case 26:
					System.out.print("Enter volume ID to detach: ");
					String volumeIdToDetach = "";
					if (id_string.hasNext())
						volumeIdToDetach = id_string.nextLine();

					if (!volumeIdToDetach.trim().isEmpty()) {
						detachVolume(volumeIdToDetach);
					} else {
						System.out.println("Invalid input. Please enter a valid volume ID.");
					}
					break;

				case 99:
					System.out.println("bye!");
					menu.close();
					id_string.close();
					return;
				default:
					System.out.println("concentration!");
			}

		}

	}

	public static void listInstances() {

		System.out.println("Listing instances....");
		boolean done = false;

		DescribeInstancesRequest request = new DescribeInstancesRequest();

		while (!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);

			for (Reservation reservation : response.getReservations()) {
				for (Instance instance : reservation.getInstances()) {
					System.out.printf(
							"[id] %s, " +
									"[AMI] %s, " +
									"[type] %s, " +
									"[state] %10s, " +
									"[monitoring state] %s",
							instance.getInstanceId(),
							instance.getImageId(),
							instance.getInstanceType(),
							instance.getState().getName(),
							instance.getMonitoring().getState());
				}
				System.out.println();
			}

			request.setNextToken(response.getNextToken());

			if (response.getNextToken() == null) {
				done = true;
			}
		}
	}

	public static void availableZones() {

		System.out.println("Available zones....");
		try {
			DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
			Iterator<AvailabilityZone> iterator = availabilityZonesResult.getAvailabilityZones().iterator();

			AvailabilityZone zone;
			while (iterator.hasNext()) {
				zone = iterator.next();
				System.out.printf("[id] %s,  [region] %15s, [zone] %15s\n", zone.getZoneId(), zone.getRegionName(),
						zone.getZoneName());
			}
			System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
					" Availability Zones.");

		} catch (AmazonServiceException ase) {
			System.out.println("Caught Exception: " + ase.getMessage());
			System.out.println("Reponse Status Code: " + ase.getStatusCode());
			System.out.println("Error Code: " + ase.getErrorCode());
			System.out.println("Request ID: " + ase.getRequestId());
		}

	}

	public static void startInstance(String instance_id) {

		System.out.printf("Starting .... %s\n", instance_id);
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StartInstancesRequest> dry_request = () -> {
			StartInstancesRequest request = new StartInstancesRequest()
					.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		StartInstancesRequest request = new StartInstancesRequest()
				.withInstanceIds(instance_id);

		ec2.startInstances(request);

		System.out.printf("Successfully started instance %s", instance_id);
	}

	public static void availableRegions() {

		System.out.println("Available regions ....");

		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DescribeRegionsResult regions_response = ec2.describeRegions();

		for (Region region : regions_response.getRegions()) {
			System.out.printf(
					"[region] %15s, " +
							"[endpoint] %s\n",
					region.getRegionName(),
					region.getEndpoint());
		}
	}

	public static void stopInstance(String instance_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StopInstancesRequest> dry_request = () -> {
			StopInstancesRequest request = new StopInstancesRequest()
					.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		try {
			StopInstancesRequest request = new StopInstancesRequest()
					.withInstanceIds(instance_id);

			ec2.stopInstances(request);
			System.out.printf("Successfully stop instance %s\n", instance_id);

		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}

	}

	public static void createInstance(String ami_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		RunInstancesRequest run_request = new RunInstancesRequest()
				.withImageId(ami_id)
				.withInstanceType(InstanceType.T2Micro)
				.withMaxCount(1)
				.withMinCount(1);

		RunInstancesResult run_response = ec2.runInstances(run_request);

		String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

		System.out.printf(
				"Successfully started EC2 instance %s based on AMI %s",
				reservation_id, ami_id);

	}

	public static void rebootInstance(String instance_id) {

		System.out.printf("Rebooting .... %s\n", instance_id);

		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		try {
			RebootInstancesRequest request = new RebootInstancesRequest()
					.withInstanceIds(instance_id);

			RebootInstancesResult response = ec2.rebootInstances(request);

			System.out.printf(
					"Successfully rebooted instance %s", instance_id);

		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}

	}

	public static void listImages() {
		System.out.println("Listing images....");

		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DescribeImagesRequest request = new DescribeImagesRequest();
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

		request.getFilters().add(new Filter().withName("name").withValues("aws-htcondor-slave"));
		request.setRequestCredentialsProvider(credentialsProvider);

		DescribeImagesResult results = ec2.describeImages(request);

		for (Image images : results.getImages()) {
			System.out.printf("[ImageID] %s, [Name] %s, [Owner] %s\n",
					images.getImageId(), images.getName(), images.getOwnerId());
		}

	}

	public static void condorStatus() {
		System.out.println("Running condor_status command on remote VM...");
		try {
			String[] command = {
					"ssh",
					"-i", "C:\\Users\\jykim\\Downloads\\cloud-test.pem",
					"ec2-user@ec2-18-215-152-15.compute-1.amazonaws.com",
					"condor_status"
			};

			Process process = Runtime.getRuntime().exec(command);

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}

			try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
				String errorLine;
				while ((errorLine = errorReader.readLine()) != null) {
					System.err.println("Error: " + errorLine);
				}
			}

			int exitCode = process.waitFor();
			if (exitCode == 0) {
				System.out.println("condor_status executed successfully on remote VM.");
			} else {
				System.err.println("condor_status exited with code: " + exitCode);
			}

		} catch (Exception e) {
			System.err.println("An error occurred while running condor_status: " + e.getMessage());
		}
	}

	public static void terminateInstance(String instance_id) {
		System.out.printf("Terminating instance %s...\n", instance_id);
		TerminateInstancesRequest req = new TerminateInstancesRequest().withInstanceIds(instance_id);
		ec2.terminateInstances(req);
		System.out.printf("Instance %s terminated.\n", instance_id);
	}

	public static void listSecurityGroups() {
		System.out.println("Describing Security Groups...");
		DescribeSecurityGroupsResult result = ec2.describeSecurityGroups();
		for (SecurityGroup sg : result.getSecurityGroups()) {
			System.out.printf("GroupName: %s, GroupId: %s, VpcId: %s\n", sg.getGroupName(), sg.getGroupId(),
					sg.getVpcId());
		}
	}

	public static void createSecurityGroup(String groupName, String description) {
		System.out.printf("Creating Security Group %s...\n", groupName);
		CreateSecurityGroupRequest req = new CreateSecurityGroupRequest()
				.withGroupName(groupName)
				.withDescription(description);
		CreateSecurityGroupResult res = ec2.createSecurityGroup(req);
		System.out.printf("Security Group Created: %s\n", res.getGroupId());
	}

	public static void deleteSecurityGroup(String groupId) {
		System.out.printf("Deleting Security Group %s...\n", groupId);
		try {
			DeleteSecurityGroupRequest deleteRequest = new DeleteSecurityGroupRequest()
					.withGroupId(groupId);

			ec2.deleteSecurityGroup(deleteRequest);
			System.out.println("Security Group deleted successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
			System.err.println("Error Code: " + e.getErrorCode());
			System.err.println("Request ID: " + e.getRequestId());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void describeSecurityGroupIngress(String groupId) {
		System.out.printf("Describing ingress rules for Security Group %s...\n", groupId);
		try {
			DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest().withGroupIds(groupId);
			DescribeSecurityGroupsResult result = ec2.describeSecurityGroups(request);

			for (SecurityGroup sg : result.getSecurityGroups()) {
				System.out.printf("\nSecurity Group: %s (%s)\n", sg.getGroupName(), sg.getGroupId());
				System.out.println("----------------------------------");
				for (IpPermission permission : sg.getIpPermissions()) {
					System.out.printf("Protocol: %s, Ports: %d - %d\n",
							permission.getIpProtocol(),
							permission.getFromPort() != null ? permission.getFromPort() : -1,
							permission.getToPort() != null ? permission.getToPort() : -1);

					for (IpRange range : permission.getIpv4Ranges()) {
						System.out.printf("CIDR: %s\n", range.getCidrIp());
					}
					System.out.println("----------------------------------");
				}
			}
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
			System.err.println("Error Code: " + e.getErrorCode());
			System.err.println("Request ID: " + e.getRequestId());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void authorizeSecurityGroupIngress(String groupId, String protocol, int port, String cidr) {
		System.out.printf("Authorizing ingress rule for SG %s on port %d...\n", groupId, port);
		try {
			IpPermission ipPermission = new IpPermission()
					.withIpProtocol(protocol)
					.withFromPort(port)
					.withToPort(port)
					.withIpv4Ranges(new IpRange().withCidrIp(cidr));

			AuthorizeSecurityGroupIngressRequest ingressRequest = new AuthorizeSecurityGroupIngressRequest()
					.withGroupId(groupId)
					.withIpPermissions(ipPermission);

			ec2.authorizeSecurityGroupIngress(ingressRequest);
			System.out.println("Ingress rule added successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
			System.err.println("Error Code: " + e.getErrorCode());
			System.err.println("Request ID: " + e.getRequestId());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void revokeSecurityGroupIngress(String groupId, String protocol, int port, String cidr) {
		System.out.printf("Revoking ingress rule for SG %s on port %d...\n", groupId, port);
		try {
			IpPermission ipPermission = new IpPermission()
					.withIpProtocol(protocol)
					.withFromPort(port)
					.withToPort(port)
					.withIpv4Ranges(new IpRange().withCidrIp(cidr));

			RevokeSecurityGroupIngressRequest revokeRequest = new RevokeSecurityGroupIngressRequest()
					.withGroupId(groupId)
					.withIpPermissions(ipPermission);

			ec2.revokeSecurityGroupIngress(revokeRequest);
			System.out.println("Ingress rule revoked successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
			System.err.println("Error Code: " + e.getErrorCode());
			System.err.println("Request ID: " + e.getRequestId());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void allocateElasticIP() {
		System.out.println("Allocating Elastic IP...");
		try {
			AllocateAddressRequest request = new AllocateAddressRequest().withDomain(DomainType.Vpc);
			AllocateAddressResult result = ec2.allocateAddress(request);
			System.out.printf("Allocated Elastic IP: %s, Allocation ID: %s\n", result.getPublicIp(),
					result.getAllocationId());
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void associateElasticIP(String instanceId, String allocationId) {
		System.out.printf("Associating Elastic IP (Allocation ID: %s) to instance %s...\n", allocationId, instanceId);
		try {
			AssociateAddressRequest request = new AssociateAddressRequest()
					.withInstanceId(instanceId)
					.withAllocationId(allocationId);
			AssociateAddressResult result = ec2.associateAddress(request);
			System.out.printf("Elastic IP associated successfully. Association ID: %s\n", result.getAssociationId());
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void disassociateElasticIP(String associationId) {
		System.out.printf("Disassociating Elastic IP (Association ID: %s)...\n", associationId);
		try {
			DisassociateAddressRequest request = new DisassociateAddressRequest().withAssociationId(associationId);
			ec2.disassociateAddress(request);
			System.out.println("Elastic IP disassociated successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void releaseElasticIP(String allocationId) {
		System.out.printf("Releasing Elastic IP (Allocation ID: %s)...\n", allocationId);
		try {
			ReleaseAddressRequest request = new ReleaseAddressRequest().withAllocationId(allocationId);
			ec2.releaseAddress(request);
			System.out.println("Elastic IP released successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void listElasticIPs() {
		System.out.println("Listing Elastic IPs...");
		try {
			DescribeAddressesRequest request = new DescribeAddressesRequest();
			DescribeAddressesResult result = ec2.describeAddresses(request);

			if (result.getAddresses().isEmpty()) {
				System.out.println("No Elastic IPs found.");
			} else {
				for (Address address : result.getAddresses()) {
					System.out.printf("Public IP: %s, Allocation ID: %s, Instance ID: %s\n",
							address.getPublicIp(),
							address.getAllocationId(),
							address.getInstanceId() != null ? address.getInstanceId() : "Not associated");
				}
			}
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void listVolumes() {
		System.out.println("Listing EBS Volumes...");
		try {
			DescribeVolumesRequest request = new DescribeVolumesRequest();
			DescribeVolumesResult result = ec2.describeVolumes(request);

			for (Volume volume : result.getVolumes()) {
				System.out.printf("Volume ID: %s, Size: %d GB, State: %s, Availability Zone: %s\n",
						volume.getVolumeId(),
						volume.getSize(),
						volume.getState(),
						volume.getAvailabilityZone());
			}
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void createVolume(String availabilityZone, int size) {
		System.out.printf("Creating EBS Volume in %s with size %d GB...\n", availabilityZone, size);
		try {
			CreateVolumeRequest request = new CreateVolumeRequest()
					.withAvailabilityZone(availabilityZone)
					.withSize(size);
			CreateVolumeResult result = ec2.createVolume(request);
			System.out.printf("Created Volume ID: %s\n", result.getVolume().getVolumeId());
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void deleteVolume(String volumeId) {
		System.out.printf("Deleting EBS Volume %s...\n", volumeId);
		try {
			DeleteVolumeRequest request = new DeleteVolumeRequest().withVolumeId(volumeId);
			ec2.deleteVolume(request);
			System.out.println("Volume deleted successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void attachVolume(String volumeId, String instanceId, String device) {
		System.out.printf("Attaching Volume %s to Instance %s on Device %s...\n", volumeId, instanceId, device);
		try {
			AttachVolumeRequest request = new AttachVolumeRequest()
					.withVolumeId(volumeId)
					.withInstanceId(instanceId)
					.withDevice(device);
			ec2.attachVolume(request);
			System.out.println("Volume attached successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

	public static void detachVolume(String volumeId) {
		System.out.printf("Detaching Volume %s...\n", volumeId);
		try {
			DetachVolumeRequest request = new DetachVolumeRequest().withVolumeId(volumeId);
			ec2.detachVolume(request);
			System.out.println("Volume detached successfully.");
		} catch (AmazonServiceException e) {
			System.err.println("AmazonServiceException: " + e.getMessage());
		} catch (AmazonClientException e) {
			System.err.println("AmazonClientException: " + e.getMessage());
		}
	}

}
