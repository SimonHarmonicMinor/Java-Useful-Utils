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
        assertThrows(IndexOutOfBoundsException.class, () -> list1.get(-4));
    }

    @Test
    void indexOfReturnsCorrectResult() {
        String item1 = "Vasya";
        String item2 = "Sanya";
        String item3 = "Valera";
        List<String> mutable = Arrays.asList(item1, item2, item3);

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
        List<String> mutable = Arrays.asList(item1, item1, item2, item2, item3, item3);

        ImmutableArrayList<String> list = new ImmutableArrayList<>(mutable);
        assertEquals(1, list.lastIndexOf(item1).getAsInt());
        assertEquals(3, list.lastIndexOf(item2).getAsInt());
        assertEquals(5, list.lastIndexOf(item3).getAsInt());
        assertFalse(list.lastIndexOf("Bugaga").isPresent());
    }

    @Test
    void slice1() {
        int item1 = 125124;
        int item2 = -141551;
        int item3 = -4125125;
        List<Integer> mutable = Arrays.asList(item1, item2, item3);

        ImmutableList<Integer> list = new ImmutableArrayList<>(mutable);
        ImmutableList<Integer> subList1 = list.slice(2);
        ImmutableList<Integer> subList2 = list.slice(0);
        ImmutableList<Integer> subList3 = list.slice(-1);
        ImmutableList<Integer> subList4 = list.slice(-2);
        ImmutableList<Integer> subList5 = list.slice(-3);

        assertEquals(1, subList1.size());
        assertEquals(item3, subList1.get(0));

        assertEquals(3, subList2.size());
        assertEquals(item1, subList2.get(0));
        assertEquals(item2, subList2.get(1));
        assertEquals(item3, subList2.get(2));

        assertEquals(1, subList3.size());
        assertEquals(item3, subList3.get(0));

        assertEquals(2, subList4.size());
        assertEquals(item2, subList4.get(0));
        assertEquals(item3, subList4.get(1));

        assertEquals(3, subList5.size());
        assertEquals(item1, subList5.get(0));
        assertEquals(item2, subList5.get(1));
        assertEquals(item3, subList5.get(2));
    }

    @Test
    void slice2() {
        String item1 = "Vasya";
        String item2 = "Sanya";
        String item3 = "Valera";
        List<String> mutable = Arrays.asList(item1, item1, item2, item2, item3, item3);

        ImmutableList<String> list = new ImmutableArrayList<>(mutable);
        ImmutableList<String> subList1 = list.slice(2, 5);
        ImmutableList<String> subList2 = list.slice(0, 6);
        ImmutableList<String> subList3 = list.slice(1, 3);
        ImmutableList<String> subList4 = list.slice(-1, 204);
        ImmutableList<String> subList5 = list.slice(5, 2);

        assertEquals(3, subList1.size());
        assertEquals(item2, subList1.get(0));
        assertEquals(item2, subList1.get(1));
        assertEquals(item3, subList1.get(2));

        assertEquals(6, subList2.size());
        assertEquals(item1, subList2.get(0));
        assertEquals(item1, subList2.get(1));
        assertEquals(item2, subList2.get(2));
        assertEquals(item2, subList2.get(3));
        assertEquals(item3, subList2.get(4));
        assertEquals(item3, subList2.get(5));

        assertEquals(2, subList3.size());
        assertEquals(item1, subList3.get(0));
        assertEquals(item2, subList3.get(1));

        assertEquals(1, subList4.size());
        assertEquals(item3, subList4.get(0));

        assertEquals(3, subList5.size());
        assertEquals(item3, subList5.get(0));
        assertEquals(item3, subList5.get(1));
        assertEquals(item2, subList5.get(2));
    }

    @Test
    void slice3() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        List<String> mutable = Arrays.asList(item1, item2, item3, item4, item5, item6);
        ImmutableList<String> list = new ImmutableArrayList<>(mutable);

        ImmutableList<String> subList1 = list.slice(0, 6, 1);
        ImmutableList<String> subList2 = list.slice(2, 5, 2);
        ImmutableList<String> subList3 = list.slice(-5, -1, 3);
        ImmutableList<String> subList4 = list.slice(-1, -6, -2);
        ImmutableList<String> subList5 = list.slice(-1, -7, -3);
        assertThrows(IllegalArgumentException.class,
                () -> list.slice(0, 1, 0));

        assertEquals(6, subList1.size());
        assertEquals(item1, subList1.get(0));
        assertEquals(item2, subList1.get(1));
        assertEquals(item3, subList1.get(2));
        assertEquals(item4, subList1.get(3));
        assertEquals(item5, subList1.get(4));
        assertEquals(item6, subList1.get(5));

        assertEquals(2, subList2.size());
        assertEquals(item3, subList2.get(0));
        assertEquals(item5, subList2.get(1));

        assertEquals(2, subList3.size());
        assertEquals(item2, subList3.get(0));
        assertEquals(item5, subList3.get(1));

        assertEquals(3, subList4.size());
        assertEquals(item6, subList4.get(0));
        assertEquals(item4, subList4.get(1));
        assertEquals(item2, subList4.get(2));

        assertEquals(2, subList5.size());
        assertEquals(item6, subList5.get(0));
        assertEquals(item3, subList5.get(1));
    }

    @Test
    void step1() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        List<String> mutable = Arrays.asList(item1, item2, item3, item4, item5, item6);
        ImmutableList<String> list = new ImmutableArrayList<>(mutable);

        ImmutableList<String> subList1 = list.step(1);
        ImmutableList<String> subList2 = list.step(2);
        ImmutableList<String> subList3 = list.step(3);
        ImmutableList<String> subList4 = list.step(4);
        ImmutableList<String> subList5 = list.step(-1);
        ImmutableList<String> subList6 = list.step(-2);
        ImmutableList<String> subList7 = list.step(-3);
        assertThrows(IllegalArgumentException.class, () -> list.step(0));

        assertEquals(6, subList1.size());
        assertEquals(item1, subList1.get(0));
        assertEquals(item2, subList1.get(1));
        assertEquals(item3, subList1.get(2));
        assertEquals(item4, subList1.get(3));
        assertEquals(item5, subList1.get(4));
        assertEquals(item6, subList1.get(5));

        assertEquals(3, subList2.size());
        assertEquals(item1, subList2.get(0));
        assertEquals(item3, subList2.get(1));
        assertEquals(item5, subList2.get(2));

        assertEquals(2, subList3.size());
        assertEquals(item1, subList3.get(0));
        assertEquals(item4, subList3.get(1));

        assertEquals(2, subList4.size());
        assertEquals(item1, subList4.get(0));
        assertEquals(item5, subList4.get(1));

        assertEquals(6, subList5.size());
        assertEquals(item6, subList5.get(0));
        assertEquals(item5, subList5.get(1));
        assertEquals(item4, subList5.get(2));
        assertEquals(item3, subList5.get(3));
        assertEquals(item2, subList5.get(4));
        assertEquals(item1, subList5.get(5));

        assertEquals(3, subList6.size());
        assertEquals(item6, subList6.get(0));
        assertEquals(item4, subList6.get(1));
        assertEquals(item2, subList6.get(2));

        assertEquals(2, subList7.size());
        assertEquals(item6, subList7.get(0));
        assertEquals(item3, subList7.get(1));
    }

    @Test
    void step2() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        List<String> mutable = Arrays.asList(item1, item2, item3, item4, item5, item6);
        ImmutableList<String> list = new ImmutableArrayList<>(mutable);

        ImmutableList<String> subList1 = list.step(0, 1);
        ImmutableList<String> subList2 = list.step(1, 2);
        ImmutableList<String> subList3 = list.step(-2, -2);
        ImmutableList<String> subList4 = list.step(-3, -3);

        assertEquals(6, subList1.size());
        assertEquals(item1, subList1.get(0));
        assertEquals(item2, subList1.get(1));
        assertEquals(item3, subList1.get(2));
        assertEquals(item4, subList1.get(3));
        assertEquals(item5, subList1.get(4));
        assertEquals(item6, subList1.get(5));

        assertEquals(3, subList2.size());
        assertEquals(item2, subList2.get(0));
        assertEquals(item4, subList2.get(1));
        assertEquals(item6, subList2.get(2));

        assertEquals(3, subList3.size());
        assertEquals(item5, subList3.get(0));
        assertEquals(item3, subList3.get(1));
        assertEquals(item1, subList3.get(2));

        assertEquals(2, subList4.size());
        assertEquals(item4, subList4.get(0));
        assertEquals(item1, subList4.get(1));
    }

    @Test
    void concatWith() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        List<String> mutable = Arrays.asList(item1, item2, item3, item4, item5, item6);
        ImmutableList<String> list = new ImmutableArrayList<>(mutable);

        assertThrows(NullPointerException.class, () -> list.concatWith(null));
        ImmutableList<String> concat1 = list.concatWith(Arrays.asList(item4, item5));
        ImmutableList<String> concat2 = list.concatWith(Immutable.emptyList());

        assertEquals(8, concat1.size());
        assertEquals(item1, concat1.get(0));
        assertEquals(item2, concat1.get(1));
        assertEquals(item3, concat1.get(2));
        assertEquals(item4, concat1.get(3));
        assertEquals(item5, concat1.get(4));
        assertEquals(item6, concat1.get(5));
        assertEquals(item4, concat1.get(6));
        assertEquals(item5, concat1.get(7));

        assertEquals(6, concat2.size());
        assertEquals(item1, concat2.get(0));
        assertEquals(item2, concat2.get(1));
        assertEquals(item3, concat2.get(2));
        assertEquals(item4, concat2.get(3));
        assertEquals(item5, concat2.get(4));
        assertEquals(item6, concat2.get(5));
    }

    @Test
    void zipWith() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        ImmutableList<String> list1 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );
        ImmutableList<String> list2 = new ImmutableArrayList<>(
                Arrays.asList(item4, item5, item6, item4)
        );
        assertThrows(NullPointerException.class, () -> list1.zipWith(null));

        ImmutableList<Pair<String, String>> zipped =
                list1.zipWith(list2);
        assertEquals(4, zipped.size());
        assertEquals(Pair.of(item1, item4), zipped.get(0));
        assertEquals(Pair.of(item2, item5), zipped.get(1));
        assertEquals(Pair.of(item3, item6), zipped.get(2));
        assertEquals(Pair.of(null, item4), zipped.get(3));
    }

    @Test
    void zipWithNext() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        ImmutableList<String> list1 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        ImmutableList<Pair<String, String>> zipped = list1.zipWithNext();

        assertEquals(5, zipped.size());
        assertEquals(Pair.of(item1, item2), zipped.get(0));
        assertEquals(Pair.of(item2, item3), zipped.get(1));
        assertEquals(Pair.of(item3, item4), zipped.get(2));
        assertEquals(Pair.of(item4, item5), zipped.get(3));
        assertEquals(Pair.of(item5, item6), zipped.get(4));
    }

    @Test
    void map() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        assertThrows(NullPointerException.class, () -> list.map(null));

        ImmutableList<String> mapped = list
                .map(String::valueOf)
                .map(s -> s + "1");

        assertEquals(3, mapped.size());
        assertEquals(item1 + "1", mapped.get(0));
        assertEquals(item2 + "1", mapped.get(1));
        assertEquals(item3 + "1", mapped.get(2));
    }

    @Test
    void flatMap() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        assertThrows(NullPointerException.class, () -> list.flatMap(null));

        ImmutableList<String> mapped = list
                .flatMap(item -> Arrays.asList(item1, item2, item3, item))
                .map(String::valueOf);
        assertEquals(12, mapped.size());
        assertEquals(item1 + "", mapped.get(0));
        assertEquals(item2 + "", mapped.get(1));
        assertEquals(item3 + "", mapped.get(2));
        assertEquals(item1 + "", mapped.get(3));

        assertEquals(item1 + "", mapped.get(4));
        assertEquals(item2 + "", mapped.get(5));
        assertEquals(item3 + "", mapped.get(6));
        assertEquals(item2 + "", mapped.get(7));

        assertEquals(item1 + "", mapped.get(8));
        assertEquals(item2 + "", mapped.get(9));
        assertEquals(item3 + "", mapped.get(10));
        assertEquals(item3 + "", mapped.get(11));
    }

    @Test
    void mapIndexed() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        assertThrows(NullPointerException.class, () -> list.mapIndexed(null));

        ImmutableList<String> mapped = list
                .mapIndexed((index, val) -> String.valueOf(val) + index);

        assertEquals(3, mapped.size());
        assertEquals(item1 + "0", mapped.get(0));
        assertEquals(item2 + "1", mapped.get(1));
        assertEquals(item3 + "2", mapped.get(2));
    }

    @Test
    void flatMapIndexed() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        assertThrows(NullPointerException.class, () -> list.flatMapIndexed(null));

        ImmutableList<String> mapped = list
                .flatMapIndexed((index, val) -> Arrays.asList(item1, item2, item3, index))
                .map(String::valueOf);
        assertEquals(12, mapped.size());
        assertEquals(item1 + "", mapped.get(0));
        assertEquals(item2 + "", mapped.get(1));
        assertEquals(item3 + "", mapped.get(2));
        assertEquals("0", mapped.get(3));

        assertEquals(item1 + "", mapped.get(4));
        assertEquals(item2 + "", mapped.get(5));
        assertEquals(item3 + "", mapped.get(6));
        assertEquals("1", mapped.get(7));

        assertEquals(item1 + "", mapped.get(8));
        assertEquals(item2 + "", mapped.get(9));
        assertEquals(item3 + "", mapped.get(10));
        assertEquals("2", mapped.get(11));
    }
}