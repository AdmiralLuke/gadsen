# Teammeeting 31 - 06.06.2023

:::info
Nächstes Meeting: 
Protokollant: Niggo
Anwesend: alle - (Alex+Olivia)
Abwesend: keiner + (Alex+Olivia)

Anfang: 17:21 Uhr
Ende: 18:xx Uhr
:::


### Agenda

- fixes
- neue Kampagnenlevel
- Preise
- Studieinreichungen


### ...bis zu diesem Meeting..

- Patch 1.4 vorbereiten

### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen

### Top 1 - Allgemeines zum Projekt | Organisatorisches


- fixes
    - kommen wieder ins changelog




#### 2.1 Programmierung

##### Patch 1.4

Inventory überarbeitet
- updaten läuft komplett über actions
- nicht mehr über initiales gamestate

Entity System
- Groups in Entities direkt überarbeitet
- Rotationen funktionieren jetzt dufte

Bugfixes
- Schuss ohne Ammo hat trotzdessen als Schuss gezählt
- Controller hatte keine Kopie des GameCharacters
- nullPointer bei Partikelpool "behoben"

Kampagne
- Level 5 und 6

Skins
- laden mit Bei Start

- Skins ordner
- Bots können Skins definieren
    - optional ändern API leicht, jedoch geht nichts kaputt


#### Random Zeugs

Studis nutzen Random nicht sondern math.random
- ungeseeded
- nochmal hinweisen auf der Website



#### 2.2 Design

#### 2.3 Testing

#### 2.4 Orga

##### Preise

*topsecretpreise* falls Studis Kampagne komplett bearbeitet
- fin shirts/beutel?
- gummidingsis?

##### Website

- Mapeinreichungen + Skineinreichungen Website
- hinweise zur Randommethode

### Top 3 - ToDos

#### 3.1 - bis zum nächsten Meeting

#### 3.2 - Zeitlich relevantes TO-DO

**Luke**
- [ ] @Test

**Olivia**
- [ ] Art

**Niggo**
- [ ] Protokoll Backup
- [x] Preise 

**Yasmin**
- [ ] Art 
- [x] Preise


**Alex**
- [ ] Art

**Corny**

- [ ] Penalty bei BotCrashes vergeben
- [ ] Networking
- [ ] **Unity Prototyp**
