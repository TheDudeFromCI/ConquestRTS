package wraithaven.conquest.server;

import java.io.PrintWriter;
import wraithaven.conquest.ClientInstance;
import wraithaven.conquest.KickMessagePacket;
import wraithaven.conquest.Packet;

public class ConnectedClient{
	public final ClientInstance client;
	public final PrintWriter out;
	public ConnectedClient(ClientInstance client, PrintWriter out){
		this.client = client;
		this.out = out;
	}
	public void kick(){
		ServerLauncher.server.kickClient(client);
	}
	public void kick(String msg){
		KickMessagePacket packet = new KickMessagePacket();
		packet.setMessage(msg);
		sendPacket(packet);
		kick();
	}
	public void sendPacket(Packet packet){
		out.println(packet.getPacketType().getHexId()+packet.compress());
	}
}