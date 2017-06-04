package utility;

public class Dispatcher {
	
	private final static String SERVER = "server";
	
	private String serverPath;
	
	
	public Dispatcher(String serverPath){
		this.setServerPath(serverPath);
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	
	public String getServer(int x) {
		return SERVER+x;
	}

}
