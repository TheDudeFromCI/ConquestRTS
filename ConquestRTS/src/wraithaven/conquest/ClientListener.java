package wraithaven.conquest;

public interface ClientListener{
	public void connectedToServer();
	public void couldNotConnect();
	public void disconnected();
	public void recivedInput(String msg);
	public void serverClosed();
	public void unknownHost();
}