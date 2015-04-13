package wraithaven.conquest;


public class ChatMessagePacket implements Packet{
	private String message;
	private String sender;
	private String code;
	public void setMessage(String sender, String message){
		this.message=message;
		this.sender=sender;
		this.code=sender+"¥"+message;
	}
	public void decode(String s){
		String[] p = s.split("¥");
		sender=p[0];
		message=p[1];
		code=s;
	}
	public String compress(){ return code; }
	public PacketType getPacketType(){ return PacketType.chatMessage; }
	public String getMessage(){ return message; }
	public String getSender(){ return sender; }
}