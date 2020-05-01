package ece651.risc.team11;

public class RiscCMDConsole extends RiscTextConsole {
    public RiscCMDConsole(RiscClient client) {
        super(client, System.in, System.out);
    }
}