package org.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import de.vandermeer.asciitable.AsciiTable;

public class RRP {
	private static URL resource = RRP.class.getClassLoader().getResource("test_data_3.csv");
	private double timeQuantum = 2;

	public static void main(String[] args) {
		AsciiTable at = new AsciiTable();
		List<String[]> csvContent = CsvReader.readCsv(resource.getPath());
		for (String[] line : csvContent) {
			at.addRule();
			at.addRow(line);
		}
		at.addRule();
		String rend = at.render();
		System.out.println(rend);
		Map<Integer, List<ProcessBean>> processList = RRP.getProcessByPriority();
		RRP test = new RRP();
		List<ProcessSplit> list = test.roundRobinWithPriority(processList);
		test.generateGantt(list);
		System.out.println(test.calculateAverageWaitingTime(list));
		System.out.println(test.calculateAverageTurnAroundTime(list));
	}

	public void generateGantt(List<ProcessSplit> list) {
		GanttGenerator.renderGantt(list, timeQuantum);
	}

	public double calculateAverageWaitingTime(List<ProcessSplit> processList) {
		HashMap<Integer, ProcessSplit> lastSplits = getLastProcesses(processList);
		double totalWaitingTime = 0;
		for (ProcessSplit v : lastSplits.values()) {
			totalWaitingTime += v.getStartTime();
		}
		return totalWaitingTime / lastSplits.size();
	}

	public double calculateAverageTurnAroundTime(List<ProcessSplit> processList) {
		HashMap<Integer, ProcessSplit> lastSplits = getLastProcesses(processList);
		double totalTurnAroundTime = 0;
		for (ProcessSplit v : lastSplits.values()) {
			totalTurnAroundTime += v.getEndTime();
		}
		return totalTurnAroundTime / lastSplits.size();
	}

	public HashMap<Integer, ProcessSplit> getLastProcesses(List<ProcessSplit> processList) {
		HashMap<Integer, ProcessSplit> lastSplits = new HashMap<>();
		for (ProcessSplit process : processList) {
			lastSplits.put(process.getProcessId(), process);
		}
		return lastSplits;
	}

	public List<ProcessSplit> roundRobinWithPriority(Map<Integer, List<ProcessBean>> processList) {
		List<ProcessSplit> processSplits = new ArrayList<>();
		processList.forEach((k, v) -> {
			processSplits.addAll(roundRobin(v, timeQuantum,
					(processSplits.isEmpty()) ? 0 : processSplits.get(processSplits.size() - 1).getEndTime()));
		});
		return processSplits;
	}

	public List<ProcessSplit> roundRobin(List<ProcessBean> processList, double timeQuantum, double prevTime) {
		Queue<ProcessBean> queue = new LinkedList<ProcessBean>(processList);
		List<ProcessSplit> processSplits = new ArrayList<>();
		double currentTime = prevTime;
		while (!queue.isEmpty()) {
			ProcessBean process = queue.poll();
			double remainingBurstTime = process.getRemainingBurstTime();
			double slice = (!queue.isEmpty()) ? Math.min(timeQuantum, remainingBurstTime) : remainingBurstTime;
			processSplits.add(
					new ProcessSplit(
							process.getId(),
							currentTime,
							currentTime + slice));

			currentTime += slice;
			process.setRemainingBurstTime(remainingBurstTime - slice);

			if (process.getRemainingBurstTime() > 0 && !queue.isEmpty()) {
				queue.offer(process);
			}
		}
		return processSplits;
	}

	public static Map<Integer, List<ProcessBean>> getProcessByPriority() {
		Map<Integer, List<ProcessBean>> processList = new HashMap<>();
		List<ProcessBean> csv = CsvReader.getProcessBeans(resource.getPath());
		for (ProcessBean process : csv) {
			int priority = process.getPriority();

			processList.computeIfAbsent(priority, k -> new ArrayList<>());
			processList.get(priority).add(process);
		}
		return processList;
	}
}
