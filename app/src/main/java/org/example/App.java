package org.example;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class App {
	public static void main(String[] args) {
	}

	public void generateGantt() {

	}

	public List<ProcessSplit> roundRobinWithPriority(Map<Integer, List<ProcessBean>> processList) {
		double timeQuantum = 2.5;
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
			double slice = Math.min(timeQuantum, remainingBurstTime);
			processSplits.add(
					new ProcessSplit(
							process.getId(),
							currentTime,
							currentTime + slice));

			currentTime += slice;
			process.setRemainingBurstTime(remainingBurstTime - slice);

			if (process.getRemainingBurstTime() > 0) {
				queue.offer(process);
			}
		}
		return processSplits;
	}

	public static Map<Integer, List<ProcessBean>> getProcessByPriority() {
		Map<Integer, List<ProcessBean>> processList = new HashMap<>();
		URL resource = App.class.getClassLoader().getResource("test_data_2.csv");
		List<ProcessBean> csv = CsvReader.getProcessBeans(resource.getPath());
		for (ProcessBean process : csv) {
			int priority = process.getPriority();

			processList.computeIfAbsent(priority, k -> new ArrayList<>());
			processList.get(priority).add(process);
		}
		return processList;
	}
}
