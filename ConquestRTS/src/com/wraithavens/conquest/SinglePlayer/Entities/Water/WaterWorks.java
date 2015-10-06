package com.wraithavens.conquest.SinglePlayer.Entities.Water;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class WaterWorks{
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
	private final int textureId;
	public WaterWorks(){
		shader = new ShaderProgram("Water");
		shader.bind();
		shader.loadUniforms("texture", "uni_offset");
		shader.setUniform1I(0, 0);
		uvAttribLocation = shader.getAttributeLocation("att_uv");
		GL20.glEnableVertexAttribArray(uvAttribLocation);
		textureId = GL11.glGenTextures();
		{
			// ---
			// Load water texture.
			// ---
			ByteBuffer pixels = BufferUtils.createByteBuffer(32*32*3*4);
			int[] pixelBuffer = new int[32*32];
			String textureFolder = WraithavensConquest.assetFolder+File.separatorChar+"BlockTextures";
			loadTexture(new File(textureFolder, "Water0.png"), pixels, pixelBuffer);
			loadTexture(new File(textureFolder, "Water1.png"), pixels, pixelBuffer);
			loadTexture(new File(textureFolder, "Water2.png"), pixels, pixelBuffer);
			pixels.flip();
			{
				// ---
				// Build texture.
				// ---
				GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, textureId);
				GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
				GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL12.glTexImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, GL11.GL_RGBA8, 32, 32, 3, 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, pixels);
			}
		}
	}
	public void addPuddle(WaterPuddle puddle){
		puddles.add(puddle);
	}
	public void dispose(){
		shader.dispose();
		GL11.glDeleteTextures(textureId);
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
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, textureId);
		for(WaterPuddle p : puddles){
			shader.setUniform3f(1, p.getX(), p.getY(), p.getZ());
			p.render(uvAttribLocation);
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
}
