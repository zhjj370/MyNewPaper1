package org.zhjj370.functions;

import com.orsonpdf.PDFDocument;
import com.orsonpdf.PDFGraphics2D;
import com.orsonpdf.Page;
import org.zhjj370.container.element.DataForBusyness;
import org.zhjj370.container.element.DataForOverdue;
import org.zhjj370.functions.element.DataForaBar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DataPlotting {

    private List<Double> unitEnergyList;

    //构造函数
    public DataPlotting(){

    }

//------------------Create charts------------------------//
    /**
     * Creates a line chart.
     *
     * @param dataset  a dataset for the line chart.
     *
     * @return An energy chart.
     */
    private JFreeChart createLineChart(XYDataset dataset,int time, String title, String xName,String yName) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, xName, yName, dataset);
        chart.setTitle(new TextTitle(title,new Font("Dialog",Font.BOLD,13)));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setLowerMargin(0.2);
        xAxis.setUpperMargin(0.2);
        xAxis.setRange(0,time+100) ;
        xAxis.setLabelFont(new Font("Dialog", Font.BOLD, 10));
        xAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 8));
        plot.setDomainAxis(xAxis);
        //xAxis.setStandardTickUnits(createStandardDateTickUnits());

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setLowerMargin(0.2);
        yAxis.setUpperMargin(0.2);
        yAxis.setLabelFont(new Font("Dialog", Font.BOLD, 10));
        yAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 8));
        plot.setRangeAxis(yAxis);

        return chart;
    }

    /**
     * Creates a bar chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createBarChart(CategoryDataset dataset,String title, String xName,String yName) {
        JFreeChart chart = ChartFactory.createBarChart(
                title, xName /* x-axis label*/,
                yName /* y-axis label */, dataset);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();



        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());



        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelPaint(Color.yellow);
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.INSIDE6, TextAnchor.BOTTOM_CENTER));

        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }

//------------------Create dataset------------------------//
    /**
     * Creates a line dataset.
     *
     * @return The dataset.
     */
    private XYDataset createUnitDatasetForLine(List<Double> unitData) {
        XYSeries series1 = new XYSeries("Unit Data");
        for(int i = 0;i< unitData.size();i++){
            series1.add(i,unitData.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);

        return dataset;
    }

    /**
     * Creates several lines dataset for load
     *
     * @return The dataset.
     */
    private XYDataset createUnitDatasetForLines(List<DataForBusyness> dataForBusyness) {
        /*XYSeries series1 = new XYSeries("Unit Data");
        for(int i = 0;i< unitData.size();i++){
            series1.add(i,unitData.get(i));
        }*/
        XYSeriesCollection dataset = new XYSeriesCollection();
        //dataset.addSeries(series1);
        Iterator<DataForBusyness> iterDataForBusyness = dataForBusyness.iterator();
        while (iterDataForBusyness.hasNext()){
            DataForBusyness data0 = iterDataForBusyness.next();
            XYSeries series0 = new XYSeries(data0.getMachineID());
            for(int i = 0;i< data0.getDataList().size();i++){
                series0.add(i,data0.getDataList().get(i));
            }
            dataset.addSeries(series0);
        }
        return dataset;
    }

    /**
     * Creates several lines dataset for the using of workstations
     * @param dataForBusyness
     * @return
     */
    private XYDataset createUnitDatasetForLines1(List<DataForBusyness> dataForBusyness) {
        /*XYSeries series1 = new XYSeries("Unit Data");
        for(int i = 0;i< unitData.size();i++){
            series1.add(i,unitData.get(i));
        }*/
        XYSeriesCollection dataset = new XYSeriesCollection();
        //dataset.addSeries(series1);
        Iterator<DataForBusyness> iterDataForBusyness = dataForBusyness.iterator();
        while (iterDataForBusyness.hasNext()){
            DataForBusyness data0 = iterDataForBusyness.next();
            XYSeries series0 = new XYSeries(data0.getMachineID());
            for(int i = 0;i< data0.getStationUsage().size();i++){
                series0.add(i,data0.getStationUsage().get(i));
            }
            dataset.addSeries(series0);
        }
        return dataset;
    }

    /**
     * Creates several lines dataset for the busyness
     * @param dataForBusyness
     * @return
     */
    private XYDataset createUnitDatasetForLines2(List<DataForBusyness> dataForBusyness) {
        /*XYSeries series1 = new XYSeries("Unit Data");
        for(int i = 0;i< unitData.size();i++){
            series1.add(i,unitData.get(i));
        }*/
        XYSeriesCollection dataset = new XYSeriesCollection();
        //dataset.addSeries(series1);
        Iterator<DataForBusyness> iterDataForBusyness = dataForBusyness.iterator();
        while (iterDataForBusyness.hasNext()){
            DataForBusyness data0 = iterDataForBusyness.next();
            XYSeries series0 = new XYSeries(data0.getMachineID());
            for(int i = 0;i< data0.getBusynessList().size();i++){
                series0.add(i,data0.getBusynessList().get(i));
            }
            dataset.addSeries(series0);
        }
        return dataset;
    }

    /**
     * Returns a bar dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createBarDataset(List<DataForaBar> dataList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Iterator<DataForaBar> iterDataList = dataList.iterator();
        while (iterDataList.hasNext()){
            DataForaBar data0 = iterDataList.next();
            dataset.addValue((double) data0.getValue(), data0.getRowKey(), data0.getColoumKey());
        }
        /*dataset.addValue(7445, "JFreeSVG", "Warm-up");
        dataset.addValue(24448, "Batik", "Warm-up");
        dataset.addValue(4297, "JFreeSVG", "Test");
        dataset.addValue(21022, "Batik", "Test");*/
        return dataset;
    }




//------------------plot chart------------------------//

    /**
     * 绘制实时耗能曲线
     * @param unitEnergyList
     * @param time
     * @throws IOException
     */
    public void plotUnitEnergy(List<Double> unitEnergyList,int time) throws IOException {
        this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLine(this.unitEnergyList),time,
                "Line chart for energy consumption per unit time","Time","Energy consumption");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/Energy.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());

        //The following program is for my thesis which is used to produce pdf images for latex
        PDFDocument pdfDoc = new PDFDocument();
        pdfDoc.setTitle("Energy-chart");
        pdfDoc.setAuthor("zhjj370@nuaa.edu.cn");
        Page page = pdfDoc.createPage(new Rectangle(500, 300));
        PDFGraphics2D g2pdf = page.getGraphics2D();
        chart.draw(g2pdf, new Rectangle(0, 0, 500, 300));
        pdfDoc.writeToFile(new File("output/pdf/Energy.pdf"));

    }

    /**
     * 绘制交货期临近指数
     * @param unitApproachingIndexForDt
     * @param time
     * @throws IOException
     */
    public void plotApproachingIndexForDt(List<Double> unitApproachingIndexForDt,int time) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLine(unitApproachingIndexForDt),time,
                "Line chart for NDI","time","index");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/NDI.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());

        //The following program is for my thesis which is used to produce pdf images for latex
        PDFDocument pdfDoc = new PDFDocument();
        pdfDoc.setTitle("NDI-chart");
        pdfDoc.setAuthor("zhjj370@nuaa.edu.cn");
        Page page = pdfDoc.createPage(new Rectangle(500, 300));
        PDFGraphics2D g2pdf = page.getGraphics2D();
        chart.draw(g2pdf, new Rectangle(0, 0, 500, 300));
        pdfDoc.writeToFile(new File("output/pdf/NDI.pdf"));
    }

    public void plotApproachingIndexForDtXX(List<Double> unitApproachingIndexForDt,int time) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLine(unitApproachingIndexForDt),time,
                "Line chart for Coefficient_energy","time","index");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/Difference from ideal value.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());
    }

    /**
     * 绘制每台机器的负载变化
     * @param dataForBusyness
     * @param time
     * @throws IOException
     */
    public void plotSingleMachineLoadChange(List<DataForBusyness> dataForBusyness,int time) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLines(dataForBusyness),time,
                "Line chart for Load Change","time","Load");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/SingleMachineLoadChange.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());
    }

    /**
     * 绘制每台机器的繁忙度变化
     * @param dataForBusyness
     * @param time
     * @throws IOException
     */
    public void plotSingleMachineBusynessChange(List<DataForBusyness> dataForBusyness,int time) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLines2(dataForBusyness),time,
                "Line chart for Busyness Change","time","Busyness");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/SingleMachineBusynessChange.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());
    }

    /**
     * 绘制机器中工位的使用情况
     * @param dataForBusyness
     * @param time
     * @throws IOException
     */
    public void plotSingleUseOfWorkstations(List<DataForBusyness> dataForBusyness,int time) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLines1(dataForBusyness),time,
                "Line chart for the using of stations","time","amount");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/The using of station.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());
    }

    /**
     * 绘制当前最大负载
     * @param dataForMaxBusynessList
     * @param time
     * @throws IOException
     */
    public void plotMaxMachineLoadChange(List<Double> dataForMaxBusynessList,int time) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        JFreeChart chart = createLineChart(createUnitDatasetForLine(dataForMaxBusynessList),time,
                "Line chart for Max Load Change","time","Load");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/MaxMachineLoadChange.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());
    }

    /**
     * 绘制超期情况
     * @param dataForOverdues
     * @throws IOException
     */
    public void plotOverdueArtifacts(List<DataForOverdue> dataForOverdues) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        List<DataForaBar> dataList = new ArrayList<>();
        Iterator<DataForOverdue> iterDataForOverdues = dataForOverdues.iterator();
        while(iterDataForOverdues.hasNext()){
            DataForOverdue dataForOverdue = iterDataForOverdues.next();
            int overTime = dataForOverdue.getOverTime();
            String partID = String.valueOf(dataForOverdue.getPartID());
            dataList.add(new DataForaBar(overTime,"",partID));
        }

        JFreeChart chart = createBarChart(createBarDataset(dataList),
                "Bar chart for overdue artifacts","Part ID","Time");

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("output/Bar chart for overdue artifacts.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());

    }






}
