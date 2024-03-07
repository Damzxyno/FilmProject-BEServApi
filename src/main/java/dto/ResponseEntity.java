package dto;
//package org.damzxyno.mmu.filmarchive.dto;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.NONE)
//public class ResponseEntity<T> {
//	@XmlElement
//	private T body;
//	@XmlElement
//	private String message = "Success";
//	@XmlElement
//	private int status = 200;
//	
//	public ResponseEntity() {}
//	public ResponseEntity(T body){
//		this.body = body;
//	}
//	public ResponseEntity (String errorMessage, int status) {
//		this.message = errorMessage;
//		this.status = status;
//	}
//	
//	public T getBody() {
//		return this.body;
//	}
//	
//	public String getMessage() {
//		return this.message;
//	}
//	
//	public int getStatus() {
//		return this.status;
//	}
//	
//	public static <T> ResponseEntity<T> success (T body){
//		return new ResponseEntity<T>(body);
//	}
//	
//	public static <T> ResponseEntity<T> error (String errorMessage, int status){
//		return new ResponseEntity<T>(errorMessage, status);
//	}
//}
