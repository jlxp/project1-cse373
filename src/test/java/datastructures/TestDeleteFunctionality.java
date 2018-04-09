package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        IList<String> list = makeBasicList();
        
        list.delete(1); 
        assertListMatches(new String[] {"a", "c"}, list);
        assertEquals(2, list.size());
    }
    @Test(timeout=SECOND)
    public void testDeleteOnFront() {
        IList<String> list = makeBasicList();
        
        list.delete(0);
        assertListMatches(new String[] {"b", "c"}, list);
        assertEquals(2, list.size());
    }
    @Test(timeout=SECOND)
    public void testDeleteSingle() {
        IList<String> list = new DoubleLinkedList<>();
        
        list.add("a");
        
        list.delete(0);
        
        assertEquals(0, list.size());
    }
    @Test(timeout=SECOND)
    public void testDeleteOOBException() {
        IList<String> list = makeBasicList();
        
        try {
            list.delete(4);
            fail("Expected Out of Bounds Exception");
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }
      
        
    }
    
    protected IList<String> makeBasicList() {
        IList<String> list = new DoubleLinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");

        return list;
    }
}
