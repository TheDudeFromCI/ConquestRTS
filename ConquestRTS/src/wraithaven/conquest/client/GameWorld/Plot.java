package wraithaven.conquest.client.GameWorld;

public interface Plot<T>{
	public void end();
	public T get();
	public boolean next();
	public void reset();
}