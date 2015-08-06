package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public class ShaderProgram{
	private static String readFile(File file){
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String s;
			while((s = in.readLine())!=null){
				sb.append(s);
				sb.append('\n');
			}
			in.close();
			return sb.toString();
		}catch(Exception exception){
			exception.printStackTrace();
			throw new RuntimeException("Could not read shader file!");
		}
	}
	private final int program;
	private final int vs;
	private final int fs;
	private final int gs;
	private int[] uniforms;
	public ShaderProgram(File vertexShader, File geometryShader, File fragmentShader){
		this(ShaderProgram.readFile(vertexShader), geometryShader==null?null:ShaderProgram
			.readFile(geometryShader), ShaderProgram.readFile(fragmentShader));
	}
	private ShaderProgram(String vertexShader, String geometryShader, String fragmentShader){
		// Vertex Shader
		vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vs, vertexShader);
		GL20.glCompileShader(vs);
		int vsStatus = GL20.glGetShaderi(vs, GL20.GL_COMPILE_STATUS);
		if(vsStatus!=GL11.GL_TRUE)
			throw new RuntimeException(GL20.glGetShaderInfoLog(vs));
		// Geometry Shader
		if(geometryShader!=null){
			gs = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
			GL20.glShaderSource(gs, geometryShader);
			GL20.glCompileShader(gs);
			int gsStatus = GL20.glGetShaderi(gs, GL20.GL_COMPILE_STATUS);
			if(gsStatus!=GL11.GL_TRUE)
				throw new RuntimeException(GL20.glGetShaderInfoLog(gs));
		}else
			gs = -1;
		// Fragment Shader
		fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fs, fragmentShader);
		GL20.glCompileShader(fs);
		int fsStatus = GL20.glGetShaderi(fs, GL20.GL_COMPILE_STATUS);
		if(fsStatus!=GL11.GL_TRUE)
			throw new RuntimeException(GL20.glGetShaderInfoLog(fs));
		// Compile Program
		program = GL20.glCreateProgram();
		GL20.glAttachShader(program, vs);
		GL20.glAttachShader(program, fs);
		if(geometryShader!=null)
			GL20.glAttachShader(program, gs);
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		int pStatus = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
		if(pStatus!=GL11.GL_TRUE)
			throw new RuntimeException(GL20.glGetProgramInfoLog(program));
	}
	public void bind(){
		if(GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)!=program)
			GL20.glUseProgram(program);
	}
	public void dispose(){
		if(GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)==program)
			GL20.glUseProgram(0);
		GL20.glDetachShader(program, vs);
		if(gs!=-1)
			GL20.glDetachShader(program, gs);
		GL20.glDetachShader(program, fs);
		GL20.glDeleteShader(vs);
		if(gs!=-1)
			GL20.glDeleteShader(gs);
		GL20.glDeleteShader(fs);
		GL20.glDeleteProgram(program);
	}
	public int getAttributeLocation(String attribute){
		return GL20.glGetAttribLocation(program, attribute);
	}
	public void loadUniforms(String... uni){
		uniforms = new int[uni.length];
		int i = 0;
		for(String s : uni){
			uniforms[i] = GL20.glGetUniformLocation(program, s);
			i++;
		}
	}
	public void setUniform1I(int index, int value){
		GL20.glUniform1i(uniforms[index], value);
	}
	public void setUniform2f(int index, float v1, float v2){
		GL20.glUniform2f(uniforms[index], v1, v2);
	}
	public void setUniform3f(int index, float v1, float v2, float v3){
		GL20.glUniform3f(uniforms[index], v1, v2, v3);
	}
}
