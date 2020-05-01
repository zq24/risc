package ece651.risc.team11;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This class is to ask questions and  get input from user
 * Dependency injection, convenient for testing
 */
public class StringAsker {
    private final Scanner scanner;
    private final PrintStream out;

    public StringAsker(InputStream in, OutputStream out) {
        scanner = new Scanner(in);
        this.out = new PrintStream(out);
    }

    public String ask(String message) {
        out.println(message);

        return scanner.nextLine().toLowerCase().trim();

    }
}