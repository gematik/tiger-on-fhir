# Changelog tiger-on-fhir

# Release 1.2.6

* upgraded ref validator to v2.5
* upgraded tiger to v3.3.0
* upgraded maven plugins to latest versions

# Release 1.2.5

* TGRFHIR-21: Evaluating the FhirPath and storing the value in a variable did not work for responses
* TGRFHIR-22: Cucumber-Step matches to several GlueCode methods

# Release 1.2.2
* Added support for reference validator plugins
* TGRFHIR-16: new test step for validating a fhirpath and storing the result into a variable added
* versions for tiger, refvalidator, plugins and mockito updated

# Release 1.2.1

* Upgrade to upcoming Tiger 3.0.0 release
* TGRFHIR-11: all BDD steps now do resolve their params using TigerGlobalConfiguration and JEXL Executor. This means you can use properties defined in your tiger yamls and JEXL / RbelPath expressions as parameters for your FHIR BDD steps.

# Release 1.2.0

* TGRISIK-12: Updates the reference validator

## Breaking changes

The depreciated Steps starting with TGR are now removed.
All steps start now with FHIR.

# Release 1.1.0

## Breaking changes

* TGRFHIR-9: Einführung deutscher Steps und Umbenennung des Anfangswortes bei den Steps.

Alle FHIR Steps beginnen nun mit "FHIR", die mit "TGR" beginnenden FHIR Steps sind deprecated und werden in einer
zukünftigen Version entfernt.

# Release 1.0.0

## Features

* TGRFHIR-4: Support custom failure messages for fhirpath validations

* Upgrade to reference validator 0.4.1

* TGR-761: It is now possible to check requests, responses or nodes of with one or more FhirPath expressions. The
  following Scenario shows a simple usage of the new BDD steps

```gherkin
 Scenario: FHIR-Validation with FHIRPath
Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
When TGR find request to path '/erp/42'
When TGR current request body evaluates the FHIRPath '${tiger.my.configurable.fhirpathExpression}'
When TGR current request body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value = "${tiger.my.configurable.author.type.valid}").exists()'
When TGR current response body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
When TGR current request body fails the FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
When TGR current response body fails the FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
When TGR current request body evaluates the FHIRPaths:
"""
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"

    """
```

