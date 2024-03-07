package dto;



public class ErrorMessage {
	
	private String message;
	
	private int errorCode;
	
	private String documentation;

	public ErrorMessage() {}
	public ErrorMessage(String message, int errorCode, String documentation) {
		this.message = message;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public String getDocumentation() {
		return this.documentation;
	}
}
