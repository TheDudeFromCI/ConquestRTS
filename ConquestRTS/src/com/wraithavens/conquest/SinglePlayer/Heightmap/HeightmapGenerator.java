package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

class HeightmapGenerator{
	private static final Vector3f tempVec = new Vector3f();
	private final WorldNoiseMachine noise;
	HeightmapGenerator(WorldNoiseMachine noise){
		this.noise = noise;
	}
	private void calculateColor(float x, float y, float[] out){
		noise.getPrairieColor(x, y, tempVec);
		out[0] = tempVec.x;
		out[1] = tempVec.y;
		out[2] = tempVec.z;
		out[3] = (float)(noise.getWorldHeight(x, y)/noise.getMaxHeight());
	}
	// ---
	// TODO Make this generate slower, over the span of several frames to avoid
	// FPS drop.
	// ---
	private HeightmapRaw generate(int offX, int offY){
		System.out.println("Generating height map.");
		System.out.println("  Origin: "+offX+", "+offY);
		HeightmapRaw raw = new HeightmapRaw();
		long time = System.currentTimeMillis();
		int x, y;
		float[] temp = new float[4];
		for(x = 0; x<WorldHeightmaps.TextureDetail; x++)
			for(y = 0; y<WorldHeightmaps.TextureDetail; y++){
				calculateColor(x/(float)WorldHeightmaps.TextureDetail*WorldHeightmaps.ViewDistance*2f
					-WorldHeightmaps.ViewDistance/2f+offX, y/(float)WorldHeightmaps.TextureDetail
					*WorldHeightmaps.ViewDistance*2f-WorldHeightmaps.ViewDistance/2f+offY, temp);
				raw.setColor(x, y, temp);
			}
		System.out.println("Finished in "+(System.currentTimeMillis()-time)+" ms.");
		File heightmapFile =
			new File(WraithavensConquest.saveFolder+File.separatorChar+"Heightmaps", offX+","+offY+".dat");
		try{
			// ---
			// First ensure the directory exists, then save the newly generated
			// heightmap for future reference.
			// ---
			HeightmapFormat format = new HeightmapFormat(heightmapFile);
			format.beginWriting();
			for(float f : raw.getColors())
				format.writeFloat(f);
			format.stopWriting();
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return raw;
	}
	HeightmapTexture getHeightmapTexture(int x, int y){
		// ---
		// When loading an already generated texture, this method is usally very
		// quick, and it has an almost unnoticable drop in FPS. Through,
		// generating father out does cause some major FPS drop.
		// ---
		File file = new File(WraithavensConquest.saveFolder+File.separatorChar+"Heightmaps", x+","+y+".dat");
		if(file.exists()){
			System.out.println("Loading Heightmap: "+file.getName());
			return new HeightmapTexture(file);
		}
		return new HeightmapTexture(generate(x, y));
	}
}
