package it.ncorti.p2p;

/**
 * @author nicola
 *
 */
/**
 * @author nicola
 *
 */
public class Message {
		
	protected int value;
	protected int freshness;
	protected int type;
		
	/**
	 * Costruttore utilizzato per generare un nuovo messaggio
	 * con un valore di partenza
	 * @param val Il valore di partenza del messaggio
	 */
	public Message(int val){
		value = val;
		freshness = 1;
		type = 1;
	}
	/**
	 * Costruttore utilizzato per generare un nuovo messaggio
	 * con un valore di partenza uguale ad 1
	 */
	public Message(){
		this(1);
	}
	/**
	 * Costruttore utilizzato per generare un nuovo messaggio
	 * permettendo di impostare tutti i campi
	 * @param val Il valore di partenza del messaggio
	 * @param fresh Il valore di freschezza del messaggio
	 * @param ty Il tipo del messaggio
	 */
	public Message(int val, int fresh, int ty){
		value = val;
		freshness = fresh;
		type = ty;
	}
	
	/**Ritorna il valore del messaggio
	 * @return Il valore contenuto nel messaggio
	 */
	public int getValue() { return value; }
	
	/**Ritorna il valore di freschezza del messaggio
	 * @return Valore dei freschezza del messaggio
	 */
	public int getFreshness() { return freshness; }
	
	/**Metodo per controllare se il messaggio e' di tipo IC
	 * @return true se il messaggio e' di tipo IC, false altrimenti
	 */
	public boolean IC(){ return (type == 1); }
	/**Metodo per controllare se il messaggio e' di tipo IS
	 * @return true se il messaggio e' di tipo IS, false altrimenti
	 */
	public boolean IS(){ return (type == 0); }
	
	
	/** Permette di confrontare quale fra due messaggi e' piu' recente
	 * @param m Secondo messaggio con cui confrontare
	 * @return true se il primo messaggio e' piu' recente, false altrimenti
	 */
	public boolean isFresherThen(Message m){ return (freshness > m.freshness); }
	/** Permette di confrontare il messaggio con un valore di freschezza
	 * @param fr Valore di freschezza con cui confrontare
	 * @return true se il messaggio e' piu' recente, false altrimenti
	 */
	public boolean isFresherThen(int fr){ return (freshness > fr); }
	
	/** Metodo per aggiornare contemporaneamente tutti i campi di un messaggio
	 * @param val Nuovo valore del messaggio
	 * @param fresh Nuovo valore di freschezza
	 * @param ty Nuovo tipo del messaggio
	 */
	public void update(int val, int fresh, int ty){
		value = val;
		freshness = fresh;
		type = ty;
	}
	
	/** Metodo per aggiornare i campi di un messaggio partendo da un altro messaggio
	 * @param m Messaggio da cui copiare i valori
	 */
	public void update(Message m){
		this.value = m.value;
		this.freshness = m.freshness;
		this.type = m.type;
	}
	
	/** Metodo per aggiornare il valore di un messaggio
	 * @param val Nuovo valore del messaggio
	 */
	public void update(int val){
		this.value = val;
	}
	
	/** Metodo per fondere due messaggi IC in uno nuovo che contenga l'informazione risultante
	 * @param m Messaggio da unire
	 */
	public void merge(Message m){
		this.value += m.value;
		this.freshness += m.freshness;
		this.type = 1;
	}
	
	/** Trasforma il messaggio in un messaggio IS, dati valore e freschezza
	 * @param val Nuovo valore del messaggio
	 * @param fr Nuovo valore di freschezza
	 */
	public void generateISMessage(int val, int fr){
		this.value = val;
		this.freshness = fr;
		this.type = 0;
	}
	
	/** Reimposta il messaggio partendo da un valore iniziale, impostando la
	 * freschezza ad 1 e il tipo ad IC
	 * @param init_value Valore iniziale da dare al messaggio
	 */
	public void resetMessage(int init_value){
		this.value = init_value;
		this.freshness = 1;
		this.type = 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String message;
		if (type == 0)
			message = "{" + value + "|" + freshness + "|IS}";
		else
			message = "{" + value + "|" + freshness + "|IC}";
		return message;
	}
	
	
}
