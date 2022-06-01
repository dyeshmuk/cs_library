import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<HashFunction<Integer>> hashFunctions = new ArrayList<>();
        hashFunctions.add(new HashFunction<Integer>() {
            @Override
            public int hash(Integer obj) {
                int hash = 7;
                hash = 31 * hash + (obj == null ? 0 : obj.hashCode());
                return Math.abs(hash);
            }
        });
        hashFunctions.add(new HashFunction<Integer>() {
            @Override
            public int hash(Integer obj) {
                long hash = obj.hashCode() + 911382323L;
                return Math.abs((int) (hash % 972663749L));
            }
        });
        hashFunctions.add(new HashFunction<Integer>() {
            @Override
            public int hash(Integer obj) {
                int hash = obj.hashCode();
                hash += (hash << 10);
                hash ^= (hash >> 6);
                hash += hash << 13;
                hash ^= hash >> 11;
                hash += hash << 15;
                return Math.abs(hash);
            }
        });

        int capacity = 1000;
        BloomFilter<Integer> filter = new BloomFilter<>(capacity, hashFunctions);
        for (int i = 0; i < 400; i++) {
            filter.add(i);
        }

        System.out.println(filter.mightContain(2)); // true positive
        System.out.println(filter.mightContain(89)); // true positive
        System.out.println(!filter.mightContain(971)); // true negative
        System.out.println(!filter.mightContain(888)); // true negative
    }

    private static class BloomFilter<T> {
        private final int capacity;
        private final int[] set;
        private final List<HashFunction<T>> hashFunctions;

        BloomFilter(int capacity, List<HashFunction<T>> hashFunctions) {
            this.capacity = capacity;
            this.set = new int[capacity];
            if (hashFunctions == null || hashFunctions.isEmpty()) {
                throw new RuntimeException("At least one hash function is required.");
            }
            this.hashFunctions = hashFunctions;
        }

        // inserts the value into the set
        public void add(T value) {
            for (HashFunction<T> f : hashFunctions) {
                this.set[f.hash(value) % this.capacity] = 1;
            }
        }

        // Returns false, when the value is definitely not in the set. This is a true negative.
        // and true, if it might be in the set. This could be a false positive, or it could be a true positive.
        public boolean mightContain(T value) {
            for (HashFunction<T> f : hashFunctions) {
                if (this.set[f.hash(value) % this.capacity] == 0) {
                    return false;
                }
            }

            return true;
        }
    }

    interface HashFunction<T> {
        int hash(T obj);
    }
}
