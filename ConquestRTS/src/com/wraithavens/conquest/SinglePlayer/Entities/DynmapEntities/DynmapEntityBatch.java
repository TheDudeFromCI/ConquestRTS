package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityMesh;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.Utility.BinaryFile;

public class DynmapEntityBatch{
	private final ArrayList<GrassTransform> entities = new ArrayList();
	private final int instanceDataId;
	private final EntityMesh mesh;
	private FloatBuffer instanceData;
	private boolean needsRebuild;
	private int modelCount;
	DynmapEntityBatch(EntityType type){
		instanceDataId = GL15.glGenBuffers();
		mesh = type.createReference();
	}
	public void addEntity(GrassTransform e){
		synchronized(entities){
			entities.add(e);
		}
		needsRebuild = true;
	}
	public void rebuildBuffer(){
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				needsRebuild = false;
				synchronized(entities){
					modelCount = entities.size();
					int size = modelCount*5;
					if(instanceData==null||instanceData.capacity()<size)
						instanceData = BufferUtils.createFloatBuffer(size);
					for(GrassTransform e : entities){
						instanceData.put(e.getX());
						instanceData.put(e.getY());
						instanceData.put(e.getZ());
						instanceData.put(e.getRotation());
						instanceData.put(e.getScale());
					}
				}
				instanceData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceDataId);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, instanceData, GL15.GL_DYNAMIC_DRAW);
			}
		});
	}
	void bind(int shadeAttribLocation){
		mesh.dynmapBatchBind(shadeAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceDataId);
	}
	void dispose(){
		mesh.removeReference();
		GL15.glDeleteBuffers(instanceDataId);
	}
	int getCount(){
		return modelCount;
	}
	int getDataType(){
		return mesh.getDataType();
	}
	int getIndexCount(){
		return mesh.getIndexCount();
	}
	int getRealCount(){
		synchronized(entities){
			return entities.size();
		}
	}
	boolean needsRebuild(){
		return needsRebuild;
	}
	void removeEntity(GrassTransform e){
		synchronized(entities){
			entities.remove(e);
		}
		needsRebuild = true;
	}
	void save(BinaryFile bin){
		synchronized(entities){
			for(GrassTransform t : entities){
				bin.addInt(mesh.getType().ordinal());
				bin.addFloat(t.getX());
				bin.addFloat(t.getY());
				bin.addFloat(t.getZ());
				bin.addFloat(t.getRotation());
				bin.addFloat(t.getScale());
			}
		}
	}
}
