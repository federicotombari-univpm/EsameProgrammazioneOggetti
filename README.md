# EsameProgrammazioneOggetti
## Introduzione
Progetto **Java** per l'esame di Programmazione ad Oggetti del corso di Ingegneria Informatica e dell'Automazione dell'[Università Politecnica delle Marche - UNIVPM](https://www.univpm.it/Entra/) (a.a. 2020/2021).

La versione di Java utilizzata per lo sviluppo del progetto è la 11 (JRE System Library JavaSE-11).

#### Autori
Sono autori del progetto gli studenti del secondo anno di corso:
- Federico Tombari - Contributo: 50%
- Joshua Sgariglia - Contributo: 50%

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
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/weather/GET_weather_citySequenceDiagram.jpg)

In seguito alla chiamta "GET /weather/city" WeatherService avendo come parametro il nome di una città, ritorna le informazioni di quella città.

- Caso in cui l'utente richiede le condizioni meteorologiche di un'area geografica definita tramite le coordinate di una città e un margine di errore:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/weather/GET_weather_cityboxSequenceDiagram.jpg)

In seguito alla chiamata "GET /weather/citybox" WeatherService ritorna all'utente le informazioni di una area geografica delimitata da coordinate e definita da un metodo della classe WeatherService a partire dal nome di una città. Questo permette di ricavare le coordinate dell'area e le rispettive informazioni sul meteo. Determinare l'area è possibile attraverso metodi che grazie alla definizione di un "errore" aumentano le coordinate della città desiderata creando un'area di interesse.

- Caso in cui l'utente richiede le condizioni meteorologiche di un'area geografica definita direttamente:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/weather/GET_weather_boxSequenceDiagram.jpg)

In seguito alla chiamata "GET /weather/box" WeatherService genera un'area di interesse a partire dalle coordinate. L'utente potrà visualizzare le informazioni meteo delle città più importanti in quell'area.

#### Parte "Stats"
- Elaborazione dei dati e calcolo delle statistiche:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/stats/POST_statsSequenceDiagram.jpg)

In seguito alla chiamata "POST /stats" StatsService elabora le statistiche nel modo richiesto dall'utente. In primo luogo verifica i filtri, poi carica i dati nel database e successivamente li filtra in base a ciò che l'utente ha specificato: i dati possono essere filtrati in base alla media o alla varianza di pressione, umidità, temperatura o visibilità, o ordinati alfabeticamente rispetto ai nomi delle città. Per fare ciò vengono chiamati dei metodi che modificano la struttura con i dati e la riordinano.
Per quanto riguarda media e varianza, queste vengono calcolate nell'ultima tappa del filtraggio, inoltre vengono arrotondate ad una valore decimale (ai millesimi) tramite un metodo della classe Utilities, nell'omonimo package.

#### Parte "Admin"
- Caso in cui l'admin vuole visualizzare lo stato dei parametri di configurazione:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/admin/GET_configSequenceDiagram.jpg)

In seguito alla chiamata "GET /config" AdminService genera uno screenshot nel quale vengono salvati tutti i parametri di configurazione nel momento in cui viene richiesto. Successivamente vengono restituiti all'utente per poterli visualizzare.

- Caso in cui l'admin vuole modificare alcuni parametri di configurazione a runtime:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/admin/POST_configSequenceDiagram.jpg)

In seguito alla chiamata "POST /config" AdminService permette la modifica dei parametri di configurazione senza dover riavviare il programma. Questo avviene tramite la chiamata di diversi metodi, in base ai parametri di configurazione scelti da modificare, che come prima cosa fanno dei test per verificare che il nuovo parametro sia corretto e successivamente lo vanno a modificare, fornendo all'utente i nuovi parametri.

- Caso in cui l'admin vuole re-inizializzare i parametri di configurazione da file:
![sequencediagram](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/diagrams/sequenceDiagrams/admin/PUT_configSequenceDiagram.jpg)

In seguito alla chiamata "PUT /config" Admin Service reinizializza i parametri di configurazione mostrandoli all'utente tramite uno screenshot.

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

**Esempio**

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

**Esempio**

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
```

Il significato dei campi è lo stesso che per il primo tipo di chiamata, l'unica differenza è che stavolta abbiamo una lista di città invece che una sola.

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

**Esempio**

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
#### Filtri
Per la parte stats vi è una sola chiamata, con mapping POST.
La chiamata si presenta come:
```path
    localhost:8080/stats?cities=[city1,city2,...]&weather=[type1,type2,...]&periodicity=[days]&datespan=[startdate,enddate]&sorting=[sorting]
