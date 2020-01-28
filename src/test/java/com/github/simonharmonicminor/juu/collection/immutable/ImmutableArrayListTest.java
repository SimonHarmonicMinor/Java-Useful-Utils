package com.github.simonharmonicminor.juu.collection.immutable;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableArrayListTest {

    @Test
    void instantiateWithCloning() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        ImmutableArrayList<Integer> immutableArrayList = new ImmutableArrayList<>(arrayList);
        assertEquals(3, immutableArrayList.size());
        assertEquals(1, immutableArrayList.get(0));
        assertEquals(2, immutableArrayList.get(1));
        assertEquals(3, immutableArrayList.get(2));

        arrayList.clear();

        assertEquals(3, immutableArrayList.size());
        assertEquals(1, immutableArrayList.get(0));
        assertEquals(2, immutableArrayList.get(1));
        assertEquals(3, immutableArrayList.get(2));
    }

    @Test
    void instantiateWithoutCloning() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        ImmutableArrayList<Integer> immutableArrayList = new ImmutableArrayList<>(arrayList, false);
        assertEquals(3, immutableArrayList.size());
        assertEquals(1, immutableArrayList.get(0));
        assertEquals(2, immutableArrayList.get(1));
        assertEquals(3, immutableArrayList.get(2));

        arrayList.clear();

        assertEquals(0, immutableArrayList.size());
    }

    @Test
    void instantiateWithImmutableArrayList() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        ImmutableArrayList<Integer> list1 = new ImmutableArrayList<>(arrayList);
        ImmutableArrayList<Integer> list2 = new ImmutableArrayList<>(list1);
        assertEquals(list1, list2);
        assertEquals(3, list1.size());
        assertEquals(1, list1.get(0));
        assertEquals(2, list1.get(1));
        assertEquals(3, list1.get(2));
    }

    @Test
    void instantiateWithIterable() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        ImmutableArrayList<Integer> list1 = new ImmutableArrayList<>(set);
        assertEquals(3, list1.size());
        assertTrue(list1.contains(1));
        assertTrue(list1.contains(2));
        assertTrue(list1.contains(3));
    }

    @Test
    void getThrowsIndexOutOfBoundsException() {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        ImmutableArrayList<Integer> list1 = new ImmutableArrayList<>(set);
        assertThrows(IndexOutOfBoundsException.class, () -> list1.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list1.get(-1));
    }

    @Test
    void indexOfReturnsCorrectResult() {
        String item1 = "Vasya";
        String item2 = "Sanya";
        String item3 = "Valera";
        List<String> mutable = new LinkedList<>();
        mutable.add(item1);
        mutable.add(item2);
        mutable.add(item3);

        ImmutableArrayList<String> list = new ImmutableArrayList<>(mutable);
        assertEquals(0, list.indexOf(item1).getAsInt());
        assertEquals(1, list.indexOf(item2).getAsInt());
        assertEquals(2, list.indexOf(item3).getAsInt());
        assertFalse(list.indexOf("Bugaga").isPresent());
    }

    @Test
    void lastIndexOfReturnsCorrectResult() {
        String item1 = "Vasya";
        String item2 = "Sanya";
        String item3 = "Valera";
        List<String> mutable = new LinkedList<>();
        mutable.add(item1);
        mutable.add(item1);
        mutable.add(item2);
        mutable.add(item2);
        mutable.add(item3);
        mutable.add(item3);

        ImmutableArrayList<String> list = new ImmutableArrayList<>(mutable);
        assertEquals(1, list.lastIndexOf(item1).getAsInt());
        assertEquals(3, list.lastIndexOf(item2).getAsInt());
        assertEquals(5, list.lastIndexOf(item3).getAsInt());
        assertFalse(list.lastIndexOf("Bugaga").isPresent());
    }

    @Test
    void subListReturnsCorrectOne() {
        String item1 = "Vasya";
        String item2 = "Sanya";
        String item3 = "Valera";
        List<String> mutable = new LinkedList<>();
        mutable.add(item1);
        mutable.add(item1);
        mutable.add(item2);
        mutable.add(item2);
        mutable.add(item3);
        mutable.add(item3);

        ImmutableList<String> list = new ImmutableArrayList<>(mutable);
        ImmutableList<String> subList1 = list.slice(2, 5);
        ImmutableList<String> subList2 = list.slice(0, 6);
        ImmutableList<String> subList3 = list.slice(1, 3);
        ImmutableList<String> subList5 = list.slice(-1, 204);
        ImmutableList<String> subList6 = list.slice(5, 2);

    }
}