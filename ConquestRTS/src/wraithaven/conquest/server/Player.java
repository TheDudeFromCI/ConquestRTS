package wraithaven.conquest.server;

import wraithaven.conquest.Packet;

public class Player{
	private final ConnectedClient client;
	private final String name;
	public Player(ConnectedClient client){
		this.client = client;
		name = "Player"; // TODO Get actual name.
	}
	public ConnectedClient getClient(){
		return client;
	}
	public String getName(){
		return name;
	}
	public void kick(){
		client.kick();
	}
	public void kick(String message){
		client.kick(message);
	}
	public void sendPacket(Packet packet){
		client.sendPacket(packet);
	}
}