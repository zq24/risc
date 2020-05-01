package ece651.risc.team11;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringAskerTest {

    @Test
    void askTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("  MoVe  \n".getBytes());
        System.setIn(in);
        StringAsker stringAsker = new StringAsker(System.in, System.out);
        String processedLine = stringAsker.ask("  MoVe  ");
        assertEquals(processedLine, "move");
    }
}