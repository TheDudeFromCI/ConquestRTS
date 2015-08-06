package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.nio.IntBuffer;
import java.util.Arrays;
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
	private static void placeTriangleIndex(int iboId, int index){
		if(triangleIndexLocation+1==triangleIndices[iboId].length)
			triangleIndices[iboId] = Arrays.copyOf(triangleIndices[iboId], triangleIndices[iboId].length+100);
		triangleIndices[iboId][triangleIndexLocation] = index;
		triangleIndexLocation++;
	}
	private static void tri(int iboId, QuadTree tree, int p1, int p2, int p3){
		placeTriangleIndex(iboId, getIndex(tree, p1));
		placeTriangleIndex(iboId, getIndex(tree, p2));
		placeTriangleIndex(iboId, getIndex(tree, p3));
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
		triangleIndices = new int[TextureCount][100];
	}
	private static int[][] triangleIndices;
	private static int triangleIndexLocation;
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
		triangleIndexLocation = 0;
		countIndices(tree, iboId);
	}
	private void countIndices(QuadTree tree, int iboId){
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
					indexCounts[iboId] += placeRawTriangles(tree, iboId);
					break;
				case 1:
					indexCounts[iboId] += 12;
					tri(iboId, tree, 1, 5, 4);
					tri(iboId, tree, 4, 3, 1);
					tri(iboId, tree, 4, 2, 3);
					tri(iboId, tree, 4, 8, 2);
					break;
				case 2:
					indexCounts[iboId] += 12;
					tri(iboId, tree, 4, 5, 4);
					tri(iboId, tree, 2, 4, 0);
					tri(iboId, tree, 3, 4, 2);
					tri(iboId, tree, 6, 4, 3);
					break;
				case 3:
					indexCounts[iboId] += 9;
					tri(iboId, tree, 4, 8, 2);
					tri(iboId, tree, 3, 4, 2);
					tri(iboId, tree, 6, 4, 3);
					break;
				case 4:
					indexCounts[iboId] += 12;
					tri(iboId, tree, 8, 0, 4);
					tri(iboId, tree, 0, 1, 4);
					tri(iboId, tree, 1, 3, 4);
					tri(iboId, tree, 3, 7, 4);
					break;
				case 5:
					indexCounts[iboId] += 9;
					tri(iboId, tree, 4, 1, 5);
					tri(iboId, tree, 4, 3, 6);
					tri(iboId, tree, 4, 7, 3);
					break;
				case 6:
					indexCounts[iboId] += 12;
					tri(iboId, tree, 4, 5, 0);
					tri(iboId, tree, 4, 0, 8);
					tri(iboId, tree, 3, 6, 4);
					tri(iboId, tree, 4, 7, 3);
					break;
				case 7:
					indexCounts[iboId] += 6;
					tri(iboId, tree, 3, 6, 4);
					tri(iboId, tree, 7, 3, 4);
					break;
				case 8:
					indexCounts[iboId] += 12;
					tri(iboId, tree, 4, 1, 0);
					tri(iboId, tree, 4, 0, 2);
					tri(iboId, tree, 6, 1, 4);
					tri(iboId, tree, 4, 2, 7);
					break;
				case 9:
					indexCounts[iboId] += 12;
					tri(iboId, tree, 4, 1, 5);
					tri(iboId, tree, 4, 6, 1);
					tri(iboId, tree, 4, 2, 7);
					tri(iboId, tree, 4, 8, 2);
					break;
				case 10:
					indexCounts[iboId] += 9;
					tri(iboId, tree, 4, 5, 0);
					tri(iboId, tree, 4, 0, 2);
					tri(iboId, tree, 4, 2, 7);
					break;
				case 11:
					indexCounts[iboId] += 6;
					tri(iboId, tree, 4, 8, 2);
					tri(iboId, tree, 4, 2, 7);
					break;
				case 12:
					indexCounts[iboId] += 9;
					tri(iboId, tree, 4, 0, 8);
					tri(iboId, tree, 4, 1, 0);
					tri(iboId, tree, 4, 6, 1);
					break;
				case 13:
					indexCounts[iboId] += 6;
					tri(iboId, tree, 4, 1, 5);
					tri(iboId, tree, 4, 6, 1);
					break;
				case 14:
					indexCounts[iboId] += 6;
					tri(iboId, tree, 4, 5, 0);
					tri(iboId, tree, 4, 0, 8);
					break;
				case 15:
					break;
			}
		}
		if(!CatchQuadTreeSizes[iboId]&&tree.size/2<QuadTreeSizes[iboId])
			return;
		for(i = 0; i<4; i++)
			if(tree.children[i]!=null)
				countIndices(tree.children[i], iboId);
	}
	private QuadTree getDownQuad(QuadTree tree){
		return getQuadTree(this.tree, tree.x, tree.z+tree.size, tree.size);
	}
	private QuadTree getLeftQuad(QuadTree tree){
		return getQuadTree(this.tree, tree.x-tree.size, tree.z, tree.size);
	}
	private QuadTree getRightQuad(QuadTree tree){
		return getQuadTree(this.tree, tree.x+tree.size, tree.z, tree.size);
	}
	private QuadTree getUpQuad(QuadTree tree){
		return getQuadTree(this.tree, tree.x, tree.z-tree.size, tree.size);
	}
	private int placeRawTriangles(QuadTree tree, int iboId){
		int i = 12;
		int state = 0;
		QuadTree t;
		t = getUpQuad(tree);
		if(t!=null)
			if(t.children[2]!=null||t.children[3]!=null){
				state |= 1;
				i += 3;
			}
		t = getRightQuad(tree);
		if(t!=null)
			if(t.children[0]!=null||t.children[2]!=null){
				state |= 2;
				i += 3;
			}
		t = getDownQuad(tree);
		if(t!=null)
			if(t.children[0]!=null||t.children[1]!=null){
				state |= 4;
				i += 3;
			}
		t = getLeftQuad(tree);
		if(t!=null)
			if(t.children[1]!=null||t.children[3]!=null){
				state |= 8;
				i += 3;
			}
		if((state&1)==1){
			tri(iboId, tree, 4, 1, 5);
			tri(iboId, tree, 4, 5, 0);
		}else{
			tri(iboId, tree, 4, 1, 0);
		}
		if((state&2)==2){
			tri(iboId, tree, 4, 3, 6);
			tri(iboId, tree, 4, 6, 1);
		}else{
			tri(iboId, tree, 4, 3, 1);
		}
		if((state&4)==4){
			tri(iboId, tree, 4, 2, 7);
			tri(iboId, tree, 4, 7, 3);
		}else{
			tri(iboId, tree, 4, 2, 3);
		}
		if((state&8)==8){
			tri(iboId, tree, 4, 0, 8);
			tri(iboId, tree, 4, 8, 2);
		}else{
			tri(iboId, tree, 4, 0, 2);
		}
		return i;
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
			indexData.put(triangleIndices[i], 0, indexCounts[i]);
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_DYNAMIC_DRAW);
	}
}
