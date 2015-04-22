package wraithaven.conquest.server;

import wraithaven.conquest.Packet;

public class Player{
	private final String name;
	private final ConnectedClient client;
	public Player(ConnectedClient client){
		this.client=client;
		name="Player";  //TODO Get actual name.
	}
	public void kick(String message){ client.kick(message); }
	public void kick(){ client.kick(); }
	public void sendPacket(Packet packet){ client.sendPacket(packet); }
	public String getName(){ return name; }
	public ConnectedClient getClient(){ return client; }
}