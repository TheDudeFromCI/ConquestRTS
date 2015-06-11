package wraithaven.conquest.client.AI.ChatSystem;

import java.util.ArrayList;

public class InfoBit{
	public float searchRelivance;
	public final ArrayList<Tag> tags = new ArrayList();
	public boolean isRelated(DatabaseSearch search){
		for(String tag : search.tags){
			for(int i = 0; i<tags.size(); i++)
				if(tags.get(i).title.equals(tag))return true;
		}
		return false;
	}
	public void countTags(DatabaseSearch search){
		searchRelivance = 0;
		int i, j;
		tagList:for(i = 0; i<search.tags.length; i++){
			for(j = 0; j<tags.size(); j++)
				if(tags.get(j).title.equals(search.tags[i])){
					searchRelivance += tags.get(j).importance*search.priority[i]*search.totalEmphasis;
					continue tagList;
				}
		}
	}
	public void addTags(ArrayList<Tag> information){
		for(int i = 0; i<tags.size(); i++)
			if(!information.contains(tags.get(i)))information.add(tags.get(i));
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Tag:[");
		for(Tag t : tags){
			if(t!=tags.get(0))sb.append(", ");
			sb.append(t);
		}
		sb.append(']');
		return sb.toString();
	}
	public String getName(){
		return tags.get(0).title;
	}
}