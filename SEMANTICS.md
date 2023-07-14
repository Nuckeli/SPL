Definition der Semantik
===================================================================================================================================================================================

Implizite Boolean-Konvertierung: Was passiert wenn nicht-Boolsche Werte an Stellen genutzt werden, wo diese erwartet werden?

Beispiele: if("a string") {...}, while(1)

Fehler: Implizite Konvertierung ist nicht erlaubt

--> Code wird übersichtlicher und verständlicher

--> es werden nur Boolsche Typen erlaubt

===================================================================================================================================================================================

Operator-Überladen: Können arithmetische Operatoren auch für Werte, die keine Zahlen sind, genutzt werden?

Beispiele: 3 + "4", "hello" + "world", "Number: " + 3, true + false, "Text" - 2

Es dürfen nur Zahlen mit Arithmetischen Operatoren verwendet werden

--> Code wird übersichtlicher und verständlicher

--> Es dürfen zwischen Variablen oder Werten die keine Zahlen sind, keine Arithmetischen Operatoren stehen
--> Überprüfen von allen Termen mit Variablen oder Werten die keine Zahlen sind

===================================================================================================================================================================================

Neudefinition von Variablen: Dürfen Variablen innerhalb des selben Gültigkeitsbereichs mehrmals definiert werden?

Beispiel var a = "before"; print a; var a = "after"; print a;

Neudefinitionen sind nicht erlaubt.

--> Code wird übersichtlicher und verständlicher

--> Speichern aller Variablennamen und wenn ein bereits existierender Variablenname erneut verwendet wird, um eine neue Variable anzulegen --> Laufzeitfehler

===================================================================================================================================================================================

Shadowing und Scoping: Ist die Neudefinition von Variablen in inneren Scopes erlaubt? Führt dies zu Shadowing oder zum Überschreiben der äußeren Variable?

Beispiel: var a = "outer"; print a; { var a = "inner"; print a; var b = "inner b"; } print a; print b;

Entscheiden Sie, ob Variablen in inneren Blöcken neu definiert werden können. --> Nein
Entscheiden Sie, ob die Neudefinition von Variablen zu Shadowing führt, oder ob innere Variablen die äußeren ersetzen. --> Durch erstes egal
Entscheiden Sie, wo der Gülitgkeitsbereich einer Variablen endet. --> Durch erstes egal

--> Code wird übersichtlicher und verständlicher

--> Wird schon durch Neudefinition von Variablen abgedeckt

===================================================================================================================================================================================

Uninitialisierte Werte: Können Variablen verwendet werden, ohne dass ein expliziter Wert zugewiesen wurde (bzw. gibt es null)?

Beispiel: var a; print a; // Error or "null"?

Entscheiden Sie, ob uninitialisierte Variablen standardmäßig den Wert null haben. --> uninitialisierte Variablen haben den Wert null
Entscheiden Sie, was passiert falls eine uninitialisierte Variable genutzt wird. --> Es wird null als "Wert" verwendet

--> Code wird übersichtlicher und verständlicher

--> Beim ausgeben von uninitialisierte Variablen wird null ausgegeben 
--> Beim verwenden von uninitialisierte Variablen mit arithmetischen Operatoren wird ein Fehler geworfen
--> Wenn eine uninitialisierte Variable anstelle eines Booleans verwendet wird, wird ein Fehler geworfen
--> Beim überprüfen auf gleichheit mit Strings die null sind gibt es eine Ausnahme