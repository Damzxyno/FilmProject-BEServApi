package dto;


public interface CustomPlainTextSerializable<T>{
	String toPlainText();
	T fromPlainText(String plainText);
}
