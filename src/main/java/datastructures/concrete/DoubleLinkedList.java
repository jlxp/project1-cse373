package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Parameter must not be null");
        }
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
    
    private void testIndexOutOfBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Wrong Index");
        }
    }
    
    @Override
    public T get(int index) {
        testIndexOutOfBounds(index);
        
        if (index == 0) {
            return this.front.data;
        } else if (index == this.size -1) {
            return this.back.data;
        } else {
            int count = 0;
            Node<T> current = this.front;
            while (current.next != null && count < index) {
                current = current.next;
                count++;
            }
            return (T) current.data;
        }
    }

    @Override
    public void set(int index, T item) {
        testIndexOutOfBounds(index);
        
        if (this.front.next == null) {
            this.front = new Node<T>(null, item, null);
        } else {
            Node<T> current = this.front;
            Node<T> temp = new Node<T>(item);
            if (index == 0) {
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
                int count = 0;
                while (current.next != null && count < index) {
                    current = current.next;
                    count++;
                }
                temp.prev = current.prev;
                temp.next = current.next; 
                current.prev.next = temp;
                current.next.prev = temp;
                current.prev = null;
                current.next = null;
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size + 1) {
            throw new IndexOutOfBoundsException("Wrong Index");
        }
        
        if (this.front == null || index == this.size) {
            this.add(item);
        } else {
            if (index == 0) {
                Node<T> temp = new Node<T>(null, item, this.front);
                this.front.prev = temp;
                this.front = temp; 
            } else if (index <= this.size / 2 + 1) {
                Node<T> current = this.front;
                int count = 0;
                while (current.next != null && count < index) {
                    current = current.next;
                    count++;
                }
                Node<T> temp = new Node<T>(current.prev, item, current);
                current.prev = temp;
                temp.prev.next = temp;
            } else {
                Node<T> current = this.back;
                int count = this.size - 1;
                while (current.prev != null && count > index) {
                    current = current.prev;
                    count--;
                }
                Node<T> temp = new Node<T>(current, item, current.next);
                current.next = temp;
                temp.next.prev = temp;
            }
            this.size++;
        }
    }

    @Override
    public T delete(int index) {
        testIndexOutOfBounds(index);
        this.size--;
        
        if (index == size - 1) {
            return this.remove();
        }
        
        if (index == 0) {
            T temp = this.front.data;
            this.front = this.front.next;
            this.front.prev.next = null;
            this.front.prev = null;
            return (T) temp; 
        }  else if (index <= this.size / 2 + 1) {
            int count = 0;
            Node<T> current = this.front;
            while (current.next != null && count < index) {
                current = current.next;
                count++;
            }
            Node<T> temp = current;
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current.next = null;
            current.prev = null;
            return (T) temp.data;
        } else {
            int count = this.size - 1;
            Node<T> current = this.back;
            while (current.prev != null && count > index) {
                current = current.prev;
                count--;
            }
            Node<T> temp = current;
            current.next.prev = current.prev;
            current.prev.next = current.next;
            current.prev = null;
            current.next = null;
            return (T) temp.data;
        }
    }

    @Override
    public int indexOf(T item) {
        Node<T> current = this.front;
        int idx = 0;
        while (current != null) {
            if (current.data == null ||current.data.equals(item)) {
                return idx;
            }
            current = current.next;
            idx++;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return this.indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

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
                throw new NoSuchElementException("empty");
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
