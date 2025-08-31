package org.example;

import com.opencsv.bean.CsvBindByName;

public class ProcessBean {
	@CsvBindByName(column = "process", required = true)
	private int id;
	@CsvBindByName(column = "burst_time", required = true)
	private double burstTime;
	@CsvBindByName(column = "burst_time", required = true)
	private double remainingBurstTime;
	@CsvBindByName
	private int priority;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(double burstTime) {
		this.burstTime = burstTime;
	}

	public double getRemainingBurstTime() {
		return remainingBurstTime;
	}

	public void setRemainingBurstTime(double remainingBurstTime) {
		this.remainingBurstTime = remainingBurstTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
