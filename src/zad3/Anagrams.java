/**
 *
 *  @author Bielecki Michał S20136
 *
 */

package zad3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Anagrams {
	private List<String> listWords;
	private List<List> listSorted;
	
	public Anagrams(String words)
	{
		listWords = new ArrayList<String>();
		File file = new File(words); 
		BufferedReader bufferedReader;
		String lineTmp;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(file));
			
			while ((lineTmp = bufferedReader.readLine()) != null) {
				String[] arrWords = lineTmp.split(" ");
				
				for (int i = 0; i < arrWords.length; i++) {
					listWords.add(arrWords[i]);
				}
			}
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	public List<List> getSortedByAnQty()
	{
		listSorted = new ArrayList<List>();
		List<String> listTmp = new ArrayList<String>();
		
		for (int counter = 0; counter < listWords.size(); counter++) {
			if (!listTmp.contains(listWords.get(counter))) {
				List<String> tmp = new ArrayList<String>();

				for (int innerCounter = 0; innerCounter < listWords.size(); innerCounter++) {
					if (isAnagram(listWords.get(counter), listWords.get(innerCounter))) {
						listTmp.add(listWords.get(innerCounter));
						tmp.add(listWords.get(innerCounter));
					}
				}
				
			listSorted.add(tmp);
			}
		}

		listSorted.sort((first, second) -> first.size() - second.size());
		return listSorted;
	}
	public String getAnagramsFor(String word)
	{
		for (int counter = 0; counter < listSorted.size(); counter++) {
			List<String> tmp = new ArrayList<String>(listSorted.get(counter));
			
			for (int innerCounter = 0; innerCounter < tmp.size(); innerCounter++) {
				if(tmp.get(innerCounter).equals(word)) {
					tmp.remove(innerCounter);
					return word+": " + tmp;
				}
			}
		}
		
		return "";
	}
	public boolean isAnagram(String firstWord, String secondWord) {
	     char[] cFirstWord = firstWord.replaceAll("[\\s]", "").toCharArray();
	     char[] cSecondWord = secondWord.replaceAll("[\\s]", "").toCharArray();
	     
	     Arrays.sort(cFirstWord);
	     Arrays.sort(cSecondWord);
	     
	     return Arrays.equals(cFirstWord, cSecondWord);
	}
}
