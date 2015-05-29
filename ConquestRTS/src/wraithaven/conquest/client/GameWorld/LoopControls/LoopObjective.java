package wraithaven.conquest.client.GameWorld.LoopControls;

public interface LoopObjective{
	public void key(long window, int key, int action);
	public void mouse(long window, int button, int action);
	public void mouseMove(long window, double xpos, double ypos);
	public void mouseWheel(long window, double xPos, double yPos);
	public void preLoop();
	public void render();
	public void update(double delta, double time);
}