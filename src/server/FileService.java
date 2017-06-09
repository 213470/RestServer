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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.FileEvent;
import model.FilesInfo;
import utility.Crawler;

@Path("/file")
public class FileService {

	private static final String SERVER_PATH = "/home/dominika/Dropbox/Server";

	private ExecutorService service = Executors.newFixedThreadPool(5);
	private Queue<FileEvent> queue = Collections.asLifoQueue(new LinkedList<FileEvent>());
	private FileDownloader fileDownloader = new FileDownloader(queue);

	public FileService() {
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					service.submit(fileDownloader);
					
				}
			}

		});
		t1.start();
	}

	@GET
	@Path("{username}")
	@Produces("application/json")
	public Response getUserById(@PathParam("username") String username) {
		System.out.println(SERVER_PATH + File.separator + username);
		if(!(new File(SERVER_PATH + File.separator + username).exists())){
			new File(SERVER_PATH + File.separator + username).mkdir();
		}
		Crawler crawler = new Crawler(SERVER_PATH + File.separator + username, username);
		crawler.scanFiles();

		FilesInfo fi = new FilesInfo();
		fi.setUsername(username);
		fi.setFileNo(crawler.getFileList().size());
		fi.setFileList(crawler.getFileList());

		ObjectMapper mapper = new ObjectMapper();
		String fiJSON = "";
		try {
			fiJSON = mapper.writeValueAsString(fi);
			System.out.println(fiJSON);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).entity(fiJSON).build();

	}
//
//	@GET
//	@Path("/get/{username}")
//	// @Path("/get")
//	@Produces("application/json")
//	// @Produces(MediaType.APPLICATION_JSON)
//	public FilesInfo getMovieInJSON(@PathParam("username") String username) {
//		System.out.println(SERVER_PATH + File.separator + username);
//		Crawler crawler = new Crawler(SERVER_PATH + File.separator + username, username);
//		crawler.scanFiles();
//
//		FilesInfo fi = new FilesInfo();
//		fi.setUsername(username);
//		fi.setFileNo(crawler.getFileList().size());
//		fi.setFileList(crawler.getFileList());
//
//		return fi;
//	}

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
			Thread.sleep(10000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printQ();
		return Response.status(201).entity(result).build();
	}

	private void enqueue(FilesInfo fi) {
		for (File f : fi.getFileList()) {
			FileEvent fe = new FileEvent();
			fe.setDestinationDirectory(SERVER_PATH + File.separator + fi.getUsername() + File.separator);
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