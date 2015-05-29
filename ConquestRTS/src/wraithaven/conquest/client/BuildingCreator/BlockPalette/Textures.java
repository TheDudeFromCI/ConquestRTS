package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.io.File;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.Voxel.MipmapQuality;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class Textures{
	private static final int TEXTURE_COLS_SHOWN = 7;
	private static final float TEXTURE_SEPERATION = 10;
	private static final float TEXTURE_SIZE = 40;
	private static final float TEXTURE_TOTAL_SIZE = TEXTURE_SIZE+TEXTURE_SEPERATION;
	private float r = 1,
			g = 1,
			b = 1;
	private int scrollbarPosition;
	private int selectedTexture;
	private final UiElement[] textures;
	private final float width,
			height;
	public Textures(float width, float height){
		this.width = width;
		this.height = height;
		File textureFolder = new File(ClientLauncher.textureFolder);
		File[] texFile = textureFolder.listFiles();
		textures = new UiElement[texFile.length];
		for(int i = 0; i<texFile.length; i++){
			textures[i] = new UiElement(Texture.getTexture(ClientLauncher.textureFolder, texFile[i].getName(), 4, MipmapQuality.HIGH));
			textures[i].h = TEXTURE_SIZE/768*height;
			textures[i].w = TEXTURE_SIZE/1024*width;
		}
		updateSliderPosition(0);
	}
	public Texture getTexture(){
		return textures[selectedTexture].texture;
	}
	public Texture getTexture(int id){
		return textures[id].texture;
	}
	public boolean mouseClick(double xPos, double yPos){
		yPos = Loop.screenRes.height-yPos;
		int lastShown = Math.min((scrollbarPosition+TEXTURE_COLS_SHOWN)*2, textures.length);
		for(int i = scrollbarPosition*2; i<lastShown; i++){
			if(xPos>=textures[i].x&&xPos<textures[i].x+textures[i].w&&yPos>=textures[i].y&&yPos<textures[i].y+textures[i].h){
				selectedTexture = i;
				return true;
			}
		}
		return false;
	}
	public void render(){
		int lastShown = Math.min((scrollbarPosition+TEXTURE_COLS_SHOWN)*2, textures.length);
		for(int i = scrollbarPosition*2; i<lastShown; i++)
			UI.renderElement(textures[i]);
		renderMain();
	}
	private void renderMain(){
		GL11.glPushMatrix();
		textures[selectedTexture].texture.bind();
		float w = 115f/1024*width;
		float h = 130f/768*height;
		GL11.glTranslatef(406f/1024*width+(Loop.screenRes.width-width)/2, Loop.screenRes.height-(594f/768*height+(Loop.screenRes.height-height)/2+h), 0);
		GL11.glColor3f(r, g, b);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, 0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(w, 0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(w, h, 0);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, h, 0);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glColor3f(1, 1, 1);
	}
	public void updateColor(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public void updateSliderPosition(float percent){
		int size = (int)Math.ceil(textures.length/2f)-TEXTURE_COLS_SHOWN;
		if(size<=0) size = 0;
		scrollbarPosition = (int)(percent*size);
		int lastShown = Math.min((scrollbarPosition+TEXTURE_COLS_SHOWN)*2, textures.length);
		for(int i = scrollbarPosition*2; i<lastShown; i++){
			textures[i].x = x(i);
			textures[i].y = y(i);
		}
	}
	private float x(int id){
		return (id-scrollbarPosition*2)/2*TEXTURE_TOTAL_SIZE/1024*width+51f/1024*width+(Loop.screenRes.width-width)/2;
	}
	private float y(int id){
		return (1-id%2)*TEXTURE_TOTAL_SIZE/768*height+75f/768*height+(Loop.screenRes.height-height)/2;
	}
}