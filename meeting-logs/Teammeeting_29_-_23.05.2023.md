# Teammeeting 29 - 23.05.2023

:::info
Nächstes Meeting: 
Protokollant: Niggo
Anwesend: alle - (Alex+Olivia)
Abwesend: keiner + (Alex+Olivia)

Anfang: 17:21 Uhr
Ende: 18:xx Uhr
:::


### Agenda

- erste studentische Einreichungen, Mails, ...
- Preise bzw. Merchandise (dringend!)


### ...bis zu diesem Meeting..

- Patch 1.3

### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen

### Top 1 - Allgemeines zum Projekt | Organisatorisches

- Website aktualisiert
    - FAQ
    - Installationsanleitung
    - Zusammenfassung von Fehlern

- "Perfekte Lösung" für den Bot hat schon recht viele Interessenten :D
    - Colin und Thomas haben wir auch schon erwischt
    - Headcounter: 43


- Mails:
    - positives Feedback
    - Fragen zu pot. Bugs/fehlenden Informationen
    - Bugreports erhalten
    
    
- Schon ein paar Einreichungen für den Wettbewerb.
- Kampagne kommt gut an und wird fleißig eingereicht


- Gamestate aktualisierung welche erst nach dem zug geschiet, ist noch nicht bei allen Studis angegkommen
    - manche erzeugen somit while(true) schleifen
    - Gamestate wird erst nach dem zug aktualisiert
    - führt auch gelegentlich zu abstürzen
    
- Wie behandeln wir Crashes beim ausführen eines Bots?
    - derzeit wird nur der Zug beendet
    - in Zukunft penalty/Minuspunkte vergeben

### Top 2 Teamleitungen

#### 2.1 Programmierung

- Problem mit 4 kernen gefixt
    - Game läuft trotzdessen

- Patch 1.4
    - sounds
    - bugfixes
    - godse
        - soll random schießen, aber geseeded natürlich
    - letzte kampagnenlevel
    - (networking)
    - für in 2 1/2 Wochen
    
    

#### 2.2 Design

- neue Kampagnenlevel, was wird benötigt
    - Corny macht den Rest

    - bereits geplant wie Kampagne sich entwickelt

	    - 1-1 move()
		- 1-2 shoot()
		- 2-1 aim()
		- 2-2 anchor
		- 3-1 Baseball Bat / Knockback
		- 3-2 Waterbomb (Elevation)
		- 4-1 HealthBox
		- 4-2 dynamic aim
		- 5-1 Grenade
		- 5-2 MultiChar
		- 6-1 WeaponBox
		- 6-2 MultiTeam

     - versucht auf wichtige Features zu beschränken, da die Bearbeitung ja begrenzt ist
         - Miolnir und Wolle nicht dabei

- Sounds?
    - 
    
- neue Partikel

- Mapanleitung für die Studis
        

#### 2.3 Testing

#### 2.4 Orga

ToDos für nach Abgabe:
- Merchandise bestellen -> (Yasniggo)
    - Merch bestenfalls bis nächste Woche
- Kampagne up-to-date halten
- Anfallende Bugs patchen
- Turniersystem finalisieren
  
----

##### Merchandise/Preise

- Für nächstes Mal raussuchen und bestenfalls schon bestellen, Thomas schreiben
- evtl. nochmal Umfrage machen, was die Studis sich für Preise vorstellen
- Bestellungen auf Rechnung


### Top 3 - ToDos

#### 3.1 - bis zum nächsten Meeting

#### 3.2 - Zeitlich relevantes TO-DO

**Luke**
- [ ] @Test

**Olivia**
- [ ] Art

**Niggo**
- [ ] Protokoll Backup
- [ ] Preise 

**Yasmin**
- [ ] Art 
- [ ] Preise


**Alex**
- [ ] Art

**Corny**

- [ ] Penalty bei BotCrashes vergeben
- [ ] Networking
- [ ] **Unity Prototyp**
