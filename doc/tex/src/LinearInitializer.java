
package it.ncorti.p2p;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;


/**
 * Inizializzatore per il modulo COUNT che assegna un valore in modo lineare,
 * partendo da 1 fino alla dimensione della rete.
 * 
 * @author Nicola Corti
 */
public class LinearInitializer implements Control {

    /** Stringa per recuperare il nome del protocollo dal file di conf */
    private static final String PAR_PROT = "protocol";
    /** Pid del protocollo */
    private final int pid;


    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
    public LinearInitializer(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    /* (non-Javadoc)
     * @see peersim.core.Control#execute()
     */
    @Override
	public boolean execute() {
        for (int i = 0; i < Network.size(); i++) {
            CountModule prot = (CountModule) Network.get(i).getProtocol(pid);
            prot.initValue(i);
        }
        return false;
    }
}
