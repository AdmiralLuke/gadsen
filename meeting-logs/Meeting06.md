
Teammeeting 06 - 13.10.22
--
:::info
[Nächstes Meeting] 20.10.22
Protokollant: Niggo
Anwesend: Luke, Corny, Alex, Olivia
Abwesend: -

Anfang: 9:24 Uhr
Ende: 10:49 Uhr
:::



### Agenda

* Protokoll-Anpassung
* Notiz (von Christian?) prolly from Niggo :P
* Git Tutorial in IntelliJ
    * Push, Commit, Pull Request, Merge,...
* Projektname festlegen (für Git von Christian)
* auf Sprache einigen für interne Doku
* Pipeline
* aktuellen Sprint zusammenfassen
* gemeinsames Meeting mit den Christians 1x im Monat


### ...bis zu diesem Meeting..

- HackNPlan Tasks selbst zuweisen und bearbeiten
- Fragen und Anmerkungen zum Sprint 


### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches

#### Top 1.1 Protokollanpassung

   - Websitenbezogene Tops 4-7 verschwinden vorläufig aus dem Protokoll, bis diese wirklich relevant sind.
        - dann vermutlich als zusammengefasstes Top 5 ausreichend

   - ebenso Aufgabenstack
        - findet sich im HackNPlan aufgeteilt als UserStories

#### Top 1.1 GitLab

Haben jetzt alle GitLab-Zugang und ein Projekt aufgesetzt bekommen

- Projektname im Git "gadsen"

- Git Tutorial vorhanden

- Christian. R hat uns bei der Pipeline mit dem Runner geholfen
    - machen kein build on push
    - Pipeline zu finden bei CI/CD

- GitLab ist "besser" als GitHub :D
    - Projekt wird auf GitLab fortgesetzt
    - Pipeline auf GitLab um änderungen sofort zu testen

- GitLab lässt sich direkt mit IntelliJ nutzen
    - mit Account anmelden


#### Top 1.2 Sprache für interne Doku

**Aussage von Christian Rössl:**

> Alles was für die Studenten relevant ist, also für den Programmierwettbewerb, auf deutsch dokumentieren. 

**Frage wie wir den Code für uns intern kommentieren.**

> Damit alles einheitlich bleibt auf Deutsch dokumentieren.



#### Top 1.3 aktuellen Sprint zusammenfassen 

Allgemein

Art-sachen direkt in HackNPlan Completed packen

2 Tasks schon durch.

Sprint Ziel können wir eventuell nicht erreichen, aufgrund unserer Abspaltung. Müssen deshalb Klassen doppelt erstellen, für Simulation und Animation.

#### Top 1.4 gemeinsames Meeting mit den Christians 1x im Monat


- Notiz (nur nochmal kleiner Einfall/Erinnerung)
    - Christian Rössl war nicht so großer Fan von Waffennutzung
    - "Waffen" dann eher nutzen um Katzen zufriedenzustellen
        - Anstatt Leben Satisfaction/Hunger
            - Gadsenminzewerfer
            - Gadsenfutterkanone zum füttern
            - Streicheleinheitenaparillus
            - 
            - etc.
    - WORMS war abstrahiert genug
    - mit den Christians nochmal absprechen

-  Christians einfach zu unserem Donnerstags 9:15-11 Termin einladen


#### Top 1.5 Commits mit Tasks verbinden
- Nummer der HackNPlan Task in die Commit Message einbringen
     - gut zum zuordnen

### Top 2 - Design | Spiel

**Wo dateien reinpacken?**
    
- Texture Packer nutzen
    - können uns ne Pipeline für Texturen bauen
    - baut einen TextureAtlas
        - eine große Bilddatei, wo unsere Sprites enthalten sind
    - effizienter als einzelne Sprites
   
- asset-Ordner nutzen
    - erstmal in den eigenen Branch
    
- Ordnerstruktur

Größe und Proportionen bei den Charakteren Einheitlich halten
> wichtig für die Hitboxen

- Skalierung/Auflösung für die Sprites damit es auf allen Bildschirmen gleich ist
    - lässt sich über die Engine/GPU lösen
    - Fit-Viewports

- Sprites auf kleiner Auflösung belassen

- wird sich im laufe der Zeit klären
    - erstmal ausprobieren

- Hintergrund kann erstmal größer sein als die Map bzw. Hochauflösender
:::warning
tldr;
Erstmal Sprites erstellen zum testen.
Rahmenbedingungen legen wir dann fest.

----

Sprites nicht hochskalieren.
In der Auflösung lassen, in der es erstellt wurde (8x8/16x16/32x32)

Hintergrund lieber größer als Spielbrett
:::

### Top 3 - Programmierung | Spiel


- Auf Sprache(n) für Klassennamen, Variablennamen, Kommentare und Dokumentation einigen
    - Klassen/Variablen: Englisch
    - Dokumentation/Kommentare: Deutsch 
    
    
### Top 4 - Gemeinsames | Spiel

- Zum arbeiten gerne in den Discord  setzen


### Top 5 - ToDos
#### Top 5.1 - bis zum nächsten Meeting...

- HackNPlan Tasks durcharbeiten
    - alles in Testing/Completed


#### Top 5.2 - Zeitlich relevantes TO-DO

- [x] Aufgaben Stack in HackNPlan-User Story umwandeln


**Luke**

- [x] Icepanel mit Git verknüpfen
- [ ] Christians bzgl Meeting anschreiben

**Olivia**
- [ ] Concept Art

**Niggo**
- [ ] Concept Art
- [x] Aufgaben Stack in HackNPlan reinschreiben User-Story
- [x] Top 4 Ideen in HackNPlan User-Story
- [ ] Protokoll Backups 
    - [x] Local
    - [ ] Github


**Alex**
- [ ] Concept Art

**Corny**

- [ ] **UNITY PROTOTYP**

- [x] Icepanel mit Git verknüpfen
- [ ] Assetpipeline einlesen
