/*
* Find Cut/Articulation Points in a graph
*
*/
import java.io.*;
import java.util.*;

class Node
{
	ArrayList<Integer> adjList;
	int dfn;
	int low;
	int scc;

	public Node()
	{
		adjList = new ArrayList<Integer>();
		dfn = -1;
		low = -1;
		scc = -1;
	}
}

public class CutPoints
{
	static Node[] nodes;
	static boolean[] active;
	static int currentdfn = 1;
	static int numCutPoints = 0;
	static boolean[] isCut;

	//start currentdfn > -1
	public static void findCutPoints(int v, int prev)
	{
		nodes[v].dfn = currentdfn;
		nodes[v].low = currentdfn;
		int subtrees = 0;
		for(int u : nodes[v].adjList)
		{
			if(nodes[u].dfn == -1)
			{
				subtrees++;
				currentdfn++;
				findCutPoints(u, v);
				if(prev != -1 && nodes[v].dfn <= nodes[u].low)
				{
					numCutPoints++;
					isCut[v] = true;
				}
				nodes[v].low = Math.min(nodes[v].low, nodes[u].low);
			}
			else if(prev != u)
				nodes[v].low = Math.min(nodes[v].low, nodes[u].dfn);
		}
		if(prev == -1 && subtrees > 1)
		{
			numCutPoints++;
			isCut[v] = true;
		}
	}

	public static void main(String[] args) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int n = Integer.parseInt(st.nextToken());
		int m = Integer.parseInt(st.nextToken());
		nodes = new Node[n + 1];
		for(int i = 0; i <= n; i++)
			nodes[i] = new Node();
		for(int i = 0; i < m; i++)
		{
			st = new StringTokenizer(br.readLine());
			int p = Integer.parseInt(st.nextToken());
			int q = Integer.parseInt(st.nextToken());
			nodes[p].adjList.add(q);
			nodes[q].adjList.add(p);
		}
		isCut = new boolean[n + 1];
		for(int i = 0; i <= n; i++)
		{
			if(nodes[i].dfn == -1)
				findCutPoints(i, -1);
		}
		PrintWriter pw = new PrintWriter(System.out);
		pw.println("Number of Cut Points: "  + numCutPoints);
		for(int i = 0; i <= n; i++)
		{
			if(isCut[i])
				pw.println(i);
		}
		pw.close();
	}
}
