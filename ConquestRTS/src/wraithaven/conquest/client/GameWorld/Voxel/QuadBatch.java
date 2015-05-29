package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.BufferUtils;
import wraithaven.conquest.client.BuildingCreator.Loop;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class QuadBatch{
	public final float centerX, centerY, centerZ;
	private int i;
	private int elementCount, indexCount;
	public int triangleCount;
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private FloatBuffer textureCoordBuffer;
	private IntBuffer indexBuffer;
	private final int vertexBufferId;
	private final int colorBufferId;
	private final int textureCoordBufferId;
	private final int indexBufferId;
	private final Texture texture;
	private final ArrayList<Quad> quads = new ArrayList();
	private static final long ZERO = 0;
	private static final int FLOAT_SIZE = 4;
	public QuadBatch(Texture texture, float centerX, float centerY, float centerZ){
		this.texture=texture;
		this.centerX=centerX;
		this.centerY=centerY;
		this.centerZ=centerZ;
		vertexBufferId=glGenBuffers();
		colorBufferId=glGenBuffers();
		textureCoordBufferId=glGenBuffers();
		indexBufferId=glGenBuffers();
	}
	public void removeQuad(Quad q){
		for(i=0; i<quads.size(); i++){
			if(quads.get(i)==q){
				quads.remove(i);
				return;
			}
		}
	}
	private Comparator<Quad> quadSorter = new Comparator<Quad>(){
		public int compare(Quad a, Quad b){
			double aDis = Math.max(Math.max(dis(a.data.get(11), a.data.get(12), a.data.get(13)), dis(a.data.get(14), a.data.get(15), a.data.get(16))), Math.max(dis(a.data.get(17), a.data.get(18), a.data.get(19)), dis(a.data.get(20), a.data.get(21), a.data.get(22))));
			double bDis = Math.max(Math.max(dis(b.data.get(11), b.data.get(12), b.data.get(13)), dis(b.data.get(14), b.data.get(15), b.data.get(16))), Math.max(dis(b.data.get(17), b.data.get(18), b.data.get(19)), dis(b.data.get(20), b.data.get(21), b.data.get(22))));
			return aDis==bDis?0:aDis<bDis?1:-1;
		}
		private double dis(float x, float y, float z){ return Math.pow(Loop.INSTANCE.getCamera().x-x, 2)+Math.pow(Loop.INSTANCE.getCamera().y-y, 2)+Math.pow(Loop.INSTANCE.getCamera().z-z, 2); }
	};
	public void recompileBuffer(){
		quads.sort(quadSorter);
		int points = 0;
		int indices = 0;
		triangleCount=0;
		for(int i = 0; i<quads.size(); i++){
			points+=4;
			indices+=6;
			triangleCount+=2;
		}
		vertexBuffer=BufferUtils.createFloatBuffer(points*3);
		colorBuffer=BufferUtils.createFloatBuffer(points*3);
		textureCoordBuffer=BufferUtils.createFloatBuffer(points*2);
		indexBuffer=BufferUtils.createIntBuffer(indices);
		elementCount=0;
		indexCount=0;
		Quad q;
		for(int i = 0; i<quads.size(); i++){
			q=quads.get(i);
			addEdge(q, 0);
			addEdge(q, 1);
			addEdge(q, 2);
			addEdge(q, 3);
			addIndex(0);
			addIndex(1);
			addIndex(2);
			addIndex(0);
			addIndex(2);
			addIndex(3);
			elementCount+=4;
			indexCount+=6;
		}
		vertexBuffer.flip();
		colorBuffer.flip();
		textureCoordBuffer.flip();
		indexBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, colorBufferId);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordBufferId);
		glBufferData(GL_ARRAY_BUFFER, textureCoordBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_DYNAMIC_DRAW);
	}
	private void addEdge(Quad q, int edge){
		colorBuffer.put(q.data.get(0)).put(q.data.get(1)).put(q.data.get(2));
		if(edge==0){
			vertexBuffer.put(q.data.get(11)).put(q.data.get(12)).put(q.data.get(13));
			textureCoordBuffer.put(q.data.get(3)).put(q.data.get(4));
		}
		if(edge==1){
			vertexBuffer.put(q.data.get(14)).put(q.data.get(15)).put(q.data.get(16));
			textureCoordBuffer.put(q.data.get(5)).put(q.data.get(6));
		}
		if(edge==2){
			vertexBuffer.put(q.data.get(17)).put(q.data.get(18)).put(q.data.get(19));
			textureCoordBuffer.put(q.data.get(7)).put(q.data.get(8));
		}
		if(edge==3){
			vertexBuffer.put(q.data.get(20)).put(q.data.get(21)).put(q.data.get(22));
			textureCoordBuffer.put(q.data.get(9)).put(q.data.get(10));
		}
	}
	public void renderPart(){
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
		glVertexPointer(3, GL_FLOAT, FLOAT_SIZE*3, ZERO);
		glBindBuffer(GL_ARRAY_BUFFER, colorBufferId);
		glColorPointer(3, GL_FLOAT, FLOAT_SIZE*3, ZERO);
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordBufferId);
		glTexCoordPointer(2, GL_FLOAT, FLOAT_SIZE*2, ZERO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, ZERO);
		VoxelWorld.trisRendered+=triangleCount;
	}
	public void cleanUp(){
		glDeleteBuffers(vertexBufferId);
		glDeleteBuffers(colorBufferId);
		glDeleteBuffers(textureCoordBufferId);
		glDeleteBuffers(indexBufferId);
	}
	public void addQuad(Quad q){ if(!quads.contains(q))quads.add(q); }
	public void clear(){ quads.clear(); }
	public Texture getTexture(){ return texture; }
	public int getSize(){ return quads.size(); }
	private void addIndex(int offset){ indexBuffer.put((short)(elementCount+offset)); }
	public Quad getQuad(int index){ return quads.get(index); }
}