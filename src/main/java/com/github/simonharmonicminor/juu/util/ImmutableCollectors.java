package com.github.simonharmonicminor.juu.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ImmutableCollectors {
    static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    static final Set<Collector.Characteristics> CH_UNORDERED_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));

    public static <T, C extends ImmutableCollection<T>>
    Collector<T, ?, C> toCollection(Function<Iterable<T>, C> collectionFactory) {
        return new CollectorImpl<>(
                (Supplier<List<T>>) ArrayList::new,
                List::add,
                (r1, r2) -> {
                    r1.addAll(r2);
                    return r1;
                },
                collectionFactory::apply,
                CH_ID
        );
    }

    public static <T> Collector<T, ?, ImmutableList<T>> toList() {
        return new CollectorImpl<>(
                (Supplier<List<T>>) ArrayList::new,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                ImmutableArrayList::new,
                CH_ID);
    }

    public static <T> Collector<T, ?, ImmutableSet<T>> toSet() {
        return new CollectorImpl<>(
                (Supplier<List<T>>) ArrayList::new,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                ImmutableHashSet::new,
                CH_UNORDERED_ID);
    }


    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }
}
