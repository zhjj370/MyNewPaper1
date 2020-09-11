package org.zhjj370.functions.gantt;

import org.zhjj370.functions.element.DataForGantt;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class GanttPlotSvg {

    static class LabelGenerator extends AbstractCategoryItemLabelGenerator implements CategoryItemLabelGenerator
    {
        private Integer category;
        private NumberFormat formatter = NumberFormat.getPercentInstance();
        public LabelGenerator(int i)
        {
            this(new Integer(i));
        }
        public LabelGenerator(Integer integer)
        {
            super("", NumberFormat.getInstance());
            category = integer;
        }

        public String generateLabel(CategoryDataset categorydataset, int i,
                                    int i_0_)
        {
            String string = null;
            string = (String) categorydataset.getRowKey(i);
            return string;
        }
    }

    public GanttPlotSvg(List<DataForGantt> data, int time) throws IOException {
        IntervalCategoryDataset dataset = createDatasetLong(data);
        JFreeChart chart = createChart(dataset,time);
        SVGGraphics2D g2 = new SVGGraphics2D(1200, 700);
        Rectangle r = new Rectangle(0, 0, 1200, 700);
        chart.draw(g2, r);
        File f = new File("output/Gantt.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());
    }



    public static IntervalCategoryDataset createDatasetLong(List<DataForGantt> dataForGanttList) {
        List<TaskSeriesLong> dataSeries = new ArrayList<>();
        //遍历数组，按不同工件分组
        //System.out.println(dataForGanttList.size());
        Iterator iteratorData1 = dataForGanttList.iterator();
        int iCount = 0;
        while (iteratorData1.hasNext()){
            DataForGantt xData = (DataForGantt) iteratorData1.next();
            String partName = xData.getPartName();
            for(int iDataSeries=0;iDataSeries<dataSeries.size();iDataSeries++){
                //Part不是新的
                if (partName.equals(dataSeries.get(iDataSeries).getKey())){
                    dataSeries.get(iDataSeries).add(new TaskLong(xData.getMachineName(),
                            new SimpleTimePeriodLong(xData.getStartTme(),xData.getEndTime())));
                    break;
                }
                //到最后都没找到Part
                if(iDataSeries==dataSeries.size()-1){
                    dataSeries.add(new TaskSeriesLong(xData.getPartName()));
                    dataSeries.get(iDataSeries).add(new TaskLong(xData.getMachineName(),
                            new SimpleTimePeriodLong(xData.getStartTme(),xData.getEndTime())));
                }
            }
            //首先将第一个加入进去
            if(iCount == 0){
                dataSeries.add(new TaskSeriesLong(xData.getPartName()));
                dataSeries.get(0).add(new TaskLong(xData.getMachineName(),
                        new SimpleTimePeriodLong(xData.getStartTme(),xData.getEndTime())));
            }
            iCount++;
        }
        //System.out.println(dataSeries.size());
        TaskSeriesLongCollection v = new TaskSeriesLongCollection();
        Iterator iteratorData2 = dataSeries.iterator();
        while (iteratorData2.hasNext()){
            v.add((TaskSeriesLong) iteratorData2.next());
        }
        //System.out.println(v.getRowCount());
        return v;
    }

    private static Date date(int var0, int var1, int var2) {
        Calendar var3 = Calendar.getInstance();
        var3.set(var2, var1, var0);
        Date var4 = var3.getTime();
        return var4;
    }

    private static JFreeChart createChart(IntervalCategoryDataset var0, int time) {
        JFreeChart chart = createGanttChart1("Gantt chart for processing", "Machine", "Time", var0, true, true, false);
        //标题设置
        chart.setTitle(new TextTitle("Gantt chart for processing",new Font("Dialog",Font.BOLD,28)));
        chart.getLegend().setItemFont(new Font("Dialog",Font.BOLD,20));

        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        plot.setRangePannable(true);
        plot.setRangeCrosshairVisible(true);
        //plot.set

        ValueAxis xAxis = plot.getRangeAxis() ;  //x轴
        xAxis.setRange(0,time+100) ;
        xAxis.setLabelFont(new Font("Dialog", Font.BOLD, 20));
        xAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 18));
        plot.setRangeAxis(xAxis);

        CategoryAxis yAxis = plot.getDomainAxis(); //y轴
        yAxis.setMaximumCategoryLabelWidthRatio(10.0F);
        yAxis.setLabelFont(new Font("Dialog", Font.BOLD, 20));
        yAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 18));
        plot.setDomainAxis(yAxis);

        GanttRenderer1 renderer = (GanttRenderer1)plot.getRenderer();
        renderer.setDrawBarOutline(false);
        //plot.setLabelFont(new Font("Dialog", Font.PLAIN, 18));
        renderer.setDefaultItemLabelGenerator(new GanttPlot.LabelGenerator(
                (Integer) null));
        renderer.setDefaultItemLabelFont(new Font("Dialog", Font.PLAIN, 20));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelPaint(Color.BLACK);
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.INSIDE6, TextAnchor.BOTTOM_CENTER));


        return chart;
    }

    /**
     * Creates a Gantt chart using the supplied attributes plus default values
     * where required.  The chart object returned by this method uses a
     * {@link CategoryPlot} instance as the plot, with a {@link CategoryAxis}
     * for the domain axis, a {@link DateAxis} as the range axis, and a
     * {@link GanttRenderer} as the renderer.
     *
     * @param title  the chart title ({@code null} permitted).
     * @param categoryAxisLabel  the label for the category axis
     *                           ({@code null} permitted).
     * @param dateAxisLabel  the label for the date axis
     *                       ({@code null} permitted).
     * @param dataset  the dataset for the chart ({@code null} permitted).
     * @param legend  a flag specifying whether or not a legend is required.
     * @param tooltips  configure chart to generate tool tips?
     * @param urls  configure chart to generate URLs?
     *
     * @return A Gantt chart.
     */
    public static JFreeChart createGanttChart1(String title,
                                               String categoryAxisLabel, String dateAxisLabel,
                                               IntervalCategoryDataset dataset, boolean legend, boolean tooltips,
                                               boolean urls) {
        ChartTheme currentTheme = new StandardChartTheme("JFree");
        CategoryAxis categoryAxis = new CategoryAxis(categoryAxisLabel);
        NumberAxis dateAxis = new NumberAxis(dateAxisLabel);

        CategoryItemRenderer renderer = new GanttRenderer1();
        if (tooltips) {
            renderer.setDefaultToolTipGenerator(
                    new IntervalCategoryToolTipGenerator(
                            "{3} - {4}", DateFormat.getDateInstance()));
        }
        if (urls) {
            renderer.setDefaultItemURLGenerator(
                    new StandardCategoryURLGenerator());
        }

        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, dateAxis,
                renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
                plot, legend);
        currentTheme.apply(chart);
        return chart;

    }

    public static void main(String[] var0) throws IOException {
        //this.unitEnergyList = unitEnergyList;
        /*IntervalCategoryDataset dataset = createDatasetLong();
        JFreeChart chart = createChart( dataset);

        SVGGraphics2D g2 = new SVGGraphics2D(500, 300);
        Rectangle r = new Rectangle(0, 0, 500, 300);
        chart.draw(g2, r);
        File f = new File("Gantt.svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());*/

    }

}
