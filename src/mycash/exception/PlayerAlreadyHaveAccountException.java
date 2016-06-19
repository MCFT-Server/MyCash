package mycash.exception;

@SuppressWarnings("serial")
public class PlayerAlreadyHaveAccountException extends Exception {

	@Override
	public String getMessage() {
		return "That player already has account.";
	}
}
