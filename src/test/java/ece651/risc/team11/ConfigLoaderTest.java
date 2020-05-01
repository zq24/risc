package ece651.risc.team11;

import org.junit.jupiter.api.Test;

class ConfigLoaderTest {

    @Test
    void getProperty() {
        ConfigLoader.getProperty("ip");
    }
}