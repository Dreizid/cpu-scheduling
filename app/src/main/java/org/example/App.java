package org.example;

import java.net.URL;
import java.util.List;
import java.util.Arrays;

import org.example.CsvReader;

public class App {
	public static void main(String[] args) {
		URL resource = App.class.getClassLoader().getResource("test_data.csv");
		List<String[]> csv = CsvReader.readCsv(resource.getPath());
		for (int line = 0; line < csv.size(); line++) {
			System.out.println(Arrays.toString(csv.get(line)));
		}
	}
}
