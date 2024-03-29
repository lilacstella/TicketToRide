package core;

import java.awt.AWTEvent;

public class PlayerEvent extends AWTEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3817585814836156911L;
	public static final int FORCE_NEXT_ROUND = -1;
	public static final int PLAYER_DRAW_ONE = 0;
	public static final int PLAYER_DRAW_TWO = 1;
	public static final int PLAYER_DRAW_THREE = 2;
	public static final int PLAYER_DRAW_FOUR = 3;
	public static final int PLAYER_DRAW_FIVE = 4;
	public static final int PLAYER_DRAW_DECK = 5;
//	public static final int PLAYER_PLACE_TRACK = 6;
	public static final int PLAYER_DRAW_TICKET = 6;
	public static final int PLAYER_DISCARD_TICKET = 7;

	private static int eventWeight;

	public PlayerEvent(int id)
	{
		super(new Object(), id);
		setWeight(id);
	}

	public int getWeight()
	{
		return eventWeight;
	}

	public void setWeight(int id)
	{
		if (id >= 0 && id < 6)
			eventWeight = 1;
		else if (id == 6 || id == 7 || id == -1)
			eventWeight = 0;
		else
			eventWeight = 2;
	}

	public PlayerEvent reEvent()
	{
		PlayerEvent clone = new PlayerEvent(this.getID());
		clone.setWeight(-2);
		this.consume();
		return clone;
	}
}
