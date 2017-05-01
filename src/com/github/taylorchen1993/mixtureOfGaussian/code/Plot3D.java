/**
 * 
 */
package com.github.taylorchen1993.mixtureOfGaussian.code;

import java.awt.Rectangle;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.controllers.keyboard.camera.CameraKeyController;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.global.Settings;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import Jama.Matrix;

/**
 * @author TaylorChen
 *
 */
public class Plot3D {
	/**
	 * 高斯混合模型函数
	 */
	private Mapper mapper=new Mapper() {
		
		@Override
		public double f(double arg0, double arg1) {
			
			return 0;
		}
	};
	/////////////////////////////////////////////////////////////////////////样本点//////////////
	/**
	 * 所有样本点
	 */
	private Scatter scatter=new Scatter();

	/**
	 * 样本点的颜色
	 */
	private float scatterColor= 0.25f;
	
	/**
	 * 样本点的宽度
	 */
	private float scatterWidth = 5f;
	
	//////////////////////////////////////////////////////////////////////////图形////////////
	
	/**
	 * 显示出的图形
	 */
	private Chart chart= new Chart(Quality.Advanced, "awt");
	
	/**
	 * 图形的范围
	 */
	private Range chartRange = new Range(0, 200);
	
	/**
	 * 显示窗口的大小
	 */
	private Rectangle DEFAULT_WINDOW = new Rectangle(0,0,600,600);
	
	/**
	 * 图形的连续性
	 */
	private int chartSteps=1;
	
	//////////////////////////////////////////////////////////////////////////////////////

	public Mapper getMapper() {
		return mapper;
	}

	public float getScatterColor() {
		return scatterColor;
	}

	public void setScatterColor(float scatterColor) {
		this.scatterColor = scatterColor;
	}

	public float getScatterWidth() {
		return scatterWidth;
	}

	public void setScatterWidth(float scatterWidth) {
		this.scatterWidth = scatterWidth;
	}

	public Range getChartRange() {
		return chartRange;
	}

	public void setChartRange(Range chartRange) {
		this.chartRange = chartRange;
	}

	public Rectangle getDEFAULT_WINDOW() {
		return DEFAULT_WINDOW;
	}

	public void setDEFAULT_WINDOW(Rectangle dEFAULT_WINDOW) {
		DEFAULT_WINDOW = dEFAULT_WINDOW;
	}

	public int getChartSteps() {
		return chartSteps;
	}

	public void setChartSteps(int chartSteps) {
		this.chartSteps = chartSteps;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public Scatter getScatter() {
		return scatter;
	}

	public void setScatter(Scatter scatter) {
		this.scatter = scatter;
	}

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public Plot3D(Mapper mapper, Scatter scatter, Chart chart) {
		super();
		this.mapper = mapper;
		this.scatter = scatter;
		this.chart = chart;
	}

	// ***************************根据mapper构造函数********************************************************
	/**
	 *  根据mapper生成plot3D对象
	 * @param mapper
	 */
	public Plot3D(Mapper mapper) {
		this.mapper=mapper;
		setScatter();
		setChart();
	}
	
	
	
	private void setChart() {
		//添加所有样本点
		chart.getScene().add(scatter);
		//添加函数图像
		Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(chartRange, chartSteps, chartRange, chartSteps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        chart.getScene().getGraph().add(surface);
        chart.addController(new CameraKeyController());
	}

	private void setScatter() {
		float x;
		float y;
		float z;

		Coord3d[] points = new Coord3d[Samples.getCount()];
		Color[] colors = new Color[Samples.getCount()];
		//遍历所有样本
		for (int i = 0; i < Samples.getCount(); i++) {
			Matrix iterSample=Samples.getNthSample(i);
			x = ((Double)iterSample.get(i, 0)).floatValue();
			y = ((Double)iterSample.get(i, 1)).floatValue();
			z = ((Double)mapper.f(x, y)).floatValue();
			
			points[i] = new Coord3d(x, y, z);

			colors[i] = new Color(x, y, z, scatterColor);
		}

		scatter = new Scatter(points, colors, scatterWidth);

	}
//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 显示图像
	 */
	public void show() {
		Settings.getInstance().setHardwareAccelerated(true);
		ChartLauncher.instructions();
		ChartLauncher.openChart(chart, getDEFAULT_WINDOW(), "");
	}
}
