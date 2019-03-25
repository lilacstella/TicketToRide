package unit_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import core.graph.Graph;
import core.graph.Rail;

public class GraphRunner
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File("game_files\\cities\\testGraphs.in"));

		while (sc.hasNextLine())
		{
			int num = sc.nextInt();
			sc.nextLine();
			Graph simpleGraph = new Graph();
			for (int i = 0; i < num; i++)
			{
				String[] input = sc.nextLine().split(" ");
				addRoute(input, simpleGraph);
			}
			printLongest(simpleGraph);
		}
	}

	private static void addRoute(String str[], Graph tempGraph)
	{
		tempGraph.add(str[0], new Rail(str[0], str[1], Integer.parseInt(str[2]), Boolean.parseBoolean(str[3]), str[4]));
	}
	
	private static void printLongest(Graph tempGraph)
	{
		System.out.println("longest Path" + tempGraph.LongestPath());
	}
}
