package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.awt.Toolkit;
import wraith.library.LWJGL.MainLoop;
import wraith.library.LWJGL.WindowInitalizer;

public class BuildingCreator{
	public static MainLoop loop;
	public static final int WORLD_BOUNDS_SIZE = 128;
	public static final boolean DEBUG = false;
	public static void main(String[] args){
		loop=new MainLoop();
		loop.create(createInitalizer());
	}
	private static WindowInitalizer createInitalizer(){
		WindowInitalizer init = new WindowInitalizer();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		init.height=dimension.height;
		init.width=dimension.width;
		init.windowName="Building Creator";
		init.loopObjective=new Loop(dimension);
		init.fullscreen=true;
		return init;
	}
}