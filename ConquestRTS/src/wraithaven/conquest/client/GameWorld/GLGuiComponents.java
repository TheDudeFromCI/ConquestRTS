package wraithaven.conquest.client.GameWorld;

import java.awt.Dimension;
import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import static org.lwjgl.opengl.GL11.*;

public class GLGuiComponents{
	public float scaleX = 1;
	public float scaleY = 1;
	private GLGuiImage img;
	private final Dimension screenSize;
	private final ArrayList<GLGuiImage> images = new ArrayList();
	public void render(){
		MatrixUtils.setupImageOrtho(screenSize.width*scaleX, screenSize.height*scaleY, -1000, 1000);
		for(int i = 0; i<images.size(); i++){
			img=images.get(i);
			img.texture.bind();
			glBegin(GL_QUADS);
			glTexCoord2f(0, 1);
			glVertex3f(screenSize.width*img.x*scaleX, screenSize.height*img.y*scaleY, img.z);
			glTexCoord2f(1, 1);
			glVertex3f(screenSize.width*(img.x*scaleX+img.w), screenSize.height*img.y*scaleY, img.z);
			glTexCoord2f(1, 0);
			glVertex3f(screenSize.width*(img.x*scaleX+img.w), screenSize.height*(img.y*scaleY+img.h), img.z);
			glTexCoord2f(0, 0);
			glVertex3f(screenSize.width*img.x*scaleX, screenSize.height*(img.y*scaleY+img.h), img.z);
			glEnd();
		}
	}
	public GLGuiComponents(Dimension screenSize){ this.screenSize=screenSize; }
	public void addComponent(GLGuiImage guiImage){ images.add(guiImage); }
	public void removeComponent(GLGuiImage guiImage){ images.remove(guiImage); }
	public void clearComponents(){ images.clear(); }
}