package wraithaven.conquest;

public class PingPacket implements Packet{
	public String compress(){
		return "";
	}
	public void decode(String s){}
	public PacketType getPacketType(){
		return PacketType.ping;
	}
}