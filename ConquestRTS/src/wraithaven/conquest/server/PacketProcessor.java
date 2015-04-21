package wraithaven.conquest.server;

import java.io.PrintWriter;
import wraithaven.conquest.HandshakePacket;
import wraithaven.conquest.Packet;
import wraithaven.conquest.PacketType;
import wraith.library.Multiplayer.ClientInstance;
import wraith.library.Multiplayer.ServerListener;

public class PacketProcessor implements ServerListener{
	public void recivedInput(ClientInstance client, String s){
		Packet packet = PacketType.create(s);
		Player player = ServerLauncher.channelManager.getPlayer(client);
		if(packet==null){
			player.kick("Sent unknown packet.");
			return;
		}
		if(packet.getPacketType()==PacketType.ping){
			player.kick(ServerLauncher.channelManager.createPingPacket().getCode());
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
		ServerLauncher.channelManager.getLobby().sendChannelPacket(packet);
	}
	public void serverClosed(){ System.exit(0); }
	public void clientDisconnected(ClientInstance client){ ServerLauncher.channelManager.removePlayer(ServerLauncher.channelManager.getPlayer(client)); }
	public void clientConncted(ClientInstance client, PrintWriter out){ ServerLauncher.channelManager.addPlayer(new Player(client, out)); }
}