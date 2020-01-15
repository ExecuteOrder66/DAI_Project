package es.uvigo.esei.dai.hybridserver;

public class InvalidPageException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidPageException() {
	}

	public InvalidPageException(String message) {
		super(message);
	}

	public InvalidPageException(Throwable cause) {
		super(cause);
	}

	public InvalidPageException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
