package wraithaven.conquest.client.GameWorld;

public interface FadeListener{
	public void onComplete();
	public void onFadeInComplete();
	public void onFadeInTick();
	public void onFadeOutComplete();
	public void onFadeOutTick();
	public void onFadeStayComplete();
	public void onFadeStayTick();
}