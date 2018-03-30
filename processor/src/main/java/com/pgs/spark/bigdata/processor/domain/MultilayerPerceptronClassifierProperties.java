package com.pgs.spark.bigdata.processor.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "multilayerPerceptronClassifier")
public class MultilayerPerceptronClassifierProperties {

	private int outputClassNumber;
	private int maxIter;
	private long seed;
	private int blockSize;
	private int featuresSize;
	private int[] hiddenLayers;

	public int getOutputClassNumber() {
		return outputClassNumber;
	}

	public void setOutputClassNumber(int outputClassNumber) {
		this.outputClassNumber = outputClassNumber;
	}

	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getFeaturesSize() {
		return featuresSize;
	}

	public void setFeaturesSize(int featuresSize) {
		this.featuresSize = featuresSize;
	}

	public int[] getHiddenLayers() {
		return hiddenLayers;
	}

	public void setHiddenLayers(int[] hiddenLayers) {
		this.hiddenLayers = hiddenLayers;
	}

}
