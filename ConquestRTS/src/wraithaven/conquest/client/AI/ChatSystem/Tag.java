package wraithaven.conquest.client.AI.ChatSystem;

public class Tag{
	public String title;
	public byte importance;
	public Tag(String title, byte importance){
		this.title = title;
		this.importance = importance;
	}
	@Override
	public String toString(){
		return title+" ("+importance+")";
	}
	public Tag(){}
}