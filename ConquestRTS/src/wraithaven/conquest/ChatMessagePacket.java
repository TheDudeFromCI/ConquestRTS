package wraithaven.conquest;

public class ChatMessagePacket implements Packet{
	private String code;
	private String message;
	private String sender;
	public String compress(){
		return code;
	}
	public void decode(String s){
		code = s;
		char[] chars = s.toCharArray();
		for(int i = 0; i<s.length(); i++){
			if(chars[i]=='¤'){
				sender = s.substring(0, i);
				message = s.substring(i+1);
				return;
			}
		}
	}
	public String getMessage(){
		return message;
	}
	public PacketType getPacketType(){
		return PacketType.chatMessage;
	}
	public String getSender(){
		return sender;
	}
	public void setMessage(String sender, String message){
		this.message = message;
		this.sender = sender;
		code = sender+"¤"+message;
	}
}