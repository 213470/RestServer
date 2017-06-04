package model;

public class UserInfo {

	String username;
	String homePath;

	public UserInfo(String username, String homePath) {
		super();
		this.username = username;
		this.homePath = homePath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

}
