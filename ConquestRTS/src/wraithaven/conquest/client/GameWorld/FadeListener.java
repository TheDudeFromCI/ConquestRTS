package wraithaven.conquest.client.GameWorld;

public interface FadeListener{
	public void onFadeOutTick();
	public void onFadeOutComplete();
	public void onFadeInTick();
	public void onFadeInComplete();
	public void onFadeStayTick();
	public void onFadeStayComplete();
	public void onComplete();
}