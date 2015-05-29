package wraithaven.conquest;

public class KickMessagePacket implements Packet{
	private String message;
	public String compress(){
		return message;
	}
	public void decode(String s){}
	public String getMessage(){
		return message;
	}
	public PacketType getPacketType(){
		return PacketType.kickMessage;
	}
	public void setMessage(String message){
		this.message = message;
	}
}