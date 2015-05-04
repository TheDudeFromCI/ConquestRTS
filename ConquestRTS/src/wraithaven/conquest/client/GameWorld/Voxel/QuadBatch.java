package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

class QuadBatch{
	private int i;
	private int elementCount;
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private FloatBuffer textureCoordBuffer;
	private ShortBuffer indexBuffer;
	private final int vertexBufferId;
	private final int colorBufferId;
	private final int textureCoordBufferId;
	private final int indexBufferId;
	private final Texture texture;
	private final ArrayList<Quad> quads = new ArrayList();
	private static final long ZERO = 0;
	private static final int FLOAT_SIZE = 4;
	QuadBatch(Texture texture){
		this.texture=texture;
		vertexBufferId=glGenBuffers();
		colorBufferId=glGenBuffers();
		textureCoordBufferId=glGenBuffers();
		indexBufferId=glGenBuffers();
	}
	void removeQuad(Quad q){
		for(i=0; i<quads.size(); i++){
			if(quads.get(i)==q){
				quads.remove(i);
				return;
			}
		}
	}
	void recompileBuffer(){
		int points = 0;
		int indices = 0;
		for(int i = 0; i<quads.size(); i++){
			if(quads.get(i).centerPoint){
				points+=5;
				indices+=12;
			}else{
				points+=4;
				indices+=6;
			}
		}
		vertexBuffer=BufferUtils.createFloatBuffer(points*3);
		colorBuffer=BufferUtils.createFloatBuffer(points*3);
		textureCoordBuffer=BufferUtils.createFloatBuffer(points*2);
		indexBuffer=BufferUtils.createShortBuffer(indices);
		elementCount=0;
		Quad q;
		try{
			for(int i = 0; i<quads.size(); i++){
				q=quads.get(i);
				if(q.centerPoint){
					addEdge(q, 0);
					addEdge(q, 1);
					addEdge(q, 2);
					addEdge(q, 3);
					addEdge(q, 4);
					addIndex(0);
					addIndex(1);
					addIndex(4);
					addIndex(1);
					addIndex(2);
					addIndex(4);
					addIndex(2);
					addIndex(3);
					addIndex(4);
					addIndex(3);
					addIndex(0);
					addIndex(4);
					elementCount+=5;
				}else{
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
				}
			}
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
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
		if(edge==0){
			vertexBuffer.put(q.data.get(23)).put(q.data.get(24)).put(q.data.get(25));
			colorBuffer.put(q.data.get(0)).put(q.data.get(1)).put(q.data.get(2));
			textureCoordBuffer.put(q.data.get(15)).put(q.data.get(16));
		}
		if(edge==1){
			vertexBuffer.put(q.data.get(26)).put(q.data.get(27)).put(q.data.get(28));
			colorBuffer.put(q.data.get(3)).put(q.data.get(4)).put(q.data.get(5));
			textureCoordBuffer.put(q.data.get(17)).put(q.data.get(18));
		}
		if(edge==2){
			vertexBuffer.put(q.data.get(29)).put(q.data.get(30)).put(q.data.get(31));
			colorBuffer.put(q.data.get(6)).put(q.data.get(7)).put(q.data.get(8));
			textureCoordBuffer.put(q.data.get(19)).put(q.data.get(20));
		}
		if(edge==3){
			vertexBuffer.put(q.data.get(32)).put(q.data.get(33)).put(q.data.get(34));
			colorBuffer.put(q.data.get(9)).put(q.data.get(10)).put(q.data.get(11));
			textureCoordBuffer.put(q.data.get(21)).put(q.data.get(22));
		}
		if(edge==4){
			vertexBuffer.put(q.data.get(35)).put(q.data.get(36)).put(q.data.get(37));
			colorBuffer.put(q.data.get(12)).put(q.data.get(13)).put(q.data.get(14));
			textureCoordBuffer.put(0.5f).put(0.5f);
		}
	}
	void renderPart(){
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
		glVertexPointer(3, GL_FLOAT, FLOAT_SIZE*3, ZERO);
		glBindBuffer(GL_ARRAY_BUFFER, colorBufferId);
		glColorPointer(3, GL_FLOAT, FLOAT_SIZE*3, ZERO);
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordBufferId);
		glTexCoordPointer(2, GL_FLOAT, FLOAT_SIZE*2, ZERO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		glDrawElements(GL_TRIANGLES, indexBuffer.limit(), GL_UNSIGNED_SHORT, ZERO);
	}
	public void cleanUp(){
		glDeleteBuffers(vertexBufferId);
		glDeleteBuffers(colorBufferId);
		glDeleteBuffers(textureCoordBufferId);
		glDeleteBuffers(indexBufferId);
	}
	void addQuad(Quad q){ if(!quads.contains(q))quads.add(q); }
	public Texture getTexture(){ return texture; }
	public int getSize(){ return quads.size(); }
	private void addIndex(int offset){ indexBuffer.put((short)(elementCount+offset)); }
}