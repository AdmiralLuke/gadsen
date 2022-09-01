# G.A.T.S

### Installation

#### Schritt 1: [Kommandozeile]
```bash
git clone https://github.com/AdmiralLuke/G.A.T.S
```
- erstellt Ordner namens ``G.A.T.S`` mit allen Unterordnern siehe Git

#### Schritt 2:
To import in Eclipse: File -> Import -> Gradle -> Existing Gradle Project

To import to Intellij IDEA: File -> Open -> build.gradle

To import to NetBeans: File -> Open Project...

Die Main zum Ausführen befindet sich in ``G.A.T.S\desktop\src\com.gats\DesktopLauncher.java``

### Aktualisierung: [Kommandozeile]
Vor jeder Bearbeitungsphase:
```bash
git pull <Branch-Name>
```
``Branch-Name`` ist optional, bei keiner Angabe aktualisiert er vom ``main``-Branch

- Bringt euren Code auf den neusten Stand

### Pushen: [Kommandozeile]
**Schritt 1:**
- Passenden Branch erstellen (**NIEMALS** auf den Main-Branch pushen!)
```bash
// Erstellen eines neuen Branch
git checkout -b <NeuerBranchName> <BranchVonDemAbgezweigtWird>

// ODER: Wechseln zu einem bereits existierenden Branch
git checkout <BranchName>
```
- Im typischen Fall zweigt ihr vom ``main``-Branch ab, ihr könnt auch von noch nicht gemergten Branches abzweigen

**Schritt 2:**
- Dateien hinzugügen die geändert wurden

Option a) Alle Datein inkl. Ordner und Unterordner:
```bash
git add .
```

Option b) Einzelne geänderte Dateien oder Ordner hinzufügen:
```bash
git add <Datei/Verzeichnis>
```

**Schritt 3:**
- Geänderte Daten hochladen und "einreichen":
```bash
git commit -m "Kurze Erkärung was sich verändert hat"
git push -u origin <BranchName>
```

**Fertig:**
* Im besten Fall wird euer Ergebnis von einer Pipeline überprüft und getestet

### Branch Merge: [Kommandozeile]
* Nachdem die Pipeline euren Code erfolgreich getestet hat und mind. eine Person die Einreichung überprüft hat, kann der Branch mit dem ``main``-Branch verbunden werden

```bash
// Optional: Wechseln zum main-Branch 
git checkout main

git merge <BranchName>
git push

// Optional: Löschen des Branch 
git branch -d <BranchName>
```

---
**Anstatt der Kommandozeile gibt es PlugIns und BuildIn Tools in z.B. IntelliJ, oder eine GitHub Desktop App**

---
Folgender Workflow wäre also optimal (kann aber auch abweichen):
```bash
git pull
git checkout -b NeuerBranch main

// ...Am Code arbeiten...

git add .
git commit -m "Changes xyz"
git push -u origin NeuerBranch
```
