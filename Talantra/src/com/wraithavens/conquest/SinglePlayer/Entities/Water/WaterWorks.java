package com.wraithavens.conquest.SinglePlayer.Entities.Water;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class WaterWorks{
	private static int loadTexture(File file){
		int textureId = GL11.glGenTextures();
		ByteBuffer pixels = BufferUtils.createByteBuffer(32*32*4);
		int[] pixelBuffer = new int[32*32];
		loadTexture(file, pixels, pixelBuffer);
		pixels.flip();
		{
			// ---
			// Build texture.
			// ---
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 32, 32, 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, pixels);
		}
		return textureId;
	}
	private static void loadTexture(File file, ByteBuffer pixels, int[] pixelBuffer){
		try{
			ImageIO.read(file).getRGB(0, 0, 32, 32, pixelBuffer, 0, 32);
		}catch(Exception exception){
			exception.printStackTrace();
			throw new RuntimeException();
		}
		int x, y, p;
		for(y = 0; y<32; y++){
			for(x = 0; x<32; x++){
				p = pixelBuffer[y*32+x];
				pixels.put((byte)(p>>16&0xFF));
				pixels.put((byte)(p>>8&0xFF));
				pixels.put((byte)(p&0xFF));
				pixels.put((byte)(p>>24&0xFF));
			}
		}
	}
	private final ArrayList<WaterPuddle> puddles = new ArrayList();
	private final ShaderProgram shader;
	private final int uvAttribLocation;
	private final int[] textures;
	private float blending;
	private int texture1;
	private int texture2;
	public WaterWorks(){
		shader = new ShaderProgram("Water");
		shader.bind();
		shader.loadUniforms("texture1", "texture2", "uni_offset", "uni_blending");
		shader.setUniform1I(0, 0);
		shader.setUniform1I(1, 1);
		uvAttribLocation = shader.getAttributeLocation("att_uv");
		GL20.glEnableVertexAttribArray(uvAttribLocation);
		String textureFolder = WraithavensConquest.assetFolder+File.separatorChar+"BlockTextures";
		textures =
			new int[]{
			loadTexture(new File(textureFolder, "Water0.png")),
			loadTexture(new File(textureFolder, "Water1.png")),
			loadTexture(new File(textureFolder, "Water2.png"))
		};
	}
	public void addPuddle(WaterPuddle puddle){
		puddles.add(puddle);
	}
	public void dispose(){
		shader.dispose();
		for(int i : textures)
			GL11.glDeleteTextures(i);
		for(WaterPuddle p : puddles)
			p.dispose();
	}
	public void removePuddle(WaterPuddle puddle){
		puddles.remove(puddle);
		puddle.dispose();
	}
	public void render(){
		if(puddles.isEmpty())
			return;
		GL11.glEnable(GL11.GL_BLEND);
		shader.bind();
		{
			// Setup the textures.
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture2);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture1);
			shader.setUniform1f(3, blending);
		}
		for(WaterPuddle p : puddles){
			shader.setUniform3f(2, p.getX(), p.getY(), p.getZ());
			p.render(uvAttribLocation);
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
	public void update(double time){
		time *= 2;
		blending = (float)(time%1);
		double subTime = time%textures.length;
		texture1 = (int)Math.floor(subTime);
		texture2 = (int)Math.ceil(subTime);
		if(texture2==textures.length)
			texture2 = 0;
		texture1 = textures[texture1];
		texture2 = textures[texture2];
	}
}
