package com.wraithavens.conquest.Math;

import java.nio.FloatBuffer;

public class Matrix4f{
	private float m00, m10, m20, m30;
	private float m01, m11, m21, m31;
	private float m02, m12, m22, m32;
	private float m03, m13, m23, m33;
	public Matrix4f(){
		setIdentity();
	}
	public void scale(float x, float y, float z){
		m00 *= x;
		m01 *= x;
		m02 *= x;
		m03 *= x;
		m10 *= y;
		m11 *= y;
		m12 *= y;
		m13 *= y;
		m20 *= z;
		m21 *= z;
		m22 *= z;
		m23 *= z;
	}
	public void setIdentity(){
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}
	public void store(FloatBuffer data){
		data.put(m00);
		data.put(m01);
		data.put(m02);
		data.put(m03);
		data.put(m10);
		data.put(m11);
		data.put(m12);
		data.put(m13);
		data.put(m20);
		data.put(m21);
		data.put(m22);
		data.put(m23);
		data.put(m30);
		data.put(m31);
		data.put(m32);
		data.put(m33);
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(m00).append(' ').append(m10).append(' ').append(m20).append(' ').append(m30).append('\n');
		sb.append(m01).append(' ').append(m11).append(' ').append(m21).append(' ').append(m31).append('\n');
		sb.append(m02).append(' ').append(m12).append(' ').append(m22).append(' ').append(m32).append('\n');
		sb.append(m03).append(' ').append(m13).append(' ').append(m23).append(' ').append(m33).append('\n');
		return sb.toString();
	}
	public void translate(float x, float y, float z){
		m30 += m00*x+m10*y+m20*z;
		m31 += m01*x+m11*y+m21*z;
		m32 += m02*x+m12*y+m22*z;
		m33 += m03*x+m13*y+m23*z;
	}
}
