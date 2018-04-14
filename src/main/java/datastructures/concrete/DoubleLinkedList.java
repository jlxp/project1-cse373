package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements IList<T> {

    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /*
     * add given item passed as a parameter at the end of the list
     * @see datastructures.interfaces.IList#add(java.lang.Object)
     */
    @Override
    public void add(T item) {
        Node<T> temp = new Node<T>(item);
        if (this.size == 0) {
            this.front = temp;
            this.back = this.front;
        } else {
            this.back.next = temp;
            temp.prev = this.back;
            this.back = this.back.next;
        }
        this.size++;
    }

    /*
     * removes and returns the item at the end of the list
     * @throws EmptyContainerException if the container is empty
     * @see datastructures.interfaces.IList#remove()
     */
    @Override
    public T remove() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        Node<T> temp = this.back; 
        if (this.back == this.front) {
            this.front = null;
            this.back = null;
        } else {
            this.back = this.back.prev;
            this.back.next = null;
            temp.prev = null;        
        }
        this.size--;
        return temp.data;
    }
    
    /*
     * @throws IndexOutOfBoundsException if index < 0 or index >= size of element
     */
    private void testIndexOutOfBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index is not within the proper range");
        }
    }
    
    /*
     * finds and returns the node using the given index and starting point that are
     * passed in as parameters
     */
    private Node<T> findNode(int index) {
        int count;
        Node<T> current;
        if (index <= this.size / 2 + (this.size % 2)) {
            count = 0;
            current = this.front;
            while (current.next != null && count < index) {
                current = current.next;
                count++;
            }
        } else {
            count = this.size - 1;
            current = this.back;
            while (current.prev != null && count > index) {
                current = current.prev;
                count--;
            }
        }
        return current;
    }
    
    /*
     * returns the item at the index passed in as a parameter
     * @throws IndexOutOfBoundsException if index < 0, index >= size
     * @see datastructures.interfaces.IList#get(int)
     */
    @Override
    public T get(int index) {
        testIndexOutOfBounds(index);
        if (index == 0) {
            return this.front.data;
        } else if (index == this.size -1) {
            return this.back.data;
        }
        return this.findNode(index).data;
    }
    
    /*
     * Overwrites the element at the given index to the given item
     * passed in as parameters
     * @throws IndexOutOfBoundsException if index < 0, index >= size
     * @see datastructures.interfaces.IList#set(int, java.lang.Object)
     */
    @Override
    public void set(int index, T item) {
        testIndexOutOfBounds(index);
        
        if (this.front.next == null) {
            this.front = new Node<T>(null, item, null);
        } else {
            if (index == 0) {
                this.delete(0);
                this.insert(0, item);
            } else if (index == this.size - 1) {
                this.remove();
                this.add(item);
            } else {                
                Node<T> current = this.findNode(index);
                Node<T> temp = new Node<T>(current.prev, item, current.next);
                current.prev.next = temp;
                current.next.prev = temp;
                current.prev = null;
                current.next = null;
            }
        }
    }
    
    /*
     * Inserts the given item at the given index, passed in as parameters,
     * if there is existing element at the given index, it shifts next 
     * elements over to the right.
     * @throws IndexOutOfBoundsException if index < 0 or index >= size + 1
     * @see datastructures.interfaces.IList#insert(int, java.lang.Object)
     */
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size + 1) {
            throw new IndexOutOfBoundsException("Index is not within the proper range");
        }
        
        if (this.front == null || index == this.size) {
            this.add(item);
        } else {
            Node<T> temp = new Node<T>(item);
            if (index == 0) {
                temp.next = this.front;
                this.front.prev = temp;
                this.front = temp; 
            } else {
                Node<T> current = this.findNode(index);
                temp.prev = current.prev;
                temp.next = current;
                current.prev = temp;
                temp.prev.next = temp; 
            } 
            this.size++;
        }
    }
    
    /*
     * deletes and returns the element of the given index passed in as a parameter
     * Shift the elements of higher indices down by one
     * @throws IndexOutOfBoundsException if index < 0 or index >= size
     * @see datastructures.interfaces.IList#delete(int)
     */
    @Override
    public T delete(int index) {
        testIndexOutOfBounds(index);
        
        if (index == size - 1) {
            return this.remove();
        }
        T temp = null;
        if (index == 0) {
            temp = this.front.data; 
            this.front = this.front.next;
            this.front.prev.next = null;
            this.front.prev = null;
        }  else {
            Node<T> current = this.findNode(index);
            temp = current.data;
            current.next.prev = current.prev;
            current.prev.next = current.next;
            current.prev = null;
            current.next = null;
        }
        this.size--;
        return temp;
    }

    /*
     * return the index of the first occurrence of given item passed in as
     * a parameter returns -1 if there is no element that matches given item
     * @see datastructures.interfaces.IList#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(T item) {
        Node<T> current = this.front;
        int idx = 0;
        while (current != null) {
            if (current.data == item && current.data == item
                    || (current.data != null && current.data.equals(item))) {
                return idx;
            }
            current = current.next;
            idx++;
        }
        return -1;
    }
    
    /*
     * returns the size of the list
     * @see datastructures.interfaces.IList#size()
     */
    @Override
    public int size() {
        return this.size;
    }
    
    /*
     * return true if there is an element that matches the given element
     * return false otherwise
     * @see datastructures.interfaces.IList#contains(java.lang.Object)
     */
    @Override
    public boolean contains(T other) {
        return this.indexOf(other) != -1;
    }

    /*
     * returns the iterator for the list
     * @see datastructures.interfaces.IList#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(this.front);
    }

    /*
     * creates a node that holds the data that can be "linked" together to create
     * a two way list
     */
    private static class Node<E> {
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }
    }

    /*
     * creates an iterator for the list
     */
    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return this.current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (this.current == null) {
                throw new NoSuchElementException("List is empty");
            }
            if (hasNext()) {
                T temp = (T) this.current.data;
                this.current = this.current.next;
                return temp;
            } 
            return null;
        }
    }
}