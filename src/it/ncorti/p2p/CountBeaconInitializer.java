
package it.ncorti.p2p;

import java.util.ArrayList;
import java.util.List;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;


/**
 * Classe che rappresenta un inizializzatore per il protocollo COUNT-BEACON.
 * Imposta un ID a tutti i nodi in modo da poterlo utilizzare per le stampe di debug
 * e comprendere come si evolve la simulazione
 * 
 * Questa classe inoltre inizializza il valore di forza di ogni esercito in modo che sia random perfetto
 * 
 * @author Nicola Corti
 */
public class CountBeaconInitializer implements Control {


    /** Stringa per recuperare il nome del protocollo dal file di conf */
    private static final String PAR_PROT = "protocol";
    /** Pid del protocollo CountBeacon */
    private final int pid;
    
    /** Lista di interi distinti per assegnare i valori di forza */
    private List<Integer> ints;


    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
    public CountBeaconInitializer(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        ints = new ArrayList<>();
        for (int j = 0; j < Network.size(); j++){
        	ints.add(j);
        }
    }

    /* (non-Javadoc)
     * @see peersim.core.Control#execute()
     */
    @Override
	public boolean execute() {
    	
        for (int i = 0; i < Network.size(); i++) {
            CountModule prot = (CountModule) Network.get(i).getProtocol(pid);
            prot.ID = i;
            if (prot instanceof CountBeaconModule){
            	CountBeaconModule cbm = (CountBeaconModule) prot;
            	int j = (int) (Math.random() * ints.size());
            	if (j >= 0 && j < ints.size()){
            		cbm.army.strenght = ints.get(j);
            		ints.remove(j);
            	} else {
            		cbm.army.strenght = ints.get(0);
            		ints.remove(0);
            	}
            }
        }

        return false;
    }
}
