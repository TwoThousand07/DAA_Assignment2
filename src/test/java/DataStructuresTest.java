import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataStructuresTest {

    @Test
    void testDynamicArrayEdgeCases() {
        DynamicArray da = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> da.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> da.remove(0));

        da.add(10);
        assertEquals(10, da.get(0));
        da.add(0, 5);
        assertEquals(5, da.get(0));
        assertEquals(10, da.get(1));

        da.remove(0);
        assertEquals(10, da.get(0));
        assertEquals(1, da.size());
    }

    @Test
    void testMyLinkedListEdgeCases() {
        MyLinkedList list = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));

        list.add(100);
        list.add(200);
        assertTrue(list.contains(100));
        assertFalse(list.contains(300));

        list.remove(0);
        assertEquals(200, list.get(0));
    }

    @Test
    void testMinHeapSortedOutputAndExceptions() {
        MinHeap heap = new MinHeap(5);
        assertThrows(IllegalStateException.class, heap::peekMin);
        assertThrows(IllegalStateException.class, heap::extractMin);

        heap.insert(50);
        heap.insert(10);
        heap.insert(30);
        heap.insert(5);

        assertEquals(5, heap.peekMin());
        assertEquals(5, heap.extractMin());
        assertEquals(10, heap.extractMin());
        assertEquals(30, heap.extractMin());
        assertEquals(50, heap.extractMin());
    }
}
