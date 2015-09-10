package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class BatchList{
	private final ArrayList<DynmapEntityBatch> batches = new ArrayList();
	private DynmapEntityBook book;
	private final int offsetAttribLocation;
	private final int rotScaleAttribLocation;
	private final int shadeAttribLocation;
	private final ShaderProgram shader;
	private final Comparator batchSorter = new Comparator<DynmapEntityBatch>(){
		public int compare(DynmapEntityBatch a, DynmapEntityBatch b){
			return a.compare(b);
		}
	};
	public BatchList(){
		shader = new ShaderProgram("DynmapEntities");
		shader.bind();
		offsetAttribLocation = shader.getAttributeLocation("att_offset");
		rotScaleAttribLocation = shader.getAttributeLocation("att_rotScale");
		shadeAttribLocation = shader.getAttributeLocation("att_shade");
		GL20.glEnableVertexAttribArray(offsetAttribLocation);
		GL20.glEnableVertexAttribArray(rotScaleAttribLocation);
		GL20.glEnableVertexAttribArray(shadeAttribLocation);
	}
	public void addBatch(DynmapEntityBatch b){
		synchronized(batches){
			batches.add(b);
			batches.sort(batchSorter);
		}
	}
	public void removeBatch(DynmapEntityBatch b){
		synchronized(batches){
			batches.remove(b);
		}
	}
	public void render(){
		book.updateVisibility();
		if(book.hasCloselyVisible()){
			shader.bind();
			GL33.glVertexAttribDivisor(offsetAttribLocation, 1);
			GL33.glVertexAttribDivisor(rotScaleAttribLocation, 1);
			EntityType boundType = null;
			synchronized(batches){
				for(DynmapEntityBatch batch : batches){
					if(boundType==null||boundType!=batch.getType()){
						boundType = batch.getType();
						batch.getMesh().dynmapBatchBind(shadeAttribLocation);
					}
					batch.bind();
					GL20.glVertexAttribPointer(offsetAttribLocation, 3, GL11.GL_FLOAT, false, 20, 0);
					GL20.glVertexAttribPointer(rotScaleAttribLocation, 2, GL11.GL_FLOAT, false, 20, 12);
					GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, batch.getIndexCount(), batch.getDataType(),
						0, batch.getCount());
				}
			}
			GL33.glVertexAttribDivisor(offsetAttribLocation, 0);
			GL33.glVertexAttribDivisor(rotScaleAttribLocation, 0);
		}
		if(book.hasDistantlyVisible()){
			// TODO
		}
	}
	public void setup(Camera camera, LandscapeWorld landscape){
		book = new DynmapEntityBook(this, camera, landscape);
	}
	DynmapEntityBook getBook(){
		return book;
	}
}
