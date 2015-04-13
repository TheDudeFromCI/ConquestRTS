package wraithaven.conquest;

public class PrepareMediaSendPacket implements Packet{
	private String fileName;
	public String compress(){ return fileName; }
	public void decode(String s){ fileName=s; }
	public PacketType getPacketType(){ return PacketType.prepareMediaSend; }
	public void setFileName(String fileName){ this.fileName=fileName; }
	public String getFileName(){ return fileName; }
}