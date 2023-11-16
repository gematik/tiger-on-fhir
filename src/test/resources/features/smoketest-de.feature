#language: de
Funktionalität: FHIR Validation Deutsch

  Szenario: FHIR-Validierung mit Hilfe des Referenz Validators
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ERP Ressource
    Und FHIR prüfe die aktuelle Antwort enthält im Body eine gültige ERP Ressource
    Wenn TGR finde die nächste Anfrage mit dem Pfad '.+'
    Dann FHIR prüfe die aktuelle Antwort enthält im Knoten '$.body' eine gültige ERP Ressource

  Szenario: FHIR-Validierung mit Hilfe des Referenz Validators und Custom Profilen
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir-custom-profile-data.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/isik1/validInOwnProfile'
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ISIK1 Ressource
    Und FHIR prüfe die aktuelle Anfrage enthält im Knoten '$.body' eine gültige ISIK1 Ressource
    Und FHIR prüfe die aktuelle Antwort enthält im Body eine gültige ISIK1 Ressource
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/isik1/invalidOwnProfile'
    Dann FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige ISIK1 Ressource, die zum Profil "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger" konform ist
    Und FHIR prüfe die aktuelle Anfrage enthält im Knoten '$.body' eine gültige ISIK1 Ressource, die zum Profil "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger" konform ist
    Dann FHIR prüfe die aktuelle Antwort enthält im Body eine gültige ISIK1 Ressource, die zum Profil "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger" konform ist
    Und FHIR prüfe die aktuelle Antwort enthält im Knoten '$.body' eine gültige ISIK1 Ressource, die zum Profil "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger" konform ist

  Szenario: FHIR-Validierung mit FHIRPath
    Gegeben sei TGR liest folgende .tgr Datei 'src/test/resources/fhir.tgr'
    Wenn TGR finde die nächste Anfrage mit dem Pfad '/erp/42'
    Dann FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value = "${tiger.my.configurable.author.type.valid}").exists()'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
    Und FHIR prüfe die aktuelle Anfrage erfüllt im Body nicht den FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
    Und FHIR prüfe die aktuelle Antwort erfüllt im Body nicht den FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
    Und FHIR die aktuelle Anfrage im Body die FHIRPath Ausdrücke erfüllt:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """
    Und FHIR die aktuelle Antwort im Body die FHIRPath Ausdrücke erfüllt:
    """
      (Bundle.entry.count() < 7 and Bundle.entry.resource.author.count() > 2).not()
      Bundle.entry.resource.author.where(type.value.matches("D.*i.+")).type != "Dinosaur"
    """

  Szenario: Reports der vorhergehenden Szenarien sollen alle Details enthalten
    Dann in "target/evidences/" exists a file matching "^evidence_FHIR-Validierung_mit_Hilfe_des_Referenz_Validators_[0-9].+\.html$" containing all of the following lines:
    """
    FHIR-Validierung mit Hilfe des Referenz Validators
    Dann FHIR pr&uuml;fe die aktuelle Anfrage enth&auml;lt im Body eine g&uuml;ltige ERP Ressource
    Und FHIR pr&uuml;fe die aktuelle Antwort enth&auml;lt im Body eine g&uuml;ltige ERP Ressource
    evidence-type-INFO"
    """
    Und in "target/evidences/" exists no file matching "^evidence_FHIR-Validierung_mit_Hilfe_des_Referenz_Validators_[0-9].+\.html$" containing any of the following lines:
    """
    evidence-type-WARN"
    evidence-type-ERROR"
    """
    Und in "target/evidences/" exists a file matching "^evidence_FHIR-Validierung_mit_Hilfe_des_Referenz_Validators_und_Custom_Profilen_[0-9].+\.html$" containing all of the following lines:
    """
    FHIR-Validierung mit Hilfe des Referenz Validators und Custom Profilen
    Dann FHIR pr&uuml;fe die aktuelle Anfrage enth&auml;lt im Body eine g&uuml;ltige ISIK1 Ressource
    Und FHIR pr&uuml;fe die aktuelle Antwort enth&auml;lt im Body eine g&uuml;ltige ISIK1 Ressource
    evidence-type-WARN"
    evidence-type-INFO"
    """
    Und in "target/evidences/" exists no file matching "^evidence_FHIR-Validierung_mit_Hilfe_des_Referenz_Validators_und_Custom_Profilen_[0-9].+\.html$" containing any of the following lines:
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
