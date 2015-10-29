package com.wraithavens.conquest.Math;

public class Vector4f{
	private static float clamp(float f, float min, float max){
		return f<min?min:f>max?max:f;
	}
	public float x;
	public float y;
	public float z;
	public float w;
	public Vector4f(){}
	public Vector4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public void add(Vector4f v){
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}
	public void clamp(float min, float max){
		x = clamp(x, min, max);
		y = clamp(y, min, max);
		z = clamp(z, min, max);
		w = clamp(w, min, max);
	}
	public void div(float f){
		x /= f;
		y /= f;
		z /= f;
		w /= f;
	}
	public void scale(float f){
		x *= f;
		y *= f;
		z *= f;
		w *= f;
	}
	public void set(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public void zero(){
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
}
