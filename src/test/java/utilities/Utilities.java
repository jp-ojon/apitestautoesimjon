package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Utilities {
	static List<Map<String, Object>> sims;

	/**
	 * Method that extracts IDs and ICCIDs from a Response
	 * @param response
	 * @param value
	 * @return result map which contains the list of IDs and ICCIDs
	 */
	public static Map<String, List<String>> getListOfIDsAndICCIDs(Response response, String value) {
		// Extracting the response body as a String
		String responseBody = response.getBody().asString();

		// Parse the response using JsonPath
		JsonPath jsonPath = new JsonPath(responseBody);

		// Extract the list of sims
		if ("order".equalsIgnoreCase(value)) {
			sims = jsonPath.getList("data.sims");
		} else if ("esimsList".equalsIgnoreCase(value)) {
			sims = jsonPath.getList("data");
		}

		// Lists to store ids and iccids
		List<String> ids = new ArrayList<>();
		List<String> iccids = new ArrayList<>();

		// Iterate through the list of sims and extract ids and iccids
		for (Map<String, Object> sim : sims) {
			ids.add((String) sim.get("id").toString());
			iccids.add((String) sim.get("iccid"));
		}
		
		// Create a result map to return
		//Map<String, List<String>> result = Map.of("ids", ids, "iccids", iccids);
		
        // Create and return a result map
        Map<String, List<String>> result = new HashMap<>();
        result.put("ids", ids);
        result.put("iccids", iccids);

        return result;
	}
}
