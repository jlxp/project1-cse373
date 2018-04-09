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

    public IList<Integer> list = new DoubleLinkedList<>();
    
    public void add() {
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
    }
    
    // Test delete is efficient
    @Test(timeout=SECOND)
    public void testDeleteFrontIsEfficient() {
        add();
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), list.delete(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteBackIsEfficient() {
        add();
        for (int i = list.size(); i > list.size(); i--) {
            assertEquals(list.get(i), list.delete(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteMiddleIsEfficient() {
        add();
        for (int i = list.size() / 2 + 1; i < list.size(); i++) {
            assertEquals(list.get(i), list.delete(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testDeleteNearEndIsEfficient() {
        add();
        for (int i = 0; i < list.size(); i++) {
            list.delete(list.size() - 2);
        }
        
    }
}
