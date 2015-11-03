package co.gm4.uhc.team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Util;

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
	private static int idCount = 1;

	/**
	 * The team ID.
	 */
	private int id;

	/**
	 * All the members of the team.
	 */
	private List<UUID> players = new Vector<>();

	/**
	 * List of all deaths.
	 */
	private List<UUID> deaths = new Vector<>();

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
		id = idCount++;
	}

	/**
	 * Get the list of all team members.
	 */
	public List<UUID> getPlayers() {
		return players;
	}

	/**
	 * Get the list of all team members.
	 */
	public synchronized List<UUID> getAsyncPlayers() {
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

	public int size() {
		return players.size();
	}

	public boolean contains(Player player) {
		return players.contains(player.getUniqueId());
	}

	public boolean add(Player player) {
		return players.add(player.getUniqueId());
	}

	/**
	 * Removes a player from the team and adds them to the dead player list.
	 * <p>
	 * Method will also show a death message.
	 * 
	 * @param player
	 *            The ID of the player that dies.
	 */
	public void die(UUID player, GM4UHC plugin) {
		players.remove(player);
		deaths.add(player);
		
		// Drop skull.
		Player p = Bukkit.getPlayer(player);
		if (p != null) {
			ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta meta = (SkullMeta)head.getItemMeta();
			meta.setOwner(p.getName());
			head.setItemMeta(meta);
			p.getWorld().dropItem(p.getLocation(), head);
		}
		
		plugin.getTeamManager().removeEmptyTeams();
		plugin.getMatch().getOffline().remove(player);

		// Remove player from the match.
		plugin.getMatch().getPlayers().remove(player);

		// Add player count.
		int playersLeft = plugin.getMatch().count();
		int teamsLeft = plugin.getTeamManager().getTeams().size();

		// Announce winner!
		if (teamsLeft == 1) {
			Team teamLeft = plugin.getTeamManager().getTeams().get(0);
			List<UUID> wonPlayers = new ArrayList<>();
			wonPlayers.addAll(teamLeft.getPlayers());
			wonPlayers.addAll(teamLeft.getDeaths());
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "WIN "
					+ teamLeft.getChatColours() + "Team #" + teamLeft.getId()
					+ " containing the players " + Util.toString(wonPlayers) + " has won!");
		}
		// Announce teams and players left.
		else {
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "DEATH "
					+ ChatColor.GREEN + "There are " + playersLeft + " players and " + teamsLeft
					+ " teams left.");
		}
	}

	public boolean remove(Player player) {
		return players.remove(player.getUniqueId());
	}

	public List<UUID> getDeaths() {
		return deaths;
	}

	@Override
	public String toString() {
		return "Team #" + id + ":" + Util.toString(players);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}

}
