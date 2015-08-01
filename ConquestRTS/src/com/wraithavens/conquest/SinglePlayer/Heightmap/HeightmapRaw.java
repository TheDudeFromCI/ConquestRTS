package com.wraithavens.conquest.SinglePlayer.Heightmap;

public class HeightmapRaw{
	private final float[] colors = new float[WorldHeightmaps.TextureDetail*WorldHeightmaps.TextureDetail*4];
	public void getColor(int x, int y, float[] out){
		out[0] = colors[(y*WorldHeightmaps.TextureDetail+x)*4];
		out[0] = colors[(y*WorldHeightmaps.TextureDetail+x)*4+1];
		out[0] = colors[(y*WorldHeightmaps.TextureDetail+x)*4+2];
		out[0] = colors[(y*WorldHeightmaps.TextureDetail+x)*4+3];
	}
	public float[] getColors(){
		return colors;
	}
	public void setColor(int x, int y, float[] in){
		colors[(y*WorldHeightmaps.TextureDetail+x)*4] = in[0];
		colors[(y*WorldHeightmaps.TextureDetail+x)*4+1] = in[1];
		colors[(y*WorldHeightmaps.TextureDetail+x)*4+2] = in[2];
		colors[(y*WorldHeightmaps.TextureDetail+x)*4+3] = in[3];
	}
}
