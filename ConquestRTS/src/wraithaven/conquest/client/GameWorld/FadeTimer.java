package wraithaven.conquest.client.GameWorld;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FadeTimer{
	private float fade;
	private int fadeIn,
			stay,
			fadeOut,
			pingDelay;
	private int fadeInStart,
			fadeOutStart;
	private ArrayList<FadeListener> fadeListeners = new ArrayList();
	private boolean started;
	private Timer timer;
	public FadeTimer(int fadeIn, int stay, int fadeOut, int pingDelay){
		if(fadeIn<0||stay<0||fadeOut<0) throw new IllegalArgumentException("Timings cannot be negative!");
		if(pingDelay<0) throw new IllegalArgumentException("Delay cannot be negative!");
		this.fadeIn = fadeInStart = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOutStart = fadeOut;
		this.pingDelay = pingDelay;
		timer = new Timer();
	}
	public void addListener(FadeListener listener){
		fadeListeners.add(listener);
	}
	private void fadeInTick(){
		fadeIn--;
		if(fadeIn==0){
			fade = 1;
			for(int i = 0; i<fadeListeners.size(); i++)
				fadeListeners.get(i).onFadeInComplete();
		}else{
			fade = 1-fadeIn/(float)fadeInStart;
			for(int i = 0; i<fadeListeners.size(); i++)
				fadeListeners.get(i).onFadeInTick();
		}
	}
	private void fadeOutTick(){
		fadeOut--;
		if(fadeOut==0){
			fade = 0;
			for(int i = 0; i<fadeListeners.size(); i++)
				fadeListeners.get(i).onFadeOutComplete();
		}else{
			fade = fadeOut/(float)fadeOutStart;
			for(int i = 0; i<fadeListeners.size(); i++)
				fadeListeners.get(i).onFadeOutTick();
		}
	}
	public float getFadeLevel(){
		return fade;
	}
	public long getPingDelay(){
		return pingDelay;
	}
	public int getRemainingFadeInTicks(){
		return fadeIn;
	}
	public int getRemainingFadeOutTicks(){
		return fadeOut;
	}
	public int getRemainingFadeStayTicks(){
		return stay;
	}
	public int getRemainingTicks(){
		return fadeIn+stay+fadeOut;
	}
	public boolean isDone(){
		return fadeIn==0&&stay==0&&fadeOut==0;
	}
	public boolean isFadeStaying(){
		return fadeIn==0&&stay>0;
	}
	public boolean isFadingIn(){
		return fadeIn>0;
	}
	public boolean isFadingOut(){
		return fadeIn==0&&stay==0&&fadeOut>0;
	}
	public boolean isStarted(){
		return started;
	}
	public void start(){
		if(started) return;
		started = true;
		if(isDone()){
			for(int i = 0; i<fadeListeners.size(); i++)
				fadeListeners.get(i).onComplete();
			return;
		}
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override public void run(){
				if(isFadingIn()) fadeInTick();
				else if(isFadeStaying()) stayTick();
				else fadeOutTick();
				if(isDone()){
					for(int i = 0; i<fadeListeners.size(); i++)
						fadeListeners.get(i).onComplete();
					cancel();
				}
			}
		}, pingDelay, pingDelay);
	}
	private void stayTick(){
		stay--;
		fade = 1;
		if(stay==0) for(int i = 0; i<fadeListeners.size(); i++)
			fadeListeners.get(i).onFadeStayComplete();
		else for(int i = 0; i<fadeListeners.size(); i++)
			fadeListeners.get(i).onFadeStayTick();
	}
}