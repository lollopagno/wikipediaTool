# Puzzle client

TODO Client:
- [ ] Schermata di selezione del proprio nome.

    Deve essere possibile tramite un pulsante registrarsi sul server all'indirizzo: POST https://java-travis-ci.herokuapp.com/players/<nome_player>
- [ ] Schermata di visualizzazione di tutti i giocatori presenti.
    Tramite la chiamata GET https://java-travis-ci.herokuapp.com/players è possibile scaricare l'elenco di tutti i player già presenti.
    Ogni 30 secondi un job dovrebbe scaricare una lista aggiornata dei giocatori sul server oppure tramite un pulsante per forzare l'aggiornamento.
    
Le due registerView soprascritte potrebbero essere anche la stessa.

- [ ] Schermata di gioco attuale
    1. Se una casella è già occupata da un altro giocatore, deve essere circondata di giallo.
    2. Se seleziono una casella già occupata, non devo fare nulla. Altrimenti, devo fare una chiamata a
    PUT https://java-travis-ci.herokuapp.com/take/<player>/<id_casella> per dichiararla come occupata.
    3. Quando seleziono un'altra casella dopo averne già selezionata una, faccio una chiamata a PUT https://java-travis-ci.herokuapp.com/move/<player>>/<id_casella>/<x>/<y>.
    Se la casella di destinazione non era già occupata, allora effettuo lo scambio (implementare anche sul server).
    4. Ad ogni mossa, fare una chiamata a GET https://java-travis-ci.herokuapp.com/check per controllare lo stato del puzzle.
    
TODO Server:
- [ ] Alla chiamata POST https://java-travis-ci.herokuapp.com/players/<nome_player> devo tornare un booleano a seconda che
il giocatore sia stato aggiunto correttamente oppure no.
- [ ] Creare chiamata DELETE https://java-travis-ci.herokuapp.com/players/<nome_player> per fare il logout.
- [ ] Generare la griglia correttamente (prendere esempio dall'applicazione client)
- [ ] La chiamata PUT move deve effettuare lo scambio e non solo il movimento
- [ ] Creare la chiamata GET check