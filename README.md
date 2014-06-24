GOSSIPICO - Peer-2-Peer Protocol Simulation
=============

This software is a simulation of the Gossipico protocol. The protocol is described in:
> [Gossip-based counting in dynamic networks](http://link.springer.com/chapter/10.1007/978-3-642-30054-7_32)

This project is the final exam of the Peer-2-Peer course at Master Degree in Computer Science at University of Pisa.
The simulation is written in Java ad use the Peersim Simulator ([Official Web](http://peersim.sourceforge.net/)).

![GOSSIPICO](https://raw.githubusercontent.com/cortinico/p2p-gossipico/master/doc/gossip_image.png)

## Documentation

For better understanding the workflow of the simulation you can read:
* [PDF Relation (in italian)](../../raw/master/doc/tex/relazione.pdf)
* [JavaDoc Documentation](http://cortinico.github.io/p2p-gossipico/)

## Executing the simulation

For executing the simulation you must execute the following steps
```bash
git clone git@github.com:cortico/p2p-pingpong.git
cd p2p-pingpong/
ant build
```
And then execute with
```
ant Execute [-Dfile=<conffile>]
```

## Config file

The config file, provided with ```[-Dfile=<conffile>]``` must be provided with a specific syntax. Some examples can be found in the folder ```examples/```.

### An example file
```
########################################################################
#	Esempio di file di configurazione per la simulazione
#	dell'algoritmo GOSSIPICO.
#	Da utilizzare insieme al simulatore Peersim
#
#	Nicola Corti - 2014
########################################################################


########################################################################
###### PARAMETRI DELLA SIMULAZIONE
########################################################################

########################
# Variabile Size
SIZE = 2000

random.seed 1234567890

########################
# Numero di cicli di simulazione
simulation.cycles SIZE

control.shf Shuffle

########################
# Dimensione della Rete
network.size SIZE
 
protocol.lnk IdleProtocol


########################################################################
###### PROTOCOLLO
########################################################################

########################
# Protocollo da Usare: Caso COUNT
#protocol.count it.ncorti.p2p.CountModule
#protocol.count.linkable lnk
#
# Funzione da usare {count, sum, max, min}
# Ricordarsi di impostare un inizializzatore
#
#protocol.count.func count
#
########################
# Protocollo da Usare: Caso BEACON
protocol.count it.ncorti.p2p.CountBeaconModule
protocol.count.linkable lnk
#
# Funzione da usare {count, sum, max, min}
# Ricordarsi di impostare un inizializzatore
#
protocol.count.func count
#
########################


########################################################################
###### TOPOLOGIA DI RETE
########################################################################

########################
# Topologia da usare: Random Graph
init.rnd WireKOut
init.rnd.protocol lnk
# Parametro K
init.rnd.k SIZE / 50
########################
# Topologia da usare: Small-world Watts and Strogatz
#init.rnd WireWS
#init.rnd.protocol lnk
# Parametro K
#init.rnd.k SIZE / 50
# Parametro Beta [0, 1]
#init.rnd.beta 0.50
########################
# Topologia da usare: Scale-Free Barabasi-Albert
#init.rnd WireScaleFreeBA
#init.rnd.protocol lnk
# Parametro K
#init.rnd.k SIZE / 50
# Grafo non orientato
#init.rnd.undir true
########################


########################################################################
###### INIZIALIZZATORI
########################################################################

########################
# Inizializzatore BEACON - Necessario per il calcolo di BEACON!
# Necessario per il corretto funzionamento
#
init.bec it.ncorti.p2p.CountBeaconInitializer 
init.bec.protocol count
########################


########################
# Inizializzatore Random
#init.lin it.ncorti.p2p.RandomInitializer
#init.lin.protocol count
#init.lin.min 10
#init.lin.max 20
########################
# Inizializzatore Peak
#init.lin it.ncorti.p2p.PeakInitializer
#init.lin.protocol count
#init.lin.value SIZE/2
########################
# Inizializzatore Random
#init.lin it.ncorti.p2p.LinearInitializer
#init.lin.protocol count
########################

########################
# INSERIRE GLI INIZIALIZZATORI DA AVVIARE
include.init rnd bec
########################



########################################################################
###### CONTROLLORI
########################################################################

########################
# Controller Statitische
control.stat it.ncorti.p2p.Statistics
control.stat.protocol count
# Modalita' Silent {true, false}
#control.stat.silent true
########################

########################
# Controller Debug
# Ricordarsi di attivare l'inizializzatore CountBeaconInitializer
#control.debug it.ncorti.p2p.Debugger
#control.debug.protocol count
########################



########################################################################
###### DINAMICITA'
########################################################################

########################
#control.dynamic DynamicNetwork
# Numero di nodi da aggiungere/rimuovere
#control.dynamic.add -1
# Ciclo di partenza
#control.dynamic.from 50
# Ciclo di fine
#control.dynamic.until 51
########################
```
