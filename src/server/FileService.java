package server;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.FileEvent;
import model.FilesInfo;

@Path("/file")
public class FileService {

	private static final String FILE_PATH = "/home/madmatts/Dropbox/Matts/files.json";
	private ExecutorService service = Executors.newFixedThreadPool(5);
	private Queue<FileEvent> queue = Collections.asLifoQueue(new LinkedList<FileEvent>());
	private TrafficController tc = new TrafficController(queue);

	public FileService() {
		service.submit(tc);
		service.shutdown();
	}

	@POST
	@Path("/post")
	@Consumes("application/json")
	public Response createProductInJSON(String jsonUpdate) {

		String result = "JSON from client" + jsonUpdate;
		ObjectMapper mapper = new ObjectMapper();
		try {
			FilesInfo filesInfo = mapper.readValue(jsonUpdate, FilesInfo.class);
			System.out.println(filesInfo);
			enqueue(filesInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printQ();
		return Response.status(201).entity(result).build();
	}

	private void enqueue(FilesInfo fi) {
		for (File f : fi.getFileList()) {
			FileEvent fe = new FileEvent();
			fe.setDestinationDirectory(
					"/home/madmatts/Dropbox/Server" + File.separator + fi.getUsername() + File.separator);
			fe.setFilename(f.getName());
			fe.setSourceDirectory(f.getPath());
			queue.add(fe);
			Random rand = new Random();
			int randInt = 0;
			try {
				randInt = rand.nextInt(4000);
				Thread.sleep(randInt);
				System.out.println(randInt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void printQ() {
		System.out.println(queue.toString());
	}

}