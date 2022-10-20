
Teammeeting 07 - 20.10.22
--
:::info
Nächstes Meeting: 27.10.2022
Protokollant: Niggo
Anwesend: Alex, Corny, Luke, Niggo, Olivia
Abwesend: -

Anfang: 09:26
Ende: 10:23
:::



### Agenda

* Sprint auswertung
* Dokumentation ansprechen
* neuen Sprint festlegen


### ...bis zu diesem Meeting..

- HackNPlan Tasks durcharbeiten
    - alles in Testing/Completed


### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches


#### Top 1.1 aktuellen Sprint zusammenfassen 
 
- Klassenstruktur steht 
    - wird eventuell noch bearbeitet 

- viel in Testing
  - gut für ersten Sprint

- nicht ganz SCRUM getreu
    - kein nutzbares bzw. funktionstüchtiges produkt
    - Grund: spezifische Anforderungen/Programmierwettbewerb


- wo Dokumentation unterbringen?


- Vorschlag: Tasks sollten ungefähr gleich aufwendig sein

- regelmäßig pushen

- Art-Sachen

#### Top 1.2 neuen Sprint festlegen

Sprint 1 Woche

- Softwaretesting
    - Testingtaskforce gebildet
    - Alex, Olivia, Luke
    - kümmern sich um das Anlegen von Tests

    - professionelle Umsetzung
        - Markdown mit Anforderungen

- Screens erstellen
    - Menu
    - Ingame

- passende Tests erstellen

- Assetpipeline aufsetzten

- Animatiorklasse

**Ziel/Milestone**
(vom letzten Sprint)

2D-Screen mit Darstellung von Texturen

### Top 2 - Design | Spiel



### Top 3 - Programmierung | Spiel
#### Top 3.1 Recap: Bisherige Architektur


**UI Package**
![](https://cdn.discordapp.com/attachments/1003729604168658954/1032555136863707186/image0.jpg)

- mithilfe von

- bekommt Nutzereingaben
    - leitet diese an die Packages weiter

überlegung bzgl. Manager-Klasse
- lässt sich auf andere (bzw. UI-Packages ) Packages aufteilen

**Simulation Package**
![](https://cdn.discordapp.com/attachments/1003729604168658954/1032555483715878922/image0.jpg)

***berechnet was passiert***

- Action-Log um graphische Änderungen festzuhalten
    - Delay bei Actions um die Darstellung zu verzögern

**Animation Package**
![](https://cdn.discordapp.com/attachments/1003729604168658954/1032555301997641788/image0.jpg)

***stellt dar was passiert***

- Entities
    - um Bewegungen von zusammenhängenden Objekten zu vereinfachen
### Top 4 - Gemeinsames | Spiel

Bild von Olivia
![](https://cdn.discordapp.com/attachments/978308488562368522/1032556082935115796/unknown.png)

Unterhaltsame Fehlermeldungen/Aktionsbeschreibungen
### Top 5 - ToDos
#### Top 5.1 - bis zum nächsten Meeting...

- Tasks in Testing/Completed 

-  Milestone erreichen

#### Top 5.2 - Zeitlich relevantes TO-DO


**Luke**

- [ ] Christians bzgl Meeting anschreiben

**Olivia**
- [ ] Concept Art

**Niggo**
- [ ] Concept Art
- [ ] Protokoll Backups 
    - [x] Local
    - [ ] Github


**Alex**
- [ ] Concept Art

**Corny**

- [ ] **UNITY PROTOTYP**

- [ ] Assetpipeline einlesen
