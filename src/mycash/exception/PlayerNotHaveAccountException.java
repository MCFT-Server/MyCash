package mycash.exception;

@SuppressWarnings("serial")
public class PlayerNotHaveAccountException extends Exception {
	@Override
	public String getMessage() {
		return "That player doesn't have cash account";
	}
}
