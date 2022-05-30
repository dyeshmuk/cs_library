import java.util.*;

public class Main {
	public static void main(String[] args) {
	    
	    LRUCache lruCache = new LRUCache<Integer, Integer>(3);
	    lruCache.put(1, 10);
	    lruCache.put(2, 20);
	    lruCache.put(3, 30);
	    lruCache.put(5, 50);
	    lruCache.get(2);
	    lruCache.get(5);
	    lruCache.delete(2);
	    lruCache.put(2, 80);
	    
	    lruCache.print();
		
	}
	
	interface Cache<K, V> {
	    void put(K key, V value);
	    void delete(K key);
	    V get(K key);
	    boolean isEmpty();
	    int size();
	}
	
	private static class LRUCache<K, V> implements Cache<K, V> {
	    private Map<K, Node<Pair<K, V>>> cache;
	    private DoublyLinkedList<Pair<K, V>> list;
	    private int capacity;
	    
	    LRUCache(int capacity) {
	        this.capacity = capacity;
	        this.cache = new HashMap<>();
	        this.list = new DoublyLinkedList<>();
	    }
	    
	    @Override
	    public void put(K key, V value) {
	        if(cache.containsKey(key)) {
	            Node<Pair<K, V>> node = cache.get(key);
	            list.remove(node);
	            
	        } else {
    	        if(cache.size() == this.capacity) {
    	            Pair<K, V> nodeValue = list.popBack();
    	            cache.remove(nodeValue.key);
    	        }
	        }
	        list.pushFront(new Pair(key, value));
	        cache.put(key, list.front());
	    }
	    
	    @Override
	    public V get(K key) { 
	        Node<Pair<K, V>> node = cache.get(key);
	        if(node != null) {
	            V value = (V)node.value.value;
                list.remove(node);
                cache.remove(key);
                list.pushFront(new Pair(key, value));
                cache.put(key, list.front());
                return value;
	        }
	        return null;
	    }
	    
	    @Override
	    public void delete(K key) {
	        Node node = cache.get(key);
	        cache.remove(key);
	        list.remove(node);
	    }
	    
	    @Override
	    public boolean isEmpty() {
	        return this.cache.size() == 0;
	    }
	    
	    @Override
	    public int size() {
	        return this.cache.size();
	    }
	    
	    public void print() {
	        Node cur = list.front();
	        int cnt = 0;
	        while(cur != null) {
	            System.out.print(cur.value);
	            if(cnt < cache.size() - 1) {
	                System.out.print("->");
	            }
	            cur = cur.next;
	            cnt++;
	        }
	    }
	    
	}
	private static class DoublyLinkedList<T> {
	    private Node<T> head;
	    private Node<T> tail;
	    private int size = 0;
	    
	    // Append a new node after the last node
	    public void pushBack(T value) {
	        Node node = new Node(value);
	        if(tail == null) {
	            head = node;
	            tail = node;
	        } else {
	            tail.next = node;
	            node.prev = tail;
	            tail.next = null;
	            tail = node;
	        }
	        size++;
	    }
	    
	    // Insert a new node before the first node
	    public void pushFront(T value) {
	        Node node = new Node(value);
	        if(head == null) {
	            tail = node;
	            head = node;
	        } else {
	            head.prev = node;
	            node.next = head;
	            head = node;
	        }
	        size++;
	    }
	    
	    // Remove and return the last node
	    public T popBack() {
	        if(tail == null) {
	            throw new RuntimeException("List is empty");
	        }
	        T value = tail.value;
	        Node prevNode = tail.prev;
	        tail = prevNode;
	        if(prevNode == null) {
	            head = null;
	        } else {
	            prevNode.next = null;
	        }
	        size--;
	        
	        return value;
	    }
	    
	    // Remove and return the first node
	    public T popFront() {
	        if(head == null) {
	            throw new RuntimeException("List is empty");
	        }
	        T value = head.value;
	        Node nextNode = head.next;
	        head = nextNode;
	        if(nextNode == null) {
	            tail = null;
	        } else {
	            nextNode.next = null;
	        }
	        size--;
	        
	        return value;
	    }
	    // Remove a node from the list
	    public void remove(Node node) {
	        Node prev = node.prev;
	        Node next = node.next;
	        
	        if(prev == null) {
	            head = next;
	        } else {
	            prev.next = next;
	            node.next = null;
	        }
	        
	        if(next == null) {
	            tail = prev;
	        } else {
	            next.prev = prev;
	            node.next = null;
	        }
	        size--;
	    }
	    
	    public Node front() {
	        return this.head;
	    }
	    
	    public Node back() {
	        return this.tail;
	    }
	    
	    public int size() {
	        return this.size;
	    }
	    
	    public void print() {
	        int cnt = 0;
	        Node cur = head;
	        while(cur != null) {
	            System.out.print(cur.value);
	            if(cnt < this.size - 1) {
	                System.out.print("->");
	            }
	            
	            cur = cur.next;
	        }
	    }
	}
	private static class Node<T> {
        T value;
        Node prev;
        Node next;
        Node(T value) {
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }
    private static class Pair<K, V> {
        K key;
        V value;
        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return "{key->"+this.key+", value->"+this.value+"}";
        }
    }
}

