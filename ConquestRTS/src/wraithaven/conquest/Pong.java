package wraithaven.conquest;

public class Pong{
	private int playerCount;
	private int maxPlayerCount;
	private int channelCount;
	private int maxChannelCount;
	private String name;
	private String motd;
	private String code;
	public Pong(String s)throws Exception{
		if(!s.startsWith("Pong"))throw new Exception("Unknown format!");
		String[] parts = s.substring(7).split("¤");
		playerCount=Integer.valueOf(parts[0]);
		maxPlayerCount=Integer.valueOf(parts[1]);
		channelCount=Integer.valueOf(parts[2]);
		maxChannelCount=Integer.valueOf(parts[3]);
		name=parts[4];
		motd=parts[5];
	}
	public Pong(int playerCount, int maxPlayerCount, int channelCount, int maxChannelCount, String name, String motd){
		this.playerCount=playerCount;
		this.maxPlayerCount=maxPlayerCount;
		this.channelCount=channelCount;
		this.maxChannelCount=maxChannelCount;
		this.name=name;
		this.motd=motd;
		StringBuilder sb = new StringBuilder();
		sb.append("Pong");
		sb.append(playerCount).append('¤');
		sb.append(maxPlayerCount).append('¤');
		sb.append(channelCount).append('¤');
		sb.append(maxChannelCount).append('¤');
		sb.append(name).append('¤');
		sb.append(motd);
		code=sb.toString();
	}
	public String getCode(){ return code; }
	public int getPlayerCount(){ return playerCount; }
	public int getMaxPlayerCount(){ return maxPlayerCount; }
	public int getChannelCount(){ return channelCount; }
	public int getMaxChannelCount(){ return maxChannelCount; }
	public String getName(){ return name; }
	public String getMOTD(){ return motd; }
}