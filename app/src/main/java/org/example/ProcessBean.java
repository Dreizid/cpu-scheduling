package org.example;

import com.opencsv.bean.CsvBindByName;

public class ProcessBean {
	@CsvBindByName(column = "process_name", required = true)
	private String name;
	@CsvBindByName(column = "burst_time", required = true)
	private int originalBurstTime;
	@CsvBindByName
	private int priority;
	private int remainingBurstTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOriginalBurstTime() {
		return originalBurstTime;
	}

	public void setOriginalBurstTime(int originalBurstTime) {
		this.originalBurstTime = originalBurstTime;
	}

	public int getRemainingBurstTime() {
		return remainingBurstTime;
	}

	public void setRemainingBurstTime(int remainingBurstTime) {
		this.remainingBurstTime = remainingBurstTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
