# Changelog tiger-on-fhir

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

