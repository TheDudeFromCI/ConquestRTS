package me.ci;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow{
	private final JFrame frame;
	public GameWindow(){
		frame=new JFrame();
		frame.setTitle("Conquest RTS");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public void setPanel(JPanel panel){
		if(frame.getContentPane().getComponentCount()>0)frame.getContentPane().remove(0);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.repaint();
	}
}