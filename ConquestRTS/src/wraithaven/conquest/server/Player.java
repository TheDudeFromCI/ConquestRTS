package wraithaven.conquest.server;

import java.io.File;
import java.io.PrintWriter;
import wraithaven.conquest.MediaSendPacket;
import wraithaven.conquest.PrepareMediaSendPacket;
import wraithaven.conquest.KickMessagePacket;
import wraithaven.conquest.Packet;
import wraith.library.Multiplayer.ClientInstance;

public class Player{
	private boolean awaitingHandshake = true;
	private final String name;
	private final ClientInstance client;
	private final PrintWriter outputStream;
	public Player(ClientInstance client, PrintWriter outputStream){
		this.client=client;
		this.outputStream=outputStream;
		name="Player";  //TODO Get actual name.
	}
	@Override public boolean equals(Object o){
		if(o instanceof Player)return o==this;
		if(o instanceof ClientInstance)return o==client;
		return false;
	}
	public void kick(String message){
		KickMessagePacket packet = new KickMessagePacket();
		packet.setMessage(message);
		sendPacket(packet);
		ServerLauncher.server.kickClient(client);
	}
	public void sendFile(File file){
		PrepareMediaSendPacket packet1 = new PrepareMediaSendPacket();
		packet1.setFileName(file.getName());
		sendPacket(packet1);
		MediaSendPacket packet2 = new MediaSendPacket();
		packet2.encode(file);
		sendPacket(packet2);
	}
	public void sendPacket(Packet packet){ outputStream.println(packet.getPacketType().getHexId()+packet.compress()); }
	public String getName(){ return name; }
	public boolean awaitingHandshake(){ return awaitingHandshake; }
	public void shakeHand(){ awaitingHandshake=false; }
}