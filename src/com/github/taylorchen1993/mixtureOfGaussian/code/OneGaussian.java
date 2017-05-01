package com.github.taylorchen1993.mixtureOfGaussian.code;

import com.github.taylorchen1993.mixtureOfGaussian.Config;

import Jama.Matrix;

/**
 * 
 */

/**
 * @author TaylorChen
 *
 */
/**
 * @author TaylorChen
 *
 */
public class OneGaussian {
	/**
	 * 在混合高斯模型里面的权重
	 */
	private Double weight ;
	/**
	 * 均值，2*1的矩阵，用于(x-mean)
	 */
	private Matrix mean=new Matrix(Config.getDimension(),1);
	/**
	 * 协方差矩阵，2*2的矩阵
	 */
	private Matrix covarianceMatrix=new Matrix(Config.getDimension(), Config.getDimension());


	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Matrix getMean() {
		return mean;
	}

	public void setMean(Matrix mean) {
		this.mean = mean;
	}

	public Matrix getCovarianceMatrix() {
		return covarianceMatrix;
	}

	public void setCovarianceMatrix(Matrix covarianceMatrix) {
		this.covarianceMatrix = covarianceMatrix;
	}
	

	public OneGaussian() {
		super();
	}

	public OneGaussian(Double weight, Matrix mean, Matrix covariance) {
		super();
		this.weight = weight;
		this.mean = mean;
		this.covarianceMatrix = covariance;
	}

	//*******************************其他函数*********************************************************
	/**
	 * 一个点在本高斯模型中的概率*本高斯模型的权重
	 * @param point n*1矩阵表示一个点
	 */
	public double getWeightedProbability(Matrix point) {
		return weight*getProbability(point);
	}
	
	/**
	 * 一个点在本高斯模型中的概率
	 * @param point n*1矩阵表示一个点
	 */
	private double getProbability(Matrix point) {
		//分母中的系数
		double down=Math.pow((2*Math.PI),Config.getDimension()/2.0)
				*Math.pow(covarianceMatrix.det(), 0.5);
		Matrix pointMinusMean=point.minus(mean);
		//[].()运算符级别最高
		double index=-0.5*pointMinusMean.transpose().times(this.getCovarianceMatrix().inverse()).times(pointMinusMean).det();
		return Math.exp(index)/down;
	}


	public OneGaussian getImprovedOneGaussian(int oneGaussianID,AllGaussian_AllSample allGaussian_AllSampleTable) {
		Matrix improvedMean=getImprovedMean(oneGaussianID, allGaussian_AllSampleTable);
		Matrix improvedCovariance=getImprovedCovarianceMatrix(oneGaussianID,improvedMean, allGaussian_AllSampleTable);
		double improvedWeight=getImprovedWeight(oneGaussianID,allGaussian_AllSampleTable);
		OneGaussian improvedOneGaussian=new OneGaussian(improvedWeight, improvedMean, improvedCovariance);
		return improvedOneGaussian;
	}



	private double getImprovedWeight(int oneGaussianID, AllGaussian_AllSample allGaussian_AllSampleTable) {
		double improvedWeight=allGaussian_AllSampleTable.getRowSumOfPostProbability(oneGaussianID)/Samples.getCount();
		return improvedWeight;
	}


	private Matrix getImprovedCovarianceMatrix(int oneGaussianID, Matrix improvedMean,AllGaussian_AllSample allGaussian_AllSampleTable) {
		Matrix upperSum=new Matrix(Config.getDimension(),Config.getDimension());
		for (int oneSampleID = 0; oneSampleID < Samples.getCount(); oneSampleID++) {
			//下一行程序错误，应该减去improvedMean，而不是现在的mean
			//Matrix sampleMinusMean=Samples.getNthSample(oneSampleID).minus(mean);
			Matrix sampleMinusImprovedMean=Samples.getNthSample(oneSampleID).minus(improvedMean);
			Matrix upperOne=sampleMinusImprovedMean.times(sampleMinusImprovedMean.transpose()).times(allGaussian_AllSampleTable.getPostProbability(oneGaussianID, oneSampleID));
			upperSum.plusEquals(upperOne);
		}
		return upperSum.times(1.0/allGaussian_AllSampleTable.getRowSumOfPostProbability(oneGaussianID));
	}


	private Matrix getImprovedMean(int oneGaussianID,AllGaussian_AllSample allGaussian_AllSampleTable) {
		Matrix upperSum=new Matrix(Config.getDimension(), 1);
		for (int sampleID = 0; sampleID < Samples.getCount(); sampleID++) {
			Matrix one=Samples.getNthSample(sampleID).times(allGaussian_AllSampleTable.getPostProbability(oneGaussianID,sampleID));
			upperSum.plusEquals(one);			
		}
		return upperSum.times(1.0/allGaussian_AllSampleTable.getRowSumOfPostProbability(oneGaussianID));
	}




	/**
	 * 输出结果
	 */
	public void printResult() {
		System.out.println("其中一个单高斯模型的参数如下");
		System.out.println("权重："+weight.toString());
		System.out.println("均值:");
		System.out.println(Double.valueOf(mean.get(0, 0)).toString());
		System.out.println(Double.valueOf(mean.get(1, 0)).toString());
		//方差
		System.out.println("协方差矩阵:");
		System.out.println(Double.valueOf(covarianceMatrix.get(0, 0)).toString()+"\t"+Double.valueOf(covarianceMatrix.get(0, 1)).toString());
		System.out.println(Double.valueOf(covarianceMatrix.get(1, 0)).toString()+"\t"+Double.valueOf(covarianceMatrix.get(1, 1)).toString());
		System.out.println("");
	}

	public void setEverySampleWeightedProbabilityInTable(int oneGaussianID,
			AllGaussian_AllSample allGaussian_AllSampleTable) {
		for (int sampleID = 0; sampleID < Samples.getCount(); sampleID++) {
			Matrix sample=Samples.getNthSample(sampleID);
 			double weightedProbability=getWeightedProbability(sample);
			allGaussian_AllSampleTable.setOneWeightedProbability(oneGaussianID, sampleID, weightedProbability);
			
		}
		
	}


}
