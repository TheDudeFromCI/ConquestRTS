package wraithaven.conquest.client.AI.ChatSystem;

import java.util.ArrayList;

public class BrainDatabase{
	public final ArrayList<InfoBit> knownInformation = new ArrayList();
	public BrainDatabase(){
		load();
	}
	private void load(){
		//TODO
	}
	public void save(){
		//TODO
	}
	public void findReliventInformation(ArrayList<InfoBit> information, DatabaseSearch search){
		for(int i = 0; i<knownInformation.size(); i++)
			if(knownInformation.get(i).isRelated(search))information.add(knownInformation.get(i));
	}
	public void getMassVocabulary(ArrayList<String> list){
		for(InfoBit info : knownInformation)
			for(Tag tag : info.tags)
				if(!list.contains(tag.title))list.add(tag.title);
	}
}