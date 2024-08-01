package api.endpoints;

public class Routes {
	// contains only the Endpoints/URLs
	public static String base_url = "https://sandbox-partners-api.airalo.com/v2/";
	
	public static String token = base_url + "token";
	public static String orders = base_url + "orders";
	public static String getorderid = base_url + "orders/{orderid}";
	public static String esims = base_url + "sims";
	public static String esimsPackageHistory = base_url + "sims/{iccid}/packages";
}
