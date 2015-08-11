package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.BinaryFile;

public class CellSorter{
	private final byte[] cells;
	private int pos = -1;
	public CellSorter(int viewDistance){
		BinaryFile bin = new BinaryFile(new File(WraithavensConquest.chunkLoadFolder, viewDistance+".dat"));
		cells = bin.getBinary();
	}
	public int getY(){
		return cells[pos*3+1]*16;
	}
	public int getZ(){
		return cells[pos*3+2]*16;
	}
	public boolean hasNext(){
		return pos<cells.length/3-1;
	}
	public void next(){
		pos++;
		if(!hasNext())
			GlError.out("All chunks loaded.");
		else if(pos>0&&pos%750==0)
			GlError.out(pos+"/"+cells.length/3+" chunks loaded.");
	}
	public void reset(){
		pos = -1;
	}
	int getX(){
		return cells[pos*3]*16;
	}
}
