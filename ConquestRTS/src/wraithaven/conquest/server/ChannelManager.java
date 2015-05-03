package wraithaven.conquest.server;

import java.util.ArrayList;
import java.util.HashMap;
import wraithaven.conquest.ClientInstance;
import wraithaven.conquest.Pong;

public class ChannelManager{
	private HashMap<Player,Channel> players = new HashMap();
	private ArrayList<Channel> channels = new ArrayList(5);
	public ChannelManager(){
		channels.add(new Channel(true, "Lobby"));
		//TODO load other channels.
	}
	public void addPlayer(Player player){
		players.put(player, getLobby());
		getLobby().addPlayer(player);
	}
	public void removePlayer(Player player){
		players.get(player).removePlayer(player);
		players.remove(player);
	}
	public void setChannel(Player player, Channel channel){
		players.get(player).removePlayer(player);
		players.put(player, channel);
		channel.addPlayer(player);
	}
	public Player getPlayer(ClientInstance client){
		for(Player player : players.keySet())if(player.getClient().client==client)return player;
		return null;
	}
	public Pong createPingPacket(){
		return new Pong(players.size(), 0, 0, channels.size(), "CI's Testing Grounds", "I'm still coding.");
	}
	public Channel getLobby(){ return channels.get(0); }
}