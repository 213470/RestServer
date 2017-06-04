package server;

import java.io.File;
import java.util.Queue;

import model.FileEvent;

public class TrafficController implements Runnable {

	private Queue<FileEvent> queue;

	public TrafficController(Queue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
			while (true) {
				if (!queue.isEmpty()) {
					FileEvent fe = queue.poll();
					File f = new File(fe.getSourceDirectory() + File.separator + fe.getFilename());
					System.out.println(
							"[" + queue.size() + "]{" + Thread.currentThread() + "}Dequeued: " + fe.toString());
					System.out.println("Sleep for: 6s.");
					Thread.sleep(4000);
				} else {

					System.out.println("Sleep for: 6s.");
					Thread.sleep(6000);

				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
