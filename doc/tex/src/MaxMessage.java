package it.ncorti.p2p;

/**
 * Classe che rappresenta un messaggio di COUNT ed utilizza la funzione di aggregazione
 * MAX in modo da trovare il massimo dei valori
 * 
 * @author nicola
 */
public class MaxMessage extends Message {

	/**
	 * Costruttore per creare un nuovo messaggio di massimo, partendo da un valore
	 * 
	 * @param val Valore iniziale
	 */
	public MaxMessage(int val){
		super(val);
	}
	
	@Override
	public void merge(Message m) {
		this.freshness += m.freshness;
		this.type = 1;
		this.value = Math.max(this.value, m.value);
	}

}
