    
### Wie erstelle ich eine Map für Gadsen?

#### Vorraussetzungen

##### Map Editor

Zum erstellen lässt sich der Tiled Map Editor nutzen.

Dieser ist kostenlos und open-source. [Hier](https://www.mapeditor.org/) kann man den Editor finden.

- Dabei ist zu beachten, dass der Download mit dem "Name Your own Price"-Modell gestaltet ist. Um Tiled kostenlos zu erhalten, könnt aber einfach auf "No thanks, just take me to the download" clicken.

##### Tileset/Tiled-Projekt


Damit ihr seht welche Boxen ihr platziert und die Karte auch die richtigen Blöcke enthält, benötigt ihr unser Tileset.

Dies erhaltet ihr auf 2 Wege:

###### Projekt-Ordner

Am einfachsten ist es, unseren *tiledProject* Ordner herunterzuladen/zu nutzen.

*derzeit innerhalb assets/res/maps/tiledProject/*


Habt ihr den Ordner, befindet sich dort nun unsere *gadsenMaps.tiled-project* Datei, welche sich mit Tiled öffnen lässt. 

*zurzeit noch innerhalb unseres Projektes auf dem 311 Branch, unter assets/res/maps/tiledProject/gadsenMaps.tiled-project*

Nun sollte man links oben in Tiled den einen Ordner sehen.

*nur recht klein zu sehen*


###### Öffnen des Tilesets

- Um das Tileset zu nutzen, muss man es in Tiled öffnen, danach ist es rechts unten im Fenster zu sehen.

###### Ohne Projekt-Ordner

Falls ihr euer eigenes Projekt erstellen wollt benötigt man für die Maps das Tileset
Dieses befindet sich *zurzeit noch innerhalb unseres Projektes auf dem 311 Branch, unter assets/res/maps/tiledProject/tileset/
*Dazu gehört alles in dem Ordner*!

Dieses kann dann mit Tiled geöffnet werden.

#### Map erstellen


Um nun eine eigene Map zu erstellen kann entweder eine bestehende Map kopiert und bearbeitet werden, oder eine neue Map-Datei erstellt werden.

##### Neue Map in Tiled


Zum erstellen einer neuen Map müsst ihr unter dem Reiter Welt -> neue Welt erstellen.

Falls ihr das Tileset noch nicht seht, dann solltet ihr dieses im Projektexplorer auf der linken Seite öffnen. Jetzt sollte es verfügbar sein um die Boxen in die Map zu malen.

- Wichtig ist für jede Map, dass diese genügend Spawnpunkte besitzt.
    - die Mindestanzahl an Spawnpunkten ist 2

#### Map exportieren

Wenn ihr nun zufrieden mit eurer Map seid, dann müsst ihr diese als JSON Datei Exportieren.
