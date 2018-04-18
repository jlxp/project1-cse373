package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;


public class ArrayDictionary<K, V> implements IDictionary<K, V> {

    private Pair<K, V>[] pairs;

    private int size;
    private static final int INIT_SIZE = 10;
    
    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(INIT_SIZE);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }
    
    /*
     * return the value that is matching with the given key passed as a parameter
     * @throws NoSuchKeyException if there is no key that matches with the given key
     * @see datastructures.interfaces.IDictionary#get(java.lang.Object)
     */
    @Override
    public V get(K key) {
        int index = this.indexOf(key);
        this.checkKey(index);
        if (this.pairs[index].key == key || (this.pairs[index].key != null &&
                this.pairs[index].key.equals(key))) {
            return (V) this.pairs[index].value;
        }
        return null; 
    }
    
    /*
     * @throws NoSuchKeyException if there is no key that matches with the key
     * passed in as a parameter
     */
    private void checkKey(int index) {
        if (index == -1) {
            throw new NoSuchKeyException("dictionary does not contain key");
        }
    }
    
    /*
     * Puts/adds the pair of key and value parameters passed in to the dictionary
     * If key is already in the dictionary, replace the value with the given value for that key
     * @see datastructures.interfaces.IDictionary#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public void put(K key, V value) {
        int index = this.indexOf(key);
        if (index == -1) {
            if (this.size < this.pairs.length) {
                this.pairs[this.size] = new Pair<>(key, value);
            } else {
                Pair<K, V>[] result = makeArrayOfPairs(this.pairs.length * 2);
                for (int i = 0; i < this.size; i++) {
                    result[i] = this.pairs[i];
                }
                result[this.size] = new Pair<>(key, value);
                this.pairs = result;
            }
            this.size++;
        } else {
            if (this.pairs[index].key == key || (this.pairs[index].key != null && 
                    this.pairs[index].key.equals(key))) {
                this.pairs[index] = new Pair<>(key, value);
            }
        }
    }

    /*
     * remove the given key passed in as a parameter and its value, from the dictionary
     * @throws NoSuchKeyException if there is no key matching key in dictionary
     * @see datastructures.interfaces.IDictionary#remove(java.lang.Object)
     */
    @Override
    public V remove(K key) {
        int index = this.indexOf(key);
        this.checkKey(index);
        V temp = null;
        if (this.pairs[this.size - 1].key == key || (this.pairs[this.size - 1].key != null && 
                this.pairs[this.size - 1].key.equals(key))) {
            temp = this.pairs[this.size - 1].value;
            this.pairs[this.size - 1] = null;
            this.size--;
            return(V) temp;
        } else {
            if (this.pairs[index].key == key || (this.pairs[index].key != null && 
                    this.pairs[index].key.equals(key))) {
                temp = this.pairs[index].value;
                this.pairs[index] = this.pairs[this.size - 1];
                this.pairs[this.size - 1] = null;
                this.size--;
                return (V) temp;
            }
        }
        this.size--;
        return (V) temp;
    }

    /*
     * return true if there is key in dictionary that matches with the given key
     * passed in as a parameter
     * return false otherwise
     * @see datastructures.interfaces.IDictionary#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(K key) {
        return indexOf(key) != -1;
    }
    
    /*
     * returns the index of a key passed in as a parameter
     * if key is not found returns -1
     */
    private int indexOf(K key) {
        for (int i = 0; i < this.size; i++) {
            if (this.pairs[i].key == key || (this.pairs[i].key != null && 
                    this.pairs[i].key.equals(key))) {
                return i; 
            }
        }
        return -1;
    }

    /*
     * return the size of dictionary
     * @see datastructures.interfaces.IDictionary#size()
     */
    @Override
    public int size() {
        return this.size;
    }

    /*
     * creates a pair of key and value
     */
    private static class Pair<K, V> {
        public K key;
        public V value;

        // initiates the pair with given key and value
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
