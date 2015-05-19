package wraithaven.conquest.client.GameWorld.Voxel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

public class Texture{
	private int textureId;
	private final String file;
	private final String folder;
	private static final int BYTES_PER_PIXEL = 4;
	private static final ArrayList<Texture> textures = new ArrayList();
	private Texture(String folder, File file, int mipmapLevel, MipmapQuality quality){
		textureId=loadTexture(loadImage(file), mipmapLevel, quality);
		textures.add(this);
		this.file=file.getName();
		this.folder=folder;
	}
	public Texture(BufferedImage buf, int mipmapLevel, MipmapQuality quality){
		textureId=loadTexture(buf, mipmapLevel, quality);
		textures.add(this);
		file="N/A";
		folder="N/A";
	}
	public void updatePixels(BufferedImage image){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, generatePixelBuffer(image));
	}
	public void dispose(){
		GL11.glDeleteTextures(textureId);
		textures.remove(this);
	}
	public void bind(){ GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId); }
	public int getId(){ return textureId; }
	private static ByteBuffer generatePixelBuffer(BufferedImage image){
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*BYTES_PER_PIXEL);
		int x, y;
		for(y=0; y<image.getHeight(); y++){
			for(x=0; x<image.getWidth(); x++){
				int pixel = pixels[y*image.getWidth()+x];
				buffer.put((byte)((pixel>>16)&0xFF));
				buffer.put((byte)((pixel>>8)&0xFF));
				buffer.put((byte)(pixel&0xFF));
				buffer.put((byte)((pixel>>24)&0xFF));
			}
		}
		buffer.flip();
		return buffer;
	}
	private static int loadTexture(BufferedImage image, int mipmapLevel, MipmapQuality quality){
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		if(mipmapLevel>0){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, quality.getQuality());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, quality.getQuality());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmapLevel-1);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		}else{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, generatePixelBuffer(image));
		return textureID;
	}
	private static BufferedImage loadImage(File file){
		try{ return ImageIO.read(file);
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	public static void disposeAll(){
		for(int i = 0; i<textures.size(); i++)textures.get(i).dispose();
		textures.clear();
	}
	public static Texture getTexture(String folder, String file, int mipmapping, MipmapQuality quality){
		for(int i = 0; i<textures.size(); i++)if(textures.get(i).file.equals(file)&&textures.get(i).folder.equals(folder))return textures.get(i);
		return new Texture(folder, new File(folder, file), mipmapping, quality);
	}
	public static Texture getTexture(String folder, String file){ return getTexture(folder, file, 0, null); }
	public static int getLoadedTextures(){ return textures.size(); }
}