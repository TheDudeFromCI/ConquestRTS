package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class DynmapChunk{
	private static void breakDown(QuadTree t, float x, float z, int depth){
		double d = distance(t, x, z);
		t.clearChildren();
		if(getDepth(d)<=depth)
			return;
		for(int i = 0; i<4; i++)
			breakDown(t.addChild(i), x, z, depth+1);
	}
	private static double distance(QuadTree t, float x, float z){
		float nx = t.x+t.size/2;
		float nz = t.z+t.size/2;
		nx = nx/Dynmap.VertexCount*Dynmap.BlocksPerChunk;
		nz = nz/Dynmap.VertexCount*Dynmap.BlocksPerChunk;
		return (nx-x)*(nx-x)+(nz-z)*(nz-z);
	}
	private static int getDepth(double d){
		float m = Dynmap.BlocksPerChunk*8;
		int i;
		for(i = 0; i<Dynmap.MaxDepth; i++){
			if(d>=m*m)
				return i;
			m /= 2;
		}
		return i;
	}
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
	private static QuadTree getQuadTree(QuadTree parent, int x, int z, int size){
		if(parent==null||parent.size<size||x<parent.x||z<parent.z||x>=parent.x+parent.size
			||z>=parent.z+parent.size)
			return null;
		if(parent.x==x&&parent.z==z&&parent.size==size)
			return parent;
		QuadTree q;
		for(int i = 0; i<4; i++){
			q = getQuadTree(parent.children[i], x, z, size);
			if(q!=null)
				return q;
		}
		return null;
	}
	private static void tri(IntBuffer data, QuadTree tree, int p1, int p2, int p3){
		data.put(getIndex(tree, p1));
		data.put(getIndex(tree, p2));
		data.put(getIndex(tree, p3));
	}
	private static final int TextureDepth = 1;
	private static final int TextureCount;
	private static final int[] QuadTreeSizes;
	private static final boolean[] CatchQuadTreeSizes;
	static{
		int total = 0;
		for(int i = 0; i<=TextureDepth; i++)
			total += (int)Math.pow(4, i);
		TextureCount = total;
		QuadTreeSizes = new int[TextureCount];
		CatchQuadTreeSizes = new boolean[TextureCount];
		int p, x;
		int j = 0;
		for(int i = 0; i<=TextureDepth; i++){
			p = (int)Math.pow(4, i);
			for(x = 0; x<p; x++){
				QuadTreeSizes[j] = Dynmap.VertexCount-1>>i;
				CatchQuadTreeSizes[j] = i==TextureDepth;
				j++;
			}
		}
	}
	private final int ibo;
	private final int[] indexCounts;
	private final int[] indexOffset;
	private final int x;
	private final int z;
	private final QuadTree tree;
	DynmapChunk(int x, int z){
		this.x = x;
		this.z = z;
		indexCounts = new int[TextureCount];
		indexOffset = new int[TextureCount];
		ibo = GL15.glGenBuffers();
		tree = new QuadTree(0, 0, Dynmap.VertexCount-1, null);
		updateIndices();
	}
	public void update(float x, float z){
		breakDown(tree, x-this.x, z-this.z, 0);
		updateIndices();
	}
	private void countIndices(int iboId){
		indexCounts[iboId] = 0;
		countIndices(tree, iboId);
	}
	private void countIndices(QuadTree tree, int iboId){
		if(!CatchQuadTreeSizes[iboId]&&tree.size<QuadTreeSizes[iboId])
			return;
		int i = 0;
		if(tree.size==QuadTreeSizes[iboId]||CatchQuadTreeSizes[iboId]&&tree.size<=QuadTreeSizes[iboId]){
			if(tree.children[0]!=null)
				i |= 1;
			if(tree.children[1]!=null)
				i |= 2;
			if(tree.children[2]!=null)
				i |= 4;
			if(tree.children[3]!=null)
				i |= 8;
			switch(i){
				case 0:
					indexCounts[iboId] += countNeededIndices(tree);
					break;
				case 1:
					indexCounts[iboId] += 12;
					break;
				case 2:
					indexCounts[iboId] += 12;
					break;
				case 3:
					indexCounts[iboId] += 9;
					break;
				case 4:
					indexCounts[iboId] += 12;
					break;
				case 5:
					indexCounts[iboId] += 9;
					break;
				case 6:
					indexCounts[iboId] += 12;
					break;
				case 7:
					indexCounts[iboId] += 6;
					break;
				case 8:
					indexCounts[iboId] += 12;
					break;
				case 9:
					indexCounts[iboId] += 12;
					break;
				case 10:
					indexCounts[iboId] += 9;
					break;
				case 11:
					indexCounts[iboId] += 6;
					break;
				case 12:
					indexCounts[iboId] += 9;
					break;
				case 13:
					indexCounts[iboId] += 6;
					break;
				case 14:
					indexCounts[iboId] += 6;
					break;
				case 15:
					break;
			}
		}
		for(i = 0; i<4; i++)
			if(tree.children[i]!=null)
				countIndices(tree.children[i], iboId);
	}
	private int countNeededIndices(QuadTree tree){
		if(tree.parent==null)
			return 12;
		int i = 12;
		QuadTree t;
		t = getUpQuad(tree);
		if(t!=null)
			if(t.children[2]!=null||t.children[3]!=null)
				i += 3;
		t = getDownQuad(tree);
		if(t!=null)
			if(t.children[0]!=null||t.children[1]!=null)
				i += 3;
		t = getLeftQuad(tree);
		if(t!=null)
			if(t.children[1]!=null||t.children[3]!=null)
				i += 3;
		t = getRightQuad(tree);
		if(t!=null)
			if(t.children[0]!=null||t.children[2]!=null)
				i += 3;
		return i;
	}
	private QuadTree getDownQuad(QuadTree tree){
		return getQuadTree(tree.x, tree.z+tree.size, tree.size);
	}
	private QuadTree getLeftQuad(QuadTree tree){
		return getQuadTree(tree.x-tree.size, tree.z, tree.size);
	}
	private QuadTree getQuadTree(int x, int z, int size){
		if(x<0)
			return null;
		if(z<0)
			return null;
		if(x>=Dynmap.VertexCount)
			return null;
		if(z>=Dynmap.VertexCount)
			return null;
		return getQuadTree(tree, x, z, size);
	}
	private QuadTree getRightQuad(QuadTree tree){
		return getQuadTree(tree.x+tree.size, tree.z, tree.size);
	}
	private QuadTree getUpQuad(QuadTree tree){
		return getQuadTree(tree.x, tree.z-tree.size, tree.size);
	}
	private void placeRawTriangles(QuadTree tree, IntBuffer data){
		int state = 0;
		QuadTree t;
		t = getUpQuad(tree);
		if(t!=null)
			if(t.children[2]!=null||t.children[3]!=null)
				state |= 1;
		t = getRightQuad(tree);
		if(t!=null)
			if(t.children[0]!=null||t.children[2]!=null)
				state |= 2;
		t = getDownQuad(tree);
		if(t!=null)
			if(t.children[0]!=null||t.children[1]!=null)
				state |= 4;
		t = getLeftQuad(tree);
		if(t!=null)
			if(t.children[1]!=null||t.children[3]!=null)
				state |= 8;
		if((state&1)==1){
			tri(data, tree, 4, 1, 5);
			tri(data, tree, 4, 5, 0);
		}else{
			tri(data, tree, 4, 1, 0);
		}
		if((state&2)==2){
			tri(data, tree, 4, 3, 6);
			tri(data, tree, 4, 6, 1);
		}else{
			tri(data, tree, 4, 3, 1);
		}
		if((state&4)==4){
			tri(data, tree, 4, 2, 7);
			tri(data, tree, 4, 7, 3);
		}else{
			tri(data, tree, 4, 2, 3);
		}
		if((state&8)==8){
			tri(data, tree, 4, 0, 8);
			tri(data, tree, 4, 8, 2);
		}else{
			tri(data, tree, 4, 0, 2);
		}
	}
	private void placeTriangles(QuadTree tree, IntBuffer data, int iboId){
		if(!CatchQuadTreeSizes[iboId]&&tree.size<QuadTreeSizes[iboId])
			return;
		int i = 0;
		if(tree.size==QuadTreeSizes[iboId]||CatchQuadTreeSizes[iboId]&&tree.size<=QuadTreeSizes[iboId]){
			if(tree.children[0]!=null)
				i = i|1;
			if(tree.children[1]!=null)
				i = i|2;
			if(tree.children[2]!=null)
				i = i|4;
			if(tree.children[3]!=null)
				i = i|8;
			placeTrianglesState(tree, data, i);
		}
		for(i = 0; i<4; i++)
			if(tree.children[i]!=null)
				placeTriangles(tree.children[i], data, iboId);
	}
	private void placeTrianglesState(QuadTree tree, IntBuffer data, int state){
		switch(state){
			case 0:
				placeRawTriangles(tree, data);
				break;
			case 1:
				tri(data, tree, 1, 5, 4);
				tri(data, tree, 4, 3, 1);
				tri(data, tree, 4, 2, 3);
				tri(data, tree, 4, 8, 2);
				break;
			case 2:
				tri(data, tree, 4, 5, 4);
				tri(data, tree, 2, 4, 0);
				tri(data, tree, 3, 4, 2);
				tri(data, tree, 6, 4, 3);
				break;
			case 3:
				tri(data, tree, 4, 8, 2);
				tri(data, tree, 3, 4, 2);
				tri(data, tree, 6, 4, 3);
				break;
			case 4:
				tri(data, tree, 8, 0, 4);
				tri(data, tree, 0, 1, 4);
				tri(data, tree, 1, 3, 4);
				tri(data, tree, 3, 7, 4);
				break;
			case 5:
				tri(data, tree, 4, 1, 5);
				tri(data, tree, 4, 3, 6);
				tri(data, tree, 4, 7, 3);
				break;
			case 6:
				tri(data, tree, 4, 5, 0);
				tri(data, tree, 4, 0, 8);
				tri(data, tree, 3, 6, 4);
				tri(data, tree, 4, 7, 3);
				break;
			case 7:
				tri(data, tree, 3, 6, 4);
				tri(data, tree, 7, 3, 4);
				break;
			case 8:
				tri(data, tree, 4, 1, 0);
				tri(data, tree, 4, 0, 2);
				tri(data, tree, 6, 1, 4);
				tri(data, tree, 4, 2, 7);
				break;
			case 9:
				tri(data, tree, 4, 1, 5);
				tri(data, tree, 4, 6, 1);
				tri(data, tree, 4, 2, 7);
				tri(data, tree, 4, 8, 2);
				break;
			case 10:
				tri(data, tree, 4, 5, 0);
				tri(data, tree, 4, 0, 2);
				tri(data, tree, 4, 2, 7);
				break;
			case 11:
				tri(data, tree, 4, 8, 2);
				tri(data, tree, 4, 2, 7);
				break;
			case 12:
				tri(data, tree, 4, 0, 8);
				tri(data, tree, 4, 1, 0);
				tri(data, tree, 4, 6, 1);
				break;
			case 13:
				tri(data, tree, 4, 1, 5);
				tri(data, tree, 4, 6, 1);
				break;
			case 14:
				tri(data, tree, 4, 5, 0);
				tri(data, tree, 4, 0, 8);
				break;
			case 15:
				break;
		}
	}
	void render(){
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		for(int i = 0; i<TextureCount; i++){
			// ---
			// TODO Bind texture here!
			// ---
			GL11.glDrawElements(GL11.GL_TRIANGLES, indexCounts[i], GL11.GL_UNSIGNED_INT, indexOffset[i]*4);
		}
	}
	void updateIndices(){
		int i;
		int totalSize = 0;
		for(i = 0; i<TextureCount; i++){
			countIndices(i);
			indexOffset[i] = totalSize;
			totalSize += indexCounts[i];
		}
		IntBuffer indexData = BufferUtils.createIntBuffer(totalSize);
		for(i = 0; i<TextureCount; i++)
			placeTriangles(tree, indexData, i);
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_DYNAMIC_DRAW);
	}
}
