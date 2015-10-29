package com.wraithavens.conquest.Math;

public class Vec3i{
	public int x;
	public int y;
	public int z;
	public Vec3i(){}
	public Vec3i(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set(Vec3i o){
		x = o.x;
		y = o.y;
		z = o.z;
	}
	public void sub(Vec3i o){
		x -= o.x;
		y -= o.y;
		z -= o.z;
	}
}
