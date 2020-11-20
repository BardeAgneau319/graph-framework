package TP2;

import Abstraction.AbstractListGraph;
import Abstraction.AbstractMatrixGraph;
import Abstraction.IDirectedGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyMatrix.AdjacencyMatrixDirectedGraph;
import GraphAlgorithms.GraphTools;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import org.javatuples.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Tp2 {

    /**
     * Parcours en largeur d'un graphe, implémentation impérative
     * @param graph Le graphe
     * @param start Le sommet d'où commencer
     * @return Les sommets atteints, dans l'odre où ils ont été atteints
     */
    public static List<Integer> parcoursEnLargeur(IDirectedGraph graph, int start) {
        int order = graph.getNbNodes();
        List<Integer> parcours = new LinkedList<>();
        List<Boolean> mark = initMark(order, start);
        Queue<Integer> toVisit = new LinkedList<>();
        toVisit.add(start);

        while(!toVisit.isEmpty()) {
            int sommet = toVisit.remove();
            parcours.add(sommet);
            for(int successeur : successeurs(sommet, graph)) {
                if(!mark.get(successeur)) {
                    mark.set(successeur, true);
                    toVisit.add(successeur);
                }
            }
        }

        return parcours;
    }

    /**
     * Parcours en profondeur prefix d'un graphe, implémentation impérative
     * @param graph Le graphe
     * @param start Le sommet d'où commencer
     * @return Les sommets atteints, dans l'odre où ils ont été atteints
     */
    public static List<Integer> parcoursEnProfondeur(IDirectedGraph graph, int start){
        int order = graph.getNbNodes();
        List<Integer> parcours = new LinkedList<>();
        List<Boolean> mark = initMark(order, start);
        Deque<Integer> toVisit = new LinkedList<>();
        toVisit.push(start);

        while(!toVisit.isEmpty()) {
            int sommet = toVisit.pop();
            parcours.add(sommet);
            for(int successeur: successeurs(sommet, graph)){
                if(!mark.get(successeur)) {
                    mark.set(successeur, true);
                    toVisit.push(successeur);
                }
            }
        }
        return parcours;
    }

    /**
     * Retourne les successeurs d'un sommet d'un graphe orienté
     * @param sommet Le sommet dont on récupère les successeurs
     * @param graphe Le graphe auquel appartient le sommet
     * @return Un ensemble représentant les successeur du sommet
     */
    private static Set<Integer> successeurs(int sommet, IDirectedGraph graphe){
        DirectedNode node = new DirectedNode(sommet);
        if(graphe instanceof AbstractListGraph) {
            DirectedGraph listGraphe = (DirectedGraph) graphe;

            return listGraphe.getNodeOfList(node).getSuccs().keySet()
                    .stream()
                    .map(AbstractNode::getLabel)
                    .collect(Collectors.toSet());
        }
        else if(graphe instanceof AbstractMatrixGraph) {
            AdjacencyMatrixDirectedGraph matrixGraph = (AdjacencyMatrixDirectedGraph) graphe;
            Set<Integer> successeurs = new HashSet<>();
            for(int i=0; i<graphe.getNbNodes(); i++){
                if(graphe.isArc(node, new DirectedNode(i))) {
                    successeurs.add(i);
                }
            }
            return successeurs;
        }
        throw new RuntimeException(String.format("No implementation for %s", graphe.getClass()));
    }

    private static List<Boolean> initMark(int order, int start){
        ArrayList<Boolean> mark = new ArrayList<>(order);
        for(int i=0; i<order; i++) {
            mark.add(false);
        }
        mark.set(start, true);
        return mark;
    }

    /**
     * Parcours en profondeur sufixe avec une implémentation récursive. Produit des effets de bords.
     * @param sommet Le sommet de départ du parcours
     * @param atteints La liste des sommets déjà atteints, mise à jour via un effet de bord
     * @param parcours L'ordre de parcours des sommets, mis à jour avec un effet de bord.
     * @param graphe Un graphe orienté quelconque. Pas d'effet de bord
     */
    private static void explorerSommet(int sommet, Set<Integer> atteints,
                                       Deque<Integer> parcours, final IDirectedGraph graphe) {
        atteints.add(sommet);
        for(int successeur : successeurs(sommet, graphe)) {
            if(!atteints.contains(successeur)) {
                explorerSommet(successeur, atteints, parcours, graphe);
            }
        }
        parcours.addLast(sommet);
    }

    /**
     * Calcule les composantes fortement connexes d'un graphe orienté
     * @param graphe Un graphe orienté quelconque
     * @return Une collection d'ensembles représentant les composantes fortement connexes
     */
    public static Collection<Set<Integer>> composantesFortementConexes(IDirectedGraph graphe) {
        Set<Integer> atteints = new HashSet<>();
        Deque<Integer> parcours = new LinkedList<>();
        int ordre = graphe.getNbNodes();

        for(int sommet=0; sommet<ordre; sommet++) {
            if(!atteints.contains(sommet)){
                explorerSommet(sommet, atteints, parcours, graphe);
            }
            if (atteints.size() >= ordre) break;
        }

        IDirectedGraph graphTranspose = graphe.computeInverse();
        Set<Integer> atteints2 = new HashSet<>();
        Collection<Set<Integer>> cfc = new LinkedList<>();
        while(!parcours.isEmpty()){
            int sommet = parcours.removeLast();

            if(!atteints2.contains(sommet)){
                LinkedList<Integer> parcours2 = new LinkedList<>();
                explorerSommet(sommet, atteints2, parcours2, graphTranspose);
                cfc.add(new HashSet<>(parcours2));
            }
            if (atteints2.size() >= ordre) break;
        }


        return cfc;
    }

    /**
     * Calcule le temps d'exécution d'une fonction. Attention : mesure imprécise, peut nécessiter de recommencer plusieurs fois.
     * @param f La fonction a exécuter
     * @param <T> Le type de retour de la fonction
     * @return Une paire contenant le temps d'exécution et le résultat de la fonction
     */
    private static <T> Pair<Long, T> tempsExecution(Supplier<T> f) {
        long debut = System.nanoTime();
        T res = f.get();
        long fin = System.nanoTime();
        long duree = fin - debut;
        return new Pair<>(duree, res);
    }

    public static void main(String[] args) {
        int[][] adjencyMatrice = GraphTools.generateGraphData(6, 8, false, true, true, 0);
        GraphTools.afficherMatrix(adjencyMatrice);

        DirectedGraph listGraphe = new DirectedGraph(adjencyMatrice);
        AdjacencyMatrixDirectedGraph matrixGraphe = new AdjacencyMatrixDirectedGraph(adjencyMatrice);

        System.out.println("Parcours en largeur (liste):");
        Pair<Long, List<Integer>> res = tempsExecution(() -> parcoursEnLargeur(listGraphe, 0));
        System.out.printf("Durée : %d nanoS, parcours : %s%n%n", res.getValue0(), res.getValue1());

        System.out.println("Parcours en largeur (matrice):");
        res = tempsExecution(() -> parcoursEnLargeur(matrixGraphe, 0));
        System.out.printf("Durée : %d nanoS, parcours : %s%n%n", res.getValue0(), res.getValue1());

        System.out.println("Parcours en profondeur (liste):");
        res = tempsExecution(() -> parcoursEnProfondeur(listGraphe, 0));
        System.out.printf("Durée : %d nanoS, parcours : %s%n%n", res.getValue0(), res.getValue1());

        System.out.println("Parcours en profondeur (matrice):");
        res = tempsExecution(() -> parcoursEnProfondeur(matrixGraphe, 0));
        System.out.printf("Durée : %d nanoS, parcours : %s%n%n", res.getValue0(), res.getValue1());


        int[][] matriceCFC = {
                {0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0}};

        System.out.println("Composantes fortement connexes (liste):");
        Pair<Long, Collection<Set<Integer>>> resCfc = tempsExecution(() -> composantesFortementConexes(new DirectedGraph(matriceCFC)));
        System.out.printf("Durée : %d nanoS, composantes :%n", resCfc.getValue0());
        for(Set<Integer> cfc : resCfc.getValue1()) System.out.println(cfc);

        System.out.println("Composantes fortement connexes (matrice):");
        resCfc = tempsExecution(() -> composantesFortementConexes(new AdjacencyMatrixDirectedGraph(matriceCFC)));
        System.out.printf("Durée : %d nanoS, composantes :%n", resCfc.getValue0());
        for(Set<Integer> cfc : resCfc.getValue1()) System.out.println(cfc);
    }


}
