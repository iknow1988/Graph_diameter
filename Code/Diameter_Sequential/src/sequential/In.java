package sequential;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class In {

	private Scanner scanner;

	private static final String CHARSET_NAME = "UTF-8";
	private static final Locale LOCALE = Locale.US;
	private static final Pattern WHITESPACE_PATTERN = Pattern
			.compile("\\p{javaWhitespace}+");
	private static final Pattern EMPTY_PATTERN = Pattern.compile("");
	private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

	public In(String s) {
		try {
			File file = new File(s);
			if (file.exists()) {
				scanner = new Scanner(file, CHARSET_NAME);
				scanner.useLocale(LOCALE);
			}
		} catch (IOException ioe) {
			System.err.println("Could not open " + s);
		}
	}

	public In(Scanner scanner) {
		this.scanner = scanner;
	}

	public boolean exists() {
		return scanner != null;
	}

	public boolean isEmpty() {
		return !scanner.hasNext();
	}

	public boolean hasNextLine() {
		return scanner.hasNextLine();
	}

	public boolean hasNextChar() {
		scanner.useDelimiter(EMPTY_PATTERN);
		boolean result = scanner.hasNext();
		scanner.useDelimiter(WHITESPACE_PATTERN);
		return result;
	}

	public String readLine() {
		String line;
		try {
			line = scanner.nextLine();
		} catch (Exception e) {
			line = null;
		}
		return line;
	}

	public char readChar() {
		scanner.useDelimiter(EMPTY_PATTERN);
		String ch = scanner.next();
		assert (ch.length() == 1) : "Internal (Std)In.readChar() error!"
				+ " Please contact the authors.";
		scanner.useDelimiter(WHITESPACE_PATTERN);
		return ch.charAt(0);
	}

	public String readAll() {
		if (!scanner.hasNextLine())
			return "";

		String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
		scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
		return result;
	}

	public String readString() {
		return scanner.next();
	}

	public int readInt() {
		return scanner.nextInt();
	}

	public double readDouble() {
		return scanner.nextDouble();
	}

	public float readFloat() {
		return scanner.nextFloat();
	}

	public long readLong() {
		return scanner.nextLong();
	}

	public short readShort() {
		return scanner.nextShort();
	}

	public byte readByte() {
		return scanner.nextByte();
	}

	public boolean readBoolean() {
		String s = readString();
		if (s.equalsIgnoreCase("true"))
			return true;
		if (s.equalsIgnoreCase("false"))
			return false;
		if (s.equals("1"))
			return true;
		if (s.equals("0"))
			return false;
		throw new InputMismatchException();
	}

	public String[] readAllStrings() {
		String[] tokens = WHITESPACE_PATTERN.split(readAll());
		if (tokens.length == 0 || tokens[0].length() > 0)
			return tokens;
		String[] decapitokens = new String[tokens.length - 1];
		for (int i = 0; i < tokens.length - 1; i++)
			decapitokens[i] = tokens[i + 1];
		return decapitokens;
	}

	public String[] readAllLines() {
		ArrayList<String> lines = new ArrayList<String>();
		while (hasNextLine()) {
			lines.add(readLine());
		}
		return lines.toArray(new String[0]);
	}

	public int[] readAllInts() {
		String[] fields = readAllStrings();
		int[] vals = new int[fields.length];
		for (int i = 0; i < fields.length; i++)
			vals[i] = Integer.parseInt(fields[i]);
		return vals;
	}

	public double[] readAllDoubles() {
		String[] fields = readAllStrings();
		double[] vals = new double[fields.length];
		for (int i = 0; i < fields.length; i++)
			vals[i] = Double.parseDouble(fields[i]);
		return vals;
	}

	public void close() {
		scanner.close();
	}
}