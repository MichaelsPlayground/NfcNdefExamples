# NFC NDEF Beispiele

Diese App schreibt und liest Daten von NFC Tags, die in  der Form von NDEF Nachrichten 
gespeichert sind. Die Datensätze werden in **NDEF Records** geschrieben und alle 
Records werden in einer **NDEF Message** zusammengefasst.

Der große Vorteil der Speicherung der Daten in strukturierter Form (NDEF) liegt darin, das sowohl 
Android- als auch iOS-Smartphones diese Datensätze direkt einlesen und verarbeiten können. Alternativ können  
Daten auch direkt auf den NFC Tag geschrieben werden, dann benötigt Ihr aber auch ein Lese-Protokoll, um  
die Daten(-sätze) auch wieder strukturiert auslesen zu können. 

Je nach gewähltem NDEF-Typ können die Daten direkt verarbeitet werden oder es wird diejenige App 
gestartet, welche die Daten verarbeitet.

Die insgesamt 8 Typen wurden vom NFC Forum standardisiert: Empty, Well-Known type [NFC RTD], 
MIME media-type [RFC 2046], Absolute URI [RFC 3986], External type [NFC RTD], Unknown, Unchanged 
und Reserved.

Die hier vorgestellten Beispiele zeigen überwiegend die Nutzung mit dem Typ **Well-known Type**
(was übersetzt "gut bekannter Typ" bedeutet) und **External Type**.

Die MainActivity beinhaltet die Schreibfunktionen auf einen Tag, die ReadNdefActivity liest einen Tag ein.

**Grundlegende Informationen zum genutzten NFC Tag**: NFC-Tag werden in 4 Klassen angeboten, für unsere Beispiele 
verwenden wir Tags der **Klasse 2**. Einer der meistverkauften Tags ist der Tag NFC **NTAG216**, welcher eine 
Brutto Speicherkapazität von 924 Bytes besitzt. Die Netto Speicherkapazität beträgt 868 Bytes - die Differenz 
entsteht durch den Header bei der Nutzung von NDEF Messages. Der Tag wird auch in zwei kleineren Ausführungen 
angeboten, aber aufgrund des niedrigeren Preises von rund 1 Euro pro NFC Tag nutze ich nicht die kleineren Tags.

**Modus zum Lesen und Schreiben der Tags**: Ein NFC Tag kann in einem Android Smartphone über drei verschiedene Modi 
gelesen und beschrieben werden:

**A Intent-Filter**: Bei dieser Methode wird in der AndroidManifest.xml - Datei ein **Intent Filter** registriert. Erkennt das 
Smartphone nun einen NFC-Tag an seinem Leser, prüft das Betriebssystem, ob sich eine App für den Typ des Tags interessiert. 

Hat sich nur eine App für diesen Typ registriert, wird die App sofort geöffnet, sollten mehrere Apps das Interesse 
angemeldet haben, wird ein App-Auswahlmenü angezeigt. Die gewählte App erhält dann die eingelesenen Daten per Intent übergeben.

Das typische Tag Erkennungszeichen (z.B. ein Ton und/oder Vibration) wird systemseitig ausgelöst.

**B Foreground Service**: Der **Foreground Service** liest den Tag nur ein, wenn die App im Vordergrund läuft. In diesem Fall 
werden auch keine anderen Apps gestartet, auch wenn sie Interesse an diesem Typ haben sollten.

In der Praxis wird der Foreground Service meist von einem Intent Filter begleitet und gleichzeitig festgelegt, das immer 
nur eine Instanz der App geöffnet bleibt (es also nicht bei jedem "Tag gefunden" eine neue Instanz der App gestartet wird.)

Das typische Tag Erkennungszeichen (z.B. ein Ton und/oder Vibration) wird systemseitig ausgelöst.

**C Reader mode**: Der **Reader modus** ist der optimale Modus wenn der Tag immer nur bei geöffneter App gelesen oder 
geschrieben werden soll. Dieser Modus ist robuster als die beiden anderen Modi und Sie behalten die volle Kontrolle über 
den Lese- oder Schreibvorgang. So kann das typische Tag Erkennungszeichen unterbunden werden und Sie können ein beliebiges 
Zeichen nutzen. In meinen Beispielen nutze ich den Vibrationseffekt.

Diese App nutzt sowohl für den Lese- als auch den Schreibvorgang den Reader Modus.



These NDEF types are implemented:
```plaintext
01 well known type - text
02 well known type - URI
03 well known type - Google streetview (Uri)
04 well known type - Email (Uri)
05 well known type - Coordinate (Uri)
06 well known type - Coordinate with user info (Uri)
07 well known type - Telefone number (Uri)
08 well known type - Address (Uri)
09 well known type - Google navigation (Uri)
10 well known type - Application (Uri)
11 well known type - Target address

```

The app icon is generated with help from **Launcher icon generator** 
(https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html), 
(options trim image and resize to 110%, color #2196F3).



