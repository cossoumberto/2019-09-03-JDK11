package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> bestCammino;
	private Integer bestPeso;
	
	public Model() {
		dao = new FoodDao();
	}
	
	public void creaGrafo(Double calorie) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getNomiPorzioniMaxCalorie(calorie));
		for(CoppiaPorzioni cp : dao.listCoppiePorzioni(calorie)) 
			Graphs.addEdge(grafo, cp.getP1(), cp.getP2(), cp.getPeso());
	}
	
	public List<String> getVertici(){
		List<String> list = new ArrayList<>(grafo.vertexSet());
		Collections.sort(list);
		return list;
	}
	
	public Integer getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<PorzionePeso> getViciniPesi(String porzione){
		List<PorzionePeso> list = new ArrayList<>();
		for(DefaultWeightedEdge e : grafo.edgesOf(porzione))
			list.add(new PorzionePeso(Graphs.getOppositeVertex(grafo, e, porzione), (int) grafo.getEdgeWeight(e)));
		Collections.sort(list);
		return list;
	}
	
	public List<String> cammino(String partenza, Integer N){
		bestCammino = null;
		bestPeso = 0;
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		cerca(parziale, 0, N);
		return bestCammino;
	}

	private void cerca(List<String> parziale, int peso, Integer N) {
		if(parziale.size()==N) {//ERRORE parziale.size()==N+1 --> sono richiesti N passi (archi)
			if(peso>bestPeso) {
				bestCammino = new ArrayList<>(parziale);
				bestPeso = peso;
			}
			return;
		} else {
			if(grafo.degreeOf(parziale.get(parziale.size()-1))>0)
				for(String s : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1)))
					if(!parziale.contains(s)) {
						parziale.add(s);
						cerca(parziale, peso+(int)grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-2), s)), N);
						parziale.remove(parziale.size()-1);
					}
		}
	}
	
	public Integer getPesoCammino() {
		return bestPeso;
	}
	
}
