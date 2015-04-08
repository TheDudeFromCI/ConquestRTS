package me.ci;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import wraith.library.WorldManagement.TileGrid.Map;

public class MapView{
	private final JPanel panel;
	private final Map map;
	public MapView(Map m){
		map=m;
		panel=new JPanel(){
			@Override public void paint(Graphics g){
				map.render((Graphics2D)g);
				g.dispose();
			}
		};
		panel.setPreferredSize(new Dimension(map.getCameraScale()*map.getSizeX(), map.getCameraScale()*map.getSizeZ()));
	}
	public JPanel getPanel(){ return panel; }
}