package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Utility.BinaryFileUtil;

class CellSorter{
	private final byte[] cells;
	private int pos = -1;
	CellSorter(int viewDistance){
		cells =
			BinaryFileUtil.readFile(new File(WraithavensConquest.assetFolder+File.separatorChar+"ChunkLoad",
				"ChunkLoad"+viewDistance+".dat"));
	}
	public int getY(){
		return cells[pos*3+1]*16;
	}
	public int getZ(){
		return cells[pos*3+2]*16;
	}
	public boolean hasNext(){
		return pos<cells.length/3;
	}
	public void next(){
		pos++;
		if(pos%10==0)
			System.out.println(pos+"/"+cells.length/3+" chunks loaded.");
	}
	public void reset(){
		pos = -1;
	}
	int getX(){
		return cells[pos*3]*16;
	}
}
