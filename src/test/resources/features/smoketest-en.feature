Feature: FHIR Validation English

  Scenario: FHIR-Validation with the help of the reference-validators
    Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
    When TGR find request to path '/erp/42'
    Then FHIR current request body is a valid ERP resource
    Then FHIR current response body is a valid ERP resource
    When TGR find next request to path '.+'
    Then FHIR current request at '$.body' is a valid ERP resource

  Scenario: FHIR-Validation with the help of the reference-validators and custom profile
    Given TGR reads the following .tgr file 'src/test/resources/fhir-custom-profile-data.tgr'
    When TGR find request to path '/isik1/validInOwnProfile'
    Then FHIR current request body is a valid ISIK1 resource
    Then FHIR current request at '$.body' is a valid ISIK1 resource
    Then FHIR current response body is a valid ISIK1 resource
    Then FHIR current request at '$.body' is a valid ISIK1 resource
    When TGR find request to path '/isik1/invalidOwnProfile'
    Then FHIR current request body is a valid ISIK1 resource and conforms to profile "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger"
    Then FHIR current request at '$.body' is a valid ISIK1 resource and conforms to profile "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger"
    Then FHIR current response body is a valid ISIK1 resource and conforms to profile "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger"
    Then FHIR current request at '$.body' is a valid ISIK1 resource and conforms to profile "https://gematik.de/fhir/ISiK/StructureDefinition/ISiKAngehoeriger"

  Scenario: FHIR-Validation with FHIRPath
    Given TGR reads the following .tgr file 'src/test/resources/fhir.tgr'
    When TGR find request to path '/erp/42'
    When FHIR current request body evaluates the FHIRPath '${tiger.my.configurable.fhirpathExpression}'
    When FHIR current request body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value = "${tiger.my.configurable.author.type.valid}").exists()'
    When FHIR current response body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value != "${tiger.my.configurable.author.type.invalid}").exists()'
    When FHIR current request body fails the FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
    When FHIR current response body fails the FHIRPath 'Bundle.entry.resource.author.where(type.value.startsWith("D")).type = "${tiger.my.configurable.author.type.invalid}"'
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
    FHIR-Validation with the help of the reference-validators
    Then FHIR current request body is a valid ISIK1 resource
    Then FHIR current response body is a valid ISIK1 resource
    evidence-type-INFO"
    evidence-type-WARN"
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
