package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityMesh;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

public class DynmapEntityBatch{
	private final ArrayList<EntityTransform> entities = new ArrayList();
	private int instanceDataId = -1;
	private EntityMesh mesh;
	private int modelCount;
	private boolean hasCloslyVisible;
	private final EntityType type;
	private FloatBuffer instanceData;
	DynmapEntityBatch(EntityType type){
		this.type = type;
	}
	public void addEntity(EntityTransform e){
		synchronized(entities){
			entities.add(e);
		}
	}
	void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceDataId);
	}
	int compare(DynmapEntityBatch other){
		return type==other.type?0:type.ordinal()>other.type.ordinal()?1:-1;
	}
	void dispose(){
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				if(mesh!=null){
					mesh.removeReference();
					GL15.glDeleteBuffers(instanceDataId);
				}
			}
		});
	}
	int getCount(){
		return modelCount;
	}
	int getDataType(){
		return mesh.getDataType();
	}
	int getIndexCount(){
		return mesh.getDynmapIndexCount();
	}
	EntityMesh getMesh(){
		return mesh;
	}
	int getRealCount(){
		synchronized(entities){
			return entities.size();
		}
	}
	EntityType getType(){
		return mesh.getType();
	}
	boolean hasCloslyVisible(){
		return hasCloslyVisible;
	}
	void removeEntity(EntityTransform e){
		synchronized(entities){
			entities.remove(e);
		}
	}
	void updateVisibility(Camera camera, LandscapeWorld landscape){
		hasCloslyVisible = false;
		synchronized(entities){
			for(EntityTransform t : entities){
				if(landscape.isWithinView((int)t.getX(), (int)t.getZ())
					&&camera.distanceSquared(t.getX(), t.getY(), t.getZ())<1000*1000){
					t.setVisibilityLevel(1);
					hasCloslyVisible = true;
					continue;
				}
				t.setVisibilityLevel(0);
			}
		}
		if(instanceDataId==-1){
			instanceDataId = GL15.glGenBuffers();
			mesh = type.createReference();
		}
		int size = 0;
		synchronized(entities){
			for(EntityTransform e : entities)
				if(e.getVisibilityLevel()==1)
					size++;
			modelCount = size;
			size *= 5;
			if(instanceData==null||instanceData.capacity()<size)
				instanceData = BufferUtils.createFloatBuffer(size);
			else
				instanceData.clear();
			for(EntityTransform e : entities){
				if(e.getVisibilityLevel()!=1)
					continue;
				instanceData.put(e.getX());
				instanceData.put(e.getY());
				instanceData.put(e.getZ());
				instanceData.put(e.getR());
				instanceData.put(e.getS());
			}
		}
		instanceData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceDataId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, instanceData, GL15.GL_STREAM_DRAW);
	}
}
