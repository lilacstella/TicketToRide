package graphics;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GraphicsGraph
{
	private GraphicsCity[] cities;
	private GraphicsRail[] rails;
	private static BufferedImage map;
	
	static
	{
		try
		{
			map = ImageIO.read(new File("game_files\\cards\\ttr_board.jpg"));
		}
		catch(IOException e)
		{
		
		}
	}
	
	public GraphicsGraph() throws FileNotFoundException
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(new File("game_files\\cities\\map.ttr"));
		cities = new GraphicsCity[in.nextInt()];
		for(int i = 0; i < cities.length; i++)
			cities[in.nextInt()] = new GraphicsCity(in.next().replaceAll("_", " "), new Point2D.Float(in.nextInt(),in.nextInt()));
		rails = new GraphicsRail[in.nextInt()];
		for(int i = 0; i < rails.length; i++)
		{
			rails[in.nextInt()-1] = new GraphicsRail(in.nextInt(),in.nextInt(),in.nextInt(), in.nextBoolean(), in.nextDouble());
			in.nextLine();

			for(int j = 0 + ((rails[i].getDoubles()) ? 0:1); j < 2; j++)
			{
				rails[i].setColor(in.next());
				rails[i].setCords(in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(),in.nextDouble());
			}
//			System.out.println(rails[i]);
			
		}
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(map, 0, 0, 1200,805, null);
		g.setStroke(new BasicStroke(3));
		for(int i = 0; i < cities.length; i++)
			cities[i].draw(g);
		for(int i = 0; i < rails.length; i++)
			rails[i].draw(g);
	}
}
