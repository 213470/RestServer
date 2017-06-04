package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnector {

	private Socket socket;

	public ClientConnector(String username) {
		loginToServer(username);
	}

	public void loginToServer(String username) {
		try {
			socket = new Socket("localhost", 6066);
			
			//Sending username to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(username);
			
			Thread.sleep(100);
			
			//Receiving connection time from server.
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.print("You have connected to server at: ");
			String answer = input.readLine();

			System.out.println(answer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
