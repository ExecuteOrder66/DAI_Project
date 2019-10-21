package es.uvigo.esei.dai.hybridserver;

public class InvalidHtmlPageException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidHtmlPageException() {
	}

	public InvalidHtmlPageException(String message) {
		super(message);
	}

	public InvalidHtmlPageException(Throwable cause) {
		super(cause);
	}

	public InvalidHtmlPageException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidHtmlPageException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
