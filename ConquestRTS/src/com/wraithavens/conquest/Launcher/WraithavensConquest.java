package com.wraithavens.conquest.Launcher;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.TitleScreen.TitleScreen;

public class WraithavensConquest extends EmptyLoop{
	public static void main(String[] args){
		programFolder = System.getProperty("user.dir")+File.separatorChar+"Data";
		assetFolder = programFolder+File.separatorChar+"Assets";
		saveFolder = programFolder+File.separatorChar+"Saves";
		saveFolder = defaultDirectory()+File.separatorChar+"Talantra Save Data";
		modelFolder = programFolder+File.separatorChar+"Models";
		loadingScreenImagesFolder = programFolder+File.separatorChar+"Loading Screen Images";
		// ---
		// TODO Remove this later, once game saving and loading is in.
		// ---
		currentGameFolder = saveFolder+File.separatorChar+"Pre-Alpha";
		WindowInitalizerBuilder builder = new WindowInitalizerBuilder();
		new WraithavensConquest(builder.build());
		System.exit(0);
	}
	private static String defaultDirectory(){
		JFileChooser fr = new JFileChooser();
		FileSystemView fw = fr.getFileSystemView();
		return fw.getDefaultDirectory().getAbsolutePath();
	}
	private static void printContextInfo(){
		System.out.println("Version info:");
		System.out.println("  Talantra Version: "+Version);
		System.out.println("  OpenGL version: "+GL11.glGetString(GL11.GL_VERSION));
		System.out.println("  LWJGL version: "+Sys.VERSION_MAJOR+"."+Sys.VERSION_MINOR);
		System.out.println("  LWJGL revision: "+Sys.VERSION_REVISION);
		System.out.println("  JNI: "+Sys.JNI_LIBRARY_NAME);
		System.out.println("  GPU: "+GL11.glGetString(GL11.GL_RENDERER));
		int extentions = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
		System.out.println("  Extensions: "+extentions);
		String s = "";
		for(int i = 0; i<extentions; i++)
			s += (i==0?"":", ")+GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
		System.out.println("    "+s);
		System.out.println("  GLSL version: "+GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
		System.out.println("  Maximum Texture Size: "+GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
		System.out.println("  Maximum 3D Texture Size: "+GL11.glGetInteger(GL12.GL_MAX_3D_TEXTURE_SIZE));
		System.out.println("  Max VBO Vertices: "+GL11.glGetInteger(GL12.GL_MAX_ELEMENTS_VERTICES));
		System.out.println("  Max VBO Indices: "+GL11.glGetInteger(GL12.GL_MAX_ELEMENTS_INDICES));
		System.out.println("End of version info.");
	}
	private static String programFolder;
	public static String assetFolder;
	public static String currentGameFolder;
	public static String loadingScreenImagesFolder;
	public static String modelFolder;
	static String saveFolder;
	public static WraithavensConquest INSTANCE;
	/**
	 * Version Breakdown: <br>
	 * Major Version - The major version release. 0 For pre-release, or 1 for
	 * offical release. <br>
	 * Minor Version - The minor version. This is the milestone id.<br>
	 * Progress Version - This the progress state for that milestone. <br>
	 * Bugfix State - This is the current bugfix state for the progress version.
	 */
	public static final String Version = "v0.1.02.0";
	private Driver driver;
	private double currentTime;
	private WraithavensConquest(WindowInitalizer init){
		super(init);
	}
	@Override
	public void cleanUp(){
		if(driver!=null)
			driver.dispose();
	}
	public int getScreenHeight(){
		return init.height;
	}
	public int getScreenWidth(){
		return init.width;
	}
	public long getWindow(){
		return mainLoop.getWindow();
	}
	@Override
	public void key(long window, int key, int action){
		if(driver!=null)
			driver.onKey(key, action);
	}
	@Override
	public void mouse(long window, int button, int action){
		if(driver!=null)
			driver.onMouse(button, action);
	}
	@Override
	public void mouseMove(long window, double xpos, double ypos){
		if(driver!=null)
			driver.onMouseMove(xpos, ypos);
	}
	@Override
	public void mouseWheel(long window, double xPos, double yPos){
		if(driver!=null)
			driver.onMouseWheel(xPos, yPos);
	}
	@Override
	public void preLoop(){
		WraithavensConquest.printContextInfo();
		super.preLoop();
		WraithavensConquest.INSTANCE = this;
		setDriver(new TitleScreen());
	}
	public void render(){
		if(driver!=null)
			driver.render();
	}
	public void setDriver(Driver newDriver){
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				if(driver!=null)
					driver.dispose();
				driver = newDriver;
				driver.initalize(currentTime);
			}
		});
	}
	@Override
	public void update(double delta, double time){
		super.update(delta, time);
		if(driver!=null)
			driver.update(delta, time);
		currentTime = time;
	}
}
