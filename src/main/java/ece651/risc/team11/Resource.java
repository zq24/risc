package ece651.risc.team11;

import java.io.Serializable;

public abstract class Resource implements Serializable, Cloneable {

    private static final long serialVersionUID = 651L;

    public Resource() {
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}