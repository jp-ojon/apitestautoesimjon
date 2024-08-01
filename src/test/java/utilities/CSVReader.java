package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	
	/**
	 * Method that reads thru input .csv test data file. Default path is src/test/resources
	 * @param filePath
	 * @return
	 */
	public static List<String[]> readCSV(String filePath) {
		List<String[]> data = new ArrayList<>();
		String line;
		try (InputStream inputStream = CSVReader.class.getClassLoader().getResourceAsStream(filePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

			while ((line = br.readLine()) != null) {
				data.add(line.split(","));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
}
