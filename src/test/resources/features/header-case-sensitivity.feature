Feature: FHIR Validation with Case-Insensitive Header Matching

  Background:
    Given TGR clear recorded messages

  Scenario: FHIR-Validation with lowercase content-type header
    Given TGR reads the following .tgr file 'src/test/resources/fhir-lowercase-header.tgr'
    When TGR find request to path '/erp/42'
    Then FHIR current request body is a valid ERP resource
    And FHIR current request body evaluates the FHIRPath 'Bundle.entry.count() > 0'

