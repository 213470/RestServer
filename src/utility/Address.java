package utility;

public enum Address {
	LOCALHOST("localhost"), 
	PORT(":8080/"), 
	POST_JSON("file/post");

	private String value;

	Address(String value) {
		this.value = value;
	}

	String getValue() {
		return value;
	}

}
