package wraithaven.conquest.server;

import wraith.library.Multiplayer.Server;

public class ServerLauncher{
	public static Server server;
	public static ChannelManager channelManager;
	public static void main(String[] args){
		channelManager=new ChannelManager();
		server=new Server(10050, new PacketProcessor());
		new AdminPanel(channelManager);
	}
}