package client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import model.UserInfo;
import utility.Crawler;

public class Client {

	public static void main(String[] args) {

		try {

			String username = JOptionPane.showInputDialog("Enter your username: ");

			UserInfo user = new UserInfo(username, args[0]);

			Crawler crawler = new Crawler(user.getHomePath() + File.separator + user.getUsername());
			crawler.scanFiles();
			if (crawler.checkForDifference()) {

//				crawler.updateJSON(username);
				crawler.returnJson(username);

				ClientRequest request = new ClientRequest("http://localhost:8080/RestServer/file/post");
				request.accept("application/json");
				request.body("application/json", crawler.returnJson(username));
//				System.out.println(crawler.returnJson(username));
				
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

}