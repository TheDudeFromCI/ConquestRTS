package wraithaven.conquest.client.AI.ChatSystem;

import java.util.ArrayList;

public class NaturalSpeech{
	public static String translateWordsToSentence(ArrayList<InfoBit> information){
		ArrayList<Tag> tags = new ArrayList();
		findImportantKeywords(tags, information);
		return buildSentence(tags);
	}
	private static void findImportantKeywords(ArrayList<Tag> tags, ArrayList<InfoBit> information){
		
	}
	private static String buildSentence(ArrayList<Tag> tags){
		return "";
	}
}