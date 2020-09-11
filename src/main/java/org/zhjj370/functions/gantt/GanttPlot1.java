package org.zhjj370.functions.gantt;

import org.zhjj370.functions.element.DataForGantt;
import org.jfree.chart.ChartPanel;
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
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.ui.UIUtils;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;

import java.awt.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class GanttPlot1 extends ApplicationFrame {

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



    public GanttPlot1(String title) {
        /*super(var1);
        JPanel var2 = createDemoPanel();
        var2.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(var2);*/
        super(title);

        System.out.println(DataProcess.dataprocess().size());
        IntervalCategoryDataset dataset = createDatasetLong(DataProcess.dataprocess());
        //
        System.out.println("dataset = " + dataset.getColumnCount());
        //System.out.println("dataset = " + dataset.getValue(0));

        //

        JFreeChart chart = createChart( dataset);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);

        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }


    public static IntervalCategoryDataset createDatasetLong() {
        TaskSeriesLong data1 = new TaskSeriesLong("Scheduled1");
        data1.add(new TaskLong("1",new SimpleTimePeriodLong((long)1.01,(long)3.03)));
        data1.add(new TaskLong("1",new SimpleTimePeriodLong((long)5.01,(long)8.83)));
        System.out.println(data1.getItemCount());
        data1.add(new TaskLong("3",new SimpleTimePeriodLong((long)2.09,(long)5.08)));
        data1.add(new TaskLong("4",new SimpleTimePeriodLong((long)4.06,(long)7.30)));
        //data1.add(new TaskLong("5",new SimpleTimePeriodLong((long)3.01,(long)7.03)));
        //data1.add(new TaskLong("6",new SimpleTimePeriodLong((long)5.01,(long)8.03)));
        //data1.add(new TaskLong("7",new SimpleTimePeriodLong((long)4.01,(long)9.00)));
        TaskSeriesLong data2 = new TaskSeriesLong("Scheduled2");
        data2.add(new TaskLong("1",new SimpleTimePeriodLong((long)7.01,(long)12)));
        TaskSeriesLong data3 = new TaskSeriesLong("Scheduled3");
        data3.add(new TaskLong("5",new SimpleTimePeriodLong((long)7.01,(long)9.83)));

        TaskSeriesLongCollection v = new TaskSeriesLongCollection();
        v.add(data1);
        //v.add(data2);
        //v.add(data3);
        return v;
    }

    public static IntervalCategoryDataset createDatasetLong(List<DataForGantt> dataForGanttList) {
        List<TaskSeriesLong> dataSeries = new ArrayList<>();
        //遍历数组，按不同工件分组
        System.out.println(dataForGanttList.size());
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
        System.out.println(dataSeries.size());
        TaskSeriesLongCollection v = new TaskSeriesLongCollection();
        Iterator iteratorData2 = dataSeries.iterator();
        while (iteratorData2.hasNext()){
            v.add((TaskSeriesLong) iteratorData2.next());
        }
        System.out.println(v.getRowCount());
        return v;
    }

    private static Date date(int var0, int var1, int var2) {
        Calendar var3 = Calendar.getInstance();
        var3.set(var2, var1, var0);
        Date var4 = var3.getTime();
        return var4;
    }

    private static JFreeChart createChart(IntervalCategoryDataset var0) {
        JFreeChart chart = createGanttChart1("Gantt Chart Demo", "Part", "Task", var0, true, true, false);

        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        plot.setRangePannable(true);
        plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0F);
        plot.setRangeCrosshairVisible(true);

        ValueAxis axis = plot.getRangeAxis() ;
        axis.setRange(0,2200) ;
        plot.setRangeAxis(axis);

        GanttRenderer1 renderer = (GanttRenderer1)plot.getRenderer();
        renderer.setDrawBarOutline(false);
        //plot.setLabelFont(new Font("Dialog", Font.PLAIN, 18));
        renderer.setDefaultItemLabelGenerator(new LabelGenerator(
                (Integer) null));

        renderer.setDefaultItemLabelFont(new Font("Dialog", Font.PLAIN, 15));
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


    public static void main(String[] var0) {
        GanttPlot1 demo = new GanttPlot1("JFreeChart: GanttDemo1.java");
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}

