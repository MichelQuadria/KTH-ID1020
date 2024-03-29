
/*
 * Implementation of red black tree
 * 
 * -Ever node is red or black
 * -Root is always black
 * -New insertions are always red
 * -A path from root to null always have the same number of black nodes.
 * -NO consecutive red nodes
 * -Null are black
 * 
 * @author michelouadria
 * */
public class RedBlackTree<Key extends Comparable<Key>, Value> {
    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    

    private class Node {
        Key key;
        Value val;
        Node left, right; // subtrees
        int N; // # nodes in this subtree
        boolean color;

        Node(Key key, Value val, int N, boolean color) {
            this.key = key;
            this.val = val;
            this.N = N;
            this.color = color;
        }
    }
        //Check if a node is null,else assert if color is RED
        private boolean isRed(Node x) {
            if (x == null)
                return false;
            return x.color == RED;
        }

        private Node rotateLeft(Node h) {
            Node x = h.right;
            h.right = x.left;
            x.left = h;
            x.color = h.color;
            h.color = RED;
            x.N = h.N;
            h.N = 1 + size(h.left) + size(h.right);
            return x;
        }

        private Node rotateRight(Node h) {
            Node x = h.left;
            h.left = x.right;
            x.right = h;
            x.color = h.color;
            h.color = RED;
            x.N = h.N;
            h.N = 1 + size(h.left) + size(h.right);
            return x;

        }

        private void flipColors(Node h) {
            h.color = RED;
            h.left.color = BLACK;
            h.right.color = BLACK;
        }

        public int size() {
            return size(root);
        }

        private int size(Node x) {
            if (x == null)
                return 0;
            else
                return x.N;
        }

        boolean contains(Key key) {
            return get(key) != null;
        }


        public Key max() {
            return max(root).key;
        }

        private Node max(Node x) {
            if (x.right == null) {
                return x;
            } else
                return max(x.right);
        }

        public Key min() {
            return min(root).key;
        }

        private Node min(Node x) {
            if (x.left == null)
                return x;
            return min(x.left);
        }

        public void put(Key key, Value val) { 
            root = put(root, key, val);
            root.color = BLACK;
        }

        public Value get(Key key) {
            return get(root, key);
        }

        private Value get(Node x, Key key) { 
                                             
            if (x == null)
                return null;
            int cmp = key.compareTo(x.key);
            if (cmp < 0)
                return get(x.left, key);
            else if (cmp > 0)
                return get(x.right, key);
            else
                return x.val;
        }

        private Node moveRedLeft(Node h) { // Assuming that h is red and both h.left and h.left.left
                                           // are black, make h.left or one of its children red.
            flipColors(h);
            if (isRed(h.right.left)) {
                h.right = rotateRight(h.right);
                h = rotateLeft(h);
            }
            return h;
        }


        public Node balance(Node h) {

            if (isRed(h.right))
                h = rotateLeft(h);
            if (isRed(h.left) && isRed(h.left.left))
                h = rotateRight(h);
            if (isRed(h.left) && isRed(h.right))
                flipColors(h);

            h.N = size(h.left) + size(h.right) + 1;
            return h;
        }
    

    private Node moveRedRight(Node h) { // Assuming that h is red and both h.right and h.right.left
                                        // are black, make h.right or one of its children red.
        flipColors(h);
        if (!isRed(h.left.left))
            h = rotateRight(h);
        return h;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null) // Do standard insert, with red link to parent.
            return new Node(key, val, 1, RED);
        int cmp = key.compareTo(h.key);
        if (cmp < 0)
            h.left = put(h.left, key, val);
        else if (cmp > 0)
            h.right = put(h.right, key, val);
        else
            h.val = val;
        if (isRed(h.right) && !isRed(h.left))
            h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left))
            h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))
            flipColors(h);
        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }
    
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null)
            return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0)
            keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0)
            queue.enqueue(x.key);
        if (cmphi > 0)
            keys(x.right, queue, lo, hi);
    }
}