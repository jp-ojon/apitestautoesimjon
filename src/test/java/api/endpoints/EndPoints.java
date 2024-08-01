package api.endpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class EndPoints {
	//contains only the CRUD methods implementation
	static String accessToken;
	
	/**
	 * Method to Authenticate
	 * @param client_id
	 * @param client_secret
	 * @return
	 */
	public static Response postRequestAccessToken(String client_id, String client_secret) {
		Response response = given().header("Accept", "application/json")
				.formParam("client_id", client_id).formParam("client_secret", client_secret).formParam("grant_type", "client_credentials")
				.when().post(Routes.token)
				.then()//.log().all()
				.extract().response();
		accessToken = response.jsonPath().getString("data.access_token");
		return response;
	}
	
	/**
	 * This method allows you to submit an order using the orders Endpoint
	 * Provide the required information, such as quantity and package ID, and include optional description if needed
	 * @param quantity
	 * @param package_id
	 * @param type
	 * @param description
	 * @return
	 */
	public static Response postSubmitOrder(int quantity , String package_id, String type, String description) {
		Response response = given().header("Accept", "application/json").header("Authorization", "Bearer "+ accessToken)
				.formParam("quantity", quantity).formParam("package_id", package_id).formParam("type", type).formParam("description", description)
				.when().post(Routes.orders)
				.then()//.log().all()
				.extract().response();
		return response;
	}
	/**
	 * This method allows you to retrieve a list of your eSIMs using the sims Endpoint.
	 * @param orderid
	 * @return
	 */
	public static Response getESIMsList(String orderid) {
		Response response = given().header("Accept", "application/json").header("Authorization", "Bearer "+ accessToken)
				//.formParam("quantity", quantity).formParam("package_id", package_id).formParam("type", type).formParam("description", description)
				//.queryParam("include", orderid +",,")
				.queryParam("page", 1)
				//.log().all()
				.when().get(Routes.esims)
				.then()//.log().all()
				.extract().response();
		return response;
	}
	
	/**
	 * This endpoint allows you to retrieve the details of a specific eSIM using the sims Endpoint
	 * @param iccid
	 * @return
	 */
	public static Response getESimPackageHistory(String iccid) {
		Response response = given().header("Accept", "application/json").header("Authorization", "Bearer "+ accessToken)
				.pathParam("iccid", iccid)
				//.formParam("quantity", quantity).formParam("package_id", package_id).formParam("type", type).formParam("description", description)
				.when().get(Routes.esimsPackageHistory)
				.then()//.log().all()
				.extract().response();
		return response;
	}
	
	/* for future use
	public static Response getOrder(String orderid) {
		Response response = given().header("Accept", "application/json").header("Authorization", "Bearer "+ accessToken)
				.pathParam("orderid", orderid)
				//.formParam("quantity", quantity).formParam("package_id", package_id).formParam("type", type).formParam("description", description)
				.when().get(Routes.getorderid)
				.then()//.log().all()
				.extract().response();
		return response;
	}
	/
	
	public static Response getOrderList() {
		Response response = given().header("Accept", "application/json").header("Authorization", "Bearer "+ accessToken)
				//.formParam("quantity", quantity).formParam("package_id", package_id).formParam("type", type).formParam("description", description)
				.when().get(Routes.orders)
				.then()//.log().all()
				.extract().response();
		return response;
	}
	*/
	
}