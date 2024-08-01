package api.tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.endpoints.EndPoints;
import io.restassured.response.Response;
import utilities.CSVReader;
import utilities.ConfigFileReader;
import utilities.Utilities;;

public class ApiTests {

	// Private API Keys stored under config.properties file
	private String apiKey = ""; //clear
	private String apiSecret = ""; //clear

	// Declare variables with common usage
	Response response;
	Map<String, List<String>> result;
	String packageId;
	String orderid;
	String type;
	String data;
	Integer quantity;
	String currency;
	Float price;

	// The @BeforeSuite annotated method will run before the execution of all the test methods in the suite.
	@BeforeSuite
	public void authenticate() {
		apiKey = ConfigFileReader.getApiKey();
		apiSecret = ConfigFileReader.getApiSecret();

		response = EndPoints.postRequestAccessToken(apiKey, apiSecret);
		Assert.assertEquals(response.getStatusCode(), 200, "Authentication failed");
	}

	// This @DataProvider method will provide data to any test method that declares that its Data Provider is named "csvData"
	@DataProvider(name = "csvData")
	public Object[][] csvDataProvider() {
		// Specify the path to your CSV file
		String filePath = "testdata.csv"; // Because of the way CSVReader was programmed, default folder path would be src/test/resources
		List<String[]> data = CSVReader.readCSV(filePath);

		// Skip header row
		data.remove(0);

		// Convert List<String[]> to Object[][]
		Object[][] dataArray = new Object[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			dataArray[i] = data.get(i);
		}
		return dataArray;
	}

	@Test(priority = 1, description = "Test Order ESIMs And Verify Details. Submit an order and verify details on the ESIMs list and ESIMs package history.", dataProvider = "csvData")
	public void testOrderESIMsAndVerifyDetails(String packageidcsv, String typecsv, String datacsv, String quantitycsv,
			String pricecsv, String currencycsv, String description) {
		
		// Convert the quantitycsv and pricecsv to the desired types
		Integer quantityCSVInt = Integer.parseInt(quantitycsv);
		Float priceCSVFloat = Float.parseFloat(pricecsv);
		
		//Submit Order and store response for verification
		response = EndPoints.postSubmitOrder(quantityCSVInt, packageidcsv, typecsv, description);
		
        // Print response body for debugging purposes
        //String responseBody = response.getBody().asString();
        //System.out.println("Response Body: " + responseBody);
		
		//Extract and Verify Post Submit Order Response. Check the response body for the correct information, including order details and eSIM properties.
		System.out.println("Post Submit Order Response Status Code: " + response.getStatusCode());
		orderid = response.jsonPath().getString("data.id");
		packageId = response.jsonPath().getString("data.package_id");
		type = response.jsonPath().getString("data.type");
		data = response.jsonPath().getString("data.data");
		quantity = response.jsonPath().get("data.quantity");
		price = response.jsonPath().getFloat("data.price");
		currency = response.jsonPath().getString("data.currency");
		System.out.printf("Orderid: %s, packageId: %s, Type: %s, Data: %s, Quantity: %s, Price: %s, Currency: %s%n",
				orderid, packageId, type, data, quantity, price, currency);
		// Assert if the values are correct. Convert both values to lower case, making the comparison case-insensitive and more flexible.
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
		Assert.assertEquals(packageId.toLowerCase(), packageidcsv.toLowerCase(), "Package ID value is not as expected");
		Assert.assertEquals(type.toLowerCase(), typecsv.toLowerCase(), "Type value is not as expected");
		Assert.assertEquals(data.toLowerCase(), datacsv.toLowerCase(), "Data value is not as expected");
		Assert.assertEquals(quantity, quantityCSVInt, "Quantity does not match the expected value");
		Assert.assertEquals(price, priceCSVFloat, "Price does not match the expected value");
		Assert.assertEquals(currency.toLowerCase(), currencycsv.toLowerCase(), "Currency does not match the expected value");

		// Calling method to get and store ids and iccids on the order. For verification later.
		result = Utilities.getListOfIDsAndICCIDs(response, "order");
		List<String> idsListOfOrder = result.get("ids");
		List<String> iccidsListOfOrder = result.get("iccids");
		System.out.println("Expected IDs: " + idsListOfOrder);
		System.out.println("Expected ICCID: " + iccidsListOfOrder);
		
		//Get ESIMs List and store response for verification
		response = EndPoints.getESIMsList("string");
		System.out.println("Get ESIMs List Response Status Code: " + response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

		// Calling the method to get ids and iccids on the ESIMs List. 
		result = Utilities.getListOfIDsAndICCIDs(response, "esimsList");
		List<String> idsESimsList = result.get("ids");
		List<String> iccidsESimsList = result.get("iccids");
		System.out.println("ESIMs List of IDs: " + idsESimsList);
		System.out.println("ESIMs List of ICCIDs: " + iccidsESimsList);

		// Assert that all expected ids and iccids from the order are found in the ESIMs List.
		boolean idsCheck = idsESimsList.containsAll(idsListOfOrder);
		boolean iccidsCheck = iccidsESimsList.containsAll(iccidsListOfOrder);
		Assert.assertTrue(idsCheck, "List does not contain all eSIMs in the order");
		Assert.assertTrue(iccidsCheck, "List does not contain all eSIMs in the order");
		if (iccidsCheck && idsCheck) {
			System.out.println("IDs Check: " + idsCheck + ", List contains all eSIMs in the order");
			System.out.println("ICCIDs Check: " + iccidsCheck + ", List contains all eSIMs in the order");
		} else {
			System.out.println("IDs Check: " + idsCheck + ", List does not contain all eSIMs in the order");
			System.out.println("ICCIDs Check: " + iccidsCheck + ", List does not contain all eSIMs in the order");
		}

		// Call getESimPackageHistory for each ICCID, and check the response body for the correct information, including order details and eSIM properties.
		for (String iccid : iccidsListOfOrder) {
			//Get ESIM Package History and store response for verification
			response = EndPoints.getESimPackageHistory(iccid);

			//Extract and Verify get ESim Package History Response. Check the response body for the correct information, including order details and eSIM properties.
			System.out.println("Get ESIM Package History Response Status Code: " + response.getStatusCode());
			packageId = response.jsonPath().getString("data[0].package.id");
			type = response.jsonPath().getString("data[0].package.type");
			data = response.jsonPath().getString("data[0].package.data");
			price = response.jsonPath().getFloat("data[0].package.price");
			System.out.printf("ICCID: %s, packageId: %s, Type: %s, Data: %s%n", iccid, packageId, type, data);
			// Assert if the values are correct. Convert both values to lower case, making the comparison case-insensitive and more flexible.
			Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
			Assert.assertEquals(packageId.toLowerCase(), packageidcsv.toLowerCase(), "Package ID value is not as expected");
			Assert.assertEquals(type.toLowerCase(), typecsv.toLowerCase(), "Type value is not as expected");
			Assert.assertEquals(data.toLowerCase(), datacsv.toLowerCase(), "Data value is not as expected");
			Assert.assertEquals(price, priceCSVFloat, "Price does not match the expected value");
		}

	}

}
