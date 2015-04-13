package wraithaven.conquest.server;

import java.io.PrintWriter;
import wraithaven.conquest.HandshakePacket;
import wraithaven.conquest.Packet;
import wraithaven.conquest.PacketType;
import wraith.library.Multiplayer.ClientInstance;
import wraith.library.Multiplayer.ServerListener;
import wraith.library.Multiplayer.Server;

public class ServerLauncher{
	public static Server server;
	public static void main(String[] args){
		final ChannelManager channelManager = new ChannelManager();
		server=new Server(10050, new ServerListener(){
			public void recivedInput(ClientInstance client, String s){
				Packet packet = PacketType.create(s);
				Player player = channelManager.getPlayer(client);
				if(packet==null){
					player.kick("Sent unknown packet.");
					return;
				}
				if(packet.getPacketType()==PacketType.handshake){
					if(((HandshakePacket)packet).isCorrectFormat()){
						if(player.awaitingHandshake())player.shakeHand();
						else player.kick("Tried to handshake twice.");
					}else player.kick("Wrong handshake format.");
					return;
				}else{
					if(player.awaitingHandshake()){
						player.kick("Failed to handshake.");
						return;
					}
					//TODO Process other packets.
				}
				channelManager.getLobby().sendChannelPacket(packet);
			}
			public void serverClosed(){ System.exit(0); }
			public void clientDisconnected(ClientInstance client){ channelManager.removePlayer(channelManager.getPlayer(client)); }
			public void clientConncted(ClientInstance client, PrintWriter out){ channelManager.addPlayer(new Player(client, out)); }
		});
		new AdminPanel(channelManager);
	}
}