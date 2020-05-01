package ece651.risc.team11;

import java.io.Serializable;

/**
 * This class represents unit.
 */
public abstract class Unit implements Serializable, Cloneable {
    private static final long serialVersionUID = 651L;

    /**
     * This field is the name of the unit.
     */
    protected String name;

    protected int bonus;

    protected Integer type;

    protected int ownerId;

    public Unit() {
        this.name = this.getClass().getSimpleName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBonus() {
        return bonus;
    }

    public Integer getType() {
        return type;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getOwnerId() {
        return ownerId;
    }

}