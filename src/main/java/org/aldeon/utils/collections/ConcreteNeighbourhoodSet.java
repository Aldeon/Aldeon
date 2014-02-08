package org.aldeon.utils.collections;

import org.aldeon.utils.various.Arithmetic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ConcreteNeighbourhoodSet<E> extends CircularTreeSet<E> implements NeighbourhoodSet<E>{

    private Arithmetic<E> arithmetic;

    public ConcreteNeighbourhoodSet(Arithmetic<E> arithmetic) {
        super(arithmetic);
        this.arithmetic = arithmetic;
    }

    @Override
    public Set<E> closestValues(E value, int maxResults) {

        Set<E> results = new HashSet<>();

        if(maxResults <= 0 || size() <= maxResults) {
            results.addAll(this);
        } else {
            Iterator<E> iRev = revIterator(value);
            Iterator<E> iFwd = fwdIterator(value);
            E vRev = iRev.next();
            E vFwd = iFwd.next();

            if(arithmetic.compare(vFwd, value) == 0) {
                vFwd = iFwd.next();
            }

            E dRev = arithmetic.sub(value, vRev);
            E dFwd = arithmetic.sub(vFwd, value);

            for(int i = 0; i < maxResults; ++i) {
                if(arithmetic.compare(dRev, dFwd) < 0) {
                    results.add(vRev);
                    vRev = iRev.next();
                    dRev = arithmetic.sub(value, vRev);
                } else {
                    results.add(vFwd);
                    vFwd = iFwd.next();
                    dFwd = arithmetic.sub(vFwd, value);
                }
            }
        }

        return results;
    }

}
