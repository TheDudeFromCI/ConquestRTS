package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Utility.NoiseGenerator;

class HeightmapGenerator{
	private static final Vector3f tempVec1 = new Vector3f();
	private static final Vector3f tempVec2 = new Vector3f();
	private final int size;
	private final float scale;
	private final NoiseGenerator noise;
	private final float maxHeight;
	HeightmapGenerator(int size, float scale, NoiseGenerator noise, float maxHeight){
		this.size = size;
		this.noise = noise;
		this.scale = scale;
		this.maxHeight = maxHeight;
	}
	HeightmapTexture getTexture(int x, int y){
		// ---
		// When loading an already generated texture, this method is usally very
		// quick, and it has an almost unnoticable drop in FPS. Through,
		// generating father out does cause some major FPS drop.
		// ---
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Heightmaps", x+","+y+".png");
		if(file.exists()){
			System.out.println("Loading Heightmap: "+file.getName());
			return new HeightmapTexture(file);
		}
		return new HeightmapTexture(generate(x, y));
	}
	private int calculateColor(float x, float y){
		float center = noise.noise(x, y);
		// ---
		// Multiply these by their max height for better results... I think...
		// ---
		float north = noise.noise(x, y-1)*maxHeight;
		float east = noise.noise(x+1, y)*maxHeight;
		float south = noise.noise(x, y+1)*maxHeight;
		float west = noise.noise(x-1, y)*maxHeight;
		tempVec1.set(2.0f, 0.0f, east-west);
		tempVec1.normalise();
		tempVec2.set(0.0f, 2.0f, north-south);
		tempVec2.normalise();
		tempVec1.cross(tempVec2);
		return ((int)(center*255)<<24)+((int)(tempVec1.x*255)<<16)+((int)(tempVec1.y*255)<<8)
			+(int)(tempVec1.z*255);
	}
	private BufferedImage generate(int offX, int offY){
		System.out.println("Generating height map.");
		System.out.println("  Origin: "+offX+", "+offY);
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		long time = System.currentTimeMillis();
		int[] rgb = new int[size*size];
		int x, y;
		for(x = 0; x<size; x++)
			for(y = 0; y<size; y++)
				rgb[y*size+x] = calculateColor(x*scale+offX, y*scale+offY);
		img.setRGB(0, 0, size, size, rgb, 0, size);
		System.out.println("Finished in "+(System.currentTimeMillis()-time)+" ms.");
		File heightmapFile =
			new File(WraithavensConquest.saveFolder+File.separatorChar+"Heightmaps", offX+","+offY+".png");
		try{
			// ---
			// First ensure the directory exists, then save the newly generated
			// heightmap for future reference.
			// ---
			heightmapFile.getParentFile().mkdirs();
			ImageIO.write(img, "PNG", heightmapFile);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return img;
	}
}
