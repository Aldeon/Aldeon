package org.aldeon.gui.various;

public enum Direction {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;

    public boolean isVertical() {
        return this == TOP || this == BOTTOM;
    }

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }

    public int getX() {
        if(this == RIGHT) return 1;
        if(this == LEFT) return -1;
        return 0;
    }

    public int getY() {
        if(this == BOTTOM) return 1;
        if(this == TOP) return -1;
        return 0;
    }

    public Direction opposite() {
        if(this == TOP)     return BOTTOM;
        if(this == BOTTOM)  return TOP;
        if(this == LEFT)    return RIGHT;
        if(this == RIGHT)   return LEFT;
        return null;
    }
}
