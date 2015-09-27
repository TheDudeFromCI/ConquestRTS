package com.wraithavens.conquest.Utility;

import java.io.File;
import java.text.NumberFormat;
import com.wraithavens.conquest.Launcher.WraithavensConquest;

public class Algorithms{
	public static String formatBytes(long bytes){
		if(bytes<1024L*2L)
			return bytes+" B";
		if(bytes<1024L*1024L*2L)
			return NumberFormat.getInstance().format(bytes/1024.0)+" kB";
		if(bytes<1024L*1024L*1024L*2L)
			return NumberFormat.getInstance().format(bytes/1024.0/1024.0)+" mB";
		return NumberFormat.getInstance().format(bytes/1024.0/1024.0/1024.0)+" gB";
	}
	public static File getChunkHeightsPath(int x, int z){
		return getLandscapePath(x, z, 64, ChunkHeightsId);
	}
	public static File getChunkPath(int x, int y, int z){
		return getLandscapePath(x, y, z, 64, ChunkId);
	}
	public static File getDistantEntityListPath(int x, int z){
		return getLandscapePath(x, z, 2048, DistantEntityListId);
	}
	public static File getDynmapFile(int x, int z){
		return getLandscapePath(x, z, 8192, DynmapId);
	}
	public static File getMassChunkHeightsPath(int x, int z){
		return getLandscapePath(x, z, 8192, MassChunkHeightsId);
	}
	public static int groupLocation(int x, int w){
		return x>=0?x/w*w:(x-(w-1))/w*w;
	}
	public static float random(float max){
		return Math.min((float)(Math.random()*max), max-0.00001f);
	}
	private static File getLandscapePath(int x, int z, int size, char id){
		String s = WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape";
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		s += File.separatorChar+(a1+","+b1);
		if(size<8192){
			a1 = Algorithms.groupLocation(x, 8192);
			b1 = Algorithms.groupLocation(z, 8192);
			s += File.separatorChar+(a1+","+b1);
		}
		if(size<2048){
			a1 = Algorithms.groupLocation(x, 2048);
			b1 = Algorithms.groupLocation(z, 2048);
			s += File.separatorChar+(a1+","+b1);
		}
		if(size<512){
			a1 = Algorithms.groupLocation(x, 512);
			b1 = Algorithms.groupLocation(z, 512);
			s += File.separatorChar+(a1+","+b1);
		}
		File file = new File(s, x+","+z+id+".dat");
		file.getParentFile().mkdirs();
		return file;
	}
	private static File getLandscapePath(int x, int y, int z, int size, char id){
		String s = WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape";
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		s += File.separatorChar+(a1+","+b1);
		if(size<8192){
			a1 = Algorithms.groupLocation(x, 8192);
			b1 = Algorithms.groupLocation(z, 8192);
			s += File.separatorChar+(a1+","+b1);
		}
		if(size<2048){
			a1 = Algorithms.groupLocation(x, 2048);
			b1 = Algorithms.groupLocation(z, 2048);
			s += File.separatorChar+(a1+","+b1);
		}
		if(size<512){
			a1 = Algorithms.groupLocation(x, 512);
			b1 = Algorithms.groupLocation(z, 512);
			s += File.separatorChar+(a1+","+b1);
		}
		File file = new File(s, x+","+y+","+z+id+".dat");
		file.getParentFile().mkdirs();
		return file;
	}
	private static final char ChunkId = 'a';
	private static final char DynmapId = 'b';
	private static final char ChunkHeightsId = 'c';
	private static final char MassChunkHeightsId = 'd';
	private static final char DistantEntityListId = 'e';
}
