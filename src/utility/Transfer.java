package utility;

public class Transfer {

	private static int PORT = 6067;

	private static int counter = 0;

	public static int incrementPort() {
		counter++;
		PORT = counter % 2 == 0 ? PORT + 1 : PORT;
		if (PORT == 6100) {
			PORT = 6067;
		}
		return PORT;
	}

	public static int getPORT() {
		return PORT;
	}

}
