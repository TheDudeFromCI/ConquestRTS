package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Frustum{
	private static final int A = 0;
	private static final int B = 1;
	private static final int BACK = 4;
	private static final int BOTTOM = 2;
	private static final int C = 2;
	private static final int D = 3;
	private static final int FRONT = 5;
	private static final int LEFT = 1;
	private static final int RIGHT = 0;
	private static final int TOP = 3;
	private static void normalizePlane(float[][] frustum, int side){
		float magnitude = (float)Math.sqrt(frustum[side][Frustum.A]*frustum[side][Frustum.A]+frustum[side][Frustum.B]*frustum[side][Frustum.B]+frustum[side][Frustum.C]*frustum[side][Frustum.C]);
		frustum[side][Frustum.A] /= magnitude;
		frustum[side][Frustum.B] /= magnitude;
		frustum[side][Frustum.C] /= magnitude;
		frustum[side][Frustum.D] /= magnitude;
	}
	private final float[] clipMatrix = new float[16];
	private final float[][] frustum = new float[6][4];
	private int i;
	private final FloatBuffer modelBuffer;
	private final float[] modelMatrix = new float[16];
	private final FloatBuffer projectionBuffer;
	private final float[] projectionMatrix = new float[16];
	public Frustum(){
		modelBuffer = BufferUtils.createFloatBuffer(16);
		projectionBuffer = BufferUtils.createFloatBuffer(16);
	}
	void calculateFrustum(){
		for(int i = 0; i<16; i++){
			clipMatrix[i] = 0;
			modelMatrix[i] = 0;
			projectionMatrix[i] = 0;
		}
		projectionBuffer.rewind();
		GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectionBuffer);
		projectionBuffer.rewind();
		projectionBuffer.get(projectionMatrix);
		modelBuffer.rewind();
		GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelBuffer);
		modelBuffer.rewind();
		modelBuffer.get(modelMatrix);
		clipMatrix[0] = modelMatrix[0]*projectionMatrix[0]+modelMatrix[1]*projectionMatrix[4]+modelMatrix[2]*projectionMatrix[8]+modelMatrix[3]*projectionMatrix[12];
		clipMatrix[1] = modelMatrix[0]*projectionMatrix[1]+modelMatrix[1]*projectionMatrix[5]+modelMatrix[2]*projectionMatrix[9]+modelMatrix[3]*projectionMatrix[13];
		clipMatrix[2] = modelMatrix[0]*projectionMatrix[2]+modelMatrix[1]*projectionMatrix[6]+modelMatrix[2]*projectionMatrix[10]+modelMatrix[3]*projectionMatrix[14];
		clipMatrix[3] = modelMatrix[0]*projectionMatrix[3]+modelMatrix[1]*projectionMatrix[7]+modelMatrix[2]*projectionMatrix[11]+modelMatrix[3]*projectionMatrix[15];
		clipMatrix[4] = modelMatrix[4]*projectionMatrix[0]+modelMatrix[5]*projectionMatrix[4]+modelMatrix[6]*projectionMatrix[8]+modelMatrix[7]*projectionMatrix[12];
		clipMatrix[5] = modelMatrix[4]*projectionMatrix[1]+modelMatrix[5]*projectionMatrix[5]+modelMatrix[6]*projectionMatrix[9]+modelMatrix[7]*projectionMatrix[13];
		clipMatrix[6] = modelMatrix[4]*projectionMatrix[2]+modelMatrix[5]*projectionMatrix[6]+modelMatrix[6]*projectionMatrix[10]+modelMatrix[7]*projectionMatrix[14];
		clipMatrix[7] = modelMatrix[4]*projectionMatrix[3]+modelMatrix[5]*projectionMatrix[7]+modelMatrix[6]*projectionMatrix[11]+modelMatrix[7]*projectionMatrix[15];
		clipMatrix[8] = modelMatrix[8]*projectionMatrix[0]+modelMatrix[9]*projectionMatrix[4]+modelMatrix[10]*projectionMatrix[8]+modelMatrix[11]*projectionMatrix[12];
		clipMatrix[9] = modelMatrix[8]*projectionMatrix[1]+modelMatrix[9]*projectionMatrix[5]+modelMatrix[10]*projectionMatrix[9]+modelMatrix[11]*projectionMatrix[13];
		clipMatrix[10] = modelMatrix[8]*projectionMatrix[2]+modelMatrix[9]*projectionMatrix[6]+modelMatrix[10]*projectionMatrix[10]+modelMatrix[11]*projectionMatrix[14];
		clipMatrix[11] = modelMatrix[8]*projectionMatrix[3]+modelMatrix[9]*projectionMatrix[7]+modelMatrix[10]*projectionMatrix[11]+modelMatrix[11]*projectionMatrix[15];
		clipMatrix[12] = modelMatrix[12]*projectionMatrix[0]+modelMatrix[13]*projectionMatrix[4]+modelMatrix[14]*projectionMatrix[8]+modelMatrix[15]*projectionMatrix[12];
		clipMatrix[13] = modelMatrix[12]*projectionMatrix[1]+modelMatrix[13]*projectionMatrix[5]+modelMatrix[14]*projectionMatrix[9]+modelMatrix[15]*projectionMatrix[13];
		clipMatrix[14] = modelMatrix[12]*projectionMatrix[2]+modelMatrix[13]*projectionMatrix[6]+modelMatrix[14]*projectionMatrix[10]+modelMatrix[15]*projectionMatrix[14];
		clipMatrix[15] = modelMatrix[12]*projectionMatrix[3]+modelMatrix[13]*projectionMatrix[7]+modelMatrix[14]*projectionMatrix[11]+modelMatrix[15]*projectionMatrix[15];
		frustum[Frustum.LEFT][Frustum.A] = clipMatrix[3]+clipMatrix[0];
		frustum[Frustum.LEFT][Frustum.B] = clipMatrix[7]+clipMatrix[4];
		frustum[Frustum.LEFT][Frustum.C] = clipMatrix[11]+clipMatrix[8];
		frustum[Frustum.LEFT][Frustum.D] = clipMatrix[15]+clipMatrix[12];
		Frustum.normalizePlane(frustum, Frustum.LEFT);
		frustum[Frustum.RIGHT][Frustum.A] = clipMatrix[3]-clipMatrix[0];
		frustum[Frustum.RIGHT][Frustum.B] = clipMatrix[7]-clipMatrix[4];
		frustum[Frustum.RIGHT][Frustum.C] = clipMatrix[11]-clipMatrix[8];
		frustum[Frustum.RIGHT][Frustum.D] = clipMatrix[15]-clipMatrix[12];
		Frustum.normalizePlane(frustum, Frustum.RIGHT);
		frustum[Frustum.BOTTOM][Frustum.A] = clipMatrix[3]+clipMatrix[1];
		frustum[Frustum.BOTTOM][Frustum.B] = clipMatrix[7]+clipMatrix[5];
		frustum[Frustum.BOTTOM][Frustum.C] = clipMatrix[11]+clipMatrix[9];
		frustum[Frustum.BOTTOM][Frustum.D] = clipMatrix[15]+clipMatrix[13];
		Frustum.normalizePlane(frustum, Frustum.BOTTOM);
		frustum[Frustum.TOP][Frustum.A] = clipMatrix[3]-clipMatrix[1];
		frustum[Frustum.TOP][Frustum.B] = clipMatrix[7]-clipMatrix[5];
		frustum[Frustum.TOP][Frustum.C] = clipMatrix[11]-clipMatrix[9];
		frustum[Frustum.TOP][Frustum.D] = clipMatrix[15]-clipMatrix[13];
		Frustum.normalizePlane(frustum, Frustum.TOP);
		frustum[Frustum.FRONT][Frustum.A] = clipMatrix[3]+clipMatrix[2];
		frustum[Frustum.FRONT][Frustum.B] = clipMatrix[7]+clipMatrix[6];
		frustum[Frustum.FRONT][Frustum.C] = clipMatrix[11]+clipMatrix[10];
		frustum[Frustum.FRONT][Frustum.D] = clipMatrix[15]+clipMatrix[14];
		Frustum.normalizePlane(frustum, Frustum.FRONT);
		frustum[Frustum.BACK][Frustum.A] = clipMatrix[3]-clipMatrix[2];
		frustum[Frustum.BACK][Frustum.B] = clipMatrix[7]-clipMatrix[6];
		frustum[Frustum.BACK][Frustum.C] = clipMatrix[11]-clipMatrix[10];
		frustum[Frustum.BACK][Frustum.D] = clipMatrix[15]-clipMatrix[14];
		Frustum.normalizePlane(frustum, Frustum.BACK);
	}
	public boolean cubeInFrustum(float x, float y, float z, float size){
		for(i = 0; i<6; i++){
			if(frustum[i][Frustum.A]*(x-size)+frustum[i][Frustum.B]*(y-size)+frustum[i][Frustum.C]*(z-size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x+size)+frustum[i][Frustum.B]*(y-size)+frustum[i][Frustum.C]*(z-size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x-size)+frustum[i][Frustum.B]*(y+size)+frustum[i][Frustum.C]*(z-size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x+size)+frustum[i][Frustum.B]*(y+size)+frustum[i][Frustum.C]*(z-size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x-size)+frustum[i][Frustum.B]*(y-size)+frustum[i][Frustum.C]*(z+size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x+size)+frustum[i][Frustum.B]*(y-size)+frustum[i][Frustum.C]*(z+size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x-size)+frustum[i][Frustum.B]*(y+size)+frustum[i][Frustum.C]*(z+size)+frustum[i][Frustum.D]>0) continue;
			if(frustum[i][Frustum.A]*(x+size)+frustum[i][Frustum.B]*(y+size)+frustum[i][Frustum.C]*(z+size)+frustum[i][Frustum.D]>0) continue;
			return false;
		}
		return true;
	}
}