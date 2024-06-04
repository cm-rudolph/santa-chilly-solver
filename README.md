# Solver für das Santa-Chilly-Rätsel

Das Rätsel wurde in c’t 28/2023 gestellt und der Code dazu befindet sich im folgenden Repository:
[https://github.com/607011/chilly/](https://github.com/607011/chilly/)

## Problemanalyse

Das Problem ist ein Problem des Handlungsreisenden (TSP). Dabei sind die einzusammelnden Geschenke die Städte und die 
Spielfigur Chilly ist der Handelsreisende. Die Distanz der Städte untereinander entspricht der Anzahl Züge, 
die gemacht werden müssen, um von einem zum nächsten Geschenk zu gelangen.

### Herausforderungen

* Durch das Rutschen der Spielfigur sind die Züge asymmetrisch. Die Distanz von Geschenk A zu Geschenk B ist in der 
  Regel eine andere als von Geschenk B zu Geschenk A.
* Die Spielfigur bleibt nur in wenigen Fällen direkt auf dem Geschenk stehen. Ein Geschenk kann aus unterschiedlichen
  Richtungen eingesammelt werden, wodurch sich die Position der Figur nach dem Einsammeln verändert und damit auch die
  folgenden Züge.

### Modell

Um die Herausforderungen zu bewältigen, wird das Problem als
[asymmetrisches Problem](https://de.wikipedia.org/wiki/Problem_des_Handlungsreisenden#Asymmetrisches_und_symmetrisches_TSP)
des Handlungsreisenden modelliert, bei dem die Kanten zwischen den Städten gerichtet sind.

Um die Problematik der abweichenden Spielerpositionen nach dem Einsammeln eines Geschenkes aus einer anderen Richtung
zu lösen, werden unterschiedliche Endpositionen nach dem Einsammeln einer Münze als Städtecluster betrachtet. Im
Allgemeinen spricht man in so einem Fall von einem
[verallgemeinerten TSP](https://de.wikipedia.org/wiki/Problem_des_Handlungsreisenden#St%C3%A4dtecluster).

### Umsetzung

Einen Solver für das TSP zu realisieren würde den zur Verfügung stehenden Zeitrahmen sprengen. Daher wird das Problem
in eine [TSPLIB-Datei](http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp95.pdf) überführt. Diese kann dann von 
einem Solver, wie beispielsweise [concorde](https://www.math.uwaterloo.ca/tsp/concorde/) eingelesen werden. Nachdem
concorde mit der [QSopt-Bibliothek](https://www.math.uwaterloo.ca/~bico/qsopt/)
[compiliert](https://www.math.uwaterloo.ca/tsp/concorde/DOC/README.html) wurde, kann mittels 
`concorde chilly.tsp` eine Datei `chilly.sol` erstellt werden, die dann wiederum durch dieses Projekt eingelesen wird.

concorde ist für akademische Forschungszwecke frei verwendbar. Dankenswerterweise wurde durch William Cook, dem
Lizenzhalter von concorde, die Nutzung und Referenzierung im Rahmen der Chilly-Challenge genehmigt.

#### Transformation

1. Mit einer Simulation des Spieles wird ein Graph aller möglicher Züge erstellt. Dabei werden Züge, bei denen Geschenke
   eingesammelt werden, separat erfasst. Hieraus lassen sich später die Informationen zu den "Städteclustern" auslesen.
2. Die kürzesten Wege zwischen allen Geschenken werden dann mittels
   [Dijkstra-Algorithmus](https://de.wikipedia.org/wiki/Dijkstra-Algorithmus) ermittelt. Hieraus entsteht ein
   reduzierter Graph, bei dem die Geschenke die Knoten und die Anzahl Schritte innerhalb des ersten Graphen die 
   Kantengewichte darstellen. Er repräsentiert ein verallgemeinertes Problem des Handlungsreisenden.
3. Um die Cluster zu entfernen, damit ein allgemeiner TSP-Solver eingesetzt werden kann, werden die Knoten eines
   Clusters mit Kanten vom Gewicht 0 verbunden und dann die ausgehenden Kanten der Knoten verschoben. Das Vorgehen ist
   auf der entsprechenden [Wikipedia-Seite](https://en.wikipedia.org/wiki/Set_TSP_problem) einfach geschildert.
4. Da nicht alle TSP-Solver auch das asymmetrische TSP lösen können, wird die (asymmetrische) Matrix nach dem Verfahren 
   von Volgenant und Jonker (ebenfalls auf einer 
   [Wikipedia-Seite](https://en.wikipedia.org/wiki/Travelling_salesman_problem#Conversion_to_symmetric) kurz 
   beschrieben) in eine symmetrische Matrix mit doppelter Zeilen-/Spaltenzahl überführt. Diese Matrix wird dann in die
   Datei `chilly.tsp` geschrieben.

### Lösung

Nachdem der in der Datei [level.txt](src/main/resources/level.txt) beschriebene Level durch die Anwendung in die Datei
[chilly.tsp](chilly.tsp) überführt wurde, danach ein TSP-Solver das Problem gelöst und die Datei 
[chilly.sol](chilly.sol) erstellt hat, gibt die Anwendung folgende Lösung mit 103 Zügen aus:

```text
RULDLDLDULRUDDLULDLULULULULDLRULURDLDLURULDLDRDLDLDLDRULDLULULDRURUDLDLRUDRULDRDLRURDRDRDRDRURLDLDRDLDR
```

## Ausführen des Programms

Das Ausführen des Programms erfordert ein im System installiertes JRE 17 oder höher.

Die ausführbare Datei kann auf der
[Release-Seite des Projektes](https://github.com/cm-rudolph/santa-chilly-solver/releases) heruntergeladen werden.

Zunächst muss die .zip oder .tar-Datei entpackt werden. Für alternative Problemstellungen muss die `level.txt`-Datei im
Ausführungsverzeichnis noch angepasst werden. Wie diese Datei aufgebaut ist, ist weiter unten in diesem Dokument
beschrieben.

Mit

```shell
bin/chilly
```

oder unter Windows

```shell
bin\chilly.bat
```

wird das Programm gestartet. Sofern eine ausführbare Datei `concorde` im `PATH` gefunden wird, wird diese mit der
`chilly.tsp`-Datei gefüttert. Wenn alles gut geht, erscheint danach die Lösung in der Kommandozeilenausgabe.

### Bauen und Ausführen des Programms

Falls man beispielsweise mit den OR-Tools experimentieren möchte, sind Codeanpassungen erforderlich und das Projekt muss
neu gebaut werden.

Nach den Codeanpassungen kann es mit gradle gebaut und dann ausgeführt werden.

Im System muss hierzu ein JDK 17 installiert sein. Gradle muss nicht installiert werden, da mit dem Projekt ein gradle
wrapper ausgeliefert wird.

Das Programm wird dann wie folgt gebaut und gestartet:

```shell
./gradlew run --quiet --console=plain
```

Unter Windows:

```shell
gradlew.bat run --quiet --console=plain
```

### Levelbeschreibung

Wenn ein anderes Level gelöst werden soll, dann muss zuvor die Datei `level.txt` angepasst werden.

In der ersten Zeile stehen die Verbindungen zwischen den Löchern, wobei die Indizes in der oberen, linken Spielfeldecke
bei den Koordinaten (0,0) beginnen. In den nachfolgenden Zeilen findet sich die Spielfeldbeschreibung. Jedes Zeichen
entspricht einem Spielfeld. Eine Zeile wird immer durch ein |-Symbol abgeschlossen.

| Zeichen     | Bedeutung    |
|-------------|--------------|
| T           | Baum         |
| #           | Fels         |
| LEERZEICHEN | Freies Feld  |
| O           | Loch         |
| $           | Geschenk     |
| P           | Startfeld    |
| X           | Ausgang      |
| \|          | Rechter Rand |
