# Teammeeting 08 - 27.10.22

:::info
Nächstes Meeting: 3.11.22
Protokollant: Niggo
Anwesend: Alex, Corny, Luke, Niggo, Olivia
Abwesend: 

Anfang: 9:18 Uhr
Ende: 10:27 Uhr
:::

### Agenda

* Protokollbackup
* Merge Request
* Sprint auswertung
* neuen Sprint festlegen
* Protokoll-Branch
* Diskussion: Dokumentationssprache
* Diskussion: GameJam

### ...bis zu diesem Meeting..

- Tasks in Testing/Completed 

-  Milestone erreichen



### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen


### Top 1 - Allgemeines zum Projekt | Organisatorisches

#### Top 1.1 Protokollbackup

- Protokolle als HTML/MD Datei in einem extra meeting-logs Branch zu finden
- Backup falls FaRa-Server mal wieder kaputt geht

#### Top 1.2 Merge Requests

![MergeOptionenGitScreenshot](https://cdn.discordapp.com/attachments/996403968316018740/1033394109353824267/unknown.png)

- Als neue Guideline zum Hochladen

Git hat keine pull request
    - merge requests
       -> Punkte wie oben abhaken 
       - ansonsten alter Branch kaputt :(
       - und zu viele commits
       

#### Top 1.3 Dokumentation Intern

Englisch als Dokumentationssprache

**Argumentation:**
Bestimmte Wörter haben keine (guten) Übersetzungen

Müsli übersetzt dann freiwillig alles Deutsche bisher

- Problem: später müssen wir dann eventuell manche Dokumentationen umschreiben.
    - falls die Datei auch externe Methoden besitzt, ist für diese dann eine deutsche Doku wichtig
    - müssen dann umschreiben

Dokumentation wird erstmal gemischt geschrieben, je nach Vorlieben


#### Top 1.4 Sprint zusammenfassen

Paar Tasks in Richtung completed, jedoch nicht fertig.

Zeitliche Gründe und Dependencies.



#### Top 1.5 neuen Sprint

noch offene Tasks werden in den neuen Sprint übernommen

#### Top 1.6 Diskussion: GameJam

Anstatt neues Game zu bearbeiten, produktive Atmosphäre nutzen um am Softwareprojekt weiterzuarbeiten.

#### Top 1.7 hübschere Pipeline & Webhook

:::danger


GIT-GADSE

:::
> biggest Stalker there is



![hehe](https://media.wired.com/photos/5f87340d114b38fa1f8339f9/master/w_1600,c_limit/Ideas_Surprised_Pikachu_HD.jpg =200x200)


Pipeline läuft nur noch auf main, dev, beta

> kann genau nachschauen welche Tests durchgelaufen ist
### Top 2 - Design | Spiel

- ...

### Top 3 - Programmierung | Spiel

Asset-Pipeline:
- noch nicht mit gradle eingebunden
- funktioniert schon manuell

- nutzt TexturePacker um Texturen vorzubereiten
    - assets auf eine große Datei
    - Texture Atlas 
    - effizienter als Texturen seperat zu laden
- Assets können in dem assets/res/sprite Ordner gepackt werden

Um die Pages zu separieren, bspw. Menu und InGame assets müssen nicht gleichzeitig genutzt werden, gibt es:

.group Datei
> festlegen in welcher Page die assets landen
> - group files werden vererbt

.exclude Datei
> Assets/Directories werden ignoriert

:::success
Dateinamen mit Endung/Verzeichnissnamen in die .group/.exclude schreiben
:::

---


Animator bekommt Viewport und darf dann zeichnen.
Hud leitet inputs an Simulation weiter.




### Top 4 - Gemeinsames | Spiel

- ...


***Protokollende aus dem letzten Protokoll übernehmen***

### Top 5 - ToDos
#### Top 5.1 - bis zum nächsten Meeting


#### Top 5.2 - Zeitlich relevantes TO-DO


**Luke**

- [ ] Christians bzgl Meeting anschreiben sobald etwas handfestes vorhanden ist :)

**Olivia**
- [ ] Concept Art

**Niggo**
- [ ] Concept Art
- [x] Protokoll Backups 
    - [x] Local
    - [x] Github


**Alex**
- [ ] Concept Art

**Corny**

- [ ] **UNITY PROTOTYP**

- [x] Assetpipeline einlesen
