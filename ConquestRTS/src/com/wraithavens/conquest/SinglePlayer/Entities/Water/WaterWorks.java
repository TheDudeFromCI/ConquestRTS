package com.wraithavens.conquest.SinglePlayer.Entities.Water;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class WaterWorks{
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
			final int textureSize = 32;
			ByteBuffer pixels = BufferUtils.createByteBuffer(textureSize*textureSize*4);
			int[] pixelBuffer = new int[textureSize*textureSize];
			File file;
			try{
				file = new File(WraithavensConquest.assetFolder+File.separatorChar+"BlockTextures", "Water.png");
				ImageIO.read(file).getRGB(0, 0, textureSize, textureSize, pixelBuffer, 0, textureSize);
			}catch(Exception exception){
				exception.printStackTrace();
				throw new RuntimeException();
			}
			int x, y, p;
			for(y = 0; y<textureSize; y++){
				for(x = 0; x<textureSize; x++){
					p = pixelBuffer[y*textureSize+x];
					pixels.put((byte)(p>>16&0xFF));
					pixels.put((byte)(p>>8&0xFF));
					pixels.put((byte)(p&0xFF));
					pixels.put((byte)(p>>24&0xFF));
				}
			}
			pixels.flip();
			{
				// ---
				// Build texture.
				// ---
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, textureSize, textureSize, 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
			}
		}
	}
	public void addPuddle(WaterPuddle puddle){
		puddles.add(puddle);
		// TODO dispose puddles.
	}
	public void dispose(){
		shader.dispose();
		GL11.glDeleteTextures(textureId);
		for(WaterPuddle p : puddles)
			p.dispose();
	}
	public void render(){
		if(puddles.isEmpty())
			return;
		GL11.glEnable(GL11.GL_BLEND);
		shader.bind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		for(WaterPuddle p : puddles){
			shader.setUniform3f(1, p.getX(), p.getY(), p.getZ());
			p.render(uvAttribLocation);
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
}
