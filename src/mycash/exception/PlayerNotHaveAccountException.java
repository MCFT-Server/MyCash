package mycash.exception;

@SuppressWarnings("serial")
public class PlayerNotHaveAccountException extends RuntimeException {
	@Override
	public String getMessage() {
		return "That player doesn't have cash account";
	}
}
