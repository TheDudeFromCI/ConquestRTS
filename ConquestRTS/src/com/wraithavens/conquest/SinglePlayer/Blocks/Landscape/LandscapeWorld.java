package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class LandscapeWorld{
	private final ArrayList<LandscapeChunk> chunks = new ArrayList();
	private final WorldNoiseMachine machine;
	public LandscapeWorld(WorldNoiseMachine machine){
		this.machine = machine;
	}
	public void render(Camera camera){
		for(LandscapeChunk c : chunks)
			if(camera.getFrustum().cubeInFrustum(c.getX(), c.getY(), c.getZ(), LandscapeChunk.LandscapeSize))
				c.render();
	}
}
