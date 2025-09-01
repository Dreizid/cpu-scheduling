package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GanttGenerator {
	private static final char TOP_LEFT = '┌';
	private static final char TOP_RIGHT = '┐';
	private static final char BOTTOM_RIGHT = '┘';
	private static final char BOTTOM_LEFT = '└';
	private static final char VERTICAL_EDGE = '│';
	private static final char HORIZONTAL_EDGE = '─';
	private static final char TOP_JUNCTION = '┬';
	private static final char BOTTOM_JUNCTION = '┴';

	public static void renderGantt(List<ProcessSplit> processList, double timeQuantum) {
		HashMap<String, String> chartTemplate = new HashMap<>();
		chartTemplate.put("top", Character.toString(TOP_LEFT));
		chartTemplate.put("middle", Character.toString(VERTICAL_EDGE));
		chartTemplate.put("bottom", Character.toString(BOTTOM_LEFT));
		chartTemplate.put("numbers", "");

		List<Map<String, String>> charts = new ArrayList<>();
		charts.add(new HashMap<String, String>(chartTemplate));

		int scale = 5;
		int columns = 40;
		int rows = 12;

		double finalTurnAroundTime = 0;
		for (ProcessSplit processSplit : processList) {
			String label = String.format("P%d", processSplit.getProcessId());
			String waitingTime = String.format("%.1f", processSplit.getStartTime());
			int width = Math.max(label.length() + waitingTime.length(),
					(int) (Math.round((processSplit.getEndTime() - processSplit.getStartTime()) * scale)));

			int lastIndex = charts.size() - 1;
			if (charts.get(lastIndex).get("top").length() - 1 + width + 1 > columns) {
				charts.get(lastIndex).put("top",
						charts.get(lastIndex).get("top").substring(0, charts.get(lastIndex).get("top").length() - 1)
								+ Character.toString(TOP_RIGHT));
				charts.get(lastIndex).put("bottom",
						charts.get(lastIndex).get("bottom").substring(0, charts.get(lastIndex).get("bottom").length() - 1)
								+ Character.toString(BOTTOM_RIGHT));

				charts.add(new HashMap<String, String>(chartTemplate));
				lastIndex++;
			}

			charts.get(lastIndex).put("top",
					charts.get(lastIndex).get("top") + Character.toString(HORIZONTAL_EDGE).repeat(width) + TOP_JUNCTION);
			charts.get(lastIndex).put("middle",
					charts.get(lastIndex).get("middle") + center(label, width) + VERTICAL_EDGE);
			charts.get(lastIndex).put("bottom",
					charts.get(lastIndex).get("bottom") + Character.toString(HORIZONTAL_EDGE).repeat(width) + BOTTOM_JUNCTION);
			charts.get(lastIndex).put("numbers",
					charts.get(lastIndex).get("numbers") + ljust(waitingTime, width + 1));
			finalTurnAroundTime = processSplit.getEndTime();
		}

		int lastIndex = charts.size() - 1;
		charts.get(lastIndex).put("top",
				charts.get(lastIndex).get("top").substring(0, charts.get(lastIndex).get("top").length() - 1)
						+ Character.toString(TOP_RIGHT));
		charts.get(lastIndex).put("bottom",
				charts.get(lastIndex).get("bottom").substring(0, charts.get(lastIndex).get("bottom").length() - 1)
						+ Character.toString(BOTTOM_RIGHT));
		charts.get(lastIndex).put("numbers",
				charts.get(lastIndex).get("numbers") + rjust(String.format("%.1f", finalTurnAroundTime), 0));
		print(charts);
	}

	private static void print(List<Map<String, String>> charts) {
		for (Map<String, String> chart : charts) {
			System.out.println(chart.get("top"));
			System.out.println(chart.get("middle"));
			System.out.println(chart.get("bottom"));
			System.out.println(chart.get("numbers"));
		}
	}

	private static String center(String text, int width) {
		if (text.length() >= width)
			return text;
		int padding = (width - text.length()) / 2;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < padding; i++)
			sb.append(" ");
		sb.append(text);

		while (sb.length() < width)
			sb.append(" ");
		return sb.toString();
	}

	public static String ljust(String text, int width) {
		if (text.length() >= width)
			return text;
		return text + " ".repeat(width - text.length());
	}

	public static String rjust(String text, int width) {
		if (text.length() >= width)
			return text;
		return " ".repeat(width - text.length()) + text;
	}
}
