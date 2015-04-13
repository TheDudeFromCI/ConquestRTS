package wraithaven.conquest;

public interface Packet{
	public String compress();
	public void decode(String s);
	public PacketType getPacketType();
}