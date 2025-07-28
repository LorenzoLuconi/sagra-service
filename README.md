# Sagra Service
Servizio REST per la gestione Sagra, per la parte grafica si veda [Sagra Web](https://github.com/LorenzoLuconi/sagra-web).

Progetto creato per la parrocchia di [San Pio X a Massa](https://www.parrocchiasanpioxmassa.it/).

Richiede:
 - Java 21
 - MySQL

Esecuzione in test:

```
./mvnw spring-boot:run -DskipTests
```

Questo crea un servizio sulla porta 8080 con database in memoria H2.

E' disponibile documentazione OpenAPI e Swagger all'indirizzo:
http://localhost:8080/swagger-ui/index.html

*TODO*: Documentazione da completare

## Risorse

### Reparti / Departments
I reparti sono una suddivisione dei prodotti utilizzata principalmente per dividere le stampe degli ordini. 

Esempio di reparti:

 - Cucina
 - Griglia
 - Bar

Le operazioni che si possono fare sulla risorsa sono le classiche operazioni CRUD.

Da notare che la cancellazione è possibile solo se il reparto non è referenziato in alcun prodotto.

### Portate / Courses
Le portate servono per suddividere i prodotti, esempio di portate:
 
- Primi
- Secondi
- Dolce
- Bevande

Le operazioni che si possono fare sulla risorsa sono le classiche operazioni CRUD.

Da notare che la cancellazione è possibile solo se la portata non è referenziato in alcun prodotto.

### Sconti / Discounts
Gli sconti servono per essere applicati negli ordini. Sono previste le operazioni CRUD essenziali. Da notare che negli ordini verrà inserita la percentuale di sconto e non l'id dello sconto che quindi può essere rimosso liberamente.

### Monitors
I monitor servono create delle schermate da visualizzare su eventuali monitor da collocare nelle varie zone della sagra per tenere sotto controllo i prodotti venduti.

A parte le varie operazioni CRUD, c'e' un ulteriore metodo _view_ che rappresenta i dati necessari da mostrare nei monitor (esempio: /v1/monitors/1/view).

Da notare che i prodotti da mostrare in un monitor mantengono l'ordinamento che viene dato al momento della creazione o modifica dello stesso.

### Prodotti / Products
I prodotti sono gli articoli che possono essere venduti/ordinati. Sono possibile le varie operazioni CRUD, ma ci sono alcune operazioni aggiuntive.

#### Quantità prodotti
La quantità di un prodotto è suddivisa in due campi:

 - _initialQuantity_, ovvero la quantità iniziale è inserita dall'operatore 
 - _availableQuantity_, corrisponde dalla quantità disponibile tipicamente calcolata come _initialQuantity_ - somma (prodotti ordinati)

La quantità è da considerarsi come disponibilità nella giornata e al momento non è possibile vedere _initialQuantity_ e _availableQuantity_ per i giorni precedenti.

Tipicamente l'amministratore inizializza le quantità disponibili nel magazzino prima di iniziare le vendite della giornata (_/v1/products/initQuantity_). L'inizializzazione è possibile farla per più prodotti in unica richiesta e si può eseguire solo se non sono stati già effettuati degli ordini nella giornata.

Se per il prodotto "X" si inizializzano delle quantità con valore 100, il prodotto avrà  _initialQuantity_ e _availableQuantity_ impostati a 100.

Differente invece se si applica un variazione delle quantità (_/v1/products/{productId}/updateQuantity_): in questo caso si inserisce una variazione della quantità (_quantityVariation_) per un singolo prodotto: ad esempio indicando una variazione di 10 verrà aggiunto questo valore a _initialQuantity_ e _availableQuantity_ (dall'esempio precedente quindi avremo un valore di 110).

Nel caso di una variazione negativa (esempio -10), è possibile che _availableQuantity_ non abbia disponibilità sufficiente e in questo caso verrà negata la variazione (HTTP code 450).

#### Prodotti collegati
Alcuni prodotti possono avere dei prodotti collegati (campo _parentId_). Un prodotto collegato mantiene un unica quantità disponibile. Questo significa che ogni variazione di quantità viene applicata al prodotto indicato nel _parentId_.

Questo serve per poter inserire lo stesso prodotti in più reparti/portate o alcune variazione dello stesso prodotto. A titolo di esempio:

 - Spaghetti al pomodoro
 - Spaghetti al pomodoro con formaggio (collegato al prodotto precedente)

Qualsiasi ordine di uno dei due prodotti andrà a diminuire la quantità del prodotto principale (Spaghetti al pomodoro).

Da notare che:
 - Non è possibile avere riferimenti a prodotti collegati a più livelli (P1 <- P2 <- P3). E' possibile avere più prodotti che referenziano lo stesso prodotto (P1 <- P2 e P1 <- P3 ...)
 - Non è possibile eliminare o variare il prodotto collegato di un prodotto. 


#### Blocco vendita
A prescindere dalla quantità disponibile è possibile bloccare un prodotto alla vendita (_/v1/products/{productId}/sellLock e sellUnlock).

I prodotti bloccati alla vendita potranno fare tutte le operazioni tranne essere inseriti in un ordine

###  Ordini / Orders
Gli ordini o le vendite sono la parte principale del servizio e sono previste le tradizionali operazioni CRUD.

Ogni operazione su un ordine modifica le quantità dei prodotti disponibili e quindi si possono ottenere in alcuni casi degli errori legati alla disponibilità del prodotto richiesto o anche errori legati a prodotti bloccati alla vendita.

E' importante sottolineare che gli ordini sono memorizzati solo se non ci sono errori nella richiesta(ovviamente), ma anche se le quantità richieste sono disponibili. Non è previsto al momento il salvataggio di ordini parziali o meglio quantità parzialmente assegnate. A titolo di esempio se ordino un prodotto di cui ho _availableQuantity_ = 1 e ordino il prodotto indicato 2 prodotti, questo bloccherà l'ordine per intero non registrando la richiesta degli altri prodotti ordinati.

### Statistiche / Stats
*TODO*

### Autenticazione
Anche se è già prevista una tabella degli utente, l'autenticazione non è ancora attiva

*TODO*







