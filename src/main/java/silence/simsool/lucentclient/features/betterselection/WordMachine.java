package silence.simsool.lucentclient.features.betterselection;

public enum WordMachine {
	UNKNOWN,
	NUMERIC,
	INTEGER,
	FLOAT,
	WORD,
	WHITESPACE,
	SPECIAL,
	DONE;

	public static boolean isDecimalSeparator(char c) {
		return c == '.' || c == ',';
	}

	public static boolean isDigit(char c) {
		return Character.isDigit(c);
	}

	public static boolean isCurrencySymbol(char c) {
		return c == '$'
			|| c == '€'
			|| c == '£'
			|| c == '¥'
			|| '\u20A0' <= c && c <= '\u20CF';
	}

	public static boolean isWordCharacter(char c) {
		return Character.isAlphabetic(c) || Character.isIdeographic(c) || c == '_';
	}

	public static boolean isWhitespaceCharacter(char c) {
		return Character.isSpaceChar(c);
	}

	public static WordMachine startRead(char c) {
		return UNKNOWN.read(c);
	}

	public WordMachine read(char c) {
		return switch (this) {
			case UNKNOWN -> {
				if (c == '_') {
					yield UNKNOWN;
				} else if (isDigit(c)) {
					yield NUMERIC;
				} else if (isCurrencySymbol(c)) {
					yield INTEGER;
				} else if (isDecimalSeparator(c)) {
					yield FLOAT;
				} else if (isWordCharacter(c)) {
					yield WORD;
				} else if (isWhitespaceCharacter(c)) {
					yield WHITESPACE;
				} else {
					yield SPECIAL;
				}
			}
			case NUMERIC -> {
				if (isDigit(c)) {
					yield NUMERIC;
				} else if (isCurrencySymbol(c)) {
					yield INTEGER;
				} else if (isDecimalSeparator(c)) {
					yield FLOAT;
				} else if (isWordCharacter(c)) {
					yield WORD;
				} else {
					yield DONE;
				}
			}
			case INTEGER -> {
				if (isDigit(c) || isCurrencySymbol(c)) {
					yield INTEGER;
				} else if (isDecimalSeparator(c)) {
					yield FLOAT;
				} else {
					yield DONE;
				}
			}
			case FLOAT -> {
				if (isDigit(c) || isCurrencySymbol(c)) {
					yield FLOAT;
				} else {
					yield DONE;
				}
			}
			case WORD -> {
				if (isWordCharacter(c) || isDigit(c)) {
					yield WORD;
				} else {
					yield DONE;
				}
			}
			case WHITESPACE -> {
				if (isWhitespaceCharacter(c)) {
					yield WHITESPACE;
				} else {
					yield DONE;
				}
			}
			case SPECIAL, DONE -> DONE;
		};
	}
}