package wraithaven.conquest.client.GameWorld.WindowUtil;

public interface GameThreadTask{
	public int getPriority();
	public boolean update();
}