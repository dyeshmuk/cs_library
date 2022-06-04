## Bloom Filter.


### Abstract
The bloom filter is a space-efficient data structure that lets you quickly check whether or not an item is in a set.

The cost paid for this efficiency is that the Bloom filter is a probabilistic data structure: it tells us that the element either `definitely is not(true negative)` in the set or `may be(false negative/true positive)` in the set.


### Time and space complexity
* insert -> `O(1)`
* lookup -> `O(1)`

* Space -> `O(1)`


### Pros
* Memory-efficient. Bloom filter takes up O(1) space, regardless of the number of items inserted.
* Lookup and Insert operations are super fast.

### Cons
* Probabilistic. This Data structure can only definitively identify `true negatives`. They cannot identify `true positives`. If a bloom filter says an item is present, that item might actually be present (a true positive) or it might not (a false positive).
* Accuracy goes down as more elements are added. Perhaps, increasing the number of hash functions might decrease of
  false positives.




