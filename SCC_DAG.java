/*
* Compress a graph into a DAG
* Uses Tarjan's algorithm
*
*/
import java.io.*;
import java.util.*;

//Node in original graph
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

//Node in condensed graph containing a cycle of nodes
class SCC
{
	ArrayList<Integer> adjList;
	long sum;
	long maxSum;

	public SCC()
	{
		adjList = new ArrayList<Integer>();
		sum = 0;
		maxSum = 0;
	}
}

public class SCC_DAG
{
	static Node[] nodes;
	static Stack<Integer> dfsStack;
	static boolean[] active;
	static int currentdfn = 1;
	static int currentscc = 1;
	static SCC[] DAG;

	//start currentdfn > -1
	public static void tarjan(int v)
	{
		nodes[v].dfn = currentdfn;
		nodes[v].low = currentdfn;
		dfsStack.push(v);
		active[v] = true;
		for(int u : nodes[v].adjList)
		{
			if(nodes[u].dfn == -1)
			{
				currentdfn++;
				tarjan(u);
				nodes[v].low = Math.min(nodes[v].low, nodes[u].low);
			}
			else if(active[u])
				nodes[v].low = Math.min(nodes[v].low, nodes[u].dfn);
		}
		if(nodes[v].dfn == nodes[v].low)
		{
			int u = dfsStack.pop();
			while(u != v)
			{
				nodes[u].scc = currentscc;
				active[u] = false;
				u = dfsStack.pop();
			}
			nodes[v].scc = currentscc;
			active[u] = false;
			currentscc++;
		}
	}

	public static void main(String[] args) throws IOException
	{
		//Input Format:
		// First line: N nodes, M edges 
		// followed by M lines with an edge each
		// 
		// Ex.
		// 4 4
		// 1 2
		// 2 3
		// 3 2
		// 2 4
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int n = Integer.parseInt(st.nextToken());
		int m = Integer.parseInt(st.nextToken());
		nodes = new Node[n + 1];
		for(int i = 1; i <= n; i++)
			nodes[i] = new Node();
		for(int i = 0; i < m; i++)
		{
			st = new StringTokenizer(br.readLine());
			int p = Integer.parseInt(st.nextToken());
			int q = Integer.parseInt(st.nextToken());
			nodes[p].adjList.add(q);
		}
		dfsStack = new Stack<>();
		active = new boolean[n + 1];
		for(int i = 1; i <= n; i++)
		{
			if(nodes[i].scc == -1)
				tarjan(i);
		}
		DAG = new SCC[currentscc];
		for(int i = 1; i < currentscc; i++)
			DAG[i] = new SCC();
		for(int v = 1; v <= n; v++)
		{
			for(int u : nodes[v].adjList)
			{
				if(nodes[u].scc != nodes[v].scc)
					DAG[nodes[v].scc].adjList.add(nodes[u].scc);
			}
		}
	}
}
