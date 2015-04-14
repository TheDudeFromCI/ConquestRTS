package wraithaven.conquest.client;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameThread{
	private final ArrayList<GameThreadTask> tasks = new ArrayList();
	public GameThread(){
		new Timer().scheduleAtFixedRate(new TimerTask(){
			private int index;
			public void run(){
				for(index=0; index<tasks.size();){
					try{ if(!tasks.get(index).update())index++;
					}catch(Exception exception){
						exception.printStackTrace();
						System.exit(1);
					}
				}
			}
		}, 50, 50);
	}
	public void addGameThreadTask(GameThreadTask task){ tasks.add(task); }
}