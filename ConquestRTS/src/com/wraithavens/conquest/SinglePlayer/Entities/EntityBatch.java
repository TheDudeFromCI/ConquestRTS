package com.wraithavens.conquest.SinglePlayer.Entities;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Math.Matrix4f;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;

public class EntityBatch extends Entity{
	private static int nextPowerOf2(int i){
		int x = 1;
		while(true){
			if(i<=x)
				return x;
			x *= 2;
		}
	}
	private final int entityCount;
	private final Vector3f center;
	private final LodRadius lod;
	private final int textureId;
	private final int textureSize;
	public EntityBatch(EntityType type, ArrayList<Matrix4f> transforms, Vector3f center, LodRadius lod){
		super(type);
		entityCount = transforms.size();
		this.center = center;
		this.lod = lod;
		textureId = GL11.glGenTextures();
		textureSize = nextPowerOf2((int)Math.ceil(Math.sqrt(transforms.size()*4)));
		GlError.out("Created entity batch.\n  Objects: "+transforms.size()+"\n  Texture Size:"+textureSize);
		{
			FloatBuffer data = BufferUtils.createFloatBuffer(textureSize*textureSize*4);
			for(Matrix4f mat : transforms)
				mat.store(data);
			data.flip();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, textureSize, textureSize, 0, GL11.GL_RGBA,
				GL11.GL_FLOAT, data);
		}
		GlError.dumpError();
	}
	@Override
	public void dispose(){
		super.dispose();
		GL11.glDeleteTextures(textureId);
		GlError.dumpError();
	}
	public int getTextureSize(){
		return textureSize;
	}
	@Override
	public void render(Camera camera){
		int l = lod.getLod(camera, center);
		if(l==-1)
			return;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		mesh.drawStaticInstanced(l, entityCount);
		GlError.dumpError();
	}
}
