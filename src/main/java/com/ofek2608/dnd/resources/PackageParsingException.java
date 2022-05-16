package com.ofek2608.dnd.resources;

public class PackageParsingException extends Exception {
	public PackageParsingException() {
	}

	public PackageParsingException(String message) {
		super(message);
	}

	public PackageParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PackageParsingException(Throwable cause) {
		super(cause);
	}

	public PackageParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
