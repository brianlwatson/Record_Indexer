package gui.QualityChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import client.communication.ClientCommunicator;
import gui.QualityChecker.ISpellCorrector;
import gui.QualityChecker.Trie;
import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;

public class SpellCorrector implements ISpellCorrector
{
	Trie trie;
	Set<String> validknowndata;
	
	public SpellCorrector()
	{
		trie = new Trie();
	}
	
	public Trie getTrie()
	{
		return trie;
	}
	
	Set<String> transpose(String word)
	{
		Set<String> transposed = new TreeSet<String>();
		for(int i = 0; i < word.length()-1; i++)
		{
			StringBuilder sb = new StringBuilder(word);
			char letter = sb.charAt(i);
			sb.deleteCharAt(i);
			sb.insert(i+1, letter);
			transposed.add(sb.toString());
		}
		return transposed;
	}
	
	Set<String> delete(String word)
	{
		Set<String> deleted = new TreeSet<String>();
		for(int i = 0; i < word.length(); i++)
		{
			StringBuilder sb = new StringBuilder(word);
			sb.deleteCharAt(i);
			deleted.add(sb.toString());
		}
		return deleted;
	}
	
	Set<String> insert(String word)
	{
		Set<String> inserted = new TreeSet<String>();
		for(int i = 0; i < word.length()+1; i++)
		{
			for(char c = 'a'; c <= 'z'; c++)
			{
				StringBuilder sb = new StringBuilder(word);
				sb.insert(i, c);
				inserted.add(sb.toString());
			}
			
			StringBuilder sb = new StringBuilder(word);
			sb.insert(i, ' ');
			inserted.add(sb.toString());
			
			sb = new StringBuilder(word);
			sb.insert(i, '.');
			inserted.add(sb.toString());
		}
		return inserted;
	}
	
	Set<String> alter(String word)
	{
		Set<String> altered = new TreeSet<String>();
		for(int i = 0; i < word.length(); i++)
		{
			for(char c = 'a'; c<='z'; c++)
			{
				StringBuilder sb = new StringBuilder(word);
				sb.setCharAt(i,c);
				altered.add(sb.toString());
			}
			
			StringBuilder sb = new StringBuilder(word);
			sb.setCharAt(i, ' ');
			altered.add(sb.toString());
			
			sb = new StringBuilder(word);
			sb.setCharAt(i, '.');
			altered.add(sb.toString());
		}
		return altered;
	}
	
	Set<String> makechanges(String word)
	{
		Set<String> changed = new TreeSet<String>();
		changed.addAll(alter(word));
		changed.addAll(delete(word));
		changed.addAll(insert(word));
		changed.addAll(transpose(word));
		return changed;
	}

	/**
	 * Use this to load things into the trie
	 */
	public void useDictionary(String dictionaryFileName) throws IOException 
	{
		
			URL path = new URL(dictionaryFileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(path.openStream()));
			
				String parsedword = in.readLine();
			//	String replacedword = parsedword.replace(",", " ");
				String[] listwords = parsedword.split(",");
				for(String s : listwords)
				{
					trie.add(s.toLowerCase());
				}
				in.close();
	}

	public Set<String> fix(Set<String> words)
	{
		Set<String> fixed = new TreeSet<String>();
		for( String s : words)
		{
			if(trie.words.contains(s))
			{
				fixed.add(s);
			}
		}
		return fixed;
	}
	
	
	/**
	 * This will make all of the iterations of edit distance one and edit distance two for a given word
	 * This will return all of the valid names that come from knowndata .txt files
	 */
	public Set<String> suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException 
	{
		Set<String> editone = new TreeSet<String>();
		editone = makechanges(inputWord.toLowerCase());
		Set<String> editedone = new TreeSet<String>();
		editedone = fix(editone);
		validknowndata = new TreeSet<String>();
		
		if(editedone.size() > 0)
		{
			for(String s : editedone)
			{
				if(trie.words.contains(s))
				{
					validknowndata.add(s);
				}
			}
		}
		
		Set<String> editedtwo = new TreeSet<String>();
		
		for(String s : editone)
		{
			editedtwo.addAll(makechanges(s));
		}
		Set<String> editedthree = new TreeSet<String>();
		editedthree = fix(editedtwo);
		
		if(editedthree.size() > 0)
		{
			for(String s : editedthree)
			{
				if(trie.words.contains(s))
				{
					validknowndata.add(s);
				}
			}
		}
		
		return validknowndata;
	}
	
	/**
	 * isKnowndata() checks if the word is found in the Trie
	 */
	public boolean isKnowndata(String s)
	{
		return trie.isKnowndata(s);
	}
}