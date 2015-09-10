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
	private final int instanceDataId;
	private final EntityMesh mesh;
	private boolean needsRebuild;
	private int modelCount;
	private boolean hasCloslyVisible;
	private boolean hasDistantlyVisible;
	DynmapEntityBatch(EntityType type){
		instanceDataId = GL15.glGenBuffers();
		mesh = type.createReference();
	}
	public void addEntity(EntityTransform e){
		synchronized(entities){
			entities.add(e);
		}
		needsRebuild = true;
	}
	public void rebuildBuffer(){
		if(!needsRebuild)
			return;
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				needsRebuild = false;
				int size = 0;
				FloatBuffer instanceData;
				synchronized(entities){
					for(EntityTransform e : entities)
						if(e.getVisibilityLevel()==1)
							size++;
					modelCount = size;
					instanceData = BufferUtils.createFloatBuffer(size*5);
					for(EntityTransform e : entities){
						if(e.getVisibilityLevel()!=1)
							continue;
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
	void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceDataId);
	}
	int compare(DynmapEntityBatch other){
		return mesh==other.mesh?0:mesh.getType().ordinal()>other.mesh.getType().ordinal()?1:-1;
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
	boolean hasDistantlyVisible(){
		return hasDistantlyVisible;
	}
	boolean needsRebuild(){
		return needsRebuild;
	}
	void removeEntity(EntityTransform e){
		synchronized(entities){
			entities.remove(e);
		}
		needsRebuild = true;
	}
	void updateVisibility(Camera camera, LandscapeWorld landscape){
		double d;
		int i;
		hasCloslyVisible = false;
		hasDistantlyVisible = false;
		synchronized(entities){
			for(EntityTransform t : entities){
				i = t.getVisibilityLevel();
				if(landscape.isWithinView((int)t.getX(), (int)t.getZ())){
					if(i!=0){
						t.setVisibilityLevel(0);
						needsRebuild = true;
					}
					continue;
				}
				d = camera.distanceSquared(t.getX(), t.getY(), t.getZ());
				if(d<1000*1000){
					hasCloslyVisible = true;
					if(i!=1){
						t.setVisibilityLevel(1);
						needsRebuild = true;
					}
				}else{
					hasDistantlyVisible = true;
					if(i!=2){
						t.setVisibilityLevel(2);
						needsRebuild = true;
					}
				}
			}
		}
	}
}
