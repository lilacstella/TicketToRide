package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;

import core.Player;
import core.PlayerEvent;
import core.Ticket;

public class GraphicsBoard extends Graphics implements View {
//	private PlayerEventListener listener;
	private GraphicsGraph graph;
	private GraphicsPlayer player;
	private String[] visible;
	private BufferedImage background;
	private BufferedImage canvas;
	private BufferedImage ticket;
	private BufferedImage trainIcon;
	private BufferedImage ticketIcon;
	private BufferedImage pointIcon;
	private BufferedImage trainCardIcon;
	private BufferedImage crown;
	private int roundWeight;
	private boolean isDeckEmpty;

	private ViewEvent lastUpdate;
	private Float mouseLoc;
	private String color;
	private PlayerEvent lastGrayRail;

	// leader board
	private Color[] list;
	private int[] points;
	private int[] trains;
	private int[] tickets;
	private int[] trainCards;
	private Player first;

	private boolean end;

	private GraphicsTicketSelections sel;
	private GraphicsColorSelections col;

	public GraphicsBoard() throws IOException {
		graph = new GraphicsGraph();
		player = new GraphicsPlayer();
		list = new Color[4];
		points = new int[4];
		trains = new int[4];
		tickets = new int[4];
		trainCards = new int[4];
		visible = new String[6];
		visible[5] = "Back";
		end = false;
		isDeckEmpty = false;
		background = ImageIO.read(new File("game_files\\background.jpg"));
		canvas = ImageIO.read(new File("game_files\\canvas.jpg"));
		ticket = ImageIO.read(new File("game_files\\cards\\ticket_card_back.jpg"));
		trainIcon = ImageIO.read(new File("game_files\\Icons\\Train Icon.png"));
		ticketIcon = ImageIO.read(new File("game_files\\Icons\\Ticket Icon.png"));
		pointIcon = ImageIO.read(new File("game_files\\Icons\\CoinIcon.png"));
		trainCardIcon = ImageIO.read(new File("game_files\\Icons\\TrainCard Icon.png"));
		crown = ImageIO.read(new File("game_files\\Icons\\CrownIcon.png"));
	}

	public void draw(Graphics2D g) {
		g.drawImage(background, 0, 0, 1920, 1080, null);
		g.setColor(new Color(214, 116, 25));
		g.setStroke(new BasicStroke(15));
		g.drawImage(canvas, 0, 0, 1240, 774, null);
		g.drawRect(5, 5, 1240, 775);
		g.setColor(g.getColor().darker());
		g.setStroke(new BasicStroke(7));
		g.drawRect(10, 10, 1231, 766);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(3));
		graph.draw(g);

		g.setStroke(new BasicStroke(15));
		// 1300, 25
		if (!end) {
			g.setColor(new Color(0, 0, 0, 150));
			for (int i = 0; i < visible.length; i++) {
				g.drawImage(color2Image(visible[i]), 1255, 130 * i, 200, 125, null);
				if (visible[i].equals("Wild") && roundWeight > 0)
					g.fillRect(1255, 130 * i, 200, 125);
				else if (visible[i].equals(""))
					g.fillRect(1255, 130 * i, 200, 125);
			}
			if (isDeckEmpty)
				g.fillRect(1255, 130 * (visible.length - 1), 200, 125);
		}

		g.drawImage(ticket, 1500, 650, 200, 125, null);

		// 1500 - 1900, 130
		g.setFont(new Font("Seriff", Font.BOLD, 60));
		g.setColor(new Color(226, 165, 83));
		drawStationBoard(g);
		g.setColor(Color.LIGHT_GRAY);

		for (int i = 0; i < 4; i++) {
			if (lastUpdate.getCurrentPlayer().equals(list[i])) {
				g.setStroke(new BasicStroke(10));
				g.setColor(Color.BLACK);
				g.drawRect(1475, 90 + i * 100, 50, 50);
				if (!end) {
					g.setColor(list[i]);
					Font nameFont = new Font("Seriff", Font.BOLD, 30);
					g.setFont(nameFont);
					g.drawString(player.getName(), 1900 - g.getFontMetrics(nameFont).stringWidth(player.getName()),
							770);
				}
				g.drawRect(5, 793, 1904, 253);
				if (sel != null && !sel.getDraw()) {
					g.setStroke(new BasicStroke(5));
					g.setColor(g.getColor().darker().darker());
					g.drawRect(7, 795, 1899, 250);
					g.setStroke(new BasicStroke(10));
				}
			}
			g.setColor(list[i]);
			g.fillRect(1475, 90 + i * 100, 50, 50);
			g.setFont(new Font("Seriff", Font.BOLD, 48));
			g.drawString("" + points[i], 1550, 130 + i * 100);
			g.drawString("" + trains[i], 1650, 130 + i * 100);
			g.drawString("" + tickets[i], 1750, 130 + i * 100);
			g.drawString("" + trainCards[i], 1850, 130 + i * 100);
//			System.out.println("first " + first + " i " + i);
			if (first != null && first.getPoints() == points[i])
				g.drawImage(crown, 1475, 92 + i * 100, 50, 50, null);
		}

