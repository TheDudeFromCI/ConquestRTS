package com.wraithavens.conquest.SinglePlayer.RenderHelpers;

import com.wraithavens.conquest.Math.Vec3i;
import com.wraithavens.conquest.Math.Vector3f;

public class Plotter implements Plot<Vec3i>{
	private static void div(Vector3f a, Vector3f b){
		a.x /= b.x;
		a.y /= b.y;
		a.z /= b.z;
	}
	private final Vector3f delta = new Vector3f();
	private final Vector3f dir = new Vector3f();
	private final Vec3i index = new Vec3i();
	private int limit;
	private final Vector3f max = new Vector3f();
	private final Vector3f off = new Vector3f();
	private int plotted;
	private final Vector3f pos = new Vector3f();
	private int sideHit;
	private final Vec3i sign = new Vec3i();
	private final Vector3f size = new Vector3f();
	public Plotter(float offx, float offy, float offz, float width, float height, float depth){
		off.set(offx, offy, offz);
		size.set(width, height, depth);
	}
	public Vector3f actual(){
		return new Vector3f(index.x*size.x+off.x, index.y*size.y+off.y, index.z*size.z+off.z);
	}
	public Vector3f delta(){
		return delta;
	}
	public Vector3f direction(){
		return dir;
	}
	public void end(){
		plotted = limit+1;
	}
	public Vec3i get(){
		return index;
	}
	public int getSideHit(){
		return sideHit;
	}
	public int limit(){
		return limit;
	}
	public Vector3f max(){
		return max;
	}
	public boolean next(){
		if(plotted++>0){
			float mx = sign.x*max.x;
			float my = sign.y*max.y;
			float mz = sign.z*max.z;
			if(mx<my&&mx<mz){
				max.x += delta.x;
				index.x += sign.x;
				sideHit = sign.x>0?1:0;
			}else if(mz<my&&mz<mx){
				max.z += delta.z;
				index.z += sign.z;
				sideHit = sign.z>0?5:4;
			}else{
				max.y += delta.y;
				index.y += sign.y;
				sideHit = sign.y>0?3:2;
			}
		}
		return plotted<=limit;
	}
	public Vector3f offset(){
		return off;
	}
	public void offset(float x, float y, float z){
		off.set(x, y, z);
	}
	public void plot(Vector3f position, Vector3f direction, int cells){
		limit = cells;
		pos.set(position);
		dir.set(direction);
		dir.normalize();
		delta.set(size);
		Plotter.div(delta, dir);
		sign.x = dir.x>0?1:dir.x<0?-1:0;
		sign.y = dir.y>0?1:dir.y<0?-1:0;
		sign.z = dir.z>0?1:dir.z<0?-1:0;
		reset();
	}
	public int plotted(){
		return plotted;
	}
	public Vector3f position(){
		return pos;
	}
	public void reset(){
		plotted = 0;
		index.x = (int)Math.floor((pos.x-off.x)/size.x);
		index.y = (int)Math.floor((pos.y-off.y)/size.y);
		index.z = (int)Math.floor((pos.z-off.z)/size.z);
		float ax = index.x*size.x+off.x;
		float ay = index.y*size.y+off.y;
		float az = index.z*size.z+off.z;
		max.x = sign.x>0?ax+size.x-pos.x:pos.x-ax;
		max.y = sign.y>0?ay+size.y-pos.y:pos.y-ay;
		max.z = sign.z>0?az+size.z-pos.z:pos.z-az;
		Plotter.div(max, dir);
	}
	public Vec3i sign(){
		return sign;
	}
	public Vector3f size(){
		return size;
	}
	public void size(float w, float h, float d){
		size.set(w, h, d);
	}
}
