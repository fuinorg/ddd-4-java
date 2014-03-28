Feature: Handling contracts 
 
 Scenario: Successfully create a contract 

	When a contract with key "C12345" is created for vendor "V00001" with name "Bulldog Inc" that is valid starting with "2014-01-01T00:00:00.000+01:00" 

	Then the following event is published 

		"""
		<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		<contract-created-event event-id="10000000-0000-0000-0000-000000000000" event-timestamp="1980-01-01T00:00:00.000+01:00"
		                        entity-id-path="Contract 10000000-0000-0000-0000-000000000000"> 
		    <valid-from>2014-01-01T00:00:00.000+01:00</valid-from>
			<contract id="10000000-0000-0000-0000-000000000000" key="C12345" />
			<vendor id="10000000-0000-0000-0000-000000000000" key="V00001" name="Bulldog Inc" />
		</contract-created-event>		
    	"""

		
 
 Scenario: Create contract with unknown vendor identifier

	When a contract is created with an invalid vendor ID 

	Then it should fail with a "VendorNotFoundException" 
	
	 	
	