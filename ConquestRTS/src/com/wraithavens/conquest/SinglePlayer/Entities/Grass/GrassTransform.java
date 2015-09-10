package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

public class GrassTransform{
	private float x;
	private float y;
	private float z;
	private float r;
	private float s;
	public GrassTransform(float x, float y, float z, float r, float s){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
	}
	public float getRotation(){
		return r;
	}
	public float getScale(){
		return s;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getZ(){
		return z;
	}
	public void setRotation(float r){
		this.r = r;
	}
	public void setScale(float s){
		this.s = s;
	}
	public void setX(float x){
		this.x = x;
	}
	public void setY(float y){
		this.y = y;
	}
	public void setZ(float z){
		this.z = z;
	}
}
