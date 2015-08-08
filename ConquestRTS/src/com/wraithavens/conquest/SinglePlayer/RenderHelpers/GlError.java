package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class GlError{
	public static void dumpError(){
		if(!Debug)
			return;
		int i = GL11.glGetError();
		if(i!=GL11.GL_NO_ERROR){
			System.out.println(">>>OpenGL Error Found!!!");
			switch(i){
				case GL11.GL_NO_ERROR:
					break;
				case GL11.GL_INVALID_ENUM:
					System.out.println("Invalid enum.");
					break;
				case GL11.GL_INVALID_VALUE:
					System.out.println("Invalid value.");
					break;
				case GL11.GL_INVALID_OPERATION:
					System.out.println("Invalid operation.");
					break;
				case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
					System.out.println("Invalid framebuffer operation.");
					break;
				case GL11.GL_OUT_OF_MEMORY:
					System.out.println("Out of memory.");
					break;
				case GL11.GL_STACK_UNDERFLOW:
					System.out.println("Stack underflow.");
					break;
				case GL11.GL_STACK_OVERFLOW:
					System.out.println("Stack overflow.");
					break;
			}
			Thread.dumpStack();
		}
	}
	public static void err(String msg){
		System.err.println(msg);
		dumpError();
	}
	public static void out(String msg){
		if(Debug){
			System.out.println(msg);
			dumpError();
		}
	}
	private static final boolean Debug = false;
}
