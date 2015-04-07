package me.ci;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import me.ci.World.Map;
import me.ci.World.WorldPopulator;

public class Loader{
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setTitle("Conquest RTS");
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		WorldPopulator worldPopulator = new WorldPopulator();
		final Map map = new Map(15, 1, 15, worldPopulator);
		map.setCameraScale(32);
		frame.add(new JPanel(){
			@Override public void paint(Graphics g){
				map.render((Graphics2D)g);
				g.dispose();
			}
		});
		frame.setVisible(true);
		while(true)frame.repaint();
	}
}