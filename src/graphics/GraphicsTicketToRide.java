package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.PlayerEvent;
import core.TicketToRide;
import graphics.GraphicsBoard;

@SuppressWarnings("serial")
public class GraphicsTicketToRide extends JPanel implements MouseListener
{
	private static JFrame window;
	private GraphicsBoard board;
	private TicketToRide game;

	public static void main(String[] args) throws FileNotFoundException
	{
		GraphicsTicketToRide test = new GraphicsTicketToRide();
		window = new JFrame("Ticket to Ride");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1920, 1080);
		window.setVisible(true);
		window.add(test);
		window.addMouseListener(test);
		window.setResizable(false);
		window.getContentPane().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
		while (true)
			test.repaint();

	}

	public GraphicsTicketToRide() throws FileNotFoundException
	{
		board = new GraphicsBoard();
		game = new TicketToRide();
		game.setView(board);
	}
	
	@Override
	protected void paintComponent(Graphics fakeG)
	{
		super.paintComponent(fakeG);
		Graphics2D g = (Graphics2D) fakeG;

		board.draw(g);
		double x = MouseInfo.getPointerInfo().getLocation().getX() - window.getLocationOnScreen().x;
		double y = MouseInfo.getPointerInfo().getLocation().getY() - window.getLocationOnScreen().y;
		
		g.setStroke(new BasicStroke(4));
		g.setColor(new Color(129, 9, 255));
		g.draw(new Line2D.Double(x - 5, y, x + 5, y));
		g.draw(new Line2D.Double(x, y - 5, x, y + 5));
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.BLACK);
		
		Point2D.Float point = new Point2D.Float();
		
		point.setLocation(x, y);
		if(board.containsPoint(point)){
			//System.out.println(board.contains(point));
			board.graphSetRails(point);
			//System.out.println(point);
			//repaint();
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
//		System.out.println(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		PlayerEvent action = board.contains(new Point2D.Float(arg0.getX(),arg0.getY()));
		if(action!=null)
			game.onPlayerEvent(action);
	}
}
