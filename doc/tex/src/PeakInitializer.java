
package it.ncorti.p2p;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;


/**
 * Inizializzatore per il modulo COUNT che assegna il valore 0 a tutti i nodi
 * e assegna un valore di picco (parametro 'val' nel file di conf) al primo nodo della
 * rete
 * 
 * @author Nicola Corti
 */
public class PeakInitializer implements Control {

    /** Stringa per recuperare il nome del protocollo dal file di conf */
    private static final String PAR_PROT = "protocol";
    /** Pid del protocollo */
    private final int pid;

    /** Stringa per recuperare il valore del picco dal file di conf */    
    private static final String PAR_VALUE = "value";
    /** Valore del picco */
    private final int value;

    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
    public PeakInitializer(String prefix) {
        value = Configuration.getInt(prefix + "." + PAR_VALUE);
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    /* (non-Javadoc)
     * @see peersim.core.Control#execute()
     */
    @Override
	public boolean execute() {
        for (int i = 0; i < Network.size(); i++) {
            CountModule prot = (CountModule) Network.get(i).getProtocol(pid);
            prot.initValue(0);
        }
        CountModule prot = (CountModule) Network.get(0).getProtocol(pid);
        prot.initValue(value);
        return false;
    }
}
