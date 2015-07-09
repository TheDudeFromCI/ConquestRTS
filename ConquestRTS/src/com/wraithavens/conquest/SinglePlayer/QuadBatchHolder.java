package com.wraithavens.conquest.SinglePlayer;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public abstract class QuadBatchHolder{
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private boolean compiled;
	public final void addQuad(Quad q, Texture t){
		if(compiled)throw new RuntimeException("Batches already compiled!");
		for(int i = 0; i<batches.size(); i++)
			if(batches.get(i).getTexture()==t
			&&!batches.get(i).isFull()){
				batches.get(i).addQuad(q);
				return;
			}
		QuadBatch batch = new QuadBatch(t);
		batch.addQuad(q);
		batches.add(batch);
	}
	public final ArrayList<QuadBatch> getBatches(){
		return batches;
	}
	public final void clearAllBatches(){
		compiled = false;
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).cleanUp();
		batches.clear();
	}
	public final void compile(){
		compiled = true;
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).compileBuffer();
	}
}