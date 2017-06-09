package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Queue;

import model.FileEvent;

public class FileDownloader implements Runnable {

	private Queue<FileEvent> queue;

	public FileDownloader(Queue<FileEvent> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);

			if (!queue.isEmpty()) {
				FileEvent fe = queue.poll();
				File source = new File(fe.getSourceDirectory());
				File destination = new File(fe.getDestinationDirectory() + File.separator + fe.getFilename());
				try {
					Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("[" + queue.size() + "]{" + Thread.currentThread() + "}Dequeued: " + fe.toString());
//				 System.out.println("Sleep for: 5s.");
//				Thread.sleep(5000);
			} else {
//				 System.out.println("Sleep for: 15s.");
//				Thread.sleep(15000);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
