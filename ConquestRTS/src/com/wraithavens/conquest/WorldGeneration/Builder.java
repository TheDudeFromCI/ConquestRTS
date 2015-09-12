package com.wraithavens.conquest.WorldGeneration;

class Builder implements Runnable{
	private final World world;
	private boolean running;
	Builder(World world){
		this.world = world;
	}
	public void run(){
		while(running){
			world.update();
			try{
				Thread.sleep(1);
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
	void killThread(){
		running = false;
	}
	void newThread(){
		Thread t = new Thread(this);
		t.setName("World Builder");
		t.setDaemon(true);
		t.start();
	}
}
