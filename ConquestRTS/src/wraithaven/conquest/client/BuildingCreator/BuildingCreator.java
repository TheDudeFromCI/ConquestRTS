package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.awt.Toolkit;
import wraithaven.conquest.client.GameWorld.LoopControls.WindowInitalizer;
import wraithaven.conquest.client.GameWorld.LoopControls.MainLoop;

public class BuildingCreator{
	private WindowInitalizer init;
	private MainLoop loop;
	public static final int WORLD_BOUNDS_SIZE = 128;
	public static final boolean DEBUG = true;
	public void launch(){
		loop=new MainLoop();
		loop.create(createInitalizer());
	}
	private WindowInitalizer createInitalizer(){
		init=new WindowInitalizer();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		init.height=dimension.height;
		init.width=dimension.width;
		init.windowName="Building Creator";
		init.loopObjective=new Loop(dimension, this);
		init.fullscreen=true;
		return init;
	}
	long getWindow(){ return loop.getWindow(); }
	WindowInitalizer getInit(){ return init; }
}