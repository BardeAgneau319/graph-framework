package GraphAlgorithms;

import java.util.ArrayList;
import java.util.List;

import Collection.Triple;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class BinaryHeapEdge {

	/**
	 * A list structure for a faster management of the heap by indexing
	 * 
	 */
	private  List<Triple<UndirectedNode,UndirectedNode,Integer>> binh;

    public BinaryHeapEdge() {
        this.binh = new ArrayList<>();
    }

    public boolean isEmpty() {
        return binh.isEmpty();
    }

    /**
	 * Insert a new edge in the binary heap
	 * 
	 * @param from one node of the edge
	 * @param to one node of the edge
	 * @param val the edge weight
	 */
    public void insert(UndirectedNode from, UndirectedNode to, int val) {
		// prelocate-up
		this.binh.add(new Triple<>(from, to, val));
		int child = this.binh.size() - 1;
		int father = Math.round((child - 1) / 2);
		while(this.binh.get(child).getThird() < this.binh.get(father).getThird() && child != 0) {
			this.swap(father, child);
			child = father;
			father = Math.round((child - 1) / 2);
		}
    }

    
    /**
	 * Removes the root edge in the binary heap, and swap the edges to keep a valid binary heap
	 * 
	 * @return the edge with the minimal value (root of the binary heap)
	 * 
	 */
    public Triple<UndirectedNode,UndirectedNode,Integer> remove() {
		if (this.isEmpty()) return null;
		this.swap(0, this.binh.size() - 1);
		Triple<UndirectedNode,UndirectedNode,Integer> result = this.binh.get(this.binh.size() - 1);
		this.binh.remove(this.binh.size() - 1);
		int father = 0;
		int best;
		while (!isLeaf(father)) {
			best = getBestChildPos(father);
			swap(father, best);
			father = best;
		}
		return result;
    }
    

    /**
	 * From an edge indexed by src, find the child having the least weight and return it
	 * 
	 * @param src an index of the list edges
	 * @return the index of the child edge with the least weight
	 */
    private int getBestChildPos(int src) {
		if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
			return Integer.MAX_VALUE;
		} else {
			int childLeft = (src * 2) + 1;
			int childRight = (src * 2) + 2;
			if (childRight >= this.binh.size())
				return childLeft;
			return this.binh.get(childLeft).getThird() <= this.binh.get(childRight).getThird() ? childLeft : childRight;
		}
    }

    private boolean isLeaf(int src) {
		int childLeft = src * 2 + 1;
		int childRight = src * 2 + 2;
		return childLeft >= this.binh.size() && childRight >= this.binh.size();
    }

    
    /**
	 * Swap two edges in the binary heap
	 * 
	 * @param father an index of the list edges
	 * @param child an index of the list edges
	 */
    private void swap(int father, int child) {         
    	Triple<UndirectedNode,UndirectedNode,Integer> temp = new Triple<>(binh.get(father).getFirst(), binh.get(father).getSecond(), binh.get(father).getThird());
    	binh.get(father).setTriple(binh.get(child));
    	binh.get(child).setTriple(temp);
    }

    
    /**
	 * Create the string of the visualisation of a binary heap
	 * 
	 * @return the string of the binary heap
	 */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Triple<UndirectedNode,UndirectedNode,Integer> no: binh) {
            s.append(no).append(", ");
        }
        return s.toString();
    }
    
    
    private String space(int x) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<x; i++) {
			res.append(" ");
		}
		return res.toString();
	}
	
	/**
	 * Print a nice visualisation of the binary heap as a hierarchy tree
	 * 
	 */	
	public void lovelyPrinting(){
		int nodeWidth = this.binh.get(0).toString().length();
		int depth = 1+(int)(Math.log(this.binh.size())/Math.log(2));
		int index=0;
		
		for(int h = 1; h<=depth; h++){
			int left = ((int) (Math.pow(2, depth-h-1)))*nodeWidth - nodeWidth/2;
			int between = ((int) (Math.pow(2, depth-h))-1)*nodeWidth;
			int i =0;
			System.out.print(space(left));
			while(i<Math.pow(2, h-1) && index<binh.size()){
				System.out.print(binh.get(index) + space(between));
				index++;
				i++;
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// ------------------------------------
    // 					TEST
	// ------------------------------------

	/**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @return a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    private boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
    	int lastIndex = binh.size()-1; 
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= lastIndex) {
                return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left);
            } else {
                return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left)
                    && binh.get(right).getThird() >= binh.get(root).getThird() && testRec(right);
            }
        }
    }

    // 5 : Sachant que les algorithmes sont identiques, la complexité est la même
	/* 6 : On commence par l'arrête la plus petite (la première)
	 et tant qu'on a pas un arbre couvrant (tous les sommets ne sont pas visités)
	 on va voir les fils et on choisis le plus petit à chaque fois
	 on retire le père qu'on stocke dans une autre liste et on vérifie si dans cette
	 deuxième liste tous les sommets ont été visités. On vérifie aussi qu'on n'a pas de cycle :
	 on ne tombe pas deux fois sur le même sommet dans la nouvelle liste (from ET to)

	 Il nous faudra donc les opérations supplémentaires :
	 - Vérifier que la nouvelle liste contient tous les sommets
	 - Vérifier que la nouvelle liste n'a pas deux fois le même sommet

	 La complexité algorithmique est alors :
	 Vérifier que la nouvelle liste contient tous les sommets + n'a pas deux fois le même sommet -> n
	 Retirer le plus petit élément à chaque fois : log (n) ou h la hauteur de l'arbre
	 et répéter cela tant qu'on n'a pas tous les sommets. Dans le pire des cas, on retire toutes les
	 arrêtes donc :
	 La complexité est donc au total de n*log(n) ou n*h la hauteur de l'arbre et n le nombre d'arrêtes
	 */
    public static void main(String[] args) {
        BinaryHeapEdge jarjarBin = new BinaryHeapEdge();
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 10;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));                        
            jarjarBin.insert(new UndirectedNode(k), new UndirectedNode(k+30), rand);            
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

