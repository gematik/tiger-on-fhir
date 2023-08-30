# Tiger-On-FHIR

![TigerLogo](doc/images/tiger2-plain.svg)

## Tiger-On-FHIR extension

This extension provides a set of simple BDD steps which can be used in combination with the Tiger RbelValidator steps to validate requests and responses. You can use the RbelValidator steps to filter request/responses and then check for valid FHIR content therein. 

```gherkin
When TGR find request to path '/erp/42'
When FHIR current request body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value = "Device").exists()'
When FHIR current response body evaluates the FHIRPath 'Bundle.entry.resource.author.type.where(value != "Dinosaur").exists()'
When FHIR current request body fails the FHIRPath 'Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists()'
When FHIR current request body evaluates the FHIRPaths:
"""
  Bundle.entry.resource.author.type.where(value != "Practitioner" and value != "Device").exists().not()
  Bundle.entry.resource.author.type.where(value != "Device") != "Dinoaur"
"""
```

In addition, you may also check complete messages for compliance with certain FHIR profiles.

```gherkin
When TGR find request to path '/erp/42'
Then FHIR current request body is valid FHIR resource of type ERP
Then FHIR current response body is valid FHIR resource of type ERP
And FHIR current request '$.body' is valid FHIR resource of type erp
```

## What is Tiger

Tiger is a BDD oriented test platform implemented by gematik GmbH.
Take a look at our short pitch video explaining the basic idea of Tiger.

[![](doc/images/tiger-promo-screenie.png)](https://youtu.be/eJJZDeuFlyI)

## Tiger-User-Manual

Every information at one place: check out our Tiger-User-Manual!

* HTML: [Tiger-User-Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html)

* PDF: [Tiger-User-Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.pdf)

## Using other libraries / source code

For the evidence reports we use the freemarker template engine. The FHIR validations are performed by the gematik FHIR Referenzvalidator engine, which itself is based upon the HAPI library.

## References

* FHIR [Wikipedia](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwiWoYuf_qj-AhWihf0HHV2lD6oQFnoECC0QAQ&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFast_Healthcare_Interoperability_Resources&usg=AOvVaw03CHVmbv1gjhZQfZlNEnNG)
* FHIR [HL7](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwiWoYuf_qj-AhWihf0HHV2lD6oQFnoECDAQAQ&url=https%3A%2F%2Fwww.hl7.org%2Ffhir%2Foverview.html&usg=AOvVaw2m5-s2cjorasSl4bfg0jp0)
* FHIR [HAPI](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjb5cvP_qj-AhVO_7sIHfhvDK8QFnoECA0QAQ&url=https%3A%2F%2Fhapifhir.io%2F&usg=AOvVaw0mku7swA105AKZ7EN0_Aem)
* gematik [FHIR Referenzvvalidator Github](https://github.com/gematik/app-referencevalidator)
