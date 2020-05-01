package ece651.risc.team11;

import org.junit.jupiter.api.Test;

public class OtherTest {
    @Test
    void otherTest() {
        Resource resource = new Food();
        Resource technology = new Technology();
        try {
            resource.clone();
            technology.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}