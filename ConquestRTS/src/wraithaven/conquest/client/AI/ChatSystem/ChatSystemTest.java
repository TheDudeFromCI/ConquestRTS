package wraithaven.conquest.client.AI.ChatSystem;

import java.io.File;
import java.util.ArrayList;

public class ChatSystemTest{
	public static void main(String[] args){
		//Prepare brain.
		Brain brain = new Brain();
		for(File f : new File("C:/Users/Phealoon/Desktop/Brain Information").listFiles())
			brain.database.knownInformation.add(BrainIO.createInfoBitFromFile(f));
		
		
		
		//Define search properties.
		String sentence = "Do you like the color red?";
		DatabaseSearch search = new DatabaseSearch();
		search.tags = new String[]{
			"red",
			"opinion",
			"question",
			"yes_or_no_question",
			"color",
			"like",
		};
		search.priority = new float[]{
			1f,
			1f,
			1.5f,
			2f,
			1f,
			1f,
		};
		search.totalEmphasis=1f;
		
		
		
		//Console appeal.
		System.out.println("Sentence: \""+sentence+"\"");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Tag List:");
		System.out.println("=========");
		printList(search);
		System.out.println();
		System.out.println();
		System.out.println();
		
		//Find results
		ArrayList<InfoBit> bits = new ArrayList();
		brain.database.findReliventInformation(bits, search);
		Brain.sortByMostRelated(bits, search);
		System.out.println("Search Results:");
		System.out.println("===============");
		printList(bits);
		
		//Filter results.
		System.out.println();
		System.out.println();
		System.out.println();
		Brain.clusterResultsAndFilter(bits, 15);
		System.out.println("Filtered Results:");
		System.out.println("=================");
		printList(bits);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(NaturalSpeech.translateWordsToSentence(bits));
	}
	private static void printList(ArrayList<InfoBit> bits){
		for(int i = 0; i<bits.size(); i++)
			System.out.println("["+bits.get(i).searchRelivance+"] "+bits.get(i).getName());
	}
	private static void printList(DatabaseSearch search){
		for(int i = 0; i<search.tags.length; i++)
			System.out.println("["+search.priority[i]+"] "+search.tags[i]);
	}
}