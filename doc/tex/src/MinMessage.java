package it.ncorti.p2p;

/**
 * Classe che rappresenta un messaggio di COUNT ed utilizza la funzione di aggregazione
 * MIN in modo da trovare il minimo dei valori
 * 
 * @author nicola
 */
public class MinMessage extends Message {

	/**
	 * Costruttore per creare un nuovo messaggio di minimo, partendo da un valore
	 * 
	 * @param val Valore iniziale
	 */
	public MinMessage(int val){
		super(val);
	}
	
	/* (non-Javadoc)
	 * @see it.ncorti.p2p.Message#merge(it.ncorti.p2p.Message)
	 */
	@Override
	public void merge(Message m) {
		this.freshness += m.freshness;
		this.type = 1;
		this.value = Math.min(this.value, m.value);
	}

}
