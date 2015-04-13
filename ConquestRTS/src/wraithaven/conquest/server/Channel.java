package wraithaven.conquest.server;

import java.util.ArrayList;
import wraithaven.conquest.Packet;

public class Channel{
	private final boolean lobby;
	private final String name;
	private final ArrayList<Player> players;
	public Channel(boolean lobby, String name){
		this.lobby=lobby;
		this.name=name;
		players=new ArrayList(lobby?16:4);
	}
	public void addPlayer(Player player){ players.add(player); }
	public void removePlayer(Player player){ players.remove(player); }
	public String getName(){ return name; }
	public boolean isLobby(){ return lobby; }
	public void sendChannelPacket(Packet packet){ for(Player player : players)player.sendPacket(packet); }
}