# EsameProgrammazioneOggetti
## Introduzione
Progetto **Java** per l'esame di Programmazione ad Oggetti del corso di Ingegneria Informatica e dell'Automazione dell'[Università Politecnica delle Marche - UNIVPM](https://www.univpm.it/Entra/) (a.a. 2020/2021).

La versione di Java utilizzata per lo sviluppo del progetto è la 11 (JRE System Library JavaSE-11).

#### Autori
Sono autori del progetto gli studenti del secondo anno di corso:
- Federico Tombari (**matricola**: , **email**: @studenti.univpm.it) - Contributo: %
- Joshua Sgariglia (**matricola**: 1092411, **email**: S1092411@studenti.univpm.it) - Contributo: %

#### Requisiti
Per poter utilizzare il programma è necessario [scaricarlo](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/archive/main.zip) sul proprio computer e avviarlo. Essendo scritto in linguaggio Java, per poterlo eseguire è necessario avere una JVM (Java Virtual Machine) installata sul proprio sistema operativo. Nel caso si voglia solo eseguire il programma è sufficiente [scaricare il JRE](https://www.java.com/it/download/manual.jsp) (Java Runtime Enviroment). E' possibile verificare la propria versione di Java da console ed eseguendo il seguente comando (valido nei sistemi Windows e Linux):
```bach
    java -version
```
Nonostante sia possibile eseguire il programma da console dopo essersi posizionati nella cartella che contiene la classe principale, consigliamo l'utilizzo di un IDE (per esempio [Eclipse](https://www.eclipse.org/downloads/)).

## 1) Descrizione e struttura
#### Descrizione generale
Il progetto consiste nello sviluppo di un [Web Service di tipo REST](http://databasemaster.it/introduzione-alle-api-rest/), realizzato anche grazie a Spring Boot, che fornisce agli utenti informazioni in tempo reale e statistiche sulle **condizioni meteorologiche** di città. Ciò avviene attraverso delle API che fanno da ponte tra il Client e il nostro server. L'utente potrà quindi effettuare delle chiamate alla nostra API per ottenere tali dati. Tutte le informazioni, compresi eventuali errori dovuti a parametri errati o errori interni del programma, sono fornite in JSON, che si sta affermando come il formato più diffuso per lo scambio di dati strutturati. Nel caso in cui, al momento dei rilascio, i dati non siano in una struttura dati JSON, l'oggetto in cui si trovano memorizzati sarà convertito automaticamente.

I dati sono ottenuti tramite chiamate alle API pubbliche di OpenWeather; in base alla situazione, saranno selezionati (non tutte le informazioni meteorologiche fornite da OpenWeather vengono utilizzate) per essere inviati all'utente, oppure saranno salvati in un database locale per una futura elaborazione di statistiche.

#### Suddivisione in package
Si può osservare la struttura del progetto seguendo [il seguente percorso](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it): 
```path
    /ProgettoOpenWeather/src/main/java/it
```
Il package "it" contiene la classe principale, ProgettoOpenWeatherApplication.java, nella quale è presente il "main" dell'applicazione, e i sette package del programma. Di seguito, una tabella descrittiva dei package:
| Nome del package |  Classi  | Descrizione |
| :------------------- | :------: | :----------------- |
| [configuration](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/configuration) | 2 | Contiene le classi che gestiscono i **parametri di configurazione** del programmma (la loro lettura, inizializzazione ed eventuale successiva modifica) e gli errori che possono verificarsi durante l'esecuzione (gestione delle eccezioni, scrittura di un log di errore ecc.).
| [controller](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/controller) | 3 | Si tratta del package dei **Controller** di tipo REST: è in queste classi che vengono ricevute le chiamate degli utenti alla nostra API, attraverso dei Mapping. |
| [exception](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/exception) | 3 | Package delle **eccezioni personalizzate** |
| [filter](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/filter) | 7 | Include le classi necessarie al **controllo**, **filtraggio** e **ordinamento** dei dati per il calcolo delle statistiche, attingendo alle informazioni presenti nel database |
| [model](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/model) | 5 | Questo package comprende classi utili per l'inserimento dei dati selezionati in **strutture dati**, talvolta eseguendo anche dei controlli, e per poter fornire all'utente i dati in maniera strutturata |
| [service](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/service) | 3 | Contiene le classi i cui metodi sono direttamente chiamati da quelli dei controller. Sono queste classi ad utilizzare istanze e metodi delle classi degli altri package per soddisfare una certa richiesta dell'utente o del'amministratore. Nessuna informazione arriva dal resto del programma ai controller senza passare prima per una classe di questo package. | 
| [utilities](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/main/java/it/utilities) | 4 | Classi di utilità per svolgere i compiti più vari, dal **download dei dati** al calcolo delle statistiche, dalla **gestione del database** a quella del thread regolato da un timer. | 

Per ottenere informazioni su singole classi, costruttori, metodi o attributi, è possibile consultare la [Javadoc](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/doc/it) generata automaticamente da Eclipse.

#### Suddivisione concettuale
A livello concettuale, il progetto è suddiviso in tre sezioni: "Weather", "Stats" e "Admin". Questa partizione si riscontra nelle classi dei package "controller" e "service" (si veda il Class Diagram).
- **Weather**: questa sezione riguarda tutto ciò che è relativo alla richiesta e all'acquisizione di informazioni meteorologiche su città, che però non vengono salvate nel database o utilizzate per elaborare statistiche. Fornisce infatti dati su alcune condizioni meteo di quel momento relative a una o più città. Una volta elaborati i parametri immessi dall'utente, i dati vengono scaricati tramite una chiamata alle API di OpenWeather, inseriti in apposite strutture dati temporanee per ottenere una certa strutturazione, che rimarrà invariata al momento della conversione automatica in JSON, e infine forniti direttamente all'utente. Le classi del package "model" vengono principalmente usate in questa parte del programma.
- **Stats**: si tratta della parte più complessa del progetto, quella relativa all'elaborazione di statistiche. Il programma include un thread regolato da un timer che periodicamente (di default ogni due ore senza ritardo iniziale, ma è possibile modificare questo valore sia in fase di inizializzazione che a runtime) scarica e salva nel database, aggiornandolo, alcune condizioni meteorologiche relative a una listà di città, la quale costituisce uno dei parametri di configurazione. Nel momento in cui un utente fa richiesta di statistiche, il database viene caricato in locale; è a questo punto che entra in gioco il package "filter", il quale contiene tre tipi di operatori, che hanno funzioni di controllo (dei filtri scelti), filtraggio (dei dati disponibili) e ordinamento (delle statistiche), rispettivamente. Tutto il processo di calcolo delle statistiche, dal caricamento del database all'ordinamento, avviene in strutture JSON. Ciò comporta un risparmio di tempo rispetto al caso in cui tutti i dati debbano essere estratti successivamente al parsing e inseriti in modelli appositi (anche il database è in formato JSON). Inoltre, la struttura in cui si trovano i dati viene modificata in maniera dinamica, e utilizzare in tal senso JSONObject e JSONArray combinati rende il compito molto semplice.
- **Admin**: è prevista anche una sezione riservata agli admin, come mostrato nello Use Case Diagram. Avendo il progetto finalità didattiche, le operazioni riservate agli admin sono di fatto utilizzabili in maniera non differente da quelle degli utenti base. Le chiamate alla nostra API relative a questa parte permettono, a runtime, di visualizzare re-inizializzare tutti i parametri di configurazione da file, oppure di sceglierne alcuni in particolare. Ciò è molto utile in caso di errori interni o di parametri inizialmente errati. Inoltre è possibile eseguire azioni relative al thread regolato dal timer: avviarlo (o riavviarlo, nel caso in cui sia già in funzione), terminarlo, vederne lo stato e i parametri specifici (delay iniziale e intervallo di tempo tra due esecuzioni del thread).

## 2) Diagrammi UML
I diagrammi UML si trovano [al seguente percorso](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams):
```path
    /ProgettoOpenWeather/diagrams
```
### 2.1) Use Case Diagram
![usecasediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/UseCaseDiagram.jpg)

La suddivisione concettuale può essere individuata già nel diagramma dei casi d'uso, andando dall'alto verso il basso: "Weather", "Stats" e infine "Admin". Nel diagramma sono presenti due attori umani, oltre all'API di OperWeather: un attore denominato Utente può fare richieste per quanto riguarda le prime due parti (richiesta di dati attuali o di statistiche). Un altro attore, l'admin, è un caso particolare di utente poiché ha la possibilità di effettuare richieste aggiuntive, riservate a lui, relative alla terza parte del progetto. I dati sono forniti dall'API di OpenWeather sia nel caso in cui l'utente ne faccia richiesta, sia nel caso dell'aggiornamento del database.

Tornando all'utente, possono essere seguite diverse strade nel caso di richieste sul meteo del momento. L'utente può infatti richiedere le informazioni meteo di una città oppure di un box di città. Per quanto riguarda il box, egli può definirne l'area geografica direttamente ("Richiesta meteo area geografica") oppure attraverso una città (prendendone le coordinate) e un margine di errore ("Richiesta meteo città" -> "Definizione margine di errore"). In questo caso, dalla prima "riga" del diagramma si ricade nella seconda. A meno di eccezioni, si giunge comunque allo Use Case chiamato "Acquisizione meteo attuale".

### 2.2) Class Diagram
![classdiagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/ClassDiagram.jpg)

Diagramma delle classe, in cui sono messe in evidenza le associazioni interne ai package. Per evitare confusione, sono state omesse molte delle associazioni tra classi di package diversi: questo vale specialmente se si pensa alle classi Configuration, ErrorManager e Utilities, che vengono utilizzate da buona parte delle altre. In alto a sinistra è possibile notare la divisione nelle tre sezioni: a ognuno dei tre controller corrisponde una sola classe nel package "service", e ogni classe di "service" non comunica con altri controller al di fuori del rispettivo. Sotto a questi due package è presente "filter", che racchiude tutte le classi coinvolte nell'elaborazione dei dati per il calcolo delle statistiche: le classi CheckerImpl, FiltratorImpl e SorterImpl estendono Operator (da cui acquisiscono principalmente gli attributi) e implementano ognuna un'interfaccia (da cui ottengono dei metodi specifici). Il package "model" include le classi in cui sono inseriti i dati in alcune richieste (usate specialmente nella parte "Weather"). Infine, in basso a destra ci sono le eccezioni personalizzate.

### 2.3) Sequence Diagrams
Di seguito sono riportati i diagrammi delle sequenze divisi in base alle tre parti del progetto.
#### Parte "Weather"
- Caso in cui l'utente richiede le condizioni meteorologiche di una città:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/weather/GET _weather_citySequenceDiagram.jpg)

- Caso in cui l'utente richiede le condizioni meteorologiche di un'area geografica definita tramite le coordinate di una città e un margine di errore:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/weather/GET_weather_cityboxSequenceDiagram.jpg)

- Caso in cui l'utente richiede le condizioni meteorologiche di un'area geografica definita direttamente:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/weather/GET_weather_boxSequenceDiagram.jpg)

