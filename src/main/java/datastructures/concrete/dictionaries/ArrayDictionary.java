package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    private final int INIT_SIZE = 10;
    
    public ArrayDictionary() {
        pairs = makeArrayOfPairs(INIT_SIZE);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        checkKey(key);
        for (int i = 0; i < this.size; i++) {
            if (this.pairs[i].key == null || this.pairs[i].key.equals(key)) {
                return (V) this.pairs[i].value;
            }
        }
        return null;
    }
    
    /*TODO: add comment*/
    private void checkKey(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException("dictionary does not contain key");
        }
    }
    
    @Override
    public void put(K key, V value) {
        if (!this.containsKey(key)) {
            if (this.size < this.pairs.length) {
                this.pairs[this.size] = new Pair<>(key, value);
            } else {
                Pair<K, V>[] result = makeArrayOfPairs(this.pairs.length * 2);
                for (int i = 0; i < this.pairs.length; i++) {
                    result[i] = this.pairs[i];
                }
                result[this.size] = new Pair<>(key, value);
                this.pairs = result;
            }
            this.size++;
        } else {
            for (int i = 0; i < this.size; i++) {
                if (this.pairs[i].key == null || this.pairs[i].key.equals(key)) {
                    this.pairs[i] = new Pair<>(key, value);
                }
            }
        }
    }

    @Override
    public V remove(K key) {
        checkKey(key);
        V temp = null;
        if (this.pairs[this.size - 1].key == null || this.pairs[this.size - 1].key.equals(key)) {
            temp = this.pairs[this.size - 1].value;
            this.pairs[this.size - 1] = null;
        } else {
            for (int i = 0; i < this.size - 1; i++) {
                if (this.pairs[i].key == null || this.pairs[i].key.equals(key)) {
                    temp = this.pairs[i].value;
                    this.pairs[i] = this.pairs[this.size - 1];
                    this.pairs[this.size - 1] = null;
                }
            }
        }
 
        this.size--;
        return (V) temp;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < this.size; i++) {
            if (this.pairs[i].key == null || this.pairs[i].key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
