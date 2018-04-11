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
     * add given item at the end of the list
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
     * remove and return the item at the end of the list
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
     * @throw IndexOutOfBoundsException if index < 0 or index > size of element
     */
    private void testIndexOutOfBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index is not within the proper range");
        }
    }
    
    /*
     * find and return the node at the given index
     */
    private Node<T> findNode(Node<T> start, int index) {
        int count;
        Node<T> current = start;
        if (start == this.back) {
            count = this.size - 1;
            while (current.prev != null && count > index) {
                current = current.prev;
                count--;
            }
        } else {
            count = 0;
            while (current.next != null && count < index) {
                current = current.next;
                count++;
            }
        }
        return current;
    }
    
    /*
     * return the item at the given index
     * @throws IndexOutOfBoundsException if index < 0, index > size
     * @see datastructures.interfaces.IList#get(int)
     */
    @Override
    public T get(int index) {
        testIndexOutOfBounds(index);
        if (index == 0) {
            return this.front.data;
        } else if (index == this.size -1) {
            return this.back.data;
        } else {
            return findNode(this.front, index).data;
        }
    }
    
    /*
     * overwrites the element at the given index to the given item
     * @throws IndexOutOfBoundsException if index < 0, index > size
     * @see datastructures.interfaces.IList#set(int, java.lang.Object)
     */
    @Override
    public void set(int index, T item) {
        testIndexOutOfBounds(index);
        
        if (this.front.next == null) {
            this.front = new Node<T>(null, item, null);
        } else {
            Node<T> current;
            Node<T> temp = new Node<T>(item);
            if (index == 0) {
                current = this.front;
                temp.next = current.next;
                current.next.prev = temp;
                current.next = null;
                this.front = temp;
            } else if (index == this.size - 1) {
                current = this.back;
                temp.prev = this.back.prev;
                current.prev.next = temp;
                current.prev = null;
                this.back = temp;
            } else {
                current = findNode(this.front, index);
                temp.prev = current.prev;
                temp.next = current.next; 
                current.prev.next = temp;
                current.next.prev = temp;
                current.prev = null;
                current.next = null;
            }
        }
    }
    
    /*
     * Inserts the given item at the given index, 
     * If there is existing element at the given index,
     * overwrites the element with given item and shifts next elements over to its next
     * @throws IndexOutOfBoundsException if index < 0 or index > size + 1
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
            Node<T> current;
            if (index == 0) {
                temp.next = this.front;
                this.front.prev = temp;
                this.front = temp; 
            } else if (index <= this.size / 2 + (this.size % 2)) {
                current = findNode(this.front, index);
                temp.prev = current.prev;
                temp.next = current;
                current.prev = temp;
                temp.prev.next = temp;
            } else {
                current = findNode(this.back, index);
                temp.prev = current;
                temp.next = current.next;
                current.next = temp;
                temp.next.prev = temp;
            }
            this.size++;
        }
    }
    
    /*
     * delete and return the element of the given index
     * Shift the elemets of higher indices down by one
     * @throws IndexOutOfBoundsException if index < 0 or index > size
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
        }  else if (index <= this.size / 2 + (this.size % 2)) {
            Node<T> current = findNode(this.front, index);
            temp = current.data;
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current.next = null;
            current.prev = null;
        } else {
            Node<T> current = findNode(this.back, index);
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
     * return the index of the first occurrence of given item
     * return -1 if there is no element that matches given item
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
     * return the size of the container
     * @see datastructures.interfaces.IList#size()
     */

    @Override
    public int size() {
        return this.size;
    }
    
    /*
     * return true if there is element that matches the given element
     * return false otherwise
     * @see datastructures.interfaces.IList#contains(java.lang.Object)
     */
    @Override
    public boolean contains(T other) {
        return this.indexOf(other) != -1;
    }

    /*
     * return the iterator for the container
     * @see datastructures.interfaces.IList#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(this.front);
    }

    /*
     * create the node that tracks the element of the container
     */
    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
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

        // Feel free to add additional constructors or methods to this class.
    }

    /*
     * create an iterator for the container
     */
    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
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
                throw new NoSuchElementException("It is empty");
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