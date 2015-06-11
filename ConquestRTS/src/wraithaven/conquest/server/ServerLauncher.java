package wraithaven.conquest.server;

import wraithaven.conquest.Server;

public class ServerLauncher{
	static ChannelManager channelManager;
	static Server server;
	public static void main(String[] args){
		ServerLauncher.channelManager = new ChannelManager();
		ServerLauncher.server = new Server(10050, new PacketProcessor());
		new AdminPanel(ServerLauncher.channelManager);
	}
}