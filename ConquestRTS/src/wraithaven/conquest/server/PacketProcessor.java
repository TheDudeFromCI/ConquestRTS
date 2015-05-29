package wraithaven.conquest.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import wraithaven.conquest.ClientInstance;
import wraithaven.conquest.HandshakePacket;
import wraithaven.conquest.Packet;
import wraithaven.conquest.PacketType;
import wraithaven.conquest.ServerListener;

public class PacketProcessor implements ServerListener{
	private ArrayList<ConnectedClient> pendingClients = new ArrayList();
	public void clientConncted(ClientInstance client, PrintWriter out){
		pendingClients.add(new ConnectedClient(client, out));
	}
	public void clientDisconnected(ClientInstance client){
		Player player = ServerLauncher.channelManager.getPlayer(client);
		if(player!=null) ServerLauncher.channelManager.removePlayer(player);
	}
	private ConnectedClient getClient(ClientInstance client){
		for(int i = 0; i<pendingClients.size(); i++)
			if(pendingClients.get(i).client==client) return pendingClients.get(i);
		return null;
	}
	public void recivedInput(ClientInstance client, String s){
		Packet packet = PacketType.create(s);
		if(packet==null){
			Player player = ServerLauncher.channelManager.getPlayer(client);
			if(player!=null){
				player.kick("Sent unknown packet.");
				return;
			}
			ConnectedClient c = getClient(client);
			if(c!=null){
				c.kick();
				pendingClients.remove(c);
			}else ServerLauncher.server.kickClient(client); // Running this
															// method is a last
															// resort, and may
															// lead to possible
															// errors.
			return;
		}
		if(packet.getPacketType()==PacketType.ping){
			ConnectedClient c = getClient(client);
			if(c!=null){
				c.out.println(ServerLauncher.channelManager.createPingPacket().getCode());
				c.kick();
				pendingClients.remove(c);
			}else ServerLauncher.server.kickClient(client);
			return;
		}
		if(packet.getPacketType()==PacketType.handshake){
			if(((HandshakePacket)packet).isCorrectFormat()){
				ConnectedClient c = getClient(client);
				if(c!=null){
					pendingClients.remove(c);
					ServerLauncher.channelManager.addPlayer(new Player(c));
				}else{
					Player player = ServerLauncher.channelManager.getPlayer(client);
					if(player!=null) player.kick("Tried to handshake twice.");
					else ServerLauncher.server.kickClient(client);
				}
			}else{
				ConnectedClient c = getClient(client);
				if(c!=null){
					pendingClients.remove(c);
					c.kick("Wrong handshake format.");
				}else{
					Player player = ServerLauncher.channelManager.getPlayer(client);
					if(player!=null) player.kick("Wrong handshake format.");
					else ServerLauncher.server.kickClient(client);
				}
			}
			return;
		}
		ConnectedClient pending = getClient(client);
		if(pending!=null){
			pendingClients.remove(pending);
			pending.kick("Failed to handshake.");
			return;
		}
		// TODO Process other packets.
		ServerLauncher.channelManager.getLobby().sendChannelPacket(packet);
	}
	public void serverClosed(){
		System.exit(0);
	}
}