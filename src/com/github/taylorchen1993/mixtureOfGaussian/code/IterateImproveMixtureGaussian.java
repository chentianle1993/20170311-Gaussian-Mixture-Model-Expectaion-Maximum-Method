package com.github.taylorchen1993.mixtureOfGaussian.code;

import java.util.LinkedList;

import com.github.taylorchen1993.mixtureOfGaussian.Config;

public class IterateImproveMixtureGaussian {
	//******************************成员变量及getter/setter***********************************************************	
	/**
	 * 迭代改进过程中，所有的高斯模型
	 */
	private static LinkedList<MixtureGaussian> allMixtureGaussians = new LinkedList<MixtureGaussian>();
	
	
	


	public static LinkedList<MixtureGaussian> getAllMixtureGaussians() {
		return allMixtureGaussians;
	}


	public static void setAllMixtureGaussians(LinkedList<MixtureGaussian> allMixtureGaussians) {
		IterateImproveMixtureGaussian.allMixtureGaussians = allMixtureGaussians;
	}

	//******************************其他函数***********************************************************
	public static void init(){
		//第零个混合高斯模型
		MixtureGaussian iterMixtureGaussian = MixtureGaussian.init();
		//TODO 显示模型
		//iterMixtureGaussian.show();
		allMixtureGaussians.add(iterMixtureGaussian);
		for (int i = 0; i < Config.getMaxIterateTime(); i++) {
			iterMixtureGaussian=iterMixtureGaussian.getImprovedObj();
			//TODO 显示模型
			//iterMixtureGaussian.show();
			allMixtureGaussians.add(iterMixtureGaussian);
		}
	}


	/**
	 * 输出结果
	 */
	public static void printResult() {
		MixtureGaussian resultMixtrueGaussians=allMixtureGaussians.getLast();
		resultMixtrueGaussians.printResult();
		
	}
}
