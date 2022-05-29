package com.ofek2608.gim_old;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;

public final class GimSerializer {
	private GimSerializer() {}

	public static String gimToNiceString(Gim gim) { //TODO test
		return gimToNiceString(new StringBuilder(), gim, "").toString();
	}

	public static String gimToCompactString(Gim gim) { //TODO test
		return gimToCompactString(new StringBuilder(), gim).toString();
	}

	public static Gim stringToGim(String str) { //TODO test
		Gim gim = new SimpleGim();
		stringToGim(gim, str, 0);
		return gim;
	}



	public static int stringToGim(Gim output, String str, int offset) {
		return Math.min(parseGim(output, (str + " ").toCharArray(), offset), str.length());
	}






	public static Gim fromResources(Function<String,String> reader, Gim output, String path) {
		String content = reader.apply(path);
		//gim file
		if (path.endsWith(".gim")) {
			stringToGim(output, content, 0);
			return output;
		}
		//unknown file
		if (path.contains("."))
			return output;
		//folder
		for (String file : content.split("\n")) {
			int dotIndex = file.lastIndexOf('.');
			fromResources(
					reader,
					output.getChild(dotIndex >= 0 ? file.substring(0, dotIndex - 1) : file),
					path + "/" + file
			);
		}

		return output;
	}










	private static StringBuilder gimToNiceString(StringBuilder builder, Gim gim, String offset) {
		Set<String> children = gim.getChildren();
		@Nullable
		String value = gim.asString();
		if (children.size() == 0) {
			if (value != null)
				addStringInQuotes(builder, value);
			else
				builder.append("null");
			return builder;
		}

		String nextOffset = offset + "  ";

		//Array
		if (value == null && gim.isArray()) {
			builder.append("[\n");
			int s = children.size();
			for (int i = 0; i < s; i++) {
				builder.append(nextOffset);
				gimToNiceString(builder, gim.getChild(i), nextOffset);
				builder.append("\n");
			}
			return builder.append(offset).append(']');
		}

		//Map
		builder.append("{\n");
		if (value != null) {
			builder.append(nextOffset);
			addStringInQuotes(builder, value);
			builder.append("\n");
		}
		for (String child : children) {
			builder.append(nextOffset).append(child).append(" : ");
			gimToNiceString(builder, gim.getChild(child), nextOffset);
			builder.append("\n");
		}
		return builder.append(offset).append('}');
	}

	private static StringBuilder gimToCompactString(StringBuilder builder, Gim gim) {
		Set<String> children = gim.getChildren();
		@Nullable
		String value = gim.asString();
		if (children.size() == 0) {
			if (value != null)
				addStringInQuotes(builder, value);
			else
				builder.append("null");
			return builder;
		}

		//Array
		if (value == null && gim.isArray()) {
			builder.append('[');
			int s = children.size();
			for (int i = 0; i < s; i++) {
				gimToCompactString(builder, gim.getChild(i));
			}
			return builder.append(']');
		}

		//Map
		builder.append('{');
		if (value != null) {
			addStringInQuotes(builder, value);
		}
		for (String key : children) {
			builder.append(key);
			Gim child = gim.getChild(key);
			Set<String> childChildren;
			while (!child.hasValue() && (childChildren = child.getChildren()).size() == 1) {
				String subChild = childChildren.iterator().next();
				builder.append('.').append(subChild);
				child = child.getChild(subChild);
			}
			gimToCompactString(builder, child);
		}
		return builder.append('}');
	}

	private static void addStringInQuotes(StringBuilder builder, String str) {
		char quoteType = str.indexOf('\"') >= 0 || str.indexOf('\'') < 0 ? '\'' : '\"';

		builder.append(quoteType);
		for (char c : str.toCharArray()) {
			builder.append(switch (c) {
				case '\0' -> "\\0";
				case '\t' -> "\\t";
				case '\n' -> "\\n";
				case '\r' -> "\\r";
				case '\b' -> "\\b";
				default -> {
					if (c == quoteType)
						yield "\\" + c;
					if (c < 0x10)
						yield "\\u000" + Integer.toHexString(c);
					if (c < 0x20)
						yield "\\u001" + Integer.toHexString(c & 0xF);
					yield c;
				}
			});
		}
		builder.append(quoteType);
	}












	private static int parseGim(Gim output, char[] chars, int i) {
		int len = chars.length;
		i = skipWhitespaces(chars, i);

		if (i >= len)
			throw new GimParseException("Unexpected EOF, expected gim.", chars, len);

		char c = chars[i];
		return switch (c) {
			case '{' -> parseGimMap(output, chars, i + 1);
			case '[' -> parseGimList(output, chars, i + 1);
			case '\"', '\'' -> parseGimString(output, chars, i);
			default -> parseGimConstant(output, chars, i);
		};
	}










