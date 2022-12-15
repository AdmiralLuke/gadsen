# Teammeeting 13 - 1.12.22

:::info
Nächstes Meeting: 8.12.22
Protokollant: Niggo
Anwesend: Corny *(wurde am Anfang komplett alleine gelassen, nur weil er ein paar Minuten zu früh da war)*, Niggo, Luke, Olivia, Alex
Abwesend: Yasmin, Kevin, Fabia Nossel

Anfang: 9:25 Uhr
Ende: Uhr
:::

### Agenda

- Sprint auswerten
- neuen Sprint festlegen


### ...bis zu diesem Meeting..

- Sprint bearbeiten

### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches

#### Top 1.1 Sprint zusammenfassen

- Menü Möglichkeit Bots auszuwählen passt sich jetzt dynamisch der Anzahl der Bots an

![](https://md.farafin.de/uploads/upload_ae10eb9c9b505dbb72fe7d22f9f1d72a.png)

![](https://md.farafin.de/uploads/upload_9b8832beef04a934f9265b6b10c306cb.png)


- grobes InputHandling für Character in der HudStage Klasse enthalten
    - bleibt nur die Frage wo es hingeleitet wird
    - sollte beim derzeitigen Player und Charakter landen
    
#### Top 1.2 neuen Sprint festlegen

Neue Tasks

- Menu Button anpassen
    - Anzahl der Bots -> Teamanzahl
    - Teamanzahl -> Anzahl der Charaktere pro Team


- Zuckerstangensprite

- Keks
    - fliegt

- Karte für die Weihnachtsaufgabe 

- Menu anpassen für Weihnachtsaufgabe
    - idee: über Spielmodus auswählen/festlegen, wie viele Teams zur auswahl stehen
        - nur 1 Team zur auswahl
        - 4 teams werden mit settings übergeben
            - 3 idle/bzw. extra bot klasse(weihnachtsbot)

:::danger
alle Tasks im HackNPlan muss bis zum nächsten Mal in Testing/Completed
*(eventuell sind manche Tasks nicht für Weihnachten essentiell)*
*nicht auf Lücke lernen*

Bei Problemen bescheid geben.
:::

*Luke erzählt Lügen*

#### Top 1.3 HackNPlan

Alles was in completed steht, wurde gemerged.
Also merge-requests schreiben ^^.


### Top 2 - Design | Spiel

- Für die Fall-Animation wäre es gut wenn wir die Sprite von der Software aus drehen können, geht das?

*Müsli hat schon wieder Dinge versprochen*

- eventuell Probleme mit "Artefakten" 
- sieht dann vermutlich nicht so gut aus
    - da wenig pixel vorhanden sind

### Top 3 - Programmierung | Spiel

- Mehr Docs generiert
- @Weihnachtsaufgabe Tag in den Docs für alles was in der Aufgabe benötigt wird
- keine Tests mehr in den Docs \o/
- alles was public und protected ist landet in der doc
    - auf deutsch dokumentieren

**ERIGNISS**


- eventuell Probleme beim Interrupten der Spieler
    - wenn man am Ende des Zuges den Spieler unterbricht, gibt es evtl. Probleme, da bspw. angefangene GameState Änderungen zu inkonsistenem GameState führen könne
    - Idee, mit seperatem Thread lösen
    - GameController nutzen


#### Manager

- ui startet/kreirt Manager
- startet den IngameScreen/nicht
- ruft die Simulation auf
- dient als Mittler zwischen Animator und Simulation/ui

### Top 4 - Gemeinsames | Spiel

- ...


### Top 5 - ToDos
#### Top 5.1 - bis zum nächsten Meeting


#### Top 5.2 - Zeitlich relevantes TO-DO

**Luke**
- [ ] interne Docs

**Olivia**
- [ ] Concept Art
- [ ] Merge-Request schreiben

**Niggo**
- [ ] Concept Art
- [x] Menu anpassen
    - [x] Teamanzahl zu Charakter pro Spiel
    - [x] Anzahl Bots = Teamanzahl    

**Alex**
- [ ] Concept Art

**Corny**

- [ ] **UNITY PROTOTYP** pmbok