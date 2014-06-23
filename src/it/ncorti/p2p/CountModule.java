package it.ncorti.p2p;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;

/**
 * Classe che rappresenta il modulo che implementa l'algoritmo COUNT,
 * la classe implementa CDProtocol e puo' essere usata all'interno del simulatore Peersim.
 * 
 * Ad ogni ciclo ogni nodo contattera' in modo casuale un altro nodo e verra' effettuato lo scambio
 * delle informazioni fra i due nodi
 * 
 * @author Nicola Corti
 */
public class CountModule implements CDProtocol {

	/** Stringa per ottenere il valore iniziale */
	protected static final String param_value = "value";
	/** Stringa per ottenere la funzione di aggregazione da usare */
	protected static final String param_aggreg = "func";
	
	/** Messaggio in attesa di essere inviato */
	protected Message waiting;
	/** Messaggio appena ricevuto */
	protected Message received;

	/** Variabile di stato: valore attuale */
	protected int state_value;
	/** Variabile di stato: freschezza attuale */
	protected int state_freshness;
	
	/** ID dell'istanza del protocollo */
	protected int ID = -1;
		
	/** Valore iniziale del calcolo */
	private int init_value;
	/** Funzione di aggregazione usata */
	private String func;
	
	
    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
	public CountModule(String prefix) {
	
		init_value = 1;
		state_value = init_value;
		state_freshness = 1;
	
		func = Configuration.getString(prefix + "." + param_aggreg);
		
		if (func.contentEquals("count") || func.contentEquals("sum")){
			waiting = new Message();
			received = new Message();
		} else if (func.contentEquals("min")){
			waiting = new MinMessage(init_value);
			received = new MinMessage(init_value);
		} else if (func.contentEquals("max")){
			waiting = new MaxMessage(init_value);
			received = new MaxMessage(init_value);
		} 
	}

	/* (non-Javadoc)
	 * @see peersim.cdsim.CDProtocol#nextCycle(peersim.core.Node, int)
	 */
	@Override
	public void nextCycle(Node node, int protocolID) {
		
		/* Funzione invocata ad ogni ciclo della simulazione
		 * 1) Contatta un nodo vicino
		 * 2) Deposita il proprio messaggio in attesa
		 * 3) Invoca l'aggiornamento del messaggio sul vicino
		 * 4) Genera un messaggio di IS partendo dallo stato
		 */
		
		CountModule next = this.getNeighboor(node, protocolID);
		if (next == null) return;
		
		next.received.update(this.waiting);
		
		if (next.updateMessage(this))
			waiting.generateISMessage(state_value, state_freshness);
	}
	
	/**
	 * Funzione per aggiornare il proprio messaggio in attesa.
	 * Si aggiorna il messaggio cercando di mantenere l'informazione contenuta nel messaggio IC 
	 * (scartando quindi eventuali messaggi IS) ed effettuando un merge quando entrambi i messaggi
	 * sono IC
	 * 
	 * @param sender Il riferimento al mittente che ha depositato il messaggio presso il nodo
	 * @return true se l'update e' andato a buon fine, false altrimenti.
	 */
	protected boolean updateMessage(CountModule sender) {
		
		if (received.IS() && waiting.IS() && received.isFresherThen(waiting)){
			waiting.update(received);
		} else if (received.IC() && waiting.IS()){
			waiting.update(received);
		} else if (received.IC() && waiting.IC()){
			waiting.merge(received);
		}
		
		// Aggiorna le variabili di stato
		if (waiting.isFresherThen(state_freshness)){
			state_freshness = waiting.getFreshness();
			state_value = waiting.getValue();
		}
		return true;
	}
	
	/**
	 * Funzione utilizzata dagli inizializzatori per impostare il valore iniziale
	 * del messaggio in attesa
	 * 
	 * @param val Valore iniziale
	 */
	protected void initValue(int val){
		this.waiting.value = val;
		this.init_value = val;
	}
	
	/**
	 * Funzione che ritorna un nodo vicino scelto in modo casuale
	 * 
	 * @param node Nodo di cui si cerca un vicino
	 * @param protocolID Id del protocollo Linkable che rappresenta la rete
	 * @return Un'istanza di CountModule, null se non ci sono vicini.
	 */
	protected CountModule getNeighboor(Node node, int protocolID){
		int linkableID = FastConfig.getLinkable(protocolID);
		Linkable link = (Linkable) node.getProtocol(linkableID);
		
		CountModule next = null;
		
		int index = (int) (Math.random() * link.degree());
		if (link.degree() == 0)
			return null;
		
		next = (CountModule) link.getNeighbor(index).getProtocol(protocolID);
				
		return next;
	}

	/**
	 * Funzione che fa ripartire il conteggio di un nodo dal proprio valore iniziale
	 */
	protected void resetCount(){
		this.state_value = init_value;
		this.state_freshness = 1;
		this.waiting.update(init_value, 1, 1);
	}
	
	/**
	 * Ritorna l'ID dell'istanza protocollo/nodo (NB: e non del nodo), necessario per le stampe di debug
	 * realizzate dalla classe Debugger 
	 * 
	 * @return L'ID del protocollo
	 */
	public int getIndex(){
		return this.ID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return ("Node " + this.getIndex() + " \t Count: r = " + this.received + " w = " + this.waiting + " \t state v = " + state_value + " f = " + state_freshness + " \t"); }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 * 
	 * Il metodo e' particolarmente importante in quanto Peersim usa il metodo clone per effettuare
	 * la generazione di nuove istanze del protocollo.
	 */
	@Override
	public Object clone(){
		CountModule node = null;
		
		try { node=(CountModule)super.clone();
		
		if (func.contentEquals("count") || func.contentEquals("sum")){
			node.waiting = new Message();
			node.received = new Message();
		} else if (func.contentEquals("min")){
			node.waiting = new MinMessage(init_value);
			node.received = new MinMessage(init_value);
		} else if (func.contentEquals("max")){
			node.waiting = new MaxMessage(init_value);
			node.received = new MaxMessage(init_value);
		}
		
		node.received.update(this.received);
		node.waiting.update(this.waiting);
		node.state_value = this.state_value;
		node.state_freshness = this.state_freshness;
		node.init_value = this.init_value;
		
		} catch (CloneNotSupportedException e) { e.printStackTrace(); }
		return node;
	}
}
