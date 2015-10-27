package co.gm4.uhc.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * A team consists out of multiple players and a team colour.
 * <p>
 * A team must be spread together and friendly fire should be disabled.
 * 
 * @author MrSugarCaney
 */
public class Team {

	/**
	 * Team count making sure all teams have a different ID.
	 */
	private static int ID_COUNT = 1;

	/**
	 * The team ID.
	 */
	private int id;

	/**
	 * All the members of the team.
	 */
	private List<Player> players = new ArrayList<>();

	/**
	 * The display colour a team has.
	 * <p>
	 * Default: {@link ChatColor#WHITE}
	 */
	private ChatColor colour = ChatColor.WHITE;

	/**
	 * The ChatColor accent the team has stored as a String.
	 * <p>
	 * E.g. {@link ChatColor#ITALIC}.
	 * <p>
	 * Default: <i>""</i>
	 */
	private String accent = "";

	public Team() {
		id = ID_COUNT++;
	}

	/**
	 * Get the list of all team members.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Get the list of all team members.
	 */
	public synchronized List<Player> getAsyncPlayers() {
		return players;
	}

	public void setColour(ChatColor colour) {
		this.colour = colour;
	}

	public void setAccent(ChatColor colour) {
		if (colour == null) {
			accent = "";
			return;
		}
		
		accent = colour + "";
	}
	
	public void setAccent(String accent) {
		this.accent = accent;
	}

	/**
	 * Returns the string including a chosen accent.
	 * 
	 * @return colour + accent
	 */
	public synchronized String getChatColours() {
		return colour + accent;
	}

	public ChatColor getColour() {
		return colour;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Team #" + id + ":" + players;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((players == null) ? 0 : players.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team)obj;
		if (colour != other.colour)
			return false;
		if (players == null) {
			if (other.players != null)
				return false;
		}
		else if (!players.equals(other.players))
			return false;
		return true;
	}

}
