package wraithaven.conquest.client.GameWorld;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import java.awt.Dimension;
import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;

public class GLGuiComponents{
	private final ArrayList<GLGuiImage> images = new ArrayList();
	private GLGuiImage img;
	private final Dimension screenSize;
	public GLGuiComponents(Dimension screenSize){
		this.screenSize = screenSize;
	}
	public void addComponent(GLGuiImage guiImage){
		images.add(guiImage);
	}
	public void clearComponents(){
		images.clear();
	}
	public GLGuiImage getComponent(int index){
		return images.get(index);
	}
	public void removeComponent(GLGuiImage guiImage){
		images.remove(guiImage);
	}
	public void render(){
		MatrixUtils.setupImageOrtho(screenSize.width, screenSize.height, -1000, 1000);
		for(int i = 0; i<images.size(); i++){
			img = images.get(i);
			img.texture.bind();
			glBegin(GL_QUADS);
			glTexCoord2f(0, 1);
			glVertex3f(img.x, img.y, img.z);
			glTexCoord2f(1, 1);
			glVertex3f(img.x+img.w, img.y, img.z);
			glTexCoord2f(1, 0);
			glVertex3f(img.x+img.w, img.y+img.h, img.z);
			glTexCoord2f(0, 0);
			glVertex3f(img.x, img.y+img.h, img.z);
			glEnd();
		}
	}
}