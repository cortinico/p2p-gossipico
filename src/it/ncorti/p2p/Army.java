package it.ncorti.p2p;

import peersim.core.Network;

/**
 * Classe che rappresenta un esercito, utilizzata dal modulo BEACON per il calcolo del combattimento fra gli esericiti.
 * Ogni nodo che implementa il protocollo COUNT-BEACON disporra' di un'istanza di questa classe.
 * 
 * @author Nicola Corti
 */
public class Army { 
	
	/** Valore che rappresenta la forza dell'esercito */
	public int strenght;
	/** Riferimento al nodo che adesso svolge il ruolo di beacon */
	public CountBeaconModule beacon;
	/** Riferimento al prossimo nodo verso il beacon */
	public CountBeaconModule nexthop;
	/** Distanza verso il beacon */
	public int distance;
	/** Immunita' verso un esercito */
	public CountBeaconModule immunity;
	
	/**
	 * Costruttore che crea un nuovo esercito a partire dal nodo che lo deve possedere.
	 * Lo inizializza con una forza random e imposta il nodo come beacon
	 * 
	 * @param node
	 */
	public Army(CountBeaconModule node){
		
		strenght = (int) (Math.random() * Network.size());
		beacon = node;
		nexthop = node;
		distance = 0;
		immunity = null;
		
	}

	/**
	 * Confronta due eserciti e indica se sono uguali
	 * 
	 * @param army Esercito con cui confrontare
	 * @return true se i due eserciti sono lo stesso (stesso beacon) oppure false
	 */
	public boolean isSameArmy(Army army) {
		return (this.beacon.equals(army.beacon));
	}

	/**
	 * Aggiorna la distanza fra due nodi appartenenti allo stesso beacon fra la piu' breve 
	 * dei due nodi
	 * 
	 * @param i Primo nodo del confronto
	 * @param j Secondo nodo del confronto
	 */
	public static void updateShortest(CountBeaconModule i, CountBeaconModule j) {

		if (i.army.distance < j.army.distance + 1){
			j.army.nexthop = i;
			j.army.distance = i.army.distance + 1;
		} else if (j.army.distance < i.army.distance + 1){
			i.army.nexthop = j;
			i.army.distance = j.army.distance + 1;
		}

	}
	
	/**
	 * Svolge una battaglia fra due nodi, andando a controllare se i due nodi appartengono allo stesso esercito,
	 * se ci sono delle immunita', oppure quale dei due nodi risulta vincitore
	 * 
	 * @param i Primo nodo coinvolto
	 * @param j Secondo nodo coinvolto
	 */
	public static void skirmish(CountBeaconModule i, CountBeaconModule j) {
		if (i.army.beacon.equals(j.army.immunity)){
			computeWinner(j, i);
			return;
		}
		if (j.army.beacon.equals(i.army.immunity)){
			computeWinner(i, j);
			return;
		}
		if (j.army.beacon.equals(i.army.beacon)){
			updateShortest(i, j);
			return;
		}
		if (i.army.strenght > j.army.strenght){
			computeWinner(i, j);
		} else {
			computeWinner(j, i);
		}
	}
	
	/**
	 * Calcola il vincitore fra due nodi che si sono contesi e ne aggiorna i parametri
	 * 
	 * @param i Nodo vincitore
	 * @param j Nodo perdente
	 */
	private static void computeWinner(CountBeaconModule i, CountBeaconModule j){

		j.army.beacon = i.army.beacon;
		j.army.strenght = i.army.strenght;
		j.army.immunity = i.army.immunity;
		j.army.distance = i.army.distance + 1;
		j.army.nexthop = i;
		j.resetCount();
	}
	
	/**
	 * Resuscita un esercito, utile quando un nodo si rende conto che uno dei suoi vicini si e' disconnesso.
	 * 
	 * @param i Nodo verso cui impostare l'immunita'
	 */
	public void revive(CountBeaconModule i){
		
		immunity = beacon;
		strenght = (int) (Math.random() * Network.size());
		beacon = i;
		nexthop = i;
		distance = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		if (immunity != null)
			return ("{ B: " + beacon.getIndex() + " S:" + strenght + " I: " + immunity.getIndex() + " D: " + distance + " Next: " + nexthop.getIndex() + " }");
		else 
			return ("{ B: " + beacon.getIndex() + " S:" + strenght + " I: null D: " + distance + " Next: " + nexthop.getIndex() + " }");
	}

}
