# Mapping Tutorial mit Niggo
*auch [hier](https://md.farafin.de/gadsenDesignDocument) zu finden*

*Ist schon mit dem Gedanken gescrhieben es als Grundlage zum Tutorial für die Studis zu nehmen*

Zurzeit haben wir noch nicht wirklich einschränkungen für Maps.

Temporäre Richtlinien/Vorschläge:
- schauen was sich gut anfühlt/einfach ausprobieren was passt
- sollte 12 Spieler unterstützen
- ausgeglichen/fair sein
    - keine zu starken Spawnpunkte/ungleichmäßig verteilten healtboxen
     
## Wie erstelle ich eine Map für Gadsen?

### Vorraussetzungen

#### Map Editor

Zum erstellen lässt sich der Tiled Map Editor nutzen.

Dieser ist kostenlos und open-source. [Hier](https://www.mapeditor.org/) kann man den Editor finden.

- Dabei ist zu beachten, dass der Download mit dem "Name Your own Price"-Modell gestaltet ist. Um Tiled kostenlos zu erhalten, könnt aber einfach auf "No thanks, just take me to the download" clicken.

#### Tileset/Tiled-Projekt

Damit ihr auch seht welche Boxen ihr platziert und die Karte auch die richtigen Blöcke enthält, benötigt ihr unser Tileset/Tiled-Projekt.
Das Projekt befindet sich *zurzeit noch innerhalb unseres Projektes auf dem 311 Branch, unter assets/res/maps/tiledProject/gadsenMaps.tiled-project*
Das Tileset befindet sich *zurzeit noch innerhalb unseres Projektes auf dem 311 Branch, unter assets/res/maps/tiledProject/tileset/\*.tsx*

Dieses kann dann mit Tiled geöffnet werden.

### Mapping
#### Map erstellen


Um nun eine eigene Map zu erstellen kann entweder eine bestehende Map kopiert und bearbeitet werden, oder eine neue Map-Datei erstellt werden.

Falls ihr das Tileset noch nicht seht, dann solltet ihr dieses im Projektexplorer auf der linken Seite öffnen. Jetzt sollte es verfügbar sein um die Boxen in die Map zu malen.

- Wichtig ist für jede Map, dass diese genügend Spawnpunkte besitzt.
    - die Mindestanzahl an Spawnpunkten ist 2

#### Map exportieren

Wenn ihr nun zufrieden mit eurer Map seid, dann müsst ihr diese als JSON Datei Exportieren.