package wraithaven.conquest;

public class HandshakePacket implements Packet{
	private static final String MESSAGE = "Wraithaven's Conquest; Handshake";
	private boolean correctFormat;
	public String compress(){
		return MESSAGE;
	}
	public void decode(String s){
		correctFormat = s.equals(MESSAGE);
	}
	public PacketType getPacketType(){
		return PacketType.handshake;
	}
	public boolean isCorrectFormat(){
		return correctFormat;
	}
}
