package wraithaven.conquest.client.AI.ChatSystem;

import java.util.ArrayList;
import java.util.Comparator;

public class Brain{
	public static String[] breakSentenceIntoTags(String sentence){
		ArrayList<String> wordList = new ArrayList();
		StringBuilder newSentence = new StringBuilder();
		char[] characters = sentence.toLowerCase().toCharArray();
		for(char c : characters){
			if(Character.isAlphabetic(c))newSentence.append(c);
			else if(c==' '){
				if(newSentence.length()==0)continue;
				wordList.add(newSentence.toString());
				newSentence.replace(0, newSentence.length(), "");
			}
		}
		if(newSentence.length()>0)wordList.add(newSentence.toString());
		return wordList.toArray(new String[wordList.size()]);
	}
	public static void sortByMostRelated(ArrayList<InfoBit> information, DatabaseSearch search){
		for(InfoBit infoBit : information)
			infoBit.countTags(search);
		information.sort(new Comparator<InfoBit>(){
			public int compare(InfoBit a, InfoBit b){
				return a.searchRelivance==b.searchRelivance?0:(a.searchRelivance>b.searchRelivance?-1:1);
			}
		});
	}
	public static void clusterResultsAndFilter(ArrayList<InfoBit> information, int maxGap){
		int cutOff = information.size();
		for(int i = 1; i<information.size(); i++)
			if(information.get(i-1).searchRelivance-information.get(i).searchRelivance>maxGap){
				cutOff=i;
				break;
			}
		for(int i = cutOff; i<information.size();)
			information.remove(i);
	}
	public final BrainDatabase database;
	public Brain(){
		database = new BrainDatabase();
	}
}