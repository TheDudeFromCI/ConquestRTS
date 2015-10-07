package com.wraithavens.conquest.Math;

public class Vec3i{
	public int x;
	public int y;
	public int z;
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
