package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import wraithaven.conquest.client.BuildingCreator.Loop;

public class QuadBatch{
	private static final int FLOAT_SIZE = 4;
	private FloatBuffer colorBuffer;
	private final int colorBufferId;
	private int elementCount,
	indexCount;
	private int i;
	public final float centerX,
			centerY,
			centerZ;
	private IntBuffer indexBuffer;
	private final int indexBufferId;
	private final ArrayList<Quad> quads = new ArrayList();
	private Comparator<Quad> quadSorter = new Comparator<Quad>(){
		public int compare(Quad a, Quad b){
			double aDis = Math.max(Math.max(dis(a.data.get(11), a.data.get(12), a.data.get(13)), dis(a.data.get(14), a.data.get(15), a.data.get(16))), Math.max(dis(a.data.get(17), a.data.get(18), a.data.get(19)), dis(a.data.get(20), a.data.get(21), a.data.get(22))));
			double bDis = Math.max(Math.max(dis(b.data.get(11), b.data.get(12), b.data.get(13)), dis(b.data.get(14), b.data.get(15), b.data.get(16))), Math.max(dis(b.data.get(17), b.data.get(18), b.data.get(19)), dis(b.data.get(20), b.data.get(21), b.data.get(22))));
			return aDis==bDis?0:aDis<bDis?1:-1;
		}
		private double dis(float x, float y, float z){
			return Math.pow(Loop.INSTANCE.getCamera().x-x, 2)+Math.pow(Loop.INSTANCE.getCamera().y-y, 2)+Math.pow(Loop.INSTANCE.getCamera().z-z, 2);
		}
	};
	private final Texture texture;
	private FloatBuffer textureCoordBuffer;
	private final int textureCoordBufferId;
	public int triangleCount;
	private FloatBuffer vertexBuffer;
	private final int vertexBufferId;
	public QuadBatch(Texture texture){
		this(texture, 0, 0, 0);
	}
	public QuadBatch(Texture texture, float centerX, float centerY, float centerZ){
		this.texture = texture;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		vertexBufferId = GL15.glGenBuffers();
		colorBufferId = GL15.glGenBuffers();
		textureCoordBufferId = GL15.glGenBuffers();
		indexBufferId = GL15.glGenBuffers();
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
	private void addIndex(int offset){
		indexBuffer.put((short)(elementCount+offset));
	}
	public void addQuad(Quad q){
		if(!quads.contains(q)) quads.add(q);
	}
	public void cleanUp(){
		GL15.glDeleteBuffers(vertexBufferId);
		GL15.glDeleteBuffers(colorBufferId);
		GL15.glDeleteBuffers(textureCoordBufferId);
		GL15.glDeleteBuffers(indexBufferId);
	}
	public void clear(){
		quads.clear();
	}
	public Quad getQuad(int index){
		return quads.get(index);
	}
	public int getSize(){
		return quads.size();
	}
	public Texture getTexture(){
		return texture;
	}
	public void recompileBuffer(){
		quads.sort(quadSorter);
		int points = 0;
		int indices = 0;
		triangleCount = 0;
		for(int i = 0; i<quads.size(); i++){
			points += 4;
			indices += 6;
			triangleCount += 2;
		}
		vertexBuffer = BufferUtils.createFloatBuffer(points*3);
		colorBuffer = BufferUtils.createFloatBuffer(points*3);
		textureCoordBuffer = BufferUtils.createFloatBuffer(points*2);
		indexBuffer = BufferUtils.createIntBuffer(indices);
		elementCount = 0;
		indexCount = 0;
		Quad q;
		for(int i = 0; i<quads.size(); i++){
			q = quads.get(i);
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
			elementCount += 4;
			indexCount += 6;
		}
		vertexBuffer.flip();
		colorBuffer.flip();
		textureCoordBuffer.flip();
		indexBuffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureCoordBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoordBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_DYNAMIC_DRAW);
	}
	public void removeQuad(Quad q){
		for(i = 0; i<quads.size(); i++){
			if(quads.get(i)==q){
				quads.remove(i);
				return;
			}
		}
	}
	public void renderPart(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferId);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, QuadBatch.FLOAT_SIZE*3, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferId);
		GL11.glColorPointer(3, GL11.GL_FLOAT, QuadBatch.FLOAT_SIZE*3, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureCoordBufferId);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, QuadBatch.FLOAT_SIZE*2, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		VoxelWorld.trisRendered += triangleCount;
	}
}