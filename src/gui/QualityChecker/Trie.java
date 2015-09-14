package gui.QualityChecker;


import java.util.Set;
import java.util.TreeSet;

public class Trie implements ITrie
{
	Set<String> words;
	int wordcount;
	int nodecount;
	Node root;
	
	public Trie()
	{
		nodecount = 1;
		wordcount = 0;
		words = new TreeSet<String>();
		root = new Node();
	}
	
	public Set<String> getWords()
	{
		return words;
	}
	
	//@Override
	public void add(String word) 
	{
		words.add(word);
	}
	
	//@Override
	public INode find(String word) 
	{
		Node cur = root;
		for(int i = 0; i < word.length(); i++)
		{
			if(cur.children[word.toLowerCase().charAt(i) - 97] == null)
			{
				return cur;
			}
			cur = cur.children[word.toLowerCase().charAt(i) - 97];
		}
		
		if(cur.getValue() == 0)
		{
			return null;
		}
		
		else
			return cur;
	}

	//@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return wordcount;
	}

	//@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodecount;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(String s : words)
		{
			sb.append(s + "\n");
		}
		return sb.toString();
	}
	
	public String freqtoString()
	{
		StringBuilder sb = new StringBuilder();
		for(String s : words)
		{
			sb.append(find(s).getValue() + '\n');
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return wordcount + nodecount;
	}

	@Override
	public boolean equals(Object o)
	{
		return true;
	}
	
	public boolean isKnowndata(String s)
	{
		if(words.contains(s))
		{
			return true;
		}
		else
			return false;
	}
	
}

