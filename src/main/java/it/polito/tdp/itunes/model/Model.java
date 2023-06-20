package it.polito.tdp.itunes.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private SimpleGraph<Track, DefaultEdge> graph;
	private List<Track> bestPlaylist;

	public Model() {
		this.dao = new ItunesDAO();
		this.bestPlaylist = new LinkedList<Track>();
	}
	
	public List<Genre> getAllGenres() {
		return this.dao.getAllGenres();
	}
	
	public void createGraph(Genre genre, Integer min, Integer max){
		this.graph = new SimpleGraph<Track, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(this.graph, this.dao.getAllVertexes(genre, min, max));
		for (Track t1 : this.graph.vertexSet()) {
			for (Track t2 : this.graph.vertexSet()) {
				if (t1.getTrackId() < t2.getTrackId()) {//Mi permette di dimezzare il numero di chiamate al DAO.
					if (this.dao.getPlaylistsTrack(t1).size() == this.dao.getPlaylistsTrack(t2).size()) {
						this.graph.addEdge(t2, t1);
					}
				}
			}
		}
	}
	
	public String nVertici() {
		return Integer.toString(this.graph.vertexSet().size());
	}
	
	public String nArchi() {
		return Integer.toString(this.graph.edgeSet().size());
	}
	
	public List<Set<Track>> connectedSets() {
		List<Set<Track>> connectedSets = new ConnectivityInspector<>(this.graph).connectedSets();
		return connectedSets;
	}
	
	public Integer countPlaylistsConnectedSet(Set<Track> tracks) {
		Set<Integer> result = new HashSet<Integer>();
		for (Track t : tracks) {
			for (Integer id : this.dao.getPlaylistsTrack(t)) {
				result.add(id);
			}
		}
		return result.size();
	}
	
	public void avviaRicorsione(Integer dTot) {
		
		this.bestPlaylist = new LinkedList<Track>();
		
		dTot = dTot*60*1000;
		
		Set<Track> connessa = this.connectedSets().get(0);
		for (Set<Track> set : this.connectedSets()) {
			if (set.size() > connessa.size()) {
				connessa = set;
			}
		}
		List<Track> dominio = new LinkedList<>(connessa);
		List<Track> parziale = new LinkedList<Track>();
		
		for (Track t : dominio) {
			if (t.getMilliseconds() < dTot) {
				parziale.add(t);
				cerca(parziale, dominio, t.getMilliseconds(), dTot);
			}	
		}
	}
	
	public void cerca(List<Track> parziale, List<Track> insiemeConnesso, Integer dParziale, Integer dTot) {
		
		for (Track t : insiemeConnesso) {
			if (!parziale.contains(t)) {
				if (dParziale + t.getMilliseconds() < dTot) {
					parziale.add(t);
					cerca(parziale, insiemeConnesso, dParziale+t.getMilliseconds(), dTot);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
		if (parziale.size() > this.bestPlaylist.size()) {
			this.bestPlaylist = new LinkedList<>(parziale);
		}
	}

	public List<Track> getBestPlaylist() {
		return bestPlaylist;
	}
	
}
