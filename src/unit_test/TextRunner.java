package unit_test;

import java.io.FileNotFoundException;
import java.util.Scanner;

import core.Player;
import core.TicketToRide;

public class TextRunner
{
	public static void main(String[] args) throws FileNotFoundException
	{
		TicketToRide game = new TicketToRide();
		// Deck decks = game.getDeck();
		Scanner sc = new Scanner(System.in);
		printPlayerHand(game.getCurrentPlayer());
		// gameEvent(game, decks, sc);
//		playerEvent(game, sc);
		sc.close();
	}

	private static void printPlayerHand(Player pl)
	{
		System.out.println("Cards: " + pl.getHand().toString() + "\nPoints: "
				+ pl.getPoints() /*
									 * "\nTickets: " + pl.getTickets().toString() + "\nCities: " + pl.getCities()
									 */);
	}
	/*
	 * private static void playerEvent(TicketToRide game, Deck decks, Scanner sc)
	 * throws FileNotFoundException { do { Player pl = game.getCurrentPlayer();
	 * 
	 * System.out.println("Current Player: " + pl.getName()); printPlayerHand(pl);
	 * System.out.println("Tickets: " + pl.getTickets().toString());
	 * System.out.println("5 cards: " + Arrays.toString(game.getVisCards()));
	 * System.out.println("Deck: " + decks.getDeck().size() + " " +
	 * Arrays.toString((decks.getDeck().toArray()))); System.out.println("Discard: "
	 * + decks.getDiscard()); System.out.print("PlayerEvent: ");
	 * 
	 * int choice = sc.nextInt();
	 * 
	 * if (choice == -2) break;
	 * 
	 * PlayerEvent plEv = new PlayerEvent(choice); game.onPlayerEvent(plEv);
	 * 
	 * printPlayerHand(pl); System.out.println(plEv.getWeight() + "\n"); } while
	 * (true); }
	 * 
	 * private static void gameEvent(TicketToRide game, Deck decks, Scanner sc)
	 * throws FileNotFoundException { Player pl = game.getCurrentPlayer();
	 * System.out.print("GameEvent: ");
	 * 
	 * int choice = sc.nextInt();
	 * 
	 * GameEvent gaEv = new GameEvent(choice, pl); game.onGameEvent(gaEv);
	 * 
	 * // printPlayerHand(pl); // System.out.println(plEv.getWeight() + "\n"); }
	 */
}
