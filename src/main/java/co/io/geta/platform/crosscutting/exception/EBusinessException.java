package co.io.geta.platform.crosscutting.exception;

/**
 * Handles the business exceptions that are generated in the flow.
 */
public class EBusinessException extends Exception {
	private static final long serialVersionUID = 7718828512143293558L;

	public EBusinessException() {
	}

	public EBusinessException(String message) {
		super(message);
	}

	public EBusinessException(Throwable cause) {
		super(cause);
	}

	public EBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public EBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
