package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {

    /*
     * create the list size of cap 
     */

    public IList<Integer> add(int cap) {
        IList<Integer> list = new DoubleLinkedList<>();
        
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        list.add(1);
        list.add(3);
        return list; 
    }
    
    // Test delete is efficient
    @Test(timeout=15*SECOND)
    public void testDeleteFrontIsEfficient() {
        int cap = 500000;
        IList<Integer> list = this.add(cap);
        
        for (int i = 0; i < cap; i++) {
           assertEquals(list.get(0), list.delete(0));
        }
        assertEquals(list.size(), 2);
        assertEquals(list.get(0), 1);
    }
    
    @Test(timeout=15*SECOND)
    public void testDeleteBackIsEfficient() {
        int cap = 500000;
        IList<Integer> list = this.add(cap);
        
        for (int i = 0; i < cap; i++) {
            assertEquals(list.get(list.size() - 1), list.delete(list.size() - 1));
        }
        assertEquals(list.size(), 2); 
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeleteNearEndIsEfficient() {
        int cap = 500000;
        IList<Integer> list = this.add(cap);
       
        for (int i = 0; i < cap - 2; i++) {
            list.delete(list.size() - 2);
        }
        assertEquals(list.size() - 2, 2);
    }
}