	private static int parseGimMap(Gim output, char[] chars, int i) {
		int len = chars.length;

		while (true) {
			i = skipWhitespaces(chars, i);
			if (i >= len)
				throw new GimParseException("Unexpected EOF, expected '}'.", chars, len);
			if (chars[i] == '}')
				break;

			int endKey = skipWord(chars, i);
			i = parseGim(endKey <= i ? output : output.getChild(new String(chars, i, endKey - i)), chars, endKey);
		}
		return i + 1;
	}



	private static int parseGimList(Gim output, char[] chars, int i) {
		int len = chars.length;

		int index = 0;


		while (true) {
			i = skipWhitespaces(chars, i);
			if (i >= len)
				throw new GimParseException("Unexpected EOF, expected ']'.", chars, len);
			if (chars[i] == ']')
				break;

			i = parseGim(output.getChild(index++), chars, i);
		}
		return i + 1;
	}


	private static int parseGimString(Gim output, char[] chars, int i) {
		int len = chars.length;
		char endChar = chars[i++];

		StringBuilder result = new StringBuilder();

		char c = 0;
		while (i < len && (c = chars[i++]) != endChar) {
			if (c != '\\') {
				result.append(c);
				continue;
			}
			if (i >= len)
				throw new GimParseException("Unexpected EOF, expected end of string.", chars, len);
			c = chars[i++];
			result.append(switch (c) {
				case '0' -> '\0';
				case 't' -> '\t';
				case 'n' -> '\n';
				case 'r' -> '\r';
				case 'b' -> '\b';
				case '\'', '\"' -> c;
				case 'u' -> {
					if (i + 3 > len)
						throw new GimParseException("Unexpected EOF, expected end of string.", chars, len);
					try {
						int num = Integer.parseInt(new String(chars, i, 4), 16);
						i += 4;
						yield num;
					} catch (NumberFormatException e) {
						throw new GimParseException("Unexpected chars after '\\u'.", chars, len);
					}
				}
				default -> throw new GimParseException("Unexpected char after '\\'", chars, i);
			});
		}
		if (c != endChar)
			throw new GimParseException("Unexpected EOF, expected end of string.", chars, len);

		output.setString(result.toString());
		return i;
	}


	private static int parseGimConstant(Gim output, char[] chars, int i) {
		int end = skipWord(chars, i);
		if (end <= i)
			throw new GimParseException("Expected constant", chars, i);
		String str = new String(chars, i, end - i);
		if (str.equals("null")) {
			output.remove();
			return end;
		}
		if (str.equals("true")) {
			output.setBoolean(true);
			return end;
		}
		if (str.equals("false")) {
			output.setBoolean(false);
			return end;
		}
		try {
			if (str.startsWith("0x")) {
				output.setLong(Long.parseLong(str.substring(2), 16));
				return end;
			}
			if (str.startsWith("0b")) {
				output.setLong(Long.parseLong(str.substring(2), 2));
				return end;
			}
		} catch (NumberFormatException ignored) {}

		try {
			output.setLong(Long.parseLong(str));
			return end;
		} catch (NumberFormatException ignored) {}

		try {
			output.setDouble(Double.parseDouble(str));
			return end;
		} catch (NumberFormatException ignored) {}

		throw new GimParseException("Expected constant", chars, i);
	}




	private static int skipWord(char[] chars, int i) {
		int len = chars.length;
		if (i >= len)
			return len;

		char c;
		do {
			if (i >= len)
				return len;
			c = chars[i++];
		} while (c == '.' || '0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || c == '_' || 'a' <= c && c <= 'z');
		return i - 1;
	}


	private static int skipWhitespaces(char[] chars, int i) {
		int len = chars.length;
		if (i >= len)
			return len;

		do {
			char c = chars[i++];

			if (c < 0x20 || c == ' ' || c == ',' || c == ':' || c == ';')
				continue;

			if (c == '/') {
				i = skipComment(chars, i);
				continue;
			}

			break;
		} while (i < len);

		return i - 1;
	}

	/**
	 * @param chars the chars
	 * @param i     the index of the second char of the comment
	 * @return the index of the end of the comment
	 */
	private static int skipComment(char[] chars, int i) {
		int len = chars.length;
		if (i >= len)
			throw new GimParseException("Unexpected EOF after a start of a comment", chars, len);

		char c = chars[i];
		if (c == '/') {
			while (i < len && chars[i++] != '\n');
			return i;
		}
		if (c == '*') {
			while (i < len - 1 && !(chars[i++] == '*' && chars[i] == '/'));
			return i + 1;
		}
		throw new GimParseException("Unexpected char after '/'", chars, i);
	}


}
