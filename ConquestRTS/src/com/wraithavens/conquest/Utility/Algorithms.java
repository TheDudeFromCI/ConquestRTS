package com.wraithavens.conquest.Utility;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;

public class Algorithms{
	public static File getChunkHeightsPath(int x, int z){
		String s = WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape";
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		s += File.separatorChar+(a1+","+b1);
		a1 = Algorithms.groupLocation(x, 8192);
		b1 = Algorithms.groupLocation(z, 8192);
		s += File.separatorChar+(a1+","+b1);
		a1 = Algorithms.groupLocation(x, 2048);
		b1 = Algorithms.groupLocation(z, 2048);
		s += File.separatorChar+(a1+","+b1);
		a1 = Algorithms.groupLocation(x, 512);
		b1 = Algorithms.groupLocation(z, 512);
		s += File.separatorChar+(a1+","+b1);
		File file = new File(s, x+","+z+".dat");
		file.getParentFile().mkdirs();
		return file;
	}
	/**
	 * Gets the file inside it's correct directory for the requested chunk.
	 */
	public static File getChunkPath(int x, int y, int z){
		String s = WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape";
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		s += File.separatorChar+(a1+","+b1);
		a1 = Algorithms.groupLocation(x, 8192);
		b1 = Algorithms.groupLocation(z, 8192);
		s += File.separatorChar+(a1+","+b1);
		a1 = Algorithms.groupLocation(x, 2048);
		b1 = Algorithms.groupLocation(z, 2048);
		s += File.separatorChar+(a1+","+b1);
		a1 = Algorithms.groupLocation(x, 512);
		b1 = Algorithms.groupLocation(z, 512);
		s += File.separatorChar+(a1+","+b1);
		File file = new File(s, x+","+y+","+z+".dat");
		file.getParentFile().mkdirs();
		return file;
	}
	/**
	 * Gets the heightmap file inside it's correct directory.
	 */
	public static File getHeightmapFile(int x, int z){
		int offset = (Dynmap.BlocksPerChunk-Dynmap.WalkingWrapDistance)/2;
		x += offset;
		z += offset;
		String s = WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape";
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		s += File.separatorChar+(a1+","+b1);
		File file = new File(s, x+","+z+".dat");
		file.getParentFile().mkdirs();
		return file;
	}
	public static File getMassChunkHeightsPath(int x, int z){
		String s = WraithavensConquest.currentGameFolder+File.separatorChar+"Landscape";
		int a1, b1;
		a1 = Algorithms.groupLocation(x, 32768);
		b1 = Algorithms.groupLocation(z, 32768);
		s += File.separatorChar+(a1+","+b1);
		File file = new File(s, x+","+z+"a.dat");
		file.getParentFile().mkdirs();
		return file;
	}
	/**
	 * This function takes any value, <i>x</i>, and groups it into evenly sized
	 * chunks.
	 *
	 * @param x
	 *            - The location.
	 * @param w
	 *            - The chunk size.
	 * @return The grouped value of x.
	 */
	public static int groupLocation(int x, int w){
		return x>=0?x/w*w:(x-(w-1))/w*w;
	}
}
