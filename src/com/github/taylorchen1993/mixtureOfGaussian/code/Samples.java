package com.github.taylorchen1993.mixtureOfGaussian.code;

import com.github.taylorchen1993.mixtureOfGaussian.Config;

/**
 * 
 */
import Jama.Matrix;

/**
 * @author TaylorChen
 *
 */
/**
 * @author TaylorChen
 *
 */
public class Samples{
	// ********************************域及所有自动生成的getter/setter**********************************************************
	/**
	 * 所有样本构成的矩阵(x1,x2,...,xn)
	 */
	private static Matrix[] samples = new Matrix[Config.getCountOfSamples()];
	/**
	 * 样本个数
	 */
	private static int count = 0;

	/**
	 * 和
	 */
	private static Matrix sum = new Matrix(Config.getDimension(), 1);

	/**
	 * 均值
	 */
	private static Matrix mean = new Matrix(Config.getDimension(), 1);

	/**
	 * 平方和
	 */
	private static Matrix quadraticSum = new Matrix(Config.getDimension(), 1);
	/**
	 * 方差
	 */
	private static Matrix variance = new Matrix(Config.getDimension(), 1);
	
	/**
	 * E(XY)
	 */
	private static double xyAvg=0.0;
	/**
	 * 协方差
	 */
	private static double covariance = 0.0;
	
	/**
	 * 协方差矩阵
	 */
	private static Matrix covarianceMatrix=new Matrix(Config.getDimension(),Config.getDimension());
	
	/**
	 * 某个样本在所有单高斯模型中的概率之和
	 */
	private static double[] OneSampleWeightedProbabilitySumInEveryGaussian=new double[Config.getCountOfSamples()];

	public static Matrix[] getSamples() {
		return samples;
	}

	public static void setSamples(Matrix[] samples) {
		Samples.samples = samples;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Samples.count = count;
	}

	public static Matrix getSum() {
		return sum;
	}

	public static void setSum(Matrix sum) {
		Samples.sum = sum;
	}

	public static Matrix getMean() {
		return mean;
	}

	public static void setMean(Matrix mean) {
		Samples.mean = mean;
	}

	public static Matrix getQuadraticSum() {
		return quadraticSum;
	}

	public static void setQuadraticSum(Matrix quadraticSum) {
		Samples.quadraticSum = quadraticSum;
	}

	public static Matrix getVariance() {
		return variance;
	}

	public static void setVariance(Matrix variance) {
		Samples.variance = variance;
	}

	public static double getCovariance() {
		return covariance;
	}

	public static void setCovariance(double covariance) {
		Samples.covariance = covariance;
	}

	public static Matrix getCovarianceMatrix() {
		return covarianceMatrix;
	}

	public static void setCovarianceMatrix(Matrix covarianceMatrix) {
		Samples.covarianceMatrix = covarianceMatrix;
	}

	public static double getXyAvg() {
		return xyAvg;
	}

	public static void setXyAvg(double xyAvg) {
		Samples.xyAvg = xyAvg;
	}

	public static double[] getOneSampleWeightedProbabilitySumInEveryGaussian() {
		return OneSampleWeightedProbabilitySumInEveryGaussian;
	}

	public static void setOneSampleWeightedProbabilitySumInEveryGaussian(
			double[] oneSampleWeightedProbabilitySumInEveryGaussian) {
		OneSampleWeightedProbabilitySumInEveryGaussian = oneSampleWeightedProbabilitySumInEveryGaussian;
	}

	// ********************************初始化函数及其他***********************************************************
	/**
	 * 主函数
	 */
	public static void init() {
		setSamples();
		setCount();
		setSum();
		setQuadraticSum();
		setMean();
		setVariance();
		setXyAvg();
		setCovariance();
		setCovarianceMatrix();
	}



	private static void setCovarianceMatrix() {
		covarianceMatrix.set(0, 0, getVariance().get(0, 0));
		covarianceMatrix.set(0, 1, covariance);
		covarianceMatrix.set(1, 0, covariance);
		covarianceMatrix.set(1, 1, getVariance().get(1, 0));
	}

	private static void setCovariance() {
		double xAvg=getMean().get(0, 0);
		double yAvg=getMean().get(1, 0);
		covariance=xyAvg-xAvg*yAvg;
	}

	private static void setXyAvg() {
		double xySum=0.0;
		for (int i = 0; i < samples.length; i++) {
			Matrix sample = samples[i];
			xySum+=sample.get(0, 0)*sample.get(1, 0);					
		}
		xyAvg=xySum/getCount();
	}

	private static void setVariance() {
		Matrix meanOfQuadraticSum = quadraticSum.times(1.0 / Config.getCountOfSamples());
		Matrix tempVariance = meanOfQuadraticSum.minus(mean.arrayTimes(mean));
		setVariance(tempVariance);
	}

	private static void setMean() {
		setMean(getSum().times(1.0/count));
	}

	private static void setQuadraticSum() {
		Matrix sum = new Matrix(Config.getDimension(), 1);
		Matrix one = new Matrix(Config.getDimension(), 1);
		for (int i = 0; i < samples.length; i++) {
			one=samples[i].arrayTimes(samples[i]);
			sum.plusEquals(one);
		}
		setQuadraticSum(sum);

	}

	private static void setSum() {
		Matrix tempSum = new Matrix(Config.getDimension(), 1);
		for (int i = 0; i < Config.getCountOfSamples(); i++) {
			tempSum.plusEquals(getNthSample(i));
		}
		setSum(tempSum);
	}

	private static void setCount() {
		setCount(Config.getCountOfSamples());
	}

	private static void setSamples() {
		for (int i = 0; i < Config.getCountOfSamples(); i++) {
			double[] iterSample = Config.getNthSample(i);
			Matrix temp=new Matrix(iterSample, Config.getDimension());
			samples[i]=temp;
		}
	}
//****************************其他函数*****************************************************************
	public static Matrix getNthSample(int i){
		return samples[i];
	}
	public static void set0ToOneSampleWeightedProbabilitySumInEveryGaussian(
			int sampleID) {
		OneSampleWeightedProbabilitySumInEveryGaussian[sampleID]=0;
	}
	public static void addToOneSampleWeightedProbabilitySumInEveryGaussian(
			int sampleID,
			double oneSampleWeightedProbabilityInThisOneGaussian) {
		OneSampleWeightedProbabilitySumInEveryGaussian[sampleID]+= oneSampleWeightedProbabilityInThisOneGaussian;
	}
	public static double getOneSampleWeightedProbabilitySumInEveryGaussian(int sampleID) {
		return OneSampleWeightedProbabilitySumInEveryGaussian[sampleID];
	}
}
