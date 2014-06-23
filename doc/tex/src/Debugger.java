package it.ncorti.p2p;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Classe di Debug che stampa su standard output tutte le informazioni di tutti i nodi della rete
 * ad ogni ciclo di esecuzione
 * 
 * @author Nicola Corti
 */
public class Debugger implements Control {

    /** Stringa per recuperare il nome del protocollo dal file di conf */
    private static final String PAR_PROT = "protocol";
    /** Pid del protocollo CountBeacon */
    private final int pid;


    /**
     * Costruttore Base invocato dal simulatore
     * @param prefix prefisso indicato nel file di configurazione
     */
	public Debugger(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}
	
	
	/* (non-Javadoc)
	 * @see peersim.core.Control#execute()
	 */
	@Override
	public boolean execute() {
		for (int i = 0; i < Network.size(); ++i){
			CountModule node = ((CountModule) Network.get(i).getProtocol(pid));
			if (node != null) System.out.println(node);
		}
		System.out.println("-------------");
		return false;
	}

}
