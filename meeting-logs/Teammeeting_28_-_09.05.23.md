# Teammeeting 28 - 09.05.23

:::info
Nächstes Meeting: 
Protokollant: Niggo
Anwesend: alle - (Alex+Olivia)
Abwesend: keiner + (Alex+Olivia)

Anfang: 17:17 Uhr
Ende: 18:xx Uhr
:::


### Agenda

- Abgabe

### ...bis zu diesem Meeting..

- Sprinten


### Top 0 - Meeting beginnen

* Blitzlicht
* Protokollant festlegen
* Agenda ansprechen
* Punkte vom letzten Meeting durchgehen

### Top 1 - Allgemeines zum Projekt | Organisatorisches


#### 1.1 Sprint zusammenfassen

noch nicht durch
was fehlt noch? Was muss noch getan werden?

:::danger
MORGEN ABGABE :'(
:::

- Kampagne Week 2

- Boxen zeug

- Maploading, ist auf dem Kampagnenbranch in arbeit

    - grundlegende Level + compositlevel

- Zulassungsbots


:::danger
DONT MIRROR MAPS

WICHTIGE TODOS

Kampagne W1, 2
Boxen
Zugriffsrechte
Bugfixes -> funktionstüchtige jar
:::

#### 1.2 Abgabe
- Abgabe Mittwoch (morgen bis 23:59) -> Christian stellt alles am Donnerstag ein
- wie testen wir die Kampagne?
    - Beschreibung der Aufgabe, ...
    - Kampagne muss über Headless-Kommandozeile auswählbar sein
    - funktioniert an sich schon
    - kampagne funktioniert headless
        - allgemein funktioniert alles
- wie testen wir auf bestanden?
    - letztes Jahr:  8 / 13 Spielen gewinnen
  
    - mit Multigame und average Score System möglich
        - ca alles über 300 - 400 Punkte zwingt dich das Spiel zu gewinnen
        
        
- was fehlt noch auf der Website?

### 1.3 Regeln einhalten
- Per Script Dinge verbieten
    - Welche Keywords? (z.B. "Manager", "@Supress", ...)

### 1.4 Waffen und Inventar

- Inventaritem Anzahl fixen
- Humanplayer waffenauswahl


<!-- erstes abschreiben
\begin{array}{|c|c|c|c|c|c|}
\hline
  \text{Waffe} & \text{Wasserpistole} &  \text{Wool} & \text{Miojlnir}& \text{Waterbomb} & \text{Bat} & \text{GRANATA} \\ 
\hline
   \mathbb{Damage} & 10 & Max(bounces*10,1) & 35 & 0 & 20 & 15\\
\hline
   \mathbb{Ammo} & \infty & Math.seal\{1+a\cdot log_2[(Teams-1)\cdot char/2+2]\} & 1 &2*char+Teams & Math.ceil\{\frac{1+a\cdot log_2[(Teams-1)\cdot char/2+2]\}}{2}&char\\
\hline
   \mathbb{Bounces} & / & 5 & / & / & / & 1 \\ 
\hline
    \mathbb{Loot} & \emptyset & 1,5& 10 & 2. 6. 8 & 4. 7 & 3. 9 \\
\hline
\end{array}
-->

\begin{array}{|c|c|c|c|c|}
\hline
  \text{Waffe} & \text{Damage} &  \text{Ammo} & \text{Bounces}& \text{Loot} \\ 
\hline
   \mathbb{Wasserpistole} & 10 &  \infty & / & \emptyset \\
\hline
   \mathbb{Wool} &Max(bounces*10,1) & Math.seal\{1+a\cdot log_2[(Teams-1)\cdot char/2+2]\}&5&1,5\\
\hline
   \mathbb{Miojlnir} & 35 & 1 & / & 10 \\ 
\hline
    \mathbb{Waterbomb} & 0 & 2\cdot char + Teams & / & 2. 6. 8\\
\hline
      \mathbb{Bat} & 20 &Math.ceil\{\frac{1+a\cdot log_2[(Teams-1)\cdot char/2+2]\}}{2}& /& 4.7 \\
\hline
      \mathbb{GRANATA} & 15 & char & 1 & 3.9 \\
    \hline
\end{array}

Math dot seal
![Mathdotseal](https://media.tenor.com/GzZqL7jg1T8AAAAC/clapping-seal-funny-animals.gif)

### 1.5 Bots

- fokusiert WasserPistolen Bot

    - Aguadse

- fokusiert Waffenboxen + Granaten Bot

    - Kamigadse

- fokusiert Mediboxen+anker

    - Bob der Abreissmeister

### 1.6 Website

-> später noch Map und Skin tutorial

### 1.7 BESTEHEN

wie viel Punkte zum bestehen?

333

### Top 2 Teamleitungen

#### 2.1 Programmierung

##### Kampagne

- Maps und Bots laden
    - nur bestimmte Maps anzeigen
    - bots für maps vordefinieren
    - waffen vordefinieren


##### Inventar

- menge der Waffen anzeigen

#### 2.2 Design

#### 2.3 Testing

#### 2.4 Orga
ToDos für nach Abgabe:
- Merchandise bestellen
- Kampagne up-to-date halten
- Anfallende Bugs patchen
- Turniersystem finalisieren
  
    
### Top 3 - ToDos

#### 3.1 - bis zum nächsten Meeting

#### 3.2 - Zeitlich relevantes TO-DO

**Luke**
- [ ] @Test

**Olivia**
- [ ] Art

**Niggo**
- [ ] Art
- [ ] Protokoll Backup

**Yasmin**
- [ ] Art 


**Alex**
- [ ] Art

**Corny**

- [ ] Networking
