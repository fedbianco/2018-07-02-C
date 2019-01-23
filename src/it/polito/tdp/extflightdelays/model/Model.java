package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private ExtFlightDelaysDAO dao;
	public Graph< Airport ,DefaultWeightedEdge> graph = null;
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}
	
	public void creaGrafo(int airlineMin) {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		for(Airport air : this.dao.loadAllAirports()) {
			if(this.dao.getMinAirline(air)>=airlineMin) {
				graph.addVertex(air);
			}
		}
			System.out.println("# Vertici: " + graph.vertexSet().size());

		for(Airport a : graph.vertexSet()) {
			for(Airport a2 : this.dao.getDestinationAirport(a)) {
				if(a!=null && a2!= null && a.getId()!= a2.getId() && graph.containsVertex(a2)) {
					Graphs.addEdge(graph, a, a2, this.dao.getWeight(a, a2));
					
				}
			}
		}
		System.out.println("Grafo creato!");
		System.out.println("# Archi: " + graph.edgeSet().size());

	}
	public List<NeighborsAirport> getNeighbour(Airport a){
		List<Airport> neighbour = new ArrayList<Airport>();
		List<NeighborsAirport> na = new ArrayList<NeighborsAirport>();
		int peso;
		neighbour.addAll(Graphs.neighborListOf(graph, a));
		for(Airport air : neighbour) {
			peso = (int) graph.getEdgeWeight(graph.getEdge(a, air));
			na.add(new NeighborsAirport(air ,peso));
			
		}
		Collections.sort(na);

		return na;
	
	}


	

}
