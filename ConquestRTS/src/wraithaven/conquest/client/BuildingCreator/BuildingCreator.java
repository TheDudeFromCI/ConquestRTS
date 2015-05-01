package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.awt.Toolkit;
import wraith.library.LWJGL.MainLoop;
import wraith.library.LWJGL.WindowInitalizer;

public class BuildingCreator{
	public static WindowInitalizer WINDOW_INIT;
	public static MainLoop loop;
	public static final int WORLD_BOUNDS_SIZE = 128;
	public static final boolean DEBUG = false;
	public static void launch(){
		loop=new MainLoop();
		loop.create(createInitalizer());
	}
	private static WindowInitalizer createInitalizer(){
		WINDOW_INIT=new WindowInitalizer();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		WINDOW_INIT.height=dimension.height;
		WINDOW_INIT.width=dimension.width;
		WINDOW_INIT.windowName="Building Creator";
		WINDOW_INIT.loopObjective=new Loop(dimension);
		WINDOW_INIT.fullscreen=true;
		return WINDOW_INIT;
	}
}