package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
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
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, size, size, 0, GL11.GL_RGBA,
			GL11.GL_UNSIGNED_BYTE, data);
		return textureId;
	}
	private final HashMap<EntityType,GrassTypeData> types = new HashMap();
	private final ArrayList<GrassPatch> patches;
	private final int OffsetAttribLocation;
	private final int RotScaleAttribLocation;
	GrassBook(int OffsetAttribLocation, int RotScaleAttribLocation, ArrayList<GrassPatch> patches){
		this.OffsetAttribLocation = OffsetAttribLocation;
		this.RotScaleAttribLocation = RotScaleAttribLocation;
		this.patches = patches;
	}
	private int bindType(EntityType type){
		assert types.containsKey(type);
		GrassTypeData data = types.get(type);
		data.bind();
		GL20.glVertexAttribPointer(OffsetAttribLocation, 3, GL11.GL_FLOAT, false, 20, 0);
		GL20.glVertexAttribPointer(RotScaleAttribLocation, 2, GL11.GL_FLOAT, false, 20, 12);
		return data.getCount();
	}
	private void rebuildDataBuffer(EntityType type){
		int count = 0;
		for(GrassPatch patch : patches)
			if(patch.getType()==type)
				count += patch.getCount();
		GrassTypeData grassType = types.get(type);
		FloatBuffer data = grassType.allocateData(count);
		for(GrassPatch patch : patches)
			if(patch.getType()==type)
				patch.store(data);
		grassType.recompile();
	}
	void addReference(EntityType type){
		if(types.containsKey(type))
			types.get(type).addReference();
		else{
			GrassTypeData data = new GrassTypeData(loadTexture(type));
			data.addReference();
			types.put(type, data);
		}
		rebuildDataBuffer(type);
	}
	void dispose(){
		for(EntityType type : types.keySet())
			types.get(type).dispose();
		types.clear();
	}
	void removeReference(EntityType type){
		GrassTypeData data = types.get(type);
		if(data.removeReferences()){
			data.dispose();
			types.remove(type);
		}
		rebuildDataBuffer(type);
	}
	void render(){
		for(EntityType type : types.keySet())
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, 12, GL11.GL_UNSIGNED_SHORT, 0, bindType(type));
	}
}
