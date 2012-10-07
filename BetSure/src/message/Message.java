package message;

public class Message {

	private Object content = null;
	
	public Message() {
		super();
	}
	
	public Message(String s) {
		this.content = s;
	}
	
	public Object getContent() {
		return this.content;
	}
}
