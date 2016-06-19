package mycash.exception;

@SuppressWarnings("serial")
public class PlayerNotHaveEnoughCashExeception extends Exception {
	@Override
	public String getMessage() {
		return "That player doesn't have enough money";
	}
}
