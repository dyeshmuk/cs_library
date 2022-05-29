## Autocomplete or Typeahead feature based on the Trie Data structure.

### Abstract
Autocomplete or Typeahead is a feature that suggests a complete word or phrase after a user has typed just a few letters. A great example is search engines, when a user types in a prefix of his search query, the search engine gives him all recommendations to auto-complete his query based on the strings stored in the datastore. Trie data structure is a perfect fit to implement this feature efficient in terms of the time complexity. 

What are some advantages and disadvantages of Trie Data structure?
### Pros
The insert and the search algorithm is O(N).
The search result can be easily printed in alphabetical order, which is difficult if we use hashing. 

### Cons
Requires a lot of memory for storing the strings. Each node of the tree contains an array of child pointers(equal to number of characters of the alphabet).

### High-level design
![Imgur](doc/trie-diagram.svg)
