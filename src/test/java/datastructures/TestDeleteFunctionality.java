package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.fail;

import java.util.Iterator;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
   
    @Test(timeout=SECOND)
    public void basicTestDelete() {
        IList<String> list = this.makeBasicList();
        
        String temp = list.delete(1); 
        assertListMatches(new String[] {"a", "c"}, list);
        assertEquals("b", temp); 
        assertEquals(2, list.size());
    }
    
    @Test(timeout=SECOND)
    public void basicTestDelete2() {
        IList<String> list = this.makeBasicList();
        list.add("d");
        String temp = list.delete(2); 
        assertListMatches(new String[] {"a", "b", "d"}, list);
        assertEquals("c", temp); 
        assertEquals(3, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOnFront() {
        IList<String> list = this.makeBasicList();
        
        String temp = list.delete(0);
        assertListMatches(new String[] {"b", "c"}, list);
        assertEquals("a", temp); 
        assertEquals(2, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOnBack() {
        IList<String> list = this.makeBasicList();
        
        String temp = list.delete(list.size() - 1);
        assertListMatches(new String[] {"a", "b"}, list);
        assertEquals("c", temp); 
        assertEquals(2, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testDeleteSingle() {
        IList<String> list = new DoubleLinkedList<>();
        
        list.add("a");
        
        list.delete(0);
        
        assertEquals(0, list.size());
        assertListMatches(new String[] {}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteUpperOOBException() {
        IList<String> list = this.makeBasicList();
        
        try {
            list.delete(4);
            fail("Expected Out of Bounds Exception");
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }   
        
        
    }
    
    @Test(timeout=SECOND)
    public void testDeleteLowerOOBException() {
        IList<String> list = this.makeBasicList();
        
        try {
            list.delete(-1);
            fail("Expected Out of Bounds Exception");
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }          
    }
    
    @Test(timeout=SECOND)
    public void testDeleteMultiple() {
        IList<String> list = this.makeBasicList();
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        assertEquals("d", list.delete(3));
        assertEquals("e", list.delete(3));
        assertEquals("g", list.delete(4));
        assertEquals("h", list.delete(4));
        try {
            list.contains("h"); // this verifies that the index was deleted
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testBackField() {
        IList<String> list = this.makeBasicList();
        int backIndex = list.size() - 1;
        assertEquals("c", list.delete(list.size() - 1));
        try {
            list.delete(backIndex); // this verifies that the index was deleted
            fail("Expected Out of Bounds Exception");
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }
        assertEquals("b", list.remove()); 
    }
    
    @Test(timeout=SECOND)
    public void testBackField2() {
        IList<String> list = this.makeBasicList();
        int backIndex = list.size() - 1;
        assertEquals("c", list.delete(list.size() - 1));
        try {
            list.contains("c"); // this verifies that the index was deleted
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }
        assertEquals("b", list.remove()); 
    }
    @Test(timeout=SECOND)
    public void testNext() {
        IList<String> list = this.makeBasicList();
        Iterator<String> iter = list.iterator();
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        list.delete(3);
        String[] arrList = new String[] {"a", "b", "c", "e", "f", "g", "h"};
        for (int i = 0; i < list.size(); i++) {
            assertEquals(arrList[i], iter.next());
        }
        
    }
}
