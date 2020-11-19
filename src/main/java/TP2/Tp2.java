package TP2;

import Abstraction.IDirectedGraph;
import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyList.UndirectedGraph;
import GraphAlgorithms.GraphTools;
import Nodes.DirectedNode;

import java.util.*;

public abstract class Tp2 {

    public static void inWidthCourse(IDirectedGraph graph, int start) {
        int order = graph.getNbNodes();
        List<Boolean> mark = initMark(order, start);
        Queue<Integer> toVisit = new LinkedList<>();
        toVisit.add(start);

        while(!toVisit.isEmpty()) {
            DirectedNode from = new DirectedNode(toVisit.remove());
            System.out.println(from.getLabel());
            for(int i=0; i<order; i++) {
                DirectedNode to = new DirectedNode(i);
                if(graph.isArc(from, to) && !mark.get(i)) {
                    mark.set(i, true);
                    toVisit.add(i);
                }
            }
        }

    }

    public static void inDepthCourse(IDirectedGraph graph, int start){
        int order = graph.getNbNodes();
        List<Boolean> mark = initMark(order, start);
        Deque<Integer> toVisit = new LinkedList<>();
        toVisit.push(start);

        while(!toVisit.isEmpty()) {
            DirectedNode from = new DirectedNode(toVisit.pop());
            System.out.println(from.getLabel());
            for(int i=order-1; i>=0; i--) {
                DirectedNode to = new DirectedNode(i);
                if(graph.isArc(from, to) && !mark.get(i)) {
                    mark.set(i, true);
                    toVisit.push(i);
                }
            }
        }
    }

    private static List<Boolean> initMark(int order, int start){
        ArrayList<Boolean> mark = new ArrayList<>(order);
        for(int i=0; i<order; i++) {
            mark.add(false);
        }
        mark.set(start, true);
        return mark;
    }

    public static void main(String[] args) {
        int[][] adjencyMatrice = GraphTools.generateGraphData(6, 8, false, true, true, 0);
        GraphTools.afficherMatrix(adjencyMatrice);
        IDirectedGraph graph = new DirectedGraph(adjencyMatrice);
        System.out.println("In width:");
        inWidthCourse(graph, 0);
        System.out.println("In depth:");
        inDepthCourse(graph, 0);
    }


}
