package org.example;

public class ProcessSplit {
	private int processId;
	private double startTime;
	private double endTime;

	public ProcessSplit(int processId, double startTime, double endTime) {
		this.processId = processId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public int getProcessId() {
		return processId;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}
}
