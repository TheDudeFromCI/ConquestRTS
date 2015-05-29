package wraithaven.conquest.client.GameWorld.WindowUtil;

public abstract class RepeatingTask implements GameThreadTask{
	private int between;
	private int passed;
	private boolean running = true;
	private int steps;
	private int tickDelay;
	public RepeatingTask(GameThread gameThread, int delay, int between){
		tickDelay = delay;
		this.between = between;
		gameThread.addGameThreadTask(this);
	}
	public abstract void run();
	public void stop(){
		running = false;
	}
	public boolean update(){
		if(!running) return true;
		passed = steps-tickDelay;
		if(passed>0&&passed%between==0) run();
		steps++;
		return false;
	}
}