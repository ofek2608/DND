package com.ofek2608.gim_old;

public class GimParseException extends RuntimeException {
	public final String code;
	public final int point;

	public GimParseException(String message, char[] code, int point) {
		super(message);
		this.code = new String(code);
		this.point = point;
	}
}
