package org.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.example.CliHandler.ProcessInputResult;

import de.vandermeer.asciitable.AsciiTable;

public class RRP {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		CliHandler cli = new CliHandler(scanner);
		ProcessInputResult input = cli.chooseInputMethod();
		generateOutput(input.getBeans(), input.getTimeQuantum(), input.getCsvContents());
		scanner.close();
	}

	public static void generateOutput(List<ProcessBean> list, double timeQuantum, List<String[]> csvContent) {
		AsciiTable at = new AsciiTable();
		for (String[] line : csvContent) {
			at.addRule();
			at.addRow(line);
		}
		at.addRule();
		String rend = at.render();
		System.out.println(rend);
		Map<Integer, List<ProcessBean>> processList = getProcessByPriority(list);
		List<ProcessSplit> rrp = roundRobinWithPriority(processList, timeQuantum);
		GanttGenerator.renderGantt(rrp, timeQuantum);
		System.out.println("Average Waiting Time: " + calculateAverageWaitingTime(rrp) + " ms");
		System.out.println("Average Turn Around Time: " + calculateAverageTurnAroundTime(rrp) + " ms");
	}

	public static double calculateAverageWaitingTime(List<ProcessSplit> processList) {
		HashMap<Integer, ProcessSplit> lastSplits = getLastProcesses(processList);
		double totalWaitingTime = 0;
		for (ProcessSplit v : lastSplits.values()) {
			totalWaitingTime += v.getStartTime();
		}
		return totalWaitingTime / lastSplits.size();
	}

	public static double calculateAverageTurnAroundTime(List<ProcessSplit> processList) {
		HashMap<Integer, ProcessSplit> lastSplits = getLastProcesses(processList);
		double totalTurnAroundTime = 0;
		for (ProcessSplit v : lastSplits.values()) {
			totalTurnAroundTime += v.getEndTime();
		}
		return totalTurnAroundTime / lastSplits.size();
	}

	public static HashMap<Integer, ProcessSplit> getLastProcesses(List<ProcessSplit> processList) {
		HashMap<Integer, ProcessSplit> lastSplits = new HashMap<>();
		for (ProcessSplit process : processList) {
			lastSplits.put(process.getProcessId(), process);
		}
		return lastSplits;
	}

	public static List<ProcessSplit> roundRobinWithPriority(Map<Integer, List<ProcessBean>> processList,
			double timeQuantum) {
		List<ProcessSplit> processSplits = new ArrayList<>();
		processList.forEach((k, v) -> {
			processSplits.addAll(roundRobin(v, timeQuantum,
					(processSplits.isEmpty()) ? 0 : processSplits.get(processSplits.size() - 1).getEndTime()));
		});
		return processSplits;
	}

	public static List<ProcessSplit> roundRobin(List<ProcessBean> processList, double timeQuantum, double prevTime) {
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

	public static Map<Integer, List<ProcessBean>> getProcessByPriority(List<ProcessBean> processBeans) {
		Map<Integer, List<ProcessBean>> processList = new HashMap<>();
		for (ProcessBean process : processBeans) {
			int priority = process.getPriority();

			processList.computeIfAbsent(priority, k -> new ArrayList<>());
			processList.get(priority).add(process);
		}
		return processList;
	}
}
