package com.wraithavens.conquest.Launcher;

public interface Driver{
	public void update(double delta, double time);
	public void render();
	public void onKey(int key, int action);
	public void onMouse(int button, int action);
	public void onMouseMove(double x, double y);
	public void onMouseWheel(double x, double y);
	public void initalize(double time);
	public void dispose();
}