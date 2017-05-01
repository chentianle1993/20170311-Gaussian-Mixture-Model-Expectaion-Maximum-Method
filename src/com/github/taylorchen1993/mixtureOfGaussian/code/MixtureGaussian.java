/**
 * 
 */
package com.github.taylorchen1993.mixtureOfGaussian.code;

import java.util.Iterator;
import java.util.LinkedList;

import org.jzy3d.plot3d.builder.Mapper;

import com.github.taylorchen1993.mixtureOfGaussian.Config;

import Jama.Matrix;

/**
 * @author TaylorChen
 *
 */
public class MixtureGaussian {
	/**
	 * 混合高斯模型
	 */
	private LinkedList<OneGaussian> mixtureGaussian = new LinkedList<>();

	/**
	 * 图像
	 */
	private Plot3D plot3d;

	private AllGaussian_AllSample allGaussian_AllSampleTable=new AllGaussian_AllSample();


	public LinkedList<OneGaussian> getMixtureGaussian() {
		return mixtureGaussian;
	}

	public void setMixtureGaussian(LinkedList<OneGaussian> mixtureGaussian) {
		this.mixtureGaussian = mixtureGaussian;
	}

	public Plot3D getPlot3d() {
		return plot3d;
	}

	public void setPlot3d(Plot3D plot3d) {
		this.plot3d = plot3d;
	}

	public MixtureGaussian() {
		super();
	}

	public MixtureGaussian(LinkedList<OneGaussian> mixtureGaussian, Plot3D plot3d) {
		super();
		this.mixtureGaussian = mixtureGaussian;
		this.plot3d = plot3d;
	}

	public Iterator<OneGaussian> iterator() {
		return mixtureGaussian.iterator();
	}

	public AllGaussian_AllSample getAllGaussian_AllSampleTable() {
		return allGaussian_AllSampleTable;
	}


	// ****************************其他函数*************************************************************
	/**
	 * 遍历SomeSamplesNum,生成第一个混合高斯模型
	 */
	public static MixtureGaussian init() {
		MixtureGaussian initModel = new MixtureGaussian();
		//  初始化时的单高斯模型的均值在样本中随机选取
		SomeSamplesNum.init();
		double weight = 1.0 / Config.getCountOfGaussian();
		Matrix mean;
		Matrix covarianceMatrix = Samples.getCovarianceMatrix();
		OneGaussian tempOneGaussian=new OneGaussian();
		for (Iterator<Integer> iterator = SomeSamplesNum.iterator(); iterator.hasNext();) {
			Integer oneSampleNum = (Integer) iterator.next();
			mean = Samples.getNthSample(oneSampleNum);
			tempOneGaussian = new OneGaussian(weight, mean, covarianceMatrix);
			initModel.add(tempOneGaussian);
		}
		return initModel;
	}

	/**
	 * 添加单高斯模型模型
	 * @param tempOneGaussian
	 */
	private void add(OneGaussian tempOneGaussian) {
		mixtureGaussian.add(tempOneGaussian);
	}
	/**
	 * 在图形中显示高斯模型
	 */
	public void show() {
		setPlot3d();
		plot3d.show();	
	}
	/**
	 * 根据高斯混合模型，new Plot3D
	 */
	private void setPlot3d() {
		Mapper mapper=new Mapper() {
			@Override
			public double f(double x, double y) {
				Matrix point=new Matrix(Config.getDimension(), 1);
				point.set(0, 0, x);
				point.set(1, 0, y);
				return getProbability(point);
			}
		};
		//为何出错？？？
		plot3d= new Plot3D(mapper);
	}
	
	/**
	 * 获得某个点在高斯混合模型中的概率
	 * @param point 输入的点
	 * @return point在高斯混合模型中的概率
	 */
	public double getProbability(Matrix point) {
		double probability=0.0;
		for (OneGaussian oneGaussian : mixtureGaussian) {
			probability+=oneGaussian.getWeightedProbability(point);
		}
		return probability;
	}
	public MixtureGaussian getImprovedObj() {
		// 生成改进后的混合高斯模型
		MixtureGaussian improvedMixtureGaussian=new MixtureGaussian();		
		
		
		//此处参见周志华机器学习P210左边笔记
		
		//开始计算这个混合高斯模型中所有单高斯模型与所有样本之间构成的矩阵
		int oneGaussianID=0;//第i个单高斯模型
		for (Iterator<OneGaussian> iterator = mixtureGaussian.iterator(); iterator.hasNext();oneGaussianID++) {
			OneGaussian oneGaussian = (OneGaussian) iterator.next();
			//对每一个单元格赋值
			oneGaussian.setEverySampleWeightedProbabilityInTable(oneGaussianID,this.allGaussian_AllSampleTable);
		}
		//对每一列求和
		this.allGaussian_AllSampleTable.setColumnSumOfweightedProbability();
		//计算每一个单元格的后验概率
		this.allGaussian_AllSampleTable.setAllCellOfPostProbability();
		//计算每一行后验概率的和
		this.allGaussian_AllSampleTable.setRowSumOfPostProbability();
		//迭代改进
		oneGaussianID=0;
		//mixtureGaussian.iterator()不能保证顺序遍历
		for (Iterator<OneGaussian> iterator = mixtureGaussian.iterator(); iterator.hasNext();oneGaussianID++) {
			OneGaussian oneGaussian2 = (OneGaussian) iterator.next();
			OneGaussian improvedOneGaussian=oneGaussian2.getImprovedOneGaussian(oneGaussianID,this.allGaussian_AllSampleTable);
			improvedMixtureGaussian.addOneGaussian(improvedOneGaussian);
		}
		
		return improvedMixtureGaussian;

	}

	private void addOneGaussian(OneGaussian improvedOneGaussian) {
		mixtureGaussian.add(improvedOneGaussian);
	}

	/**
	 * 输出结果
	 */
	public void printResult() {
		System.out.print("\n输出:");
		OneGaussian oneGaussian=new OneGaussian();
		for (Iterator<OneGaussian> iterator = mixtureGaussian.iterator(); iterator.hasNext();) {
			oneGaussian = (OneGaussian) iterator.next();
			oneGaussian.printResult();
		}
	}


}