#### Parte "Stats"
- Elaborazione dei dati e calcolo delle statistiche:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/stats/POST_statsSequenceDiagram.jpg)

#### Parte "Admin"
- Caso in cui l'admin vuole visualizzare lo stato dei parametri di configurazione:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/admin/GET_configSequenceDiagram.jpg)

- Caso in cui l'admin vuole modificare alcuni parametri di configurazione a runtime:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/admin/POST_configSequenceDiagram.jpg)

- Caso in cui l'admin vuole re-inizializzare i parametri di configurazione da file:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/admin/PUT_configSequenceDiagram.jpg)

## 3) Guida all'utilizzo dell'API
Una volta scaricato il progetto sul proprio dispositivo e dopo averlo avviato tramite console o con l'aiuto di un IDE, è possibile utilizzando attraverso un qualsiasi motore di ricerca tramite l'indirizzo

```path
    localhost:8080/
```
dove "8080" è il numero predefinito di porta. A seguire tutte le chiamate possibili, divise per sezioni.

### 3.1) API "Weather" - meteo attuale

#### 1) Richiesta meteo - città singola
La chiamata si presenta come:
```path
    localhost:8080/weather/city?city=[name]
```
Dove il tipo di mapping è GET.
Vi è un filtro da immettere come "Param":
- name: il nome della città di cui si vogliono ottenere le condizioni meteo attuali.

**Un esempio:**

Richiesta:
```path
    localhost:8080/weather/city?city=New York
```
Risposta:
```json
    {
    "name": "New York",
    "coords": {
        "latitude": 40.7143,
        "longitude": -74.006
    },
    "cityWeather": {
        "pressure": 1004.0,
        "humidity": 52.0,
        "temperature": 5.0,
        "visibility": 10000.0,
        "general": "Clear"
    }
}
```
Dove:
- name: il nome della città;
- coords: le coordinate della città;
    - latitude: la latitudine della città;
    - longitude: la longitudine della città;
- cityWeather: il meteo della città;
    - pressure: la pressione in millibar;
    - humidity: l'umidità in percentuale;
    - temperature: la temperatura in gradi Celsius;
    - visibility: la visibilità in metri;
    - general: le condizioni meteo generali.

#### 2) Richiesta meteo - gruppo di città (box definito da città)
La chiamata si presenta come:
```path
    localhost:8080/weather/citybox?city=[name]&errordef=[errorlat,errorlon]
```
Dove il tipo di mapping è GET.
Vi sono filtri da immettere come "Params":
- name: il nome della città di cui si vogliono ottenere le condizioni meteo attuali;
- errorlat: margine di errore della latitudine;
- errorlon: margine di errore della longitudine.

**Un esempio:**

Richiesta:
```path
    localhost:8080/weather/citybox?city=Pescara&errordef=1,1
    ```
Risposta:
```json
    [
    {
        "name": "Montesilvano",
        "coords": {
            "latitude": 42.5039,
            "longitude": 14.138
        },
        "cityWeather": {
            "pressure": 1015.0,
            "humidity": 50.0,
            "temperature": 11.0,
            "visibility": 10000.0,
            "general": "Clouds"
        }
    },
    {
        "name": "Pescara",
        "coords": {
            "latitude": 42.4602,
            "longitude": 14.2102
        },
        "cityWeather": {
            "pressure": 1015.0,
            "humidity": 50.0,
            "temperature": 11.0,
            "visibility": 10000.0,
            "general": "Clouds"
        }
    },
    {...}
]


#### 3) Richiesta meteo - gruppo di città (box definito direttamente)
La chiamata si presenta come:
```path
    localhost:8080/weather/box?boxdef=[minlat,minlon,maxlat,maxlon,zoom]
```  
Dove il tipo di mapping è GET.
Vi sono filtri da immettere come "Params":
- minlat: il valore minimo della latitudine;
- minlon: il valore minimo della longitudine;
- maxlat: il valore massimo della latitudine;
- maxlon: il valore massimo della longitudine;
- zoom: lo zoom (un valore maggiore equivale a più città rilevate).

**Un esempio:**

Richiesta:
```path
    localhost:8080/weather/box?boxdef=42,13,43,15,8
```
Risposta:
```json
    [
    {
        "name": "Pescara",
        "coords": {
            "latitude": 42.4602,
            "longitude": 14.2102
        },
        "cityWeather": {
            "pressure": 1015.0,
            "humidity": 50.0,
            "temperature": 11.0,
            "visibility": 10000.0,
            "general": "Clouds"
        }
    },
    {
        "name": "L'Aquila",
        "coords": {
            "latitude": 42.3506,
            "longitude": 13.3995
        },
        "cityWeather": {
            "pressure": 1005.0,
            "humidity": 61.0,
            "temperature": 6.0,
            "visibility": 2263.0,
            "general": "Clouds"
        }
    },
    {
        "name": "Ascoli Piceno",
        "coords": {
            "latitude": 42.8548,
            "longitude": 13.5749
        },
        "cityWeather": {
            "pressure": 1017.0,
            "humidity": 69.0,
            "temperature": 8.0,
            "visibility": 10000.0,
            "general": "Clouds"
        }
    }
]
}
```
Il significato dei campi è lo stesso che per il primo tipo di chiamata, l'unica differenza è che stavolta abbiamo una lista di città invece che una sola.

### 3.2) API "Stats" - statistiche
### 3.1) API "Admin" - amministrazione


## 4) Gestione delle eccezioni
### ErrorManager
### Error Log

## 5) Aspetti Tecnici
### Test
### Software e strumenti utilizzati
### Librerie esterne
