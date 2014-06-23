package it.ncorti.p2p;

import java.util.ArrayList;
import java.util.List;

import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;

/**
 * Classe che rappresenta il modulo che implementa l'algoritmo COUNT-BEACON,
 * la classe implementa CDProtocol e puo' essere usata all'interno del simulatore Peersim.
 * 
 * Oltre ad eseguire la computazione di COUNT, con una probabilita' p = 0.50 verra' effettuata
 * la computazione BEACON fra due nodi casuali
 * 
 * @author Nicola Corti
 */
public class CountBeaconModule extends CountModule {

	/** Esercito del nodo */
	protected Army army;
	
	/** Lista dei nodi che si sono disconnessi 
	 *  Utile per non notificare due volte la disconessione
	 */
	private List<Node> disconnected;
	
	
    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
	public CountBeaconModule(String prefix) {
		super(prefix);
		army = new Army(this);
		disconnected = new ArrayList<>();
	}

	
	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#
	 * 	nextCycle(peersim.core.Node, int)
	 */
	@Override
	public void nextCycle(Node node, int protocolID) {
		super.nextCycle(node, protocolID);
		
		int linkableID = FastConfig.getLinkable(protocolID);
		Linkable link = (Linkable) node.getProtocol(linkableID);
			
		
		// Se uno dei vicini e' morto, resuscito l'esercito
		if (!checkNeighboor(link)){
			this.army.revive(this);
			this.resetCount();
		}
				
		// Con probabilita' p = 0.50 faccio una computazione BEACON fra due nodi 
		if (Math.random() > 0.50){
						
			CountBeaconModule next = null;
			int count = link.degree();
			
			// Itero fin quando non trovo un nodo up
			while (next == null && count != 0){
				int index = (int) (Math.random()* link.degree());
				Node d = link.getNeighbor(index);
				if (d.isUp()) next = (CountBeaconModule)d.getProtocol(protocolID);
				count--;
			}
			
			if (count == 0 && next == null) return;	
			
			// Eseguo la schermaglia oppure aggiorno il percorso
			if (next.army.isSameArmy(this.army)){
				Army.updateShortest(this, next);
			} else {
				Army.skirmish(this, next);
			}
		}
	}

	/**
	 * Funzione per controllare se tutti i nodi vicini sono sempre up
	 * 
	 * @param link Protocollo che rappresenta la rete
	 * @return true se sono sutti up, false altrimenti
	 */
	private boolean checkNeighboor(Linkable link) {
		
		for (int i = 0; i < link.degree(); i++){
			Node e = link.getNeighbor(i);
			if (!e.isUp() && !disconnected.contains(e)){
				disconnected.add(e);
				return false;
			}
		}
		return true;
	}

	
	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#
	 *	updateMessage(it.ncorti.p2p.CountModule)
	 */
	@Override
	protected boolean updateMessage(CountModule sender) {
		
		// Scarta i messaggi che non sono
		if (sender instanceof CountBeaconModule){
			CountBeaconModule send = (CountBeaconModule) sender;
			if (send.army.isSameArmy(this.army)){
				return super.updateMessage(sender);
			}
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#getNeighboor(peersim.core.Node, int)
	 */
	@Override
	protected CountModule getNeighboor(Node node, int protocolID) {
		// Ritorna sempre il vicino piu' prossimo al beacon, tranne se lui stesso e' il beacon
		
		if (waiting.IC() && !this.isBeacon())
			return this.army.nexthop;
		return super.getNeighboor(node, protocolID);
	}

	
	/**
	 * Funzione che ritorna true se il nodo in questione e' il beacon dell'esercito.
	 * 
	 * @return true se il nodo e' il beacon, false altrimenti
	 */
	private boolean isBeacon() {
		return (army.beacon.equals(this));
	}
	
	
	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#clone()
	 */
	@Override
	public Object clone(){
		CountBeaconModule node = null;
		node=(CountBeaconModule)super.clone();
		node.army = new Army(node);
		node.disconnected = new ArrayList<>();
		return node;
	}
	
	/* (non-Javadoc)
	 * @see it.ncorti.p2p.CountModule#toString()
	 */
	@Override
	public String toString(){
		return super.toString() + "\t | Beacon : " + army;
	}
	
}
