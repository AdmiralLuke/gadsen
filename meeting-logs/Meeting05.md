Teammeeting 05 - 06.10
--
:::info 
Nächstes Meeting: 13.10.22
Protokollant: Niggo
Anwesend: Luke, Olivia, Corny, Alex, Niggo
Abwesend: -

Anfang: 11:18 Uhr
Ende: 12:24 Uhr
::: 

[**HackNPlan**](https://app.hacknplan.com/p/173169/kanban?categoryId=-1&boardId=0)
[**GitHub Spiel**](https://github.com/AdmiralLuke/G.A.T.S)

### Agenda

- User Story im HackNPlan begonnen
- Zusammenfassung der "Main"-Branches
- Beginn der Arbeitsphase
    - ersten Sprint planen
    - Tasks festlegen
    - Tasks zuweisen

### ...bis zu diesem Meeting


- HackNPlan anschauen/rumprobieren
- Github ReadMe durchgehen
- Test Branch zum rumspielen nutzen


### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches

#### 1.1 Zusammenfassung Branches
- Steht in der README

- Main Branch als Release Branch gedacht
    - Alles was dort existiert ist vollständig fertig

- Beta Branch 
    - stabil, dokumentiert
    - vor dem Push von anderer Person abgesegnet
    - das was vorher Guideline für Main war

- Dev Branch
    - hier befinden sich Sachen die reviewed werden sollen

- Debug Branch
    - code aus Main + Debug Tools


- jeder kann/sollte eigenen Branch erstellen
    - Name sollte Dev_[Erweiterung]
    - dabei keine Namenskonventionen


Corny hat in seinem Branch schon eine Ordnerstruktur basierend auf dem Icepanel

- UI
    - Menus/Einstellungen
    - also Grundlegende Oberflächen
    
    
- Animation
    - Darstellungen von Spielern, etc.
    - stellt dar was Simulation berechnet

- Manager
    - kommuniziert mit den anderen Klassen
    - eventuell wird dieses Package später nochmal aufgeteilt
    -
    
- Simulation
    - "Herzstück"
    - kümmert sich um "alles" was unsere Spieler brauchen 
        - Waffen, Spieler, ...
    - berechnet das Spiel, schickt die Daten an Animation
    - Trennung wichtig für schnelle Auswertung von dem Turnier/Wettbewerb


nach Möglichkeit die Struktur verwenden, noch ein bisschen WIP
#### 1.2 Beginn der Arbeitsphase
- erster Sprint steht an
    - Themeneingrenzung:
    - Dauer: 2 Wochen
    
- Zuweisung siehe HackNPlan

(Scrum -> Gedränge auf Deutsch??
geklaut aus dem Rugby)

:::danger
Kommunikation ist wichtig.
:::

**Was ist ein Sprint?**

- begrenzter Arbeitszyklus
- 1-2 Wochen

- nach Abschluss wird über den Sprint gesprochen
    - bewerten Arbeit
        - wie haben die aufgaben geklappt
- am Ende soll ein "funktionierendes" Ergebnis bestehen
    - muss nicht komplex sein
    - Hauptsache es ist nutzbar
    - darauf lässt sich dann in jedem Bereich gut weiterarbeiten 
   
   
:::danger 
Jeder teilt sich im HackNPlan einer Aufgabe zu
:::
### Top 2 - Design | Spiel

- Dokumentierung nicht nur von Code
    - vernünftige Ordnerstruktur und Dateinamen


### Top 3 - Programmierung | Spiel

- *noch nichts*

### Top 4 - Gemeinsames | Spiel

- UserStory wurde im Backlog bei HackNPlan angelegt

- wir haben Rechte bekommen :smile: 


fehlt evtl. noch/Ideen:

- Waffen besitzen beim Abschuss einen Rückstoß, dieser kann/soll als Fortbewegung (Sprung) genutzt werden 

- Schusswaffen haben begrenzte Munition?
- Waffen können aufgesammelt werden/befinden sich in Kisten ?
- Es gibt eine Moglichkeit zwischen den Waffen zu wechseln

Ideen:
- begrenzter Inventarplatz/Spieler müssen Waffen und Munition managen
- falls ein Spieler für gewisse Zeit keine Aktion tätigt/zu lange brauch, wird sein  Zug beendet

### Top 5 - Design | Website

- *noch nichts*

### Top 6 - Programmierung | Website

- *noch nichts*

### Top 7 - Gemeinsames | Website

- Top wird erstmal aus dem Protokoll gestrichen

- Website benötigen wir derzeit noch nicht


### Top 8 - ToDos
#### Top 8.1 - Aufgaben-Stack (für später) wird

- GitLab organisieren (von Christian)
- Website zum Einreichen/Erstellen von Maps
- Parabeln und Kollisionsfindung
- Website Top wieder hinzufügen, sobald bedarf besteht


#### Top 8.2 - bis zum nächsten Meeting...

- bei HackNPlan selbst Tasks zuweisen
- Sprint bearbeiten


#### Top 8.3 - Zeitlich relevantes TO-DO

- Aufgaben Stack in HackNPlan-User Story umwandeln


**Lukas**

- [ ] Ticket System für die Mails
- [ ] Icepanel mit Git verknüpfen

**Olivia**
- [ ] Concept Art

**Niggo**
- [ ] Concept Art
- [ ] Aufgaben Stack in HackNPlan reinschreiben User-Story
- [ ] Top 4 Ideen in HackNPlan User-Story
- [ ] Protokoll Backups Github/Local

**Alex**
- [ ] Concept Art

**Corny**

- [ ] **UNITY PROTOTYP**

- [ ] Icepanel mit Git verknüpfen


