## 1. Aufbau der Lösung
**CustomSPLVisitor-Klasse:**
- erweitert die SPLBaseVisitor<SPLValue>-Klasse und enthält Methoden zum Besuchen und Auswerten verschiedener Elemente des SPL-Programms. In dieser Klasse findet gleichzeitig semantische Prüfung und Interpretation statt
- Die Klasse verfügt über eine Symboltabelle (symbolTable), um Variablen und ihre Werte während der Auswertung des Programms zu speichern
- Jede visit...-Methode ist für die Auswertung eines spezifischen AST-Knotens verantwortlich und führt die entsprechenden Aktionen aus, um das Programm zu interpretieren

**SPLValue:**
- SPLValue ist eine abstrakte Klasse, die als Basisklasse für andere Werttypen wie Zahlen, Zeichenketten und boolesche Werte dient
- Die abgeleiteten Klassen (SPLNumberValue, SPLStringValue, SPLBooleanValue und SPLUndefinedValue) implementieren spezifische Methoden, um Operationen auf den jeweiligen Werttypen auszuführen und sie in verschiedenen Formaten zurückzugeben (als Double, Zeichenkette oder booleschen Wert)
## 2. Definition der Semantik


### 2.1 Implizite Boolean-Konvertierung: Was passiert wenn nicht-Boolsche Werte an Stellen genutzt werden, wo diese erwartet werden?

Beispiele: if("a string") {...}, while(1)

Fehler: Implizite Konvertierung ist nicht erlaubt

**Entscheidung:**
- es werden nur Boolsche Typen erlaubt
- Durchsetzung: Überprüfen von Boolschen Ausdrücken

**Begründung:**
- Code wird übersichtlicher und verständlicher zu lesen

---

### 2.2 Operator-Überladen: Können arithmetische Operatoren auch für Werte, die keine Zahlen sind, genutzt werden?

Beispiele: 3 + "4", "hello" + "world", "Number: " + 3, true + false, "Text" - 2

**Entscheidung:**
- Es dürfen nur Zahlen mit Arithmetischen Operatoren verwendet werden
- Es dürfen zwischen Variablen oder Werten die keine Zahlen sind, keine Arithmetischen Operatoren stehen
- Durchsetzung: Überprüfen von Termen

**Begründung:**
- Code wird übersichtlicher und verständlicher
- Weniger Fehleranfällig, da man immer weiß welchen Typen man verwendet und keine unterschiedlichen Typen miteinander vermischt werden

---

### 2.3 Neudefinition von Variablen: Dürfen Variablen innerhalb des selben Gültigkeitsbereichs mehrmals definiert werden?

Beispiel var a = "before"; print a; var a = "after"; print a;

**Entscheidung:**
- Neudefinitionen sind nicht erlaubt.
- Durchsetzung: Speichern aller Variablennamen und wenn ein bereits existierender Variablenname erneut verwendet wird, um eine neue Variable anzulegen sonst Laufzeitfehler

**Begründung:**
- Code wird übersichtlicher und verständlicher
- Vermeidung von Fehlern. Mehrfache Definitionen, insbesondere wenn es sich um globale oder statische Variablen handelt, können zu unerwartetem Verhalten führen, da sie an verschiedenen Stellen im Code unterschiedliche Werte haben können.
- Ressourcenmanagement: Keine unnötige Speicherbelegung
---

### 2.4 Shadowing und Scoping: Ist die Neudefinition von Variablen in inneren Scopes erlaubt? Führt dies zu Shadowing oder zum Überschreiben der äußeren Variable?

Beispiel: var a = "outer"; print a; { var a = "inner"; print a; var b = "inner b"; } print a; print b;

- Entscheiden Sie, ob Variablen in inneren Blöcken neu definiert werden können.
- Entscheiden Sie, ob die Neudefinition von Variablen zu Shadowing führt, oder ob innere Variablen die äußeren ersetzen.
- Entscheiden Sie, wo der Gülitgkeitsbereich einer Variablen endet.

**Entscheidung:**
- Variablen dürfen in inneren Blöcken nicht neudefiniert werden sonst gibt es einen Fehler
- Kein Shadowing
- Alle Variablen sind im Grunde global bei uns :)
- Durchsetzung: Siehe Neudefinitionen von Variablen

**Begründung:**
- Code wird übersichtlicher und verständlicher
- Scope-Konflikte vermeiden

---

### 2.5 Uninitialisierte Werte: Können Variablen verwendet werden, ohne dass ein expliziter Wert zugewiesen wurde (bzw. gibt es null)?

Beispiel: var a; print a; // Error or "null"?

- Entscheiden Sie, ob uninitialisierte Variablen standardmäßig den Wert null haben.
- Entscheiden Sie, was passiert, falls eine uninitialisierte Variable genutzt wird. 

**Entscheidung:**
- uninitialisierte Variablen haben den Wert null
- Beim Ausgeben von uninitialisierte Variablen wird null ausgegeben 
- Beim Verwenden von uninitialisierte Variablen mit arithmetischen Operatoren wird ein Fehler geworfen
- Wenn eine uninitialisierte Variable anstelle eines Booleans verwendet wird, wird ein Fehler geworfen
- Beim Verwenden von uninitialisierte Variablen mit Vergleichsoperatoren wird ein Fehler geworfen
- Beim Überprüfen auf Gleichheit mit Variablen die "null" sind, gibt es ein Fehler

**Begründung:**
- Code wird übersichtlicher und verständlicher
- Implementierung ist uns schwergefallen :)