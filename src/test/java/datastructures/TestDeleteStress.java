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

    // Test delete is efficient
    @Test(timeout=SECOND)
    public void testAddAndDeleteIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
        for (int i = cap - 1 / 2; i < cap; i++) {
            assertEquals(list.get(i), list.delete(i));
        }
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), list.delete(i));
        }
    }
}
