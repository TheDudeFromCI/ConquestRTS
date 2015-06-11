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
	private static final int BYTES_PER_PIXEL = 4;
	private static final ArrayList<Texture> textures = new ArrayList();
	private static final boolean[] TRANSPARENT_RETURN = new boolean[1];
	public static void disposeAll(){
		for(int i = 0; i<Texture.textures.size(); i++)
			Texture.textures.get(i).dispose();
		Texture.textures.clear();
	}
	private static ByteBuffer generatePixelBuffer(BufferedImage image){
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*Texture.BYTES_PER_PIXEL);
		int x, y;
		byte b;
		Texture.TRANSPARENT_RETURN[0] = false;
		for(y = 0; y<image.getHeight(); y++){
			for(x = 0; x<image.getWidth(); x++){
				int pixel = pixels[y*image.getWidth()+x];
				buffer.put((byte)((pixel>>16)&0xFF));
				buffer.put((byte)((pixel>>8)&0xFF));
				buffer.put((byte)(pixel&0xFF));
				buffer.put(b = (byte)((pixel>>24)&0xFF));
				if(b!=-1) Texture.TRANSPARENT_RETURN[0] = true;
			}
		}
		buffer.flip();
		return buffer;
	}
	public static int getLoadedTextures(){
		return Texture.textures.size();
	}
	public static Texture getTexture(String folder, String file){
		return Texture.getTexture(folder, file, 0, null);
	}
	public static Texture getTexture(String folder, String file, int mipmapping, MipmapQuality quality){
		for(int i = 0; i<Texture.textures.size(); i++)
			if(Texture.textures.get(i).file.equals(file)&&Texture.textures.get(i).folder.equals(folder)) return Texture.textures.get(i);
		return new Texture(folder, new File(folder, file), mipmapping, quality);
	}
	private static BufferedImage loadImage(File file){
		try{
			return ImageIO.read(file);
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
		return null;
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
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, Texture.generatePixelBuffer(image));
		return textureID;
	}
	public final String file;
	private final String folder;
	private int textureId;
	public final boolean transparent;
	public Texture(BufferedImage buf, int mipmapLevel, MipmapQuality quality){
		textureId = Texture.loadTexture(buf, mipmapLevel, quality);
		Texture.textures.add(this);
		file = "N/A";
		folder = "N/A";
		transparent = Texture.TRANSPARENT_RETURN[0];
	}
	private Texture(String folder, File file, int mipmapLevel, MipmapQuality quality){
		textureId = Texture.loadTexture(Texture.loadImage(file), mipmapLevel, quality);
		Texture.textures.add(this);
		this.file = file.getName();
		this.folder = folder;
		transparent = Texture.TRANSPARENT_RETURN[0];
	}
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	public void dispose(){
		GL11.glDeleteTextures(textureId);
		Texture.textures.remove(this);
	}
	public int getId(){
		return textureId;
	}
	public void updatePixels(BufferedImage image){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, Texture.generatePixelBuffer(image));
	}
}