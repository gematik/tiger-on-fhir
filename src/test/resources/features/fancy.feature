Feature: FHIR Validation

  Scenario: FHIR-Validation with the help of the reference-validators
    Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
    When TGR find request to path '/erp/42'
    Then TGR current request body is valid FHIR resource of type ERP
    Then TGR current response body is valid FHIR resource of type ERP
    When TGR find next request to path '.+'
    Then TGR current request '$.body' is valid FHIR resource of type erp


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
    When TGR current response body evaluates the FHIRPaths:
    """
    (Bundle.entry.count() < 7 and Bundle.entry.resource.author.count() > 2).not()
    Bundle.entry.resource.author.where(type.value.matches("D.*i.+")).type != "Dinosaur"
    """

  Scenario: Report of previous scenarios should exist and contain its Details
    Given in "target/evidences/" exists a file matching "^evidence_FHIR-Validation_with_the_help_of_the_reference-validators_.+\.html$" containing all of the following lines:
    """
    FHIR-Validation with the help of the reference-validators
    Then TGR current request body is valid FHIR resource of type ERP
    Then TGR current response body is valid FHIR resource of type ERP
    """
    Given in "target/evidences/" exists a file matching "^evidence_FHIR-Validation_with_FHIRPath_.+\.html$" containing all of the following lines:
    """
    FHIR-Validation with FHIRPath
    Bundle.entry.resource.author.type.where(value =
    """
