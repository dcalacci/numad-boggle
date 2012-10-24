package edu.madcourse.dancalacci.boggle;

public class Multiplayer_Received_row_content {

	public String userName;
	public String acceptText = "Accept";
	public String rejectText = "Reject";

	public Multiplayer_Received_row_content( String userName ) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAcceptText() {
		return acceptText;
	}

	public String getRejectText() {
		return acceptText;
	}
}


