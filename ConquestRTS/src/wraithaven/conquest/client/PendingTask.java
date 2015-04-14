package wraithaven.conquest.client;

public abstract class PendingTask implements GameThreadTask{
	private int tickDelay;
	public PendingTask(int delay){
		tickDelay=delay;
		ClientLauncher.gameThread.addGameThreadTask(this);
	}
	public boolean update(){
		if(tickDelay==0){
			run();
			return true;
		}
		tickDelay--;
		return false;
	}
	public int getRemainingTicks(){ return tickDelay; }
	public abstract void run();
}