# Teammeeting 14 - 08.12.22

:::info
Nächstes Meeting: 15.12.22
Protokollant: Nüggo
Anwesend: *Corneliukeiviaggo*, Corny, Luke, Olivia, Niggo, Alex
Abwesend: 

Anfang: 9:20 Uhr
Ende: 10:08 Uhr
:::

### Agenda

- Sprint auswerten
- neuen Sprint festlegen (Wie geht es weiter?)

### ...bis zu diesem Meeting..

- fertigmachen was zu machen ist

### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches

#### Top 1.1 Sprint zusammenfassen

- vieles geschafft
- corny hat Architektur angepasst, bzw Threads dazugeschmissen

![](https://cdn.discordapp.com/attachments/996403968316018740/1049823851502129182/0CF17441-CEDF-4B03-838D-195E5FD1C707.jpg)

- grüne Boxen sind Threads
- jeder Bot+HumanPlayer haben einen Thread

- Controller umgeändert um Race Conditions zu vermeiden

- Manager führt jetzt Simulation aus
    - Async Queue welche Commands von anderen Klassen kommen
    
    - Bots sollen noch auf die Events subscriben können


- Pipeline funktioniert wieder
    - Test aktualisiert
    
#### Top 1.2 neuen Sprint festlegen

#### Top 1.3 Wie geht es weiter

- Christian erhält morgen den Prototypen
- Fragen ob Git öffentlich werden kann

- Anfragen/Issues/Mails beantworten/reagieren
- Stuff fixen

- Weihnachtspause
    - Refactorn/aufräumen
    
    
### Top 2 - Design | Spiel

- ...

### Top 3 - Programmierung | Spiel

- Menu updaten
    - Bot Liste übergeben aus dem Menu
    - Methode im Manager nutzen
    - Liste aus Klassen
    - Verzeichnis Bots/Bot package
    - dedicated start button/erkennbar
 

- Bots schreiben und testen

---

Api-Freeze ca 20/21 Uhr

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