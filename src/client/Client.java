package client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import model.FilesInfo;
import model.UserInfo;
import utility.Crawler;

public class Client {

	private static String USERNAME = "";

	public static void main(String[] args) {
		USERNAME = JOptionPane.showInputDialog("Enter your username: ");
		try {

			UserInfo user = new UserInfo(USERNAME, args[0]);

			Crawler crawler = new Crawler(user.getHomePath() + File.separator + user.getUsername(), user.getUsername());
			crawler.scanFiles();
			if (crawler.checkForDifference(getServerStructure().getFileList())) {

				crawler.updateJSON();
				crawler.returnJson();

				ClientRequest request = new ClientRequest("http://localhost:8080/RestServer/file/post");
				request.accept("application/json");
				request.body("application/json", crawler.returnJson());
				System.out.println(crawler.returnJson());

				ClientResponse<String> response = request.post(String.class);

				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}

				BufferedReader br = new BufferedReader(
						new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
			} else {
				System.out.println("\nDropbox Up-to-date\n");
			}
			// Synchronizer sync = new Synchronizer(crawler, user);
			// sync.start();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private static FilesInfo getServerStructure() {

		ClientRequest request = new ClientRequest("http://localhost:8080/RestServer/file/" + USERNAME);

		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<String> response;
		try {
			response = request.get(String.class);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));

			String output;
			String responseJSON = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				responseJSON += output;
				System.out.println(output);
			}
			ObjectMapper mapper = new ObjectMapper();
			FilesInfo serverStructure = mapper.readValue(responseJSON, FilesInfo.class);
			return serverStructure;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}