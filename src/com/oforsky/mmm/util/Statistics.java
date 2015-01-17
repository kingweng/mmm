package com.oforsky.mmm.util;

import java.util.Arrays;

public class Statistics {
	double[] data;
	double size;

	public Statistics(double[] data) {
		this.data = data;
		size = data.length;
	}

	public Statistics(int[] data) {
		double[] temp = asDoubleArray(data);
		this.data = temp;
		size = data.length;
	}

	private double[] asDoubleArray(int[] data) {
		double[] temp = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			temp[i] = data[i];
		}
		return temp;
	}

	public double getMean() {
		double sum = 0.0;
		for (double a : data)
			sum += a;
		return sum / size;
	}

	public double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (double a : data)
			temp += (mean - a) * (mean - a);
		return temp / size;
	}

	public double getStdDev() {
		return Math.sqrt(getVariance());
	}

	public double median() {
		double[] b = new double[data.length];
		System.arraycopy(data, 0, b, 0, b.length);
		Arrays.sort(b);

		if (data.length % 2 == 0) {
			return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
		} else {
			return b[b.length / 2];
		}
	}

}
