package it.ncorti.p2p;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Classe per la raccolta delle statistiche dell'esecuzione.
 * Vengono raccolte informazioni sul numero di messaggi IC e IS presenti ad ogni ciclo.
 * Viene inoltre visualizzato quando vengono completate le seguenti fasi (nel caso di COUNT-BEACON)
 * 
 * 1) Raggiunto un singolo BEACON
 * 2) Tutti i messaggi IC sono stati raccolti in un singolo nodo
 * 3) L'informazione e' stata condivisa a tutti i nodi presenti nella rete
 * 
 * Impostato la configurazione su ('silent true') verranno solo visualizzate le 3 fasi, senza indicare
 * il conteggio dei messaggi ad ogni ciclo
 * 
 * @author Nicola Corti
 */
public class Statistics implements Control {

    /** Stringa per recuperare il nome del protocollo dal file di conf */
    private static final String PAR_PROT = "protocol";
    /** Pid del protocollo */
    private final int pid;

    /** Stringa per recuperare l'impostazione del silent dal file di conf */    
    private static final String PAR_SILENT = "silent";
    /** Valore del picco */
    private boolean silent = true;
	
    /** Variabile di stato per indicare la fase in cui ci si trova */
	private int phase = 1;

	
	/**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
	public Statistics(String prefix){
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
		this.silent = Configuration.getBoolean(prefix + "." + PAR_SILENT, false); 
	}
	
	
	
	/* (non-Javadoc)
	 * @see peersim.core.Control#execute()
	 */
	@Override
	public boolean execute() {
		
		// Conteggio messaggi IS
		int IScount = 0;
		// Conteggio messaggi IC
		int ICcount = 0;
		// Conteggio beacon
		int BeaconCount = 0;
		// Valore atteso dalla rete
		int ExpectedVal = -1;
		// Flag che indica se tutti i nodi hanno lo stesso valore
		boolean SameVal = true;
				
		for (int i = 0; i < Network.size(); ++i){
			
			CountModule node = ((CountModule) Network.get(i).getProtocol(this.pid));
			
			if (node instanceof CountBeaconModule){
				CountBeaconModule cnode = (CountBeaconModule) node;
				if (cnode.army.distance == 0) BeaconCount++;
			}
			
			if (node.waiting.IC()) ICcount++;
			if (node.waiting.IS()) {
				IScount++;
				if (ExpectedVal == -1) ExpectedVal = node.waiting.value;
			}
			
			SameVal &= (node.waiting.value == ExpectedVal);
			
		}
		
		if (Network.get(0).getProtocol(this.pid) instanceof CountBeaconModule){
		
			// Aggiorna fase nel caso COUNT-BEACON
			if (BeaconCount == 1 && ICcount != 1 && !SameVal && phase == 1){
				System.out.println(" --------------- PHASE 1 COMPLETE @" + CommonState.getIntTime() + " ------------- ");
				phase++;
			}
			if (BeaconCount == 1 && ICcount == 1 && !SameVal && phase == 2){
				System.out.println(" --------------- PHASE 2 COMPLETE @" + CommonState.getIntTime() + " ------------- ");
				phase++;
			}
			if (BeaconCount == 1 && ICcount == 1 && SameVal && phase == 3){
				System.out.println(" --------------- PHASE 3 COMPLETE @" + CommonState.getIntTime() + " ------------- ");
				System.out.println(" ###############    CONVERGENCE  @" + CommonState.getIntTime() + "  ############# ");
				System.out.println(" ###############    IC  @" + ExpectedVal + "  ############# ");
				
				phase++;
				System.exit(0);
			}
			
			if (!silent) System.out.println("CYCLE: " + CommonState.getIntTime() + " - IC: " + ICcount + " - IS: " + IScount + " - Beacon: " + BeaconCount);
		} else {
			
			// Aggiorna fase nel caso COUNT
			if (ICcount == 1 && !SameVal && phase == 1){
				System.out.println(" --------------- PHASE 1 COMPLETE @" + CommonState.getIntTime() + " ------------- ");
				phase++;
			}
			if (ICcount == 1 && SameVal && phase == 2){
				System.out.println(" --------------- PHASE 2 COMPLETE @" + CommonState.getIntTime() + " ------------- ");
				System.out.println(" ###############    CONVERGENCE  @" + CommonState.getIntTime() + "   ############# ");
				System.out.println(" ###############    IC  @" + ExpectedVal + "  ############# ");
				
				phase++;
				System.exit(0);
			}
			
			if (!silent) System.out.println("CYCLE: " + CommonState.getIntTime() + " - IC: " + ICcount + " - IS: " + IScount);
		}
			
		SameVal = false;	
		return false;
	}
}
