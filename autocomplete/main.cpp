#include <iostream>
#include <string>
#include <queue>
#include <unordered_map>
#include <set>

using namespace std;

enum Order { BFS, DFS };

struct Node {
   unordered_map<char, Node*> children;
   bool is_leaf;
};

class Trie {
    private:
        Node *root;
    
        // Prints the tree in DFS order
        void print_dfs_order(Node* node) {
            for (auto entry : node->children) {
                cout << entry.first << " ";
                if(entry.second != NULL) {
                    print_dfs_order(entry.second);
                }
            }
        }
        
        // Prints the tree in BFS order
        void print_bfs_order(Node* node) {
            queue<Node*> q;
            q.push(node);
            
            while(!q.empty()) {
                auto vertex = q.front();
                q.pop();
                for (auto entry : vertex->children) {
                    cout << entry.first << " ";
                    if(entry.second != NULL) {
                        q.push(entry.second);
                    }
                }
            }
        }
        
        void dfs(Node* node, vector<string> &list, string word) {
            if(node->is_leaf) {
                list.push_back(word);
            }
            for (auto entry : node->children) {
                if(entry.second != NULL) {
                    dfs(entry.second, list, word + entry.first);
                }
            }
        }
    
    public:
        Trie() {
            this->root = new Node();
        }
        
        // Inserts a word into the tree
        void insert(string word) {
            Node *cur = root;
            for (auto ch: word) {
                Node* node = cur->children[ch];
                if(!node) {
                    cur->children[ch] = new Node(); 
                }
                cur = cur->children[ch];
            }
            
            cur->is_leaf = true;
        }
        
        // Prints the tree in the order specified by a user
        void print(Order order) {
            Node *cur = root;
            if(order == BFS) {
                print_bfs_order(cur);
            } else {
                print_dfs_order(cur);
            }
        }
        
        // Checks if an input string contained in the tree
        bool find(string word) {
            Node *cur = root;
            for (auto ch: word) {
                Node* node = cur->children[ch];
                if(node == NULL) {
                    return false;
                }
                cur = node;
            }
            return cur->is_leaf;
        }
        
        // Returns all the words in the tree whose common prefix is 
        // the input prefix string thus returns all the suggestions for autocomplete.
        vector<string> startWith(string prefix) {
            vector<string> list;
            Node *cur = root;
            for (auto ch: prefix) {
                Node* node = cur->children[ch];
                if(node == NULL) {
                    return list;
                }
                cur = node;
            }
            
            dfs(cur, list, prefix);
            return list;
        }
};

int main() {
    Trie tree;
    string input_words[] = {
        "stack", 
        "stackoverflow", 
        "stacktrace", 
        "step", 
        "stock", 
        "low", 
        "lower"
    };
    for (auto word : input_words) {
        tree.insert(word);
    }
    tree.print(BFS);
    cout << endl;
    tree.print(DFS);
    cout << endl;
    cout << tree.find("stacktrace") << endl;
    cout << tree.find("dummy") << endl;
    vector<string> list = tree.startWith("st");
    for (auto word: list) {
        cout << word << endl;
    }
    return 0;
}
