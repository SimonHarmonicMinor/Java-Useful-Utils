package com.kirekov.juu.collection.immutable;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void filter() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        ImmutableList<String> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        ImmutableList<String> filtered1 = list.filter(val -> false);
        ImmutableList<String> filtered2 = list.filter(val -> true);
        ImmutableList<String> filtered3 = list.filter(
                Predicate.isEqual(item1)
                        .or(Predicate.isEqual(item4))
        );

        assertTrue(filtered1.isEmpty());

        assertEquals(6, filtered2.size());
        assertEquals(item1, filtered2.get(0));
        assertEquals(item2, filtered2.get(1));
        assertEquals(item3, filtered2.get(2));
        assertEquals(item4, filtered2.get(3));
        assertEquals(item5, filtered2.get(4));
        assertEquals(item6, filtered2.get(5));

        assertEquals(2, filtered3.size());
        assertEquals(item1, filtered3.get(0));
        assertEquals(item4, filtered3.get(1));
    }

    @Test
    void filterIndexed() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        ImmutableList<String> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        ImmutableList<String> filtered1 = list.filterIndexed((index, val) -> false);
        ImmutableList<String> filtered2 = list.filterIndexed((index, val) -> true);
        ImmutableList<String> filtered3 = list.filterIndexed(
                (index, val) -> val.startsWith("6") || index % 2 == 0
        );


        assertTrue(filtered1.isEmpty());

        assertEquals(6, filtered2.size());
        assertEquals(item1, filtered2.get(0));
        assertEquals(item2, filtered2.get(1));
        assertEquals(item3, filtered2.get(2));
        assertEquals(item4, filtered2.get(3));
        assertEquals(item5, filtered2.get(4));
        assertEquals(item6, filtered2.get(5));

        assertEquals(4, filtered3.size());
        assertEquals(item1, filtered3.get(0));
        assertEquals(item3, filtered3.get(1));
        assertEquals(item5, filtered3.get(2));
        assertEquals(item6, filtered3.get(3));
    }

    @Test
    void forEachIndexed() {
        String item1 = "1dasdgsdag";
        String item2 = "2dasdgsdag";
        String item3 = "3dasdgsdag";
        String item4 = "4dasdgsdag";
        String item5 = "5dasdgsdag";
        String item6 = "6dasdgsdag";
        ImmutableList<String> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        BiConsumer<Integer, String> consumer = mock(BiConsumer.class);

        assertThrows(NullPointerException.class, () -> list.forEachIndexed(null));
        list.forEachIndexed(consumer);

        verify(consumer, times(1)).accept(0, item1);
        verify(consumer, times(1)).accept(1, item2);
        verify(consumer, times(1)).accept(2, item3);
        verify(consumer, times(1)).accept(3, item4);
        verify(consumer, times(1)).accept(4, item5);
        verify(consumer, times(1)).accept(5, item6);
    }

    @Test
    void sorted() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4)
        );

        ImmutableList<Integer> naturalSort = list.sorted(
                Comparator.comparingInt(x -> x)
        );
        ImmutableList<Integer> reversedSort = list.sorted(
                (x1, x2) -> x2 - x1
        );

        assertEquals(4, naturalSort.size());
        assertEquals(item2, naturalSort.get(0));
        assertEquals(item3, naturalSort.get(1));
        assertEquals(item1, naturalSort.get(2));
        assertEquals(item4, naturalSort.get(3));

        assertEquals(4, reversedSort.size());
        assertEquals(item4, reversedSort.get(0));
        assertEquals(item1, reversedSort.get(1));
        assertEquals(item3, reversedSort.get(2));
        assertEquals(item2, reversedSort.get(3));
    }

    @Test
    void limit() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        int item6 = -12415;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        assertThrows(IllegalArgumentException.class, () -> list.limit(-1));
        ImmutableList<Integer> limit1 = list.limit(0);
        ImmutableList<Integer> limit2 = list.limit(1);
        ImmutableList<Integer> limit3 = list.limit(3);
        ImmutableList<Integer> limit4 = list.limit(6);
        ImmutableList<Integer> limit5 = list.limit(16);

        assertTrue(limit1.isEmpty());

        assertEquals(1, limit2.size());
        assertEquals(item1, limit2.get(0));

        assertEquals(3, limit3.size());
        assertEquals(item1, limit3.get(0));
        assertEquals(item2, limit3.get(1));
        assertEquals(item3, limit3.get(2));

        assertEquals(6, limit4.size());
        assertEquals(item1, limit4.get(0));
        assertEquals(item2, limit4.get(1));
        assertEquals(item3, limit4.get(2));
        assertEquals(item4, limit4.get(3));
        assertEquals(item5, limit4.get(4));
        assertEquals(item6, limit4.get(5));

        assertEquals(6, limit5.size());
        assertEquals(item1, limit5.get(0));
        assertEquals(item2, limit5.get(1));
        assertEquals(item3, limit5.get(2));
        assertEquals(item4, limit5.get(3));
        assertEquals(item5, limit5.get(4));
        assertEquals(item6, limit5.get(5));
    }

    @Test
    void skip() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        int item6 = -12415;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        assertThrows(IllegalArgumentException.class, () -> list.skip(-1));
        ImmutableList<Integer> skip1 = list.skip(0);
        ImmutableList<Integer> skip2 = list.skip(1);
        ImmutableList<Integer> skip3 = list.skip(3);
        ImmutableList<Integer> skip4 = list.skip(6);
        ImmutableList<Integer> skip5 = list.skip(16);

        assertEquals(6, skip1.size());
        assertEquals(item1, skip1.get(0));
        assertEquals(item2, skip1.get(1));
        assertEquals(item3, skip1.get(2));
        assertEquals(item4, skip1.get(3));
        assertEquals(item5, skip1.get(4));
        assertEquals(item6, skip1.get(5));

        assertEquals(5, skip2.size());
        assertEquals(item2, skip2.get(0));
        assertEquals(item3, skip2.get(1));
        assertEquals(item4, skip2.get(2));
        assertEquals(item5, skip2.get(3));
        assertEquals(item6, skip2.get(4));

        assertEquals(3, skip3.size());
        assertEquals(item4, skip3.get(0));
        assertEquals(item5, skip3.get(1));
        assertEquals(item6, skip3.get(2));

        assertTrue(skip4.isEmpty());

        assertTrue(skip5.isEmpty());
    }

    @Test
    void reversed() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        int item6 = -12415;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );

        ImmutableList<Integer> reversed = list.reversed();

        assertEquals(6, reversed.size());
        assertEquals(item6, reversed.get(0));
        assertEquals(item5, reversed.get(1));
        assertEquals(item4, reversed.get(2));
        assertEquals(item3, reversed.get(3));
        assertEquals(item2, reversed.get(4));
        assertEquals(item1, reversed.get(5));
    }

    @Test
    void toList() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        int item6 = -12415;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );
        ImmutableList<Integer> newList = list.toList();
        assertEquals(list, newList);
    }

    @Test
    void toMutableList() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        int item6 = -12415;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item6)
        );
        List<Integer> mutable = list.toMutableList();

        assertEquals(list.size(), mutable.size());
        assertEquals(item1, mutable.get(0));
        assertEquals(item2, mutable.get(1));
        assertEquals(item3, mutable.get(2));
        assertEquals(item4, mutable.get(3));
        assertEquals(item5, mutable.get(4));
        assertEquals(item6, mutable.get(5));

        mutable.clear();
        assertEquals(6, list.size());
    }

    @Test
    void toSet() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item5)
        );
        ImmutableSet<Integer> set = list.toSet();

        assertEquals(5, set.size());
        assertTrue(set.contains(item1));
        assertTrue(set.contains(item2));
        assertTrue(set.contains(item3));
        assertTrue(set.contains(item4));
        assertTrue(set.contains(item5));
    }

    @Test
    void toMutableSet() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item5)
        );
        Set<Integer> set = list.toMutableSet();

        assertEquals(5, set.size());
        assertTrue(set.contains(item1));
        assertTrue(set.contains(item2));
        assertTrue(set.contains(item3));
        assertTrue(set.contains(item4));
        assertTrue(set.contains(item5));

        set.clear();
        assertEquals(6, list.size());
    }

    @Test
    void stream() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item5)
        );

        Stream<Integer> stream = list.stream();
        assertFalse(stream.isParallel());
    }


    @Test
    void parallelStream() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5, item5)
        );

        Stream<Integer> stream = list.parallelStream();
        assertTrue(stream.isParallel());
    }

    @Test
    void hashCodeTest() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list1 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        ImmutableList<Integer> list2 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void toStringTest() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list1 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        ImmutableList<Integer> list2 = new ImmutableArrayList<>(Collections.emptyList());

        assertNotNull(list1.toString());
        assertNotNull(list2.toString());
    }

    @Test
    void notContains() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        assertTrue(list.notContains("dasd"));
        assertFalse(list.notContains(item1));
        assertTrue(list.notContains(-1));
    }

    @Test
    void containsAll() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 0;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        List<Integer> containing = Arrays.asList(item1, item2);
        List<Integer> notContaining = Arrays.asList(item1, item2, item3, -item1);

        assertTrue(list.containsAll(containing));
        assertFalse(list.containsAll(notContaining));
    }

    @Test
    void containsAny() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        List<Integer> containing = Arrays.asList(item1, item2, -item3);
        List<Integer> notContaining = Arrays.asList(-item1, -item2, -item3);

        assertTrue(list.containsAny(containing));
        assertFalse(list.containsAny(notContaining));
    }

    @Test
    void allMatch() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        assertTrue(list.allMatch(x -> true));
        assertTrue(list.allMatch(x -> x != 0));
        assertFalse(list.allMatch(x -> x > 0));
    }

    @Test
    void anyMatch() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        assertTrue(list.anyMatch(x -> true));
        assertTrue(list.anyMatch(x -> x > 0));
        assertTrue(list.anyMatch(x -> x == 1));
        assertFalse(list.anyMatch(x -> x == 0));
    }

    @Test
    void noneMatch() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        int item4 = 41151;
        int item5 = 44124;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item4, item5)
        );

        assertFalse(list.noneMatch(x -> true));
        assertFalse(list.noneMatch(x -> x > 0));
        assertFalse(list.noneMatch(x -> x == 1));
        assertTrue(list.noneMatch(x -> x == 0));
    }

    @Test
    void reduce2() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        int identity = 12414;
        int reduced = list.reduce(identity, Integer::sum);
        assertEquals(identity + item1 + item2 + item3, reduced);
    }

    @Test
    void reduce1() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );
        ImmutableList<Integer> emptyList = new ImmutableArrayList<>(
                Collections.emptyList()
        );

        Optional<Integer> reduced = list.reduce(Integer::sum);

        assertFalse(emptyList.reduce(Integer::sum).isPresent());
        assertTrue(reduced.isPresent());
        assertEquals(item1 + item2 + item3, reduced.get());
    }

    @Test
    void min() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        Optional<Integer> min = list.min(Comparator.comparingInt(x -> x));

        assertTrue(min.isPresent());
        assertEquals(item2, min.get());

        Optional<Integer> minWithLength = list.min(
                Comparator.comparingInt(x -> String.valueOf(x).length())
        );

        assertTrue(minWithLength.isPresent());
        assertEquals(item3, minWithLength.get());
    }

    @Test
    void max() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );

        Optional<Integer> max = list.max(Comparator.comparingInt(x -> x));

        assertTrue(max.isPresent());
        assertEquals(item1, max.get());

        Optional<Integer> maxWithLength = list.max(
                Comparator.comparingInt(x -> String.valueOf(x).length())
        );

        assertTrue(maxWithLength.isPresent());
        assertEquals(item2, maxWithLength.get());
    }

    @Test
    void findFirst0() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );
        ImmutableList<Integer> emptyList = new ImmutableArrayList<>(
                Collections.emptyList()
        );

        Optional<Integer> firstPresent = list.findFirst();
        Optional<Integer> firstEmpty = emptyList.findFirst();

        assertTrue(firstPresent.isPresent());
        assertEquals(item1, firstPresent.get());

        assertFalse(firstEmpty.isPresent());
    }

    @Test
    void findFirst1() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );
        ImmutableList<Integer> emptyList = new ImmutableArrayList<>(
                Collections.emptyList()
        );

        Optional<Integer> firstPresent = list.findFirst(x -> x == 1);
        Optional<Integer> firstEmpty = emptyList.findFirst(x -> x == 12);

        assertTrue(firstPresent.isPresent());
        assertEquals(item3, firstPresent.get());

        assertFalse(firstEmpty.isPresent());
    }

    @Test
    void equalsAndNotEquals() {
        int item1 = 1251;
        int item2 = -14214;
        int item3 = 1;
        ImmutableList<Integer> list1 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );
        ImmutableList<Integer> list2 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3)
        );
        ImmutableList<Integer> list3 = new ImmutableArrayList<>(
                Arrays.asList(item1, item2, item3, item3)
        );

        assertEquals(list1, list2);
        assertNotEquals(list1, list3);
    }
}