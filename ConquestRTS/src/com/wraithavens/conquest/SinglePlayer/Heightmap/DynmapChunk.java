package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class DynmapChunk{
	private static int getIndex(QuadTree tree, int point){
		switch(point){
			case 0:
				return tree.z*Dynmap.VertexCount+tree.x;
			case 1:
				return tree.z*Dynmap.VertexCount+tree.x+tree.size;
			case 2:
				return (tree.z+tree.size)*Dynmap.VertexCount+tree.x;
			case 3:
				return (tree.z+tree.size)*Dynmap.VertexCount+tree.x+tree.size;
			case 4:
				return (tree.z+tree.size/2)*Dynmap.VertexCount+tree.x+tree.size/2;
			case 5:
				return tree.z*Dynmap.VertexCount+tree.x+tree.size/2;
			case 6:
				return (tree.z+tree.size/2)*Dynmap.VertexCount+tree.x+tree.size;
			case 7:
				return (tree.z+tree.size)*Dynmap.VertexCount+tree.x+tree.size/2;
			case 8:
				return (tree.z+tree.size/2)*Dynmap.VertexCount+tree.x;
		}
		return -1;
	}
	private static void placeTriangles(QuadTree tree, IntBuffer data, int state){
		switch(state){
			case 0:
				tri(data, tree, 0, 1, 4);
				tri(data, tree, 1, 3, 4);
				tri(data, tree, 3, 2, 4);
				tri(data, tree, 2, 0, 4);
				break;
			case 1:
				tri(data, tree, 4, 5, 1);
				tri(data, tree, 1, 3, 4);
				tri(data, tree, 3, 2, 4);
				tri(data, tree, 2, 8, 4);
				break;
			case 2:
				tri(data, tree, 0, 5, 4);
				tri(data, tree, 0, 4, 2);
				tri(data, tree, 2, 4, 3);
				tri(data, tree, 3, 4, 6);
				break;
			case 3:
				tri(data, tree, 2, 8, 4);
				tri(data, tree, 2, 4, 3);
				tri(data, tree, 3, 4, 6);
				break;
			case 4:
				tri(data, tree, 8, 0, 4);
				tri(data, tree, 0, 1, 4);
				tri(data, tree, 1, 3, 4);
				tri(data, tree, 3, 7, 4);
				break;
			case 5:
				tri(data, tree, 5, 1, 4);
				tri(data, tree, 1, 3, 4);
				tri(data, tree, 3, 7, 4);
				break;
			case 6:
				tri(data, tree, 0, 5, 4);
				tri(data, tree, 8, 0, 4);
				tri(data, tree, 4, 6, 3);
				tri(data, tree, 3, 7, 4);
				break;
			case 7:
				tri(data, tree, 4, 6, 3);
				tri(data, tree, 4, 3, 7);
				break;
			case 8:
				tri(data, tree, 0, 1, 4);
				tri(data, tree, 2, 0, 4);
				tri(data, tree, 4, 1, 6);
				tri(data, tree, 7, 2, 4);
				break;
			case 9:
				tri(data, tree, 5, 1, 4);
				tri(data, tree, 1, 6, 4);
				tri(data, tree, 7, 2, 4);
				tri(data, tree, 2, 8, 4);
				break;
			case 10:
				tri(data, tree, 0, 5, 4);
				tri(data, tree, 2, 0, 4);
				tri(data, tree, 7, 2, 4);
				break;
			case 11:
				tri(data, tree, 2, 8, 4);
				tri(data, tree, 7, 2, 4);
				break;
			case 12:
				tri(data, tree, 8, 0, 4);
				tri(data, tree, 0, 1, 4);
				tri(data, tree, 1, 6, 4);
				break;
			case 13:
				tri(data, tree, 5, 1, 4);
				tri(data, tree, 1, 6, 4);
				break;
			case 14:
				tri(data, tree, 0, 5, 4);
				tri(data, tree, 8, 0, 4);
				break;
			case 15:
				break;
		}
	}
	private static void tri(IntBuffer data, QuadTree tree, int p1, int p2, int p3){
		data.put(getIndex(tree, p1));
		data.put(getIndex(tree, p2));
		data.put(getIndex(tree, p3));
	}
	private final int ibo;
	private final int x;
	private final int z;
	private int indexCount;
	private final QuadTree tree;
	DynmapChunk(int x, int z){
		this.x = x;
		this.z = z;
		ibo = GL15.glGenBuffers();
		tree = new QuadTree(0, 0, Dynmap.VertexCount-1);
		{
			tree.addChild(0).addChild(0);
			tree.children[0].addChild(1);
			tree.children[0].addChild(2);
		}
		updateIndices();
	}
	private void countIndices(){
		indexCount = 0;
		countIndices(tree);
	}
	private void countIndices(QuadTree tree){
		int i = 0;
		if(tree.children[0]!=null)
			i = i|1;
		if(tree.children[1]!=null)
			i = i|2;
		if(tree.children[2]!=null)
			i = i|4;
		if(tree.children[3]!=null)
			i = i|8;
		switch(i){
			case 0:
				indexCount += 12;
				break;
			case 1:
				indexCount += 12;
				break;
			case 2:
				indexCount += 12;
				break;
			case 3:
				indexCount += 9;
				break;
			case 4:
				indexCount += 12;
				break;
			case 5:
				indexCount += 9;
				break;
			case 6:
				indexCount += 12;
				break;
			case 7:
				indexCount += 6;
				break;
			case 8:
				indexCount += 12;
				break;
			case 9:
				indexCount += 12;
				break;
			case 10:
				indexCount += 9;
				break;
			case 11:
				indexCount += 6;
				break;
			case 12:
				indexCount += 9;
				break;
			case 13:
				indexCount += 6;
				break;
			case 14:
				indexCount += 6;
				break;
			case 15:
				break;
		}
		for(i = 0; i<4; i++)
			if(tree.children[i]!=null)
				countIndices(tree.children[i]);
	}
	private void placeTriangles(QuadTree tree, IntBuffer data){
		int i = 0;
		if(tree.children[0]!=null)
			i = i|1;
		if(tree.children[1]!=null)
			i = i|2;
		if(tree.children[2]!=null)
			i = i|4;
		if(tree.children[3]!=null)
			i = i|8;
		placeTriangles(tree, data, i);
		for(i = 0; i<4; i++)
			if(tree.children[i]!=null)
				placeTriangles(tree.children[i], data);
	}
	void render(ShaderProgram shader){
		shader.setUniform2f(0, x*Dynmap.BlocksPerChunk, z*Dynmap.BlocksPerChunk);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
	}
	void updateIndices(){
		countIndices();
		IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
		placeTriangles(tree, indexData);
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_DYNAMIC_DRAW);
	}
}
