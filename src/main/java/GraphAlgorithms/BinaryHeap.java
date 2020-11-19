package GraphAlgorithms;


public class BinaryHeap {

    private int[] nodes;
    private int pos;

    public BinaryHeap() {
        this.nodes = new int[32];
        for (int i = 0; i < nodes.length; i++) {
            this.nodes[i] = Integer.MAX_VALUE;
        }
        this.pos = 0;
    }

    public void resize() {
        int[] tab = new int[this.nodes.length + 32];
        for (int i = 0; i < nodes.length; i++) {
            tab[i] = Integer.MAX_VALUE;
        }
        System.arraycopy(this.nodes, 0, tab, 0, this.nodes.length);
        this.nodes = tab;
    }

    public boolean isEmpty() {
        return pos == 0;
    }

    public void insert(int element) {
    	if (this.pos == this.nodes.length) { // is full
    	    this.resize();
        }
    	// prelocate-up
    	this.nodes[this.pos] = element;
    	int child = this.pos;
    	int father = Math.round((child - 1) / 2);
        while(this.nodes[child] < this.nodes[father] && child != 0) {
            this.swap(father, child);
            child = father;
            father = Math.round((child - 1) / 2);
        }
    	this.pos++;
    }

    public int remove() {
        if (this.isEmpty()) return Integer.MAX_VALUE;
    	this.swap(0, this.pos - 1);
    	int result = this.nodes[this.pos - 1];
    	this.nodes[this.pos - 1] = Integer.MAX_VALUE;
    	int father = 0;
    	int childLeft = father * 2 + 1;
    	int childRight = father * 2 + 2;
    	boolean hasChild = childLeft < this.pos || childRight < this.pos;
    	boolean canSwapLeft = childLeft < this.pos ? this.nodes[childLeft] < this.nodes[father] : false;
        boolean canSwapRight = childRight < this.pos ? this.nodes[childRight] < this.nodes[father] : false;
    	while (hasChild && (canSwapLeft || canSwapRight)) {
    	    if (canSwapLeft && canSwapRight) {
    	        if (this.nodes[childLeft] <= this.nodes[childRight]) {
    	            swap(father, childLeft);
    	            father = childLeft;
                } else {
                    swap(father, childRight);
                    father = childRight;
                }
            } else if (canSwapLeft) {
                    swap(father, childLeft);
                    father = childLeft;
            } else if (canSwapRight) {
                swap(father, childRight);
                father = childRight;
            }
            childLeft = father * 2 + 1;
            childRight = father * 2 + 2;
            hasChild = childLeft < this.pos || childRight < this.pos;
            canSwapLeft = childLeft < this.pos ? this.nodes[childLeft] < this.nodes[father] : false;
            canSwapRight = childRight < this.pos ? this.nodes[childRight] < this.nodes[father] : false;
        }
    	return result;
    }

    private int getBestChildPos(int src) {
        if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
            return Integer.MAX_VALUE;
        } else {
            int childLeft = src * 2 + 1;
            int childRight = src * 2 + 2;
            if (childRight < this.pos)
                return childLeft;
            return this.nodes[childLeft] <= this.nodes[childRight] ? childLeft : childRight;
        }
    }

    
    /**
	 * Test if the node is a leaf in the binary heap
	 * 
	 * @returns true if it's a leaf or false else
	 * 
	 */	
    private boolean isLeaf(int src) {
    	int childLeft = src * 2 + 1;
        int childRight = src * 2 + 2;
        return childLeft >= this.pos && childRight >= this.pos;
    }

    private void swap(int father, int child) {
        int temp = nodes[father];
        nodes[father] = nodes[child];
        nodes[child] = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pos; i++) {
            s.append(nodes[i]).append(", ");
        }
        return s.toString();
    }

    /**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @returns a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    public boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= pos) {
                return nodes[left] >= nodes[root] && testRec(left);
            } else {
                return nodes[left] >= nodes[root] && testRec(left) && nodes[right] >= nodes[root] && testRec(right);
            }
        }
    }

    public static void main(String[] args) {
        BinaryHeap jarjarBin = new BinaryHeap();
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 20;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));
            System.out.print("insert " + rand);
            jarjarBin.insert(rand);            
            k--;
        }
        System.out.println("\n" + jarjarBin);
        jarjarBin.remove();
        System.out.println("\n" + jarjarBin);
        jarjarBin.remove();
        System.out.println("\n" + jarjarBin);
        jarjarBin.remove();
        System.out.println("\n" + jarjarBin);
        System.out.println(jarjarBin.test());
    }

}
