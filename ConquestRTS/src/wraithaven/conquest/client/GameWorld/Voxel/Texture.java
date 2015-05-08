package wraithaven.conquest.client.GameWorld.Voxel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

public class Texture{
	private int textureId;
	private static final int BYTES_PER_PIXEL = 4;
	public Texture(File file, int mipmapLevel, MipmapQuality quality){ textureId=loadTexture(loadImage(file), mipmapLevel, quality); }
	public Texture(BufferedImage buf, int mipmapLevel, MipmapQuality quality){ textureId=loadTexture(buf, mipmapLevel, quality); }
	public void bind(){ GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId); }
	public int getId(){ return textureId; }
	public void dispose(){ GL11.glDeleteTextures(textureId); }
	private static int loadTexture(BufferedImage image, int mipmapLevel, MipmapQuality quality){
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
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
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
}