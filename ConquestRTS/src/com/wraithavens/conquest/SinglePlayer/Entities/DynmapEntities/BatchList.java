package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class BatchList{
	private final ArrayList<DynmapEntityBatch> batches = new ArrayList();
	private final int offsetAttribLocation;
	private final int rotScaleAttribLocation;
	private final int shadeAttribLocation;
	private final ShaderProgram shader;
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
		batches.add(b);
	}
	public void render(){
		if(batches.isEmpty())
			return;
		shader.bind();
		GL33.glVertexAttribDivisor(offsetAttribLocation, 1);
		GL33.glVertexAttribDivisor(rotScaleAttribLocation, 1);
		for(DynmapEntityBatch batch : batches){
			batch.bind(shadeAttribLocation);
			GL20.glVertexAttribPointer(offsetAttribLocation, 3, GL11.GL_FLOAT, false, 20, 0);
			GL20.glVertexAttribPointer(rotScaleAttribLocation, 2, GL11.GL_FLOAT, false, 20, 12);
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, batch.getIndexCount(), batch.getDataType(), 0,
				batch.getCount());
		}
		GL33.glVertexAttribDivisor(offsetAttribLocation, 0);
		GL33.glVertexAttribDivisor(rotScaleAttribLocation, 0);
	}
}
