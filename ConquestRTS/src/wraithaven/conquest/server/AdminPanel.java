package wraithaven.conquest.server;

import java.awt.Dimension;
import javax.swing.JFrame;

public class AdminPanel{
	private final ChannelManager channelManager;
	private final JFrame frame;
	public AdminPanel(ChannelManager channelManager){
		this.channelManager = channelManager;
		frame = new JFrame();
		frame.setTitle("Wraithaven's Conquest (Server)");
		frame.setSize(700, 500);
		frame.setMinimumSize(new Dimension(400, 350));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public ChannelManager getChannelManager(){
		return channelManager;
	}
}