package com.wraithavens.conquest.Utility.Debug;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ColorConsole extends JFrame{
	private static final int EDGE_BUFFER = 3;
	private static final int SCROLL_BAR_WIDTH = 30;
	private static final int INPUT_HEIGHT = 25;
	private static final Color INPUT_COLOR = new Color(50, 50, 50);
	private static final Color SCROLL_BAR_BACKGROUND_COLOR = new Color(30, 30, 30);
	private static final Color SCROLL_BAR_FOREGROUND_COLOR = new Color(70, 70, 70);
	private static final int SCROLL_BAR_SIZE = 70;
	private static final int MaxLineCount = 100;
	private String text = "";
	private String typed = "";
	private int scroll;
	private int maximumScroll;
	private boolean carret = true;
	private boolean allowInput;
	private boolean moveDownRequest;
	private final ArrayList<ConsoleListener> consoleListeners = new ArrayList<>();
	public ColorConsole(boolean hasInput){
		allowInput = hasInput;
		setSize(500, 500);
		setLocationRelativeTo(null);
		setTitle("Debug Console");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(200, 200));
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e){
				if(!allowInput)
					return;
				if(e.getKeyChar()==KeyEvent.VK_BACK_SPACE){
					if(typed.length()>0)
						typed = typed.substring(0, typed.length()-1);
				}else if(e.getKeyChar()==KeyEvent.VK_ENTER){
					for(ConsoleListener consoleListener : consoleListeners)
						consoleListener.onCommandSent(typed);
					typed = "";
				}else
					typed += e.getKeyChar();
				repaint();
			}
		});
		addMouseWheelListener(new MouseAdapter(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				scroll += e.getWheelRotation()*10;
				repaint();
			}
		});
		new Timer().scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				if(!isDisplayable()){
					cancel();
					return;
				}
				carret = !carret;
				repaint();
			}
		}, 500, 500);
		add(new JPanel(){
			@Override
			public void paint(Graphics g1){
				Graphics2D g = (Graphics2D)g1;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setFont(new Font("Courier", Font.PLAIN, 15));
				boolean moveDown = moveDownRequest&&scroll==maximumScroll;
				moveDownRequest = false;
				calculateMaximumScroll(g.getFontMetrics().getHeight());
				if(moveDown)
					scroll = maximumScroll;
				g.setColor(Color.black);
				g.fillRect(0, 0, getWidth(), getHeight());
				renderString(text, g, EDGE_BUFFER, EDGE_BUFFER-scroll);
				if(allowInput){
					g.setColor(INPUT_COLOR);
					g.fillRect(0, getHeight()-INPUT_HEIGHT, getWidth(), INPUT_HEIGHT);
					g.setColor(Color.white);
					renderString(typed+(carret?"_":""), g, EDGE_BUFFER, getHeight()-INPUT_HEIGHT+EDGE_BUFFER);
				}
				g.setColor(SCROLL_BAR_BACKGROUND_COLOR);
				g.fillRect(getWidth()-SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH, getHeight()-INPUT_HEIGHT);
				g.setColor(SCROLL_BAR_FOREGROUND_COLOR);
				double scrollPercent = maximumScroll==0?0:scroll/(double)maximumScroll;
				g.fillRect(getWidth()-SCROLL_BAR_WIDTH,
					(int)((getHeight()-INPUT_HEIGHT-SCROLL_BAR_SIZE)*scrollPercent), SCROLL_BAR_WIDTH,
					SCROLL_BAR_SIZE);
				g.dispose();
			}
			private void calculateMaximumScroll(int charHeight){
				maximumScroll = 0;
				for(char c : text.toCharArray())
					if(c=='\n')
						maximumScroll += charHeight;
				maximumScroll -= getHeight()-EDGE_BUFFER-(allowInput?INPUT_HEIGHT:0);
				if(maximumScroll<0)
					maximumScroll = 0;
				if(scroll<0)
					scroll = 0;
				if(scroll>maximumScroll)
					scroll = maximumScroll;
			}
			private void renderString(String s, Graphics2D g, int tx, int ty){
				FontMetrics fm = g.getFontMetrics();
				float x = 0;
				float y = 0;
				boolean lookingForColor = false;
				for(char c : s.toCharArray()){
					if(c=='¥'){
						lookingForColor = true;
						continue;
					}
					if(lookingForColor){
						g.setColor(ChatColor.getBySymbol(c).getColor());
						lookingForColor = false;
						continue;
					}
					if(c=='\n'){
						x = 0;
						y += fm.getHeight();
						continue;
					}
					if(x+fm.charWidth(c)>=getWidth()-EDGE_BUFFER-EDGE_BUFFER-SCROLL_BAR_WIDTH){
						x = 0;
						y += fm.getHeight();
					}
					g.drawString(String.valueOf(c), x+tx, y+fm.getAscent()+ty);
					x += fm.charWidth(c);
				}
			}
		});
		setVisible(true);
	}
	public void addConsoleListener(ConsoleListener consoleListener){
		consoleListeners.add(consoleListener);
	}
	public void clear(){
		text = "";
		scroll = maximumScroll = 0;
		repaint();
	}
	public void print(String msg){
		if(msg.startsWith("¥"))
			text += msg;
		else
			text += ChatColor.WHITE+msg;
		trimBuffer();
		moveDownRequest = true;
		repaint();
	}
	public void println(String msg){
		print(msg+"\n");
	}
	public void removeConsoleListener(ConsoleListener consoleListener){
		consoleListeners.remove(consoleListener);
	}
	private int getLineCount(){
		int i = 0;
		for(char c : text.toCharArray())
			if(c=='\n')
				i++;
		return i;
	}
	private void trimBuffer(){
		int count = getLineCount();
		if(count>MaxLineCount){
			int clear = MaxLineCount-count;
			int cleared = 0;
			int charactersOff = 0;
			for(char c : text.toCharArray()){
				charactersOff++;
				if(c=='\n'){
					cleared++;
					if(cleared==clear)
						break;
				}
			}
			text = text.substring(charactersOff);
		}
	}
}
