package org.example;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
	public static List<String[]> readCsv(String filePath) {
		try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
			List<String[]> allRows = reader.readAll();

			return allRows;
		} catch (IOException | CsvException e) {
			e.printStackTrace();

			return null;
		}
	}

	public static List<ProcessBean> getProcessBeans(String filePath) {
		try {
			List<ProcessBean> beans = new CsvToBeanBuilder<ProcessBean>(new FileReader(filePath)).withType(ProcessBean.class)
					.build().parse();
			return beans;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
