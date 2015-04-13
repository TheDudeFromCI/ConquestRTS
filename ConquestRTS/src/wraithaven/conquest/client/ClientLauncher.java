package wraithaven.conquest.client;

import wraithaven.conquest.ChatMessagePacket;
import wraithaven.conquest.KickMessagePacket;
import wraithaven.conquest.Packet;
import wraithaven.conquest.PacketType;
import wraithaven.conquest.HandshakePacket;
import wraith.library.WindowUtil.ChatColor;
import wraith.library.Multiplayer.ClientListener;
import wraith.library.Multiplayer.Client;
import wraith.library.WindowUtil.ConsoleListener;
import wraith.library.WindowUtil.ColorConsole;

public class ClientLauncher{
	private static Client client;
	private static ColorConsole console;
	public static void main(String[] args){
		console=new ColorConsole(true);
		console.setTitle("Wraithaven's Conquest");
		console.addConsoleListener(new ConsoleListener(){
			public void onCommandSent(String msg){ client.send("000Player¤"+msg); }
		});
		client=new Client("localhost", 10050, new ClientListener(){
			public void recivedInput(String s){
				Packet packet = PacketType.create(s);
				if(packet==null)return;
				if(packet instanceof KickMessagePacket){
					console.println(ChatColor.ORANGE+((KickMessagePacket)packet).getMessage());
					return;
				}
				if(packet instanceof ChatMessagePacket){
					ChatMessagePacket chatPacket = (ChatMessagePacket)packet;
					console.println(ChatColor.YELLOW+chatPacket.getSender()+": "+chatPacket.getMessage());
					return;
				}
			}
			public void unknownHost(){ console.println(ChatColor.RED+"Unknown host."); }
			public void serverClosed(){ console.println(ChatColor.RED+"Server closed."); }
			public void disconnected(){ console.println(ChatColor.RED+"Disconnected from server."); }
			public void couldNotConnect(){ console.println(ChatColor.RED+"Could not connect to server."); }
			public void connectedToServer(){ console.println(ChatColor.GREEN+"Connected to server."); }
		});
		if(client.isConnected())client.send("001"+new HandshakePacket().compress());
	}
}