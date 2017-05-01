/**
 * 
 */
package com.github.taylorchen1993.mixtureOfGaussian.code;

import com.github.taylorchen1993.mixtureOfGaussian.Config;

/**
 * 为了改进高斯混合模型而创造的类
 * 其是一张二维表v(i,j)，即第i个高斯混合模型，第j个样本
 * 参见周志华机器学习P210左边笔记
 * @author TaylorChen
 *
 */
public class AllGaussian_AllSample {
	/**
	 * 第j个样本在第i个高斯混合模型下的带权重的概率
	 */
	private double[][] weightedProbability=new double[Config.getCountOfGaussian()][Samples.getCount()];
	
	/**
	 * 对每一列带权重的概率求和
	 */
	private double[] everyColumnSumOfweightedProbability=new double[Samples.getCount()];
	
	/**
	 * 第j个样本在第i个高斯混合模型下的后验概率
	 */
	private double[][] postProbability=new double[Config.getCountOfGaussian()][Samples.getCount()];

	/**
	 * 每一行后验概率的和
	 */
	private double[] rowSumOfPostProbability=new double[Config.getCountOfGaussian()];
	
	public double[][] getWeightedProbability() {
		return weightedProbability;
	}

	public void setWeightedProbability(double[][] weightedProbability) {
		this.weightedProbability = weightedProbability;
	}

	public double[] getEveryColumnSumOfweightedProbability() {
		return everyColumnSumOfweightedProbability;
	}

	public void setEveryColumnSumOfweightedProbability(double[] everyColumnSumOfweightedProbability) {
		this.everyColumnSumOfweightedProbability = everyColumnSumOfweightedProbability;
	}

	public double[][] getPostProbability() {
		return postProbability;
	}

	public void setPostProbability(double[][] postProbability) {
		this.postProbability = postProbability;
	}

	public AllGaussian_AllSample() {
		super();
	}

	public AllGaussian_AllSample(double[][] weightedProbability, double[] everyColumnSumOfweightedProbability,
			double[][] postProbability) {
		super();
		this.weightedProbability = weightedProbability;
		this.everyColumnSumOfweightedProbability = everyColumnSumOfweightedProbability;
		this.postProbability = postProbability;
	}
	public double[] getRowSumOfPostProbability() {
		return rowSumOfPostProbability;
	}

	public void setRowSumOfPostProbability(double[] rowSumOfPostProbability) {
		this.rowSumOfPostProbability = rowSumOfPostProbability;
	}

	//**************************************************************************其他函数***********
	public void setOneWeightedProbability(int oneGaussianNum,int oneSampleID,double oneWeightedProbability) {
		weightedProbability[oneGaussianNum][oneSampleID] = oneWeightedProbability;
	}

	public void setColumnSumOfweightedProbability() {
		for (int oneGaussianID = 0; oneGaussianID < Config.getCountOfGaussian(); oneGaussianID++) {
			for (int sampleID = 0; sampleID < Samples.getCount(); sampleID++) {
				//第i个单高斯模型，第j个样本
				double cell=getWeightedProbability(oneGaussianID,sampleID);
				addToColumnSumOfWeightedProbability(sampleID,cell);
			}
		}
	}

	private void addToColumnSumOfWeightedProbability(int sampleID, double cell) {
		everyColumnSumOfweightedProbability[sampleID]+=cell;
	}

	private double getWeightedProbability(int oneGaussianID, int sampleID) {
		return weightedProbability[oneGaussianID][sampleID];
	}

	/**
	 * 对每一个单元格计算后验概率
	 */
	public void setAllCellOfPostProbability() {
		for (int oneGaussianID = 0; oneGaussianID < Config.getCountOfGaussian(); oneGaussianID++) {
			for (int sampleID = 0; sampleID < Samples.getCount(); sampleID++) {
				//第i个单高斯模型，第j个样本
				double postProbability=getWeightedProbability(oneGaussianID, sampleID)/getEveryColumnSumOfweightedProbability(sampleID);
				setCellOfPostProbability(oneGaussianID,sampleID,postProbability);
			}
		}
	}

	private void setCellOfPostProbability(int oneGaussianID, int sampleID, double postProbability2) {
		postProbability[oneGaussianID][sampleID]=postProbability2;
	}

	private double getEveryColumnSumOfweightedProbability(int j) {
		return everyColumnSumOfweightedProbability[j];
	}

	public void setRowSumOfPostProbability() {
		for (int oneGaussianID = 0; oneGaussianID < Config.getCountOfGaussian(); oneGaussianID++) {
			for (int sampleID = 0; sampleID < Samples.getCount(); sampleID++) {
				double postProbability=getCellOfPostProbability(oneGaussianID,sampleID);
				addToRowSumOfPostProbability(oneGaussianID,postProbability);
			}			
		}
	}

	private void addToRowSumOfPostProbability(int i, double postProbability2) {
		rowSumOfPostProbability[i]+=postProbability2;
	}

	private double getCellOfPostProbability(int i, int j) {
		
		return postProbability[i][j];
	}

	public double getPostProbability(int oneGaussianID, int oneSampleID) {
		return postProbability[oneGaussianID][oneSampleID];
	}

	public double getRowSumOfPostProbability(int oneGaussianID) {
		return rowSumOfPostProbability[oneGaussianID];
	}
}
