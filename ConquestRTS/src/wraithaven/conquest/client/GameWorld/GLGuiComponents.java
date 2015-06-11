package wraithaven.conquest.client.GameWorld;

import java.awt.Dimension;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
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
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(img.x, img.y, img.z);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(img.x+img.w, img.y, img.z);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(img.x+img.w, img.y+img.h, img.z);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(img.x, img.y+img.h, img.z);
			GL11.glEnd();
		}
	}
}