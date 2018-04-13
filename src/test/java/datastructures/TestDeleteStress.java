package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {

    /*
     * create the list size of cap 
     */
    public IList<Integer> add() {
        int cap = 500000;
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
        IList<Integer> list = this.add();
        int cap = list.size();
        for (int i = 0; i < cap; i++) {
           assertEquals(list.get(0), list.delete(0));
        }
        assertEquals(list.size(),0); 
    }
    
    @Test(timeout=15*SECOND)
    public void testDeleteBackIsEfficient() {
        IList<Integer> list = this.add();
        int cap = list.size();
        for (int i = cap - 1; i > 0; i--) {
            assertEquals(list.get(i), list.delete(i));
        }
        assertEquals(list.size(),1); 
    }
    
    @Test(timeout= 15 * SECOND)
    public void testDeleteMiddleIsEfficient() {
        IList<Integer> list = this.add();
        int cap = list.size();
        for (int i = 0; i < cap / 2 - 1; i++) {
            list.delete(list.size() / 2 + (list.size() % 2));
            assertEquals(list.size(), cap - i - 1);
            System.out.println("idx: " + i + ", size" + list.size());
        }
        System.out.println(list.size());
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeleteNearEndIsEfficient() {
        IList<Integer> list = this.add();
        int cap = list.size();
        for (int i = 0; i < cap - 2; i++) {
            list.delete(list.size() - 2);
        }
        assertEquals(list.size(), 2);
    }
}
