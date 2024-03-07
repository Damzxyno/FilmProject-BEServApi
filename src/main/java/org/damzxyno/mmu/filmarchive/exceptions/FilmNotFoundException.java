package org.damzxyno.mmu.filmarchive.exceptions;

public class FilmNotFoundException extends NullPointerException{
	private static final long serialVersionUID = 1L;

	public FilmNotFoundException(String message) {
		super(message);
	}
}
