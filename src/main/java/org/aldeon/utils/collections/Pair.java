package org.aldeon.utils.collections;

public class Pair<P,Q> {

    private P p;
    private Q q;

    public Pair(P p, Q q) {
        setP(p);
        setQ(q);
    }

    public Pair() {

    }

    public P getP() {
        return p;
    }

    public Q getQ() {
        return q;
    }

    public void setP(P p) {
        this.p = p;
    }

    public void setQ(Q q) {
        this.q = q;
    }
}
