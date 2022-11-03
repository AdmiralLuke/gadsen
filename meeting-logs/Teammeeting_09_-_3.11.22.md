# Teammeeting 09 - 3.11.22

:::info
Nächstes Meeting: 10.11.22
Protokollant: Niggo 
Anwesend: Alex, Corny, Luke, Niggo, Oliva
Abwesend: -

Anfang: 9:18 Uhr
Ende: 10:16 Uhr

:::

### Agenda
* Sprint auswertung
* neuen Sprint festlegen
* Christian Treff
* What has changed

### ...bis zu diesem Meeting..

- Sprint bearbeiten
- Tasks in Completed oder Testing


### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches

#### Top 1.1 Sprint zusammenfassen

- sind gut vorangekommen auf dem Gamejam

- Format für Assets Task bleibt vorerst noch

FUTURE IS HERE

- Pipeline ist auf dem Git
- pipeline, core und desktop seperat getestet/buildet


#### Top 1.2 neuen Sprint festlegen

Wir müssen uns auf einen Namen einigen :D.


G.A.D.S.E.N - Offizieller Name für Titel- Ingame

GADSEN - Name für Erwähnung im Code, damit nichts kaputt geht



**Map**

- wie soll die Map vorliegen
    - wie abspeichern
    - Tool nutzen?


#### Top 1.3 Christians Treffen

- Tobias hatte darauf hingewiesen, dass wir uns bald mit den Christians zusammensetzen sollten. 

### Top 2 - Design | Spiel

- ...

### Top 3 - Programmierung | Spiel

#### Top 3.1 Zusammenfassung von dem was im Sprint geschehen ist.

**Die Asset-Pipeline**
- hat upgrades bekommen
- läuft jetzt auf dem git
- falls veränderungen vorgenommen wurden wird diese automatisch beim Starten des Spiels ausgeführt

- Assets in anderen Ordner, werden einfach so in resources kopiert
- exclude files für diese Dateien werden beachtet


**Asset Manager**
- kümmert sich darum, dass der Output vom Texture Packer und andere benötigten resourcen geladen werden
        
**Menu Screen**
- besitzt einen Table
    - dieser enthält dann unsere gewünschten Buttons für die Einstellungen etc
        
    
**Ingame Screen**
   - ruft den Animator auf, kümmert sich um das rendern des Geschehens
   - besitzt die Hud-Stage, welche später auch für Input zuständig ist
   
**GADS**
- kümmert sich derzeit um "alles"
    - wechselt zum Menuscreen
    - startet den Assetmanager
    - kommuniziert mit simulation und reicht den gamestate weiter
- wird aufgerufen wenn das Programm gestartet wird
 

**ActionLog**
- ist jetzt eine ArrayDoubleEndedQUEUE
    - kein size limit
    - hat Funktionen die Luke brauchte 

**Brauchen wir vllt doch eine Manager Klasse**
- evtl hilfreich für das Starten als Headless Prozess

- evtl besser als alles in das UI-Package zu schmeißen
    - da wir dann beim starten ohne Interface Probleme bekommen könnten

### Top 4 - Gemeinsames | Spiel


- ...



### Top 5 - ToDos
#### Top 5.1 - bis zum nächsten Meeting


#### Top 5.2 - Zeitlich relevantes TO-DO


**Luke**

- [ ] Christians bzgl Meeting anschreiben sobald etwas handfestes vorhanden ist :)

**Olivia**
- [ ] Concept Art

**Niggo**
- [ ] Concept Art



**Alex**
- [ ] Concept Art

**Corny**

- [ ] **UNITY PROTOTYP** pmbok