		if (!end)
			player.draw(g);

		g.setFont(new Font("Seriff", Font.BOLD, 16));
		g.setColor(Color.black);
		g.drawImage(pointIcon, 1540, 25, 50, 50, null);
		// g//.drawString("points", 1540, 50);
		g.drawImage(trainIcon, 1660, 25, 50, 50, null);
		// g.drawString("tickets", 1745, 50);
		g.drawImage(ticketIcon, 1745, 25, 50, 50, null);
		g.drawImage(trainCardIcon, 1825, 25, 75, 50, null);
		// g.drawString("trainCards", 1825, 50);

		if (lastUpdate.players.peek().getTickets().size() == 0) {
			sel = drawStartTickets();
			sel.setDraw(true);
		}

		if (sel.getDraw()) {
			sel.setLoc(mouseLoc);
			sel.draw(g);
		}

		g.setColor(Color.black);

		if (col != null && col.getDraw())
			col.draw(g);

		if (end)
			drawEndScreen(g);
	}

	public void drawStationBoard(Graphics2D g) {
		g.setStroke(new BasicStroke(5));
		g.setColor(new Color(244, 158, 66));
		g.fillRect(1458, 80, 452, 380);
		g.setColor(new Color(214, 116, 25).darker().darker());
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 3));
		g.drawLine(1458, 165, 1920, 165);
		g.drawLine(1458, 265, 1920, 265);
		g.drawLine(1458, 365, 1920, 365);
		g.fillRect(1456, 20, 457, 60);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5));
		g.drawRect(1458, 20, 452, 60);
		g.drawRect(1458, 20, 452, 440);
	}

	public void drawEndScreen(Graphics2D g) {
		ArrayList<Player> winners = new ArrayList<Player>();
		ArrayList<Player> path = new ArrayList<Player>();
		ArrayList<Player> ticket = new ArrayList<Player>();
		for (Player p : lastUpdate.players) {
			if (!winners.contains(p) && p.getWin(0))
				winners.add(p);
			if (!path.contains(p) && p.getWin(1))
				path.add(p);
			if (!ticket.contains(p) && p.getWin(2))
				ticket.add(p);
		}
		if (winners.size() == 1) {
			Player winner = winners.get(0);
			Color c;
			if (winner.getName().equals("Smashboy"))
				c = Color.YELLOW;
			else if (winner.getName().equals("Teewee"))
				c = new Color(142, 68, 173);
			else if (winner.getName().equals("Rhail Island Z"))
				c = Color.GREEN;
			else if (winner.getName().equals("Cleveland Z"))
				c = Color.RED;
			else
				c = Color.WHITE;

			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
			g.setColor(c);
			g.fillRect(5, 793, 1904, 253);
		} else {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(5, 793, 1904, 253);
		}

		String winPut = "";
		//lastUpdate.getSortedPlayer().toString().replaceAll("\\[|\\]", "");
		ArrayList<Player> sorted = lastUpdate.getSortedPlayer();
		for(int i = 0; i < sorted.size(); i++)
			winPut+= (i+1) + ". " + sorted.get(i).getName() + " = " + sorted.get(i).getPoints() + " ";
		String pathPut = path.toString().replaceAll("\\[|\\]", "");
		String tickPut = ticket.toString().replaceAll("\\[|\\]", "");
		//int pointnum = winners.get(0).getPoints();
		int disnum = path.get(0).getDis();
		int compNum = ticket.get(0).getComp();

		g.setColor(Color.BLACK);
		g.setFont(new Font("Seriff", Font.BOLD, 36));
		g.drawString("Winner: " + winPut, 15, 830);
		g.drawString("Longest Path: " + pathPut + " , " + disnum, 15, 890);
		g.drawString("Most Tickets: " + tickPut + " , " + compNum, 15, 950);
	}

	@Override
	public void observe(ViewEvent event) {
		lastUpdate = event;
		update(event);
	}

	@Override
	public String color() {
		String temp = color;
		color = "";
		col = null;
		return temp;
	}

	public boolean ended() {
		return end;
	}

	@Override
	public void update(Object e) {
		ViewEvent update = (ViewEvent) e;
		roundWeight = update.roundWeight;
		isDeckEmpty = update.gameDeck.getDeck().isEmpty();
		// System.out.println(isDeckEmpty);
		if (update.getID() == 1)
			end = true;
		graph.update(update.map);
		player.update(update.players.peek());
//		visible = update.visible;
		for (int i = 0; i < update.visible.length; i++) {
			visible[i] = update.visible[i];
		}
		visible[5] = "Back";

		for (int i = 0; i < update.players.size(); i++) {
			Player temp = update.players.peek();
			update.players.offer(update.players.poll());
//			System.out.println(update.getSortedPlayer().size());
//			System.out.println(temp.getName());

			switch (temp.getName()) {
			case ("Smashboy"):
				list[i] = (Color.yellow);
				break;
			case ("Rhail Island Z"):
				list[i] = (Color.green);
				break;
			case ("Teewee"):
				list[i] = (new Color(142, 68, 173));
				break;
			case ("Cleveland Z"):
				list[i] = (Color.red);
				break;
			default:
				list[i] = Color.black;
			}
			points[i] = temp.getPoints();
			trains[i] = temp.getTrains();
			tickets[i] = temp.getTickets().size();
			trainCards[i] = temp.getTrainCardsNum();

			Player firstP = update.getFirstPlayer();
			first = firstP;
//			System.out.println(temp.getPoints() + " " + first);
		}
	}

	public boolean getDraw() {
		return sel != null && sel.getDraw();
	}

	public PlayerEvent contains(Float cord) {
		if (end)
			return null;
		if (sel.getDraw())
			return sel.contains(cord);
		if (col != null && col.getDraw()) {
			col.contains(cord);
			color = col.getColor();
			return lastGrayRail;
		}
//initial ticket
			if (cord.x >= 1500 && cord.x <= 1700 && cord.y >= 650 && cord.y <= 775) {
//			System.out.println("Tickets selection");
				@SuppressWarnings("unchecked")
				Stack<Ticket> temp1 = (Stack<Ticket>) lastUpdate.tickets.clone();
				ArrayList<Ticket> temp2 = new ArrayList<Ticket>();
				if (temp1.size() < 3)
					return null;
				for (int i = 0; i < 3; i++)
					temp2.add(temp1.pop());
				if (roundWeight == 1)
					return null;
				sel = new GraphicsTicketSelections(temp2, 3);
				sel.contains(cord);
			}

		player.contains(cord);

		// if selected then no
		if (!sel.getDraw() && sel.getSelected())
			return null;

		if (cord.x >= 1255 && cord.x <= 1455) {
			if (cord.y >= 0 && cord.y <= 125)
				return new PlayerEvent(PlayerEvent.PLAYER_DRAW_ONE);
			if (cord.y >= 130 && cord.y <= 255)
				return new PlayerEvent(PlayerEvent.PLAYER_DRAW_TWO);
			if (cord.y >= 260 && cord.y <= 385)
				return new PlayerEvent(PlayerEvent.PLAYER_DRAW_THREE);
			if (cord.y >= 390 && cord.y <= 515)
				return new PlayerEvent(PlayerEvent.PLAYER_DRAW_FOUR);
			if (cord.y >= 520 && cord.y <= 645)
				return new PlayerEvent(PlayerEvent.PLAYER_DRAW_FIVE);
			if (cord.y >= 650 && cord.y <= 775)
				return new PlayerEvent(PlayerEvent.PLAYER_DRAW_DECK);
		}

		PlayerEvent pE = graph.contains(cord);
		// System.out.println(pE);

		if (pE != null)
			if (GraphicsGraph.lastRail().getOwners()[pE.getID() % 10 - 8] != null)
				return pE;
			else if (lastUpdate.map.getRail((pE.getID() - 8) / 10).getColor().contains("Gray")) {
				ArrayList<String> colors = lastUpdate.getSuffColors(lastUpdate.map.getRail((pE.getID() - 8) / 10));
				if (colors.size() == 0)
					return null;
				col = new GraphicsColorSelections(colors);
				lastGrayRail = pE;
			} else
				return pE;

		return null;
	}

	public GraphicsTicketSelections drawStartTickets() {
		@SuppressWarnings("unchecked")
		Stack<Ticket> temp1 = (Stack<Ticket>) lastUpdate.tickets.clone();
		ArrayList<Ticket> temp2 = new ArrayList<Ticket>();
		for (int i = 0; i < 5; i++)
			temp2.add(temp1.pop());
		GraphicsTicketSelections select = new GraphicsTicketSelections(temp2, 5);
		select.setLoc(mouseLoc);
		return select;
	}

	public void graphSetRails() {
		graph.setRails(mouseLoc);
	}

	public void ticketCheck() {
		if (sel != null)
			sel.setLoc(mouseLoc);
	}

	public void setLoc(Float loc) {
		if (sel == null || !sel.getDraw())
			playerCheck(loc);
		mouseLoc = loc;
	}

	private void playerCheck(Float loc) {
		player.hovers(loc);
	}

	public boolean containsPoint(Float point) {
		if (sel.getDraw())
			return false;
		return (graph.contains(point) != null);
	}

	@Override
	public void drawPath(List<Integer> path) {
		graph.setLongestPath(path);
	}
}