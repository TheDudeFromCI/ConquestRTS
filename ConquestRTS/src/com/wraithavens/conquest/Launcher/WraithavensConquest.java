package com.wraithavens.conquest.Launcher;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.TitleScreen.TitleScreen;

public class WraithavensConquest extends EmptyLoop{
	public static void main(String[] args){
		programFolder = System.getProperty("user.dir")+File.separatorChar+"Data";
		assetFolder = programFolder+File.separatorChar+"Assets";
		saveFolder = programFolder+File.separatorChar+"Saves";
		chunkLoadFolder = programFolder+File.separatorChar+"ChunkLoad";
		saveFolder = defaultDirectory()+File.separatorChar+"Talantra Save Data";
		modelFolder = programFolder+File.separatorChar+"Models";
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
		GlError.dumpError();
		System.out.println("Version info:");
		System.out.println("  OpenGL version: "+GL11.glGetString(GL11.GL_VERSION));
		System.out.println("  LWJGL version: "+Sys.VERSION_MAJOR+"."+Sys.VERSION_MINOR);
		System.out.println("  LWJGL revision: "+Sys.VERSION_REVISION);
		System.out.println("  JNI: "+Sys.JNI_LIBRARY_NAME);
		System.out.println("  GPU: "+GL11.glGetString(GL11.GL_RENDERER));
		GlError.dumpError();
		int extentions = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
		System.out.println("  Extensions: "+extentions);
		for(int i = 0; i<extentions; i++)
			System.out.println("    "+GL30.glGetStringi(GL11.GL_EXTENSIONS, i));
		GlError.dumpError();
		System.out.println("  GLSL version: "+GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
		// int glslVersions =
		// GL11.glGetInteger(GL43.GL_NUM_SHADING_LANGUAGE_VERSIONS);
		// System.out.println("  Other supported GLSL versions: "+glslVersions);
		System.out.println("  Maximum Texture Size: "+GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
		System.out.println("End of version info.");
		GlError.dumpError();
	}
	static String programFolder;
	public static String assetFolder;
	public static String chunkLoadFolder;
	public static String currentGameFolder;
	// TODO Remove unused code found by UCDetector
	// public static String loadingScreenImagesFolder;
	public static String modelFolder;
	public static String saveFolder;
	public static WraithavensConquest INSTANCE;
	private Driver driver;
	private Driver newDriver;
	private double currentTime;
	private WraithavensConquest(WindowInitalizer init){
		super(init);
	}
	@Override
	public void cleanUp(){
		if(driver!=null)
			driver.dispose();
		GlError.dumpError();
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
		GlError.dumpError();
	}
	@Override
	public void mouse(long window, int button, int action){
		if(driver!=null)
			driver.onMouse(button, action);
		GlError.dumpError();
	}
	@Override
	public void mouseMove(long window, double xpos, double ypos){
		if(driver!=null)
			driver.onMouseMove(xpos, ypos);
		GlError.dumpError();
	}
	@Override
	public void mouseWheel(long window, double xPos, double yPos){
		if(driver!=null)
			driver.onMouseWheel(xPos, yPos);
		GlError.dumpError();
	}
	@Override
	public void preLoop(){
		System.out.println("Started OpenGL program.");
		WraithavensConquest.printContextInfo();
		super.preLoop();
		WraithavensConquest.INSTANCE = this;
		setDriver(new TitleScreen());
		GlError.dumpError();
	}
	public void render(){
		if(driver!=null)
			driver.render();
		if(newDriver!=null){
			if(driver!=null)
				driver.dispose();
			driver = newDriver;
			newDriver = null;
			driver.initalize(currentTime);
		}
		GlError.dumpError();
	}
	public void setDriver(Driver driver){
		newDriver = driver;
	}
	@Override
	public void update(double delta, double time){
		super.update(delta, time);
		if(driver!=null)
			driver.update(delta, time);
		currentTime = time;
		GlError.dumpError();
	}
}
