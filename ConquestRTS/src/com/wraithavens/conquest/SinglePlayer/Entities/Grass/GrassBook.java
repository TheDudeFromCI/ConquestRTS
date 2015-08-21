package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.Utility.BinaryFile;

public class GrassBook{
	private static int loadTexture(EntityType type){
		File file = new File(WraithavensConquest.modelFolder, type.fileName);
		BinaryFile bin = new BinaryFile(file);
		ByteBuffer data = BufferUtils.createByteBuffer(bin.size());
		for(int i = 0; i<bin.size(); i++)
			data.put(bin.getByte());
		data.flip();
		int size = (int)Math.sqrt(bin.size()/4);
		int textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, size, size, 0, GL11.GL_RGBA,
			GL11.GL_UNSIGNED_BYTE, data);
		return textureId;
	}
	private final HashMap<EntityType,Integer> references = new HashMap();
	private final HashMap<EntityType,Integer> textures = new HashMap();
	void addReference(EntityType type){
		if(references.containsKey(type))
			references.put(type, references.get(type)+1);
		else{
			references.put(type, 1);
			textures.put(type, loadTexture(type));
		}
	}
	void bindType(EntityType type){
		assert textures.containsKey(type);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(type));
	}
	void dispose(){
		for(EntityType type : textures.keySet())
			GL11.glDeleteTextures(textures.get(type));
		references.clear();
		textures.clear();
	}
	void removeReference(EntityType type){
		if(references.containsKey(type)){
			int val = references.get(type);
			if(val==1){
				references.remove(type);
				textures.remove(type);
				return;
			}
			references.put(type, val-1);
		}
	}
}
