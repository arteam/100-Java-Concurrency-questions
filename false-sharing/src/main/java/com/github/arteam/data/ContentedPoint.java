package com.github.arteam.data;

import sun.misc.Contended;

public class ContentedPoint {

    @Contended
    private long x;

    private long y;

    public ContentedPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
}
