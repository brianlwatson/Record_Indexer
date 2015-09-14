package gui.QualityChecker;

public class Node implements ITrie.INode
{
	int frequency;
	Node[] children;
	
	public Node()
	{
		frequency = 0;
		children = new Node[26];
	}
	
	
	public void augfrequency()
	{
		frequency++;
	}
	
	
	//@Override
	public int getValue() {
		return frequency;
	}

}
