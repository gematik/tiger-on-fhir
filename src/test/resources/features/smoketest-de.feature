#language: de
Funktionalität: FHIR Validation Deutsch

  Hintergrund:
    Gegeben sei TGR lösche aufgezeichnete Nachrichten

  Szenario: FHIR-Validierung mit Ref Validator
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ERP Ressource
    Und FHIR prüfe die aktuelle Antwort enthält im Body eine gültige ERP Ressource
    Wenn TGR finde die nächste Anfrage mit dem Pfad '.+'
    Dann FHIR prüfe die aktuelle Antwort enthält im Knoten '$.body' eine gültige ERP Ressource

  Szenario: FHIR-Validierung mit Custom Profile
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ERP Ressource, die zum Profil "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0" konform ist
    Und FHIR prüfe die aktuelle Anfrage enthält im Knoten '$.body' eine gültige ERP Ressource, die zum Profil "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0" konform ist
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Knoten '$.body' den FHIRPath '${tiger.my.configurable.fhirpathExpression}' mit der Fehlermeldung "Fehler"
    Und FHIR die aktuelle Anfrage im Knoten '$.body' den FHIRPath '${tiger.my.configurable.fhirpathExpression}' erfüllt
    Und FHIR die aktuelle Anfrage im Knoten '$.body' den FHIRPath '${tiger.my.configurable.fhirpathExpression}' mit der Fehlermeldung "Fehler" erfüllt
    Und FHIR prüfe aktuelle Anfrage im Knoten '$.body' erfüllt den FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Knoten '$.body' den FHIRPath '${tiger.my.configurable.fhirpathExpression}' mit der Fehlermeldung "Fehler"

  Szenario: FHIR-Validierung mit FHIRPath
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    Dann FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath '${tiger.my.configurable.fhirpathExpression}' mit der Fehlermeldung 'Fehler'
    Dann FHIR die aktuelle Anfrage im Body den FHIRPath '${tiger.my.configurable.fhirpathExpression}' erfüllt
    Dann FHIR die aktuelle Anfrage im Body den FHIRPath '${tiger.my.configurable.fhirpathExpression}' mit der Fehlermeldung 'Fehler' erfüllt
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value = "${tiger.my.configurable.author.type.valid}").exists()'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()' mit der Fehlermeldung 'Fehler'
    Und FHIR die aktuelle Antwort im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()' mit der Fehlermeldung 'Fehler' erfüllt
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Body nicht den FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Body nicht den FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
    Und FHIR die aktuelle Antwort im Body nicht den FHIRPath 'false' erfüllt
    Und FHIR prüfe die aktuelle Antwort erfüllt im Knoten '$.body' den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Knoten '$.body' den FHIRPath 'true' mit der Fehlermeldung 'Fehler'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Knoten '$.body' den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
    Und FHIR die aktuelle Antwort im Knoten '$.body' den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()' mit der Fehlermeldung 'Fehler' erfüllt
    Und FHIR prüfe die aktuelle Antwort erfüllt im Knoten '$.body' nicht den FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
    Und FHIR die aktuelle Antwort im Knoten '$.body' nicht den FHIRPath 'false' erfüllt

    Und FHIR die aktuelle Anfrage im Body die FHIRPath Ausdrücke erfüllt:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Body die FHIRPath Ausdrücke:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Knoten '$.body' die FHIRPath Ausdrücke:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """
    Und FHIR die aktuelle Anfrage im Knoten '$.body' die FHIRPath Ausdrücke erfüllt:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """
    Und FHIR die aktuelle Antwort im Body die FHIRPath Ausdrücke erfüllt:
    """
      (Bundle.entry.count() < 7 and Bundle.entry.resource.author.count() > 2).not()
      Bundle.entry.resource.author.where(type.value.matches("D.*i.+")).type != "Dinosaur"
    """
    Und FHIR prüfe die aktuelle Antwort erfüllt im Body die FHIRPath Ausdrücke:
    """
      (Bundle.entry.count() < 7 and Bundle.entry.resource.author.count() > 2).not()
      Bundle.entry.resource.author.where(type.value.matches("D.*i.+")).type != "Dinosaur"
    """

  Szenario: Reports der vorhergehenden Szenarien sollen alle Details enthalten
    Dann in "target/evidences/" exists a file matching "^evidence_FHIR-Validierung_mit_Ref_Valida[0-9a-f\-]+_.+\.html$" containing all of the following lines:
    """
    FHIR-Validierung mit Ref Validator
    Dann FHIR pr&uuml;fe die aktuelle Anfrage enth&auml;lt im Body eine g&uuml;ltige ERP Ressource
    Und FHIR pr&uuml;fe die aktuelle Antwort enth&auml;lt im Body eine g&uuml;ltige ERP Ressource
    evidence-type-INFO"
    """
    Und in "target/evidences/" exists no file matching "^evidence_FHIR-Validierung_mit_Ref_Valida[0-9a-f\-]+_.+\.html$" containing any of the following lines:
    """
    evidence-type-WARN"
    evidence-type-ERROR"
    """
    Und in "target/evidences/" exists a file matching "^evidence_FHIR-Validierung_mit_Custom_Pr[0-9a-f\-]+_.+\.html$" containing all of the following lines:
    """
    FHIR-Validierung mit Custom Profile
    Dann FHIR pr&uuml;fe die aktuelle Anfrage enth&auml;lt im Body eine g&uuml;ltige ERP Ressource, die zum Profil &quot;https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0&quot; konform ist
    Und FHIR pr&uuml;fe die aktuelle Anfrage enth&auml;lt im Knoten '$.body' eine g&uuml;ltige ERP Ressource, die zum Profil &quot;https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0&quot; konform ist
    evidence-type-INFO
    evidence-type-INFO
    """
    Und in "target/evidences/" exists no file matching "^evidence_FHIR-Validierung_mit_Custom_Pr[0-9a-f\-]+_.+\.html$" containing any of the following lines:
    """
    evidence-type-ERROR"
    """
    Und in "target/evidences/" exists a file matching "^evidence_FHIR-Validierung_mit_FHIRPath_.+\.html$" containing all of the following lines:
    """
    FHIR-Validierung mit FHIRPath
    Und FHIR pr&uuml;fe die aktuelle Anfrage erf&uuml;llt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value = &quot;${tiger.my.configurable.author.type.valid}&quot;).exists()'
    evidence-type-INFO"
    """
    Und in "target/evidences/" exists no file matching "^evidence_FHIR-Validierung_mit_FHIRPath_.+\.html$" containing any of the following lines:
    """
    evidence-type-WARN"
    evidence-type-ERROR"
    """

  Szenario: FHIR-Validierung unter Verwendung eines Plugins mit Hilfe des Referenz Validators
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir-plugins.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/minimal/42'
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige minimal Ressource
    Und FHIR prüfe die aktuelle Antwort enthält im Body eine gültige minimal Ressource
    Wenn TGR finde die nächste Anfrage mit dem Pfad '.+'
    Dann FHIR prüfe die aktuelle Antwort enthält im Knoten '$.body' eine gültige minimal Ressource


  Szenario: FHIR-Validierung und Speicherung in eine Variable
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    Dann FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value = "Device").exists()'
    Dann FHIR evaluate FHIRPath 'Bundle.entry.resource.author.type.where(value = "Device").count()' on current response body and store first element as primitive value in variable "test"
    Dann FHIR prüfe die aktuelle Antwort den FHIRPath 'Bundle.entry.resource.author.type.where(value = "Device").count()' und speichere das erste Ergebnis als primitiven Wert in Variable "test2"
    Dann TGR print variable "test"

  Szenario: FHIR-Validation unter Verwendung des Reference-Validators
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR die aktuelle Anfrage im Body eine gültige ERP Ressource enthält
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ERP Ressource
    Dann FHIR die aktuelle Anfrage im Body eine gültige ERP Ressource, die zum Profil "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0" konform ist enthält
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ERP Ressource, die zum Profil "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0" konform ist
    Dann FHIR die aktuelle Antwort im Body eine gültige ERP Ressource enthält
    Dann FHIR prüfe die aktuelle Antwort enthält im Body eine gültige ERP Ressource
    Dann FHIR current request body is a valid ERP resource and conforms to profile "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0"
    Dann FHIR die aktuelle Anfrage im Knoten '$.body' eine gültige ERP Ressource enthält
    Dann FHIR prüfe die aktuelle Anfrage enthält im Knoten '$.body' eine gültige ERP Ressource