```
Vi sono filtri da immettere come "Params":
- city1,city2,...: le città di cui si vogliono ottenere le statistiche;
- type1,type2,...: le condizioni meteo di cui si vogliono ottenere le statistiche;
- days: la periodicità (in giorni) con cui si vogliono ottenere le statistiche;
- startdate,enddate: le date di inizio e di fine del periodo;
- sorting: l'ordinamento finale delle statistiche.

Ecco tutti i valori possibili per i parametri:
- cities: una o più città tra quelle predefinite (vedi file di configurazione). Se nessuna città è scelta, verrano considerate tutte quelle predefinite;
- weather: uno o più tra "pressure" (oppure "prs"), "humidity" (oppure "hum"), "temperature" (oppure "tmp") e "visibility" (oppure "vis"). Se nessuno è scelto, verranno considerati tutti e quattro;
- periodicity: un valore numerico intero positivo; "daily", "weekly" e "monthly" sono valori predefiniti;
- datespan: date di inizio e di fine secondo il formato "yyyy-MM-dd";
- sorting:
    - tipo 1) diviso in tre parti "[1][2][3]", dove:
        - la prima, "max" oppure "min", per ordinare in ordine descrescente o crescente di valore;
        - la seconda, "Prs", "Hum", "Tmp" oppure "Vis", per scegliere la condizione meteo in base al quale ordinare;
        - la terza, "Avg" oppure "Var", per ordinare in base alla media oppure alla varianza.
    - tipo 2) in alternativa, si può ordinare alfabeticamente in base ai nomi delle città:
        - "atoz" oppure "AtoZ" per l'ordine alfabetico;
        - "ztoa" oppure "ZtoA" per l'ordine alfabetico inverso.

#### Esempio
Richiesta:
```path
    localhost:8080/stats?cities=Fano&periodicity=1&datespan=2021-01-10,2021-01-14&sorting=maxPrsAvg&weather=pressure
```
Risposta:
```json
    [
    {
        "periodStartTime": "2021-01-10_00:00:00",
        "periodEndTime": "2021-01-10_23:59:59",
        "list": [
            {
                "name": "Macerata",
                "weatherStats": {
                    "pressure": {
                        "average": 1019.4,
                        "variance": 0.24
                    }
                }
            },
            {
                "name": "Fano",
                "weatherStats": {
                    "pressure": {
                        "average": 1014.2,
                        "variance": 0.16
                    }
                }
            }
        ]
    },
    {...}
    {
        "periodStartTime": "2021-01-14_00:00:00",
        "actualEndTime": "2021-01-14_23:59:59",
        "periodEndTime": "2021-01-14_23:59:59",
        "list": [
            {
                "name": "Macerata",
                "weatherStats": {
                    "pressure": {
                        "average": 1012.0,
                        "variance": 13.5
                    }
                }
            },
            {
                "name": "Fano",
                "weatherStats": {
                    "pressure": {
                        "average": 1009.875,
                        "variance": 3.609
                    }
                }
            }
        ]
    }
]
```
Si noti che nell'ultimo elemento compaiono due elementi di fine data: questo perché il periodo scelto non è sempre divisibile esattamente per la periodicità, per cui
- "actualEndTime" indica la data di fine effettiva;
- "periodEndTime" indica la data di fine dell'ultimo periodo definito.

### 3.3) API "Admin" - amministrazione
Per la lettura, modifica o inizializzazione di configuration
La chiamata nei tre diversi casi è sempre del tipo:
```path
    localhost:8080/config
```
Variano tuttavia il mapping e i parametri da immettere

#### Mapping GET
Nessun parametro. Il JSON restituito è del tipo:
```json
{
    "files": {
        "configfile": "config.json",
        "datafile": "database.json"
    },
    "numeric": {
        "logcounter": 0,
        "delay": 7200,
        "periodicity": 10,
        "initialdelay": 0,
        "zoom": 10,
        "errorwidth": 2
    },
    "citylist": [
        "Ancona",
        "Pesaro",
        "Fano",
        "San Benedetto Del Tronto",
        "Ascoli Piceno",
        "Senigallia",
        "Civitanova Marche",
        "Macerata",
        "Jesi",
        "Fermo"
    ],
    "main": {
        "unit": "config.json",
        "apikey": "56989104be7410276956586c1fb09bf6",
        "city": "Ancona",
        "startdate": "2021-01-10"
    }
}
```
Dove:
- "files": nomi dei file
    - "configfile": nome del file di configurazione
    - "datafile": nome del database
- "numeric": parametri di configurazione numerici
    - "logcounter": contatore dei log di errore scritti
    - "delay": ritardo di base del thread gestito dal timer
    - "periodicity": periodicità di default
    - "initialdelay": ritardo iniziale del thread gestito dal timer
    - "zoom": zoom di default
    - "errorwidth": errore di default per le coordinate geografiche
- "citylist": lista di città di default
- "main": parametri di configurazione principali
    - "unit": sistema di misura
    - "apikey": API Key per poter effettuare chiamate a OpenWeather
    - "city": città di default
    - "startdate": data di inizio di default

#### Mapping PUT
Metodo per inizializzare i parametri di configurazione da file. Non richiede parametri in input. Il JSON restituito (dopo che i parametri sono stati aggiornati) è lo stesso della chiamata precedente.

#### Mapping POST
Metodo per inizializzare alcuni parametri di configurazione, controllando che siano corretti. Viene richiesto un body in JSON he può avere i seguenti campi:
- "apikey": nuovo valore dell'API Key;
- "city": nuovo nome della città di default;
- "startdate": nuova data di inizio di default;
- "unit": nuovo sistema di misurazione;
- "database": nuovo nome del file database;
- "zoom": nuovo valore dello zoom del box.

**Esempio**
Body:
```json
{
    "zoom":12,
    "city":"Fermo",
    "apikey": "wrongKey"

}
```
Risposta:
```json
{
    "errorlist": [
        {
            "errorId": 306,
            "info": "it.exception.InvalidParameterException",
            "message": "The chosen API Key isn't valid, try with another one",
            "timestamp": "2021-01-21_23:20:57"
        }
    ],
    "message": "No internal errors occurred",
    "info": "Updated parameters: default City, default Zoom."
}
```
Dove:
- "errorlist": lista delle modifiche fallite (vedi gestione delle eccezioni);
- "message": messaggio sull'esito della chiamata
- "info": lista delle modifiche andate a buon fine.

#### Gestione del thread regolato da un timer
La chiamata, con mapping POST, è del tipo
```path
    localhost:8080/tiemrtask/{action}
```
Dove:
- "action": azione da eseguire, che può assumere i valori
    - "start" per avviarlo;
    - "stop" oppure "end" per terminarlo;
    - "perform" per eseguire il thread una volta sola;
    - "status" oppure "info" per ottenere le informazioni relative.
Vi è inoltre un parametro booleano "forced", non necessario e utile sono nel caso in cui "action" sia "start", per riavviare forzatamente il thread nel caso sia già in esecuzione.

**Esempio**
Body:
```path
localhost:8080/timertask/start?forced=false
```
Risposta:
```json
{
    "result": "Timed thread was not restarted",
    "message": "No internal errors occurred",
    "info": "Timed thread was already running"
}
```

## 4) Gestione delle eccezioni
### ErrorManager
Le eccezioni sono gestite tramite "try-catch" e "throw(s)"
Nel package "configuration" è presente la classe ErrorManager: questa è utilizzata in modo particolare nei "catch" presenti nelle classi "service": infatti, ogni volta che viene lanciata un eccezione viene creata e ritornata all'utente una nuova istanza di ErrorManager, che varia in base al tipo di eccezione. Nel caso si tratti di un errore interno di funzionamento, ErrorManager scrive anche un error log.

**Esempio**
Chiamata che lancia un'eccezione:
```path
    localhost:8080/weather/city?city=nonesiste
```
Risposta:
```json
{
    "errorId": 305,
    "info": "it.exception.DataNotFoundException",
    "message": "Web server returned no data, city name might be invalid",
    "timestamp": "2021-01-21_23:34:51"
}
```
Dove:
- "erroId": codice dell'errore (personalizzato)
- "info": tipo di eccezione
- "message": messaggio dell'errore
- "timestamp": data in cui è stata lanciata l'eccezione

### Error Log
Al seguente percorso:

https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/archive/error%20logs

è disponibile un archivio di log di errori che ci sono capitati durante le realizzazione del progetto.

## 5) Aspetti Tecnici
### Test unitari
I test unitari si trovano in una loro sezione a parte, divisi in quattro classi in base al tipo di test. Il percorso è [il seguente](https://github.com/federicotombari-univpm/EsameProgrammazioneOggetti/tree/main/ProgettoOpenWeather/src/test/java/it):

```path
    /ProgettoOpenWeather/src/test/java/it
```

### Software e strumenti utilizzati
- Eclipse (IDE) - principale
- Visual Studio Code (IDE) - per il README.md
- Spring framework (Spring Boot, Spring Initializer, ...)

### Librerie esterne
- JSON Simple - versione 1.1.1
