
package it.ncorti.p2p;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Inizializzatore per il modulo COUNT che assegna il valore a tutti i nodi
 * in modo casuale, potendo indicare il valore massimo e minimo in cui generare i numeri casuali
 * 
 * @author Nicola Corti
 */
public class RandomInitializer implements Control {

    /** Stringa per recuperare il nome del protocollo dal file di conf */
    private static final String PAR_PROT = "protocol";
    /** Stringa per recuperare il valore massimo dal file di conf */
    private static final String PAR_MAX = "max";
    /** Stringa per recuperare il valore minimo dal file di conf */
    private static final String PAR_MIN = "min";
    
    /** Pid del protocollo */
    private final int pid;
    /** Valore massimo del random */
    private final int max;
    /** Valore minimo del random */
    private final int min;

    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
    public RandomInitializer(String prefix) {
        max = Configuration.getInt(prefix + "." + PAR_MAX);
        min = Configuration.getInt(prefix + "." + PAR_MIN);
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    /* (non-Javadoc)
     * @see peersim.core.Control#execute()
     */
    @Override
	public boolean execute() {
        for (int i = 0; i < Network.size(); i++) {
            CountModule prot = (CountModule) Network.get(i).getProtocol(pid);
            int val = (int) (Math.random() * (max - min));
            val += min;
            prot.initValue(val);
        }
        return false;
    }
}
