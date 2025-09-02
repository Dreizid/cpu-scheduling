package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliHandler {

	private final Scanner scanner;

	public CliHandler(Scanner scanner) {
		this.scanner = scanner;
	}

	class ProcessInputResult {
		private final List<ProcessBean> beans;
		private final double timeQuantum;
		private final List<String[]> csvContents;

		public ProcessInputResult(List<ProcessBean> beans, double timeQuantum, List<String[]> csvContents) {
			this.beans = beans;
			this.timeQuantum = timeQuantum;
			this.csvContents = csvContents;
		}

		public List<ProcessBean> getBeans() {
			return beans;
		}

		public double getTimeQuantum() {
			return timeQuantum;
		}

		public List<String[]> getCsvContents() {
			return csvContents;
		}
	}

	public ProcessInputResult readInput() {
		int processCount = 1;

		List<ProcessBean> beans = new ArrayList<>();
		List<String[]> content = new ArrayList<>();
		content.add(new String[] { "process", "burst_time", "priority" });
		System.out.println("Enter burst time for each process (N for skip or finish):");
		while (true) {
			String[] row = new String[3];
			System.out.printf("P%d (Burst Time): \n", processCount);
			String burstTime = scanner.nextLine();
			if (burstTime.equalsIgnoreCase("n")) {
				break;
			}
			System.out.printf("P%d (Priority): \n", processCount);
			String priority = scanner.nextLine();
			if (priority.equalsIgnoreCase("n")) {
				break;
			}

			try {
				ProcessBean bean = new ProcessBean();
				bean.setId(processCount);
				bean.setBurstTime(Double.parseDouble(burstTime));
				bean.setPriority(Integer.parseInt(priority));
				bean.setRemainingBurstTime(Double.parseDouble(burstTime));
				beans.add(bean);
				row[0] = String.valueOf(processCount);
				row[1] = burstTime;
				row[2] = priority;
				content.add(row);
			} catch (NumberFormatException e) {
				System.out.println(e);
			}
			processCount++;
		}
		System.out.println("Enter time quantum: ");
		String timeQuantum = scanner.nextLine();

		return new ProcessInputResult(beans, Double.valueOf(timeQuantum), content);
	}

	public ProcessInputResult readCsv() {
		System.out.println("Eneter the CSV Filepath: ");
		;
		String filePath = scanner.nextLine();
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			System.out.println("File not found: " + filePath);
			return null;
		}
		System.out.println("Enter time quantum: ");
		String timeQuantum = scanner.nextLine();

		return new ProcessInputResult(CsvReader.getProcessBeans(filePath), Double.valueOf(timeQuantum),
				CsvReader.readCsv(filePath));

	}

	public ProcessInputResult chooseInputMethod() {
		while (true) {
			System.out.println("Choose an input method: ");
			System.out.println("1. Manual input");
			System.out.println("2. CSV file");
			System.out.print("Enter 1 or 2: ");

			String choice = scanner.nextLine();
			switch (choice) {
				case "1" -> {
					return readInput();
				}
				case "2" -> {
					return readCsv();
				}
				case "3" -> {
					break;
				}
				default -> System.out.println("Invalid choice. Try again.");
			}
		}
	}
}
