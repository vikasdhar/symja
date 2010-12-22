/*
 * $Id: MapEntry.java 3382 2010-11-29 23:01:59Z kredel $
 */

package edu.jas.util;


import java.util.Map;

/**
 * MapEntry helper class implements Map.Entry.
 * Required until JDK 1.6 becomes every where available.
 * @see java.util.AbstractMap.SimpleImmutableEntry in JDK 1.6.
 * @author Heinz Kredel.
 */

public class MapEntry<K,V> implements Map.Entry<K,V> {


    final K key;


    final V value;


    /**
     * Constructor.
     */
    public MapEntry(K k, V v) {
        key = k;
        value = v;
    }


    /**
     * Get the key.
     * @see java.util.Map.Entry#getKey()
     */
    public K getKey() {
        return key;
    }


    /**
     * Get the value.
     * @see java.util.Map.Entry#getValue()
     */
    public V getValue() {
        return value;
    }


    /**
     * Set the value.
     * Is not implemented.
     * @see java.util.Map.Entry#setValue(V)
     */
    public V setValue(V value) {
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * Comparison with any other object.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object b) {
        if (!(b instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<K,V> me = (Map.Entry<K,V>) b;
        return key.equals(me.getKey()) && value.equals(me.getValue());
    }


    /**
     * Hash code for this MapEntry.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return key.hashCode() * 37 + value.hashCode();
    }
 
}