package application;

import java.io.IOException;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import software.amazon.awssdk.services.s3.model.S3Exception;

public class Consolidator {

	public static String ReadCSVByProduct(String bucketName, String date) throws IOException {

		// Get file
		Regions region = Regions.US_EAST_1;
		S3Object fullObject = null;
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region).build();

		fullObject = s3Client.getObject(new GetObjectRequest(bucketName, date + "-byproduct.csv"));
		//

		// Convert Input into String
		try (S3ObjectInputStream data = fullObject.getObjectContent()) {
			return IOUtils.toString(data);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		//
	}

	public static String ReadCSVByStore(String bucketName, String date) throws IOException {

		// Get file
		Regions region = Regions.US_EAST_1;
		S3Object fullObject = null;
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region).build();

		fullObject = s3Client.getObject(new GetObjectRequest(bucketName, date + "-bystores.csv"));
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
		String date = args[0];

		String csv_data_product = "";
		String csv_data_store = "";

		int total_retailer_profit = 0;
		int most_profitable_store = 0;
		int most_profitable_store_value = 0;
		int least_profitable_store = 0;
		int least_profitable_store_value = Integer.MAX_VALUE;

		try {
			csv_data_product = ReadCSVByProduct(bucketName, date);
			csv_data_store = ReadCSVByStore(bucketName, date);
			// System.out.print(csv_data_product);
			// System.out.print(csv_data_store);

		} catch (S3Exception e) {
			// System.err.println(e.awsErrorDetails().errorMessage());
			// System.exit(1);
		}

		// Converting the csv files to Arrays
		List<String> data_store = Arrays.asList(csv_data_store.split("\r\n"));
		List<String> data_product = Arrays.asList(csv_data_product.split("\r\n"));

		int nb_store = data_store.size();
		int nb_product = data_product.size();

		// processing data from stores
		for (int i = 1; i < nb_store; i++) { // start at 1 because of descriptive line for the csv file useless here
			String[] store_i_profit = data_store.get(i).split(";");
			int store_i_profit_value = Integer.parseInt(store_i_profit[1]);
			total_retailer_profit = total_retailer_profit + store_i_profit_value;

			if (store_i_profit_value < least_profitable_store_value) {
				least_profitable_store_value = store_i_profit_value;
				least_profitable_store = i;
			}
			if (store_i_profit_value > most_profitable_store_value) {
				most_profitable_store_value = store_i_profit_value;
				most_profitable_store = i;
			}

		}

		System.out.println("######## STORES DATA ########");
		System.out.println("Total retailer profit = " + total_retailer_profit + "$");
		System.out.println("Most profitable store is store n°" + most_profitable_store);
		System.out.println("Least profitable store is store n°" + least_profitable_store);

		System.out.println(" ");

		System.out.println("######## PRODUCT DATA ########");

		System.out.println(
				"----------------------------------------");
		System.out.printf("%10s %10s %5s %7s", "PRODUCT", "QUANTITY", "SOLD", "PROFIT");
		System.out.println();
		System.out.println(
				"----------------------------------------");
		// processing data from product
		for (int i = 1; i < nb_product; i++) { // start at 1 because of descriptive line for the csv file useless here
			String[] product_i_data = data_product.get(i).split(";");
			/*
			 * System.out.println("Product n°" +(i-1)+ " :");
			 * System.out.println("Total quantity = " + product_i_data[1]);
			 * System.out.println("Total sold = " + product_i_data[2]);
			 * System.out.println("Total profit = " + product_i_data[3] + "$");
			 */

			System.out.format("%10s %10s %5s %7s", i - 1, product_i_data[1], product_i_data[2],
					product_i_data[3] + "$");
			System.out.println();
		}

		System.out.println(
				"----------------------------------------");
	}

}
