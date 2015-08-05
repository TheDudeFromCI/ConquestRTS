package com.wraithavens.conquest.SinglePlayer.Skybox;

import com.wraithavens.conquest.SinglePlayer.Heightmap.WorldHeightmaps;

public interface MountainRenderer{
	public float getCameraX();
	public float getCameraY();
	public float getCameraZ();
	public WorldHeightmaps getHeightmap();
	public void renderMesh();
	public void renderSkybox();
}
