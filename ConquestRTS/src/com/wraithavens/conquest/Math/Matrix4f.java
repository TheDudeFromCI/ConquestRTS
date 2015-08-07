package com.wraithavens.conquest.Math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Matrix4f{
	private final FloatBuffer data;
	public Matrix4f(){
		data = BufferUtils.createFloatBuffer(16);
		setIdentity();
	}
	public void add(int col, int row, float val){
		set(row, col, get(col, row)+val);
	}
	public float get(int col, int row){
		return data.get(col*4+row);
	}
	public FloatBuffer read(){
		if(data.position()!=0)
			data.flip();
		return data;
	}
	public void scale(float x, float y, float z){
		set(0, 0, get(0, 0)*x);
		set(0, 1, get(0, 1)*x);
		set(0, 2, get(0, 2)*x);
		set(0, 3, get(0, 3)*x);
		set(1, 0, get(1, 0)*y);
		set(1, 1, get(1, 1)*y);
		set(1, 2, get(1, 2)*y);
		set(1, 3, get(1, 3)*y);
		set(2, 0, get(2, 0)*z);
		set(2, 1, get(2, 1)*z);
		set(2, 2, get(2, 2)*z);
		set(2, 3, get(2, 3)*z);
	}
	public void set(int col, int row, float val){
		data.put(col*4+row, val);
	}
	public void setIdentity(){
		data.put(0, 1.0f);
		data.put(1, 0.0f);
		data.put(2, 0.0f);
		data.put(3, 0.0f);
		data.put(4, 0.0f);
		data.put(5, 1.0f);
		data.put(6, 0.0f);
		data.put(7, 0.0f);
		data.put(8, 0.0f);
		data.put(9, 0.0f);
		data.put(10, 1.0f);
		data.put(11, 0.0f);
		data.put(12, 0.0f);
		data.put(13, 0.0f);
		data.put(14, 0.0f);
		data.put(15, 1.0f);
	}
	public void translate(float x, float y, float z){
		add(3, 0, get(0, 0)*x+get(1, 0)*y+get(2, 0)*z);
		add(3, 1, get(0, 1)*x+get(1, 1)*y+get(2, 1)*z);
		add(3, 2, get(0, 2)*x+get(1, 2)*y+get(2, 2)*z);
		add(3, 3, get(0, 3)*x+get(1, 3)*y+get(2, 3)*z);
	}
}
