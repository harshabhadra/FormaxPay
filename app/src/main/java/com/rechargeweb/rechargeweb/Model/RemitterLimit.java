package com.rechargeweb.rechargeweb.Model;

public class RemitterLimit {

    private int total;

    public RemitterLimit(int remaining) {
        this.total = remaining;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
