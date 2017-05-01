/**
 * 
 */
package com.github.taylorchen1993.mixtureOfGaussian.code;

import java.util.HashSet;
import java.util.Iterator;

import com.github.taylorchen1993.mixtureOfGaussian.Config;

/**
 * @author TaylorChen
 *
 */
/**
 * 此处用继承要好一些？？？？？？
 * @author TaylorChen
 * 
 */
public class SomeSamplesNum{
	/**
	 * 用于初始化混合高斯模型时，设置每个单高斯模型的均值时所用的样本标号
	 */
	private static HashSet<Integer> someSamplesNum = new HashSet<>();

	public static HashSet<Integer> getSomeSampleNum() {
		return someSamplesNum;
	}

	public static void setSomeSampleNum(HashSet<Integer> someSampleNum) {
		SomeSamplesNum.someSamplesNum = someSampleNum;
	}

	public static Iterator<Integer> iterator() {
		return someSamplesNum.iterator();
	}

	// *********************************init********************************************************
	/**
	 * 获得Samples的一部分抽样
	 */
	public static void init() {
		Integer randomSampleNum=0;
		while (someSamplesNum.size()<Config.getCountOfGaussian()) {
			Double temp=Math.random()*Samples.getCount();
			randomSampleNum=temp.intValue();
			someSamplesNum.add(randomSampleNum);
		}
	}

}
