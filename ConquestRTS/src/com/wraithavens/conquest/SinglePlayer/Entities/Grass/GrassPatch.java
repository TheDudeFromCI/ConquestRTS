package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;

public class GrassPatch{
	private static int nextPowerOf2(int i){
		int x = 1;
		while(true){
			if(i<=x)
				return x;
			x *= 2;
		}
	}
	private final int textureId;
	private final int textureSize;
	private final int grassCount;
	private final EntityType grassType;
	public GrassPatch(EntityType grassType, ArrayList<Vector3f> locations){
		this.grassType = grassType;
		textureId = GL11.glGenTextures();
		{
			grassCount = locations.size();
			textureSize = nextPowerOf2((int)Math.ceil(Math.sqrt(grassCount)));
			GlError.out("Created grass patch.\n  Objects: "+grassCount+"\n  Texture Size:"+textureSize);
			FloatBuffer data = BufferUtils.createFloatBuffer(textureSize*textureSize*3);
			for(Vector3f l : locations){
				data.put(l.x);
				data.put(l.y);
				data.put(l.z);
			}
			data.flip();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, textureSize, textureSize, 0, GL11.GL_RGB,
				GL11.GL_FLOAT, data);
		}
		GlError.dumpError();
	}
	public void dispose(){
		GL11.glDeleteTextures(textureId);
	}
	public EntityType getType(){
		return grassType;
	}
	void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	int getCount(){
		return grassCount;
	}
	int getTextureSize(){
		return textureSize;
	}
}
