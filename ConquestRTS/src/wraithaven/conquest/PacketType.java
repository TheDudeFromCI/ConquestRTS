package wraithaven.conquest;


public enum PacketType{
	chatMessage(ChatMessagePacket.class),
	handshake(HandshakePacket.class),
	kickMessage(KickMessagePacket.class);
	private final String hexId;
	private final Class packetClass;
	private PacketType(Class packetClass){
		hexId=getHexCode(ordinal());
		this.packetClass=packetClass;
	}
	public String getHexId(){ return hexId; }
	public static Packet create(String code){
		try{
			Packet packet;
			for(PacketType pt : values()){
				if(code.startsWith(pt.hexId)){
					packet=(Packet)pt.packetClass.newInstance();
					packet.decode(code.substring(3));
					return packet;
				}
			}
		}catch(Exception exception){ exception.printStackTrace(); }
		return null;
	}
	private static String getHexCode(int id){
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toHexString(id));
		while(sb.length()<3)sb.insert(0, '0');
		return sb.toString();
	}
}