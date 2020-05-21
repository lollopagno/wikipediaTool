Ci devono 2 tipi diversi di attori -> Players e Arbitro

Attore Player:
	1) All'inizio del gioco deve scegliere il suo numero
		a. Riceve dall'arbitro il messaggio START per capire quando deve generare la stringa e di quale lunghezzaSS
		b. Deve comunicare all'arbitro quando è pronto tramite il messaggio "READY"
		c. Cambio di stato -> Da inizializzazione a pronto
	2) Tentativo da svolgere
		a. Quando il player riceve il messaggio "START_TURN", genera una stringa random.
		b. La invia con il messaggio "GUESS" contenente la stringa generata al player target scelto a caso
	3) Tentivo ricevuto
		a. Sono stato scelto come target
		b. Devo comunicare all'attore che mi ha interpellato la soluzione (giusta o sbagliata)
	4) Risposta ricevuta
		a. Aggiorno le mie possibili risposte
		b. Comunico all'arbitro il messaggio "FINE_TURNO" che ho svolto il mio turno
		

Arbitro:
	1) Riceve il messaggio START_GAME dalla View per capire quando iniziare il gioco oppure no.
	2) Quando tutti i player sono pronti (deve avere un contatore interno), quindi quando riceve da tutti un messaggio "READY" che sono pronti, fa partire il primo turno (altro contatore)
	3) Genera l'ordine casuale dei player e invia il messaggio di tentativo al player stabilito
	4) Riceve il messaggio di FineTurno dal player che aveva svegliato e sveglia il prossimo
		a. Se non ci sono altri player da interpellare allora rincomincia il turno.

View:
Gestione della view di selezione partecipanti (già fatta)
Gestione della view di gestione partecipanti:
	1) Permettere di visionare lo stato dei partecipanti
	2) Deve essere aggiornata quando il modello invia un segnale di view aggiornata
Questa view potrebbe essere considerata come un attore.

Judge Actor:
Gestione dell'arbitro. Deve ricevere i messaggi dei giocatori, coordinarli e informare la view dei cambiamenti che vengono fatti.

Player Actor:
Gestione del giocatore. Generano la propria sequenza (immutabile) e una possibile sequenza per ogni altro giocatore, si scambiano i tentativi e informano la view se indovinano qualcosa.

Model:
Sequenza:
Lista di numeri che possono essere indovinati oppure no. Dentro a ogni sequenza devono esserci 