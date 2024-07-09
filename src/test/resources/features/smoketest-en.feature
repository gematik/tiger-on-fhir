Feature: FHIR Validation English

  Background:
    Given TGR clear recorded messages

  Scenario: FHIR-Validation with the help of the reference-validators
    Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
    When TGR find request to path '/erp/42'
    And FHIR current response body evaluates the FHIRPath 'true' with error message 'blabla'
    And FHIR current response at '$.body' evaluates the FHIRPath 'true'
    Then FHIR current request body is a valid ERP resource
    Then FHIR current response body is a valid ERP resource
    Then FHIR current response body is a valid ERP resource and conforms to profile "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0"
    When TGR find next request to path '.+'
    Then FHIR current request at '$.body' is a valid ERP resource

  Scenario: FHIR-Validation with the help of the reference-validators and custom profile
    Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
    When TGR find request to path '/erp/42'
    Then FHIR current request body is a valid ERP resource and conforms to profile "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0"
    Then FHIR current request at '$.body' is a valid ERP resource and conforms to profile "https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0"
    Then FHIR current request at '$.body' fails the FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
    Then FHIR current request at '$.body' evaluates the FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    Then FHIR current request at '$.body' evaluates the FHIRPath '${tiger.my.configurable.fhirpathExpression}' with error message 'Error'

  Scenario: FHIR-Validation with FHIRPath
    Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
    When TGR find request to path '/erp/42'
    When FHIR current request body evaluates the FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    When FHIR current request body evaluates the FHIRPath '${tiger.my.configurable.fhirpathExpression}' with error message 'Error'
    When FHIR current request body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value = "${tiger.my.configurable.author.type.valid}").exists()'
    When FHIR current response body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
    When FHIR current request body fails the FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
    When FHIR current response body fails the FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
    When FHIR current response at '$.body' fails the FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
    When FHIR current request body evaluates the FHIRPaths:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """
    When FHIR current response body evaluates the FHIRPaths:
    """
      (Bundle.entry.count() < 7 and Bundle.entry.resource.author.count() > 2).not()
      Bundle.entry.resource.author.where(type.value.matches("D.*i.+")).type != "Dinosaur"
    """
    When FHIR current request at '$.body' evaluates the FHIRPaths:
    """
      Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
      Bundle.entry.resource.author.type.where(value != "Device") != "${tiger.my.configurable.author.type.invalid}"
    """

  Scenario: Report of previous scenarios should exist and contain its Details
    Then in "target/evidences/" exists a file matching "^evidence_FHIR-Validation_with_the_help_of_the_reference-validators_[0-9].+\.html$" containing all of the following lines:
    """
    FHIR-Validation with the help of the reference-validators
    Then FHIR current request body is a valid ERP resource
    Then FHIR current response body is a valid ERP resource
    evidence-type-INFO"
    """
    And in "target/evidences/" exists no file matching "^evidence_FHIR-Validation_with_the_help_of_the_reference-validators_[0-9].+\.html$" containing any of the following lines:
    """
    evidence-type-WARN"
    evidence-type-ERROR"
    """
    And in "target/evidences/" exists a file matching "^evidence_FHIR-Validation_with_the_help_of_the_reference-validators_and_custom_profile_[0-9].+\.html$" containing all of the following lines:
    """
    FHIR-Validation with the help of the reference-validators and custom profile
    Then FHIR current request body is a valid ERP resource and conforms to profile &quot;https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0&quot;
    Then FHIR current request at '$.body' is a valid ERP resource and conforms to profile &quot;https://fhir.kbv.de/StructureDefinition/KBV_PR_ERP_Bundle|1.1.0&quot;
    evidence-type-INFO
    evidence-type-INFO
    """
    And in "target/evidences/" exists no file matching "^evidence_FHIR-Validation_with_the_help_of_the_reference-validators_and_custom_profile_[0-9].+\.html$" containing any of the following lines:
    """
    evidence-type-ERROR"
    """
    And in "target/evidences/" exists a file matching "^evidence_FHIR-Validation_with_FHIRPath_.+\.html$" containing all of the following lines:
    """
    FHIR-Validation with FHIRPath
    Bundle.entry.resource.author.type.where(value =
    evidence-type-INFO"
    """
    And in "target/evidences/" exists no file matching "^evidence_FHIR-Validation_with_FHIRPath_.+\.html$" containing any of the following lines:
    """
    evidence-type-WARN"
    evidence-type-ERROR"
    """

  Scenario: FHIR-Validation using plugin with the help of the reference-validator
    Given TGR reads the following .tgr file 'src/test/resources/fhir-plugins.tgr'
    When TGR find request to path '/minimal/42'
    Then FHIR current request body is a valid minimal resource
    Then FHIR current response body is a valid minimal resource
    When TGR find next request to path '.+'
    Then FHIR current request at '$.body' is a valid minimal resource