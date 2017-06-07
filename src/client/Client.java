package client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import model.FilesInfo;
import model.UserInfo;
import utility.Crawler;

public class Client {

	private static String USERNAME = "";

	public static void main(String[] args) {
		USERNAME = JOptionPane.showInputDialog("Enter your username: ");

		System.out.println(getServerStructure().toString());

		// try {
		//
		// UserInfo user = new UserInfo(USERNAME, args[0]);
		//
		// Crawler crawler = new Crawler(user.getHomePath() + File.separator +
		// user.getUsername());
		// crawler.scanFiles();
		// if (crawler.checkForDifference()) {
		//
		// crawler.updateJSON(USERNAME);
		// crawler.returnJson(USERNAME);
		//
		// ClientRequest request = new
		// ClientRequest("http://localhost:8080/RestServer/file/post");
		// request.accept("application/json");
		// request.body("application/json", crawler.returnJson(USERNAME));
		// System.out.println(crawler.returnJson(USERNAME));
		//
		// ClientResponse<String> response = request.post(String.class);
		//
		// if (response.getStatus() != 201) {
		// throw new RuntimeException("Failed : HTTP error code : " +
		// response.getStatus());
		// }
		//
		// BufferedReader br = new BufferedReader(
		// new InputStreamReader(new
		// ByteArrayInputStream(response.getEntity().getBytes())));
		//
		// String output;
		// System.out.println("Output from Server .... \n");
		// while ((output = br.readLine()) != null) {
		// System.out.println(output);
		// }
		// } else {
		// System.out.println("\nDropbox Up-to-date\n");
		// }
		// // Synchronizer sync = new Synchronizer(crawler, user);
		// // sync.start();
		//
		// } catch (MalformedURLException e) {
		//
		// e.printStackTrace();
		//
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		//
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		//
		// }

	}

	private static FilesInfo getServerStructure() {
		// ClientRequest request = new
		// ClientRequest("http://localhost:8080/RestServer/file/get/" +
		// USERNAME);
		ClientRequest request = new ClientRequest("http://localhost:8080/RestServer/file/get");
		// request.accept("application/json");
		// request.body("application/json");
		// System.out.println();
		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<String> response;
		try {
			response = request.get(String.class);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try {
		// response = request.get(FilesInfo.class);
		// FilesInfo fi = response.getEntity();
		// return fi;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return null;
	}

}