package co.gm4.uhc.game;

/**
 * @author MrSugarCaney
 */
public enum MatchState {

	/**
	 * Waiting for the match to start.
	 */
	LOBBY,

	/**
	 * The intermediate state.
	 * <p>
	 * The moment everyone is being spread and the countdown has started.
	 */
	PREPARING,

	/**
	 * The match has started.
	 */
	RUNNING;

}
