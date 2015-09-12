package com.wraithavens.conquest.WorldGeneration;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.Algorithms;

public class WorldFiles{
	private final StringBuilder sb = new StringBuilder();
	public File getChunkStack(int x, int z){
		newFile();
		findDirectory(x, z, 64);
		end(x, z, "a");
		return compile();
	}
	public File getTempChunkLayer(int x, int z, int stage){
		newFile();
		findDirectory(x, z, 64);
		end(x, z, "Temp"+stage);
		File file = compile();
		file.deleteOnExit();
		return file;
	}
	private File compile(){
		File file = new File(sb.toString());
		file.getParentFile().mkdirs();
		return file;
	}
	private void end(int x, int z, String id){
		sb.append(x);
		sb.append(',');
		sb.append(z);
		sb.append(id);
		sb.append(".dat");
	}
	private void findDirectory(int x, int z, int size){
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		sb.append(File.separatorChar);
		sb.append(a1);
		sb.append(',');
		sb.append(b1);
		if(size<8192){
			a1 = Algorithms.groupLocation(x, 8192);
			b1 = Algorithms.groupLocation(z, 8192);
			sb.append(File.separatorChar);
			sb.append(a1);
			sb.append(',');
			sb.append(b1);
		}
		if(size<2048){
			a1 = Algorithms.groupLocation(x, 2048);
			b1 = Algorithms.groupLocation(z, 2048);
			sb.append(File.separatorChar);
			sb.append(a1);
			sb.append(',');
			sb.append(b1);
		}
		if(size<512){
			a1 = Algorithms.groupLocation(x, 512);
			b1 = Algorithms.groupLocation(z, 512);
			sb.append(File.separatorChar);
			sb.append(a1);
			sb.append(',');
			sb.append(b1);
		}
	}
	private void newFile(){
		sb.setLength(0);
		sb.append(WraithavensConquest.currentGameFolder);
		sb.append(File.separatorChar);
		sb.append("World");
	}
}
