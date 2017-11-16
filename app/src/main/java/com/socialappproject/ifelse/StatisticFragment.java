package com.socialappproject.ifelse;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public StatisticFragment() {
        // Required empty public constructor
    }

    public static StatisticFragment newInstance() {
        StatisticFragment fragment = new StatisticFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        BarChart chart = (BarChart) view.findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);

        chart.setDrawValuesForWholeStack(true);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);

        chart.setHighlightEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDescription("");

        XAxis x = chart.getXAxis();
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yLabels = chart.getAxisLeft();
        yLabels.setDrawGridLines(false);
        YAxis yLabels1 = chart.getAxisRight();
        yLabels1.setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

        chart.animateXY(2000, 2000);
        chart.invalidate();

        return view;
    }
    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e0 = new BarEntry(10.000f, 0); // 10대 미만 남
        valueSet1.add(v1e0);
        BarEntry v1e1 = new BarEntry(100.000f, 1); // 10대 남
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(60.000f, 2); // 20대 남
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(90.000f, 3); // 30대 남
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(40.000f, 4); // 40대 남
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(10.000f, 5); // 50대 남
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(0.000f, 6); // 60대 이상 남
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e0 = new BarEntry(50.000f, 0); // 10대 미만 여
        valueSet2.add(v2e0);
        BarEntry v2e1 = new BarEntry(90.000f, 1); // 10대 여
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(120.000f, 2); // 20대 여
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(60.000f, 3); // 30대 여
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(20.000f, 4); // 40대 여
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 5); // 50대 여
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(1.000f, 6); // 60대 이상 여
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(ContextCompat.getColor(this.getContext(), R.color.men));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColor(ContextCompat.getColor(this.getContext(), R.color.women));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("10대 미만");
        xAxis.add("10대");
        xAxis.add("20대");
        xAxis.add("30대");
        xAxis.add("40대");
        xAxis.add("50대");
        xAxis.add("60대 이상");
        return xAxis;
    }


    public class MyBarValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;
        private Context context;

        int toggle;
        float totalVal;

        public MyBarValueFormatter(Context context){
            toggle = 0;
            totalVal = 0;
            this.context = context;
            mFormat = new DecimalFormat("###,###,###,##0");
        }

        @Override
        public String getFormattedValue(float value) {

            if(toggle % 3 == 0){
                toggle++;
                totalVal =  value;
                return "";
            }
            else if(toggle % 3 == 1){
                toggle++;
                totalVal =  totalVal + value;
                return "";
            }
            else{
                toggle++;
                totalVal = totalVal + value;
                return context.getResources().getString(R.string.project_id) + " " + mFormat.format(totalVal) + "000";
            }
        }
    }

    /*
    ScreenUtility screenUtility;
        BarChart bar;
        String nameOfChart,xAxisDesciption,yAxisDescription;
        TextView xAxisName;


        public BarChartFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            //this.setRetainInstance(true);
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {

            }
        }
        public View getView(){
            return bar.getRootView();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            screenUtility = new ScreenUtility(getActivity());


            ArrayList<BarEntry> entries = new ArrayList<>();

            BarDataSet dataSet = new BarDataSet(entries,nameOfChart);
            dataSet.setColors(getColors());
            dataSet.setStackLabels(new String[]{"CU1", "CU2"});
            dataSet.setValueFormatter(new MyValueFormatter());

            ArrayList<String> labels = new ArrayList<String>();

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(dataSet);

            bar = new BarChart(getActivity());

            BarData data = new BarData(labels,dataSets);
            bar.setData(data);

            bar.setDrawValuesForWholeStack(true);
            bar.setDrawBarShadow(false);
            bar.setDrawValueAboveBar(false);

            bar.setHighlightEnabled(false);
            bar.setDrawGridBackground(false);
            bar.setDescription("");

            XAxis x = bar.getXAxis();
            x.setDrawGridLines(false);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis yLabels = bar.getAxisLeft();
            yLabels.setDrawGridLines(false);
            YAxis yLabels1 = bar.getAxisRight();
            yLabels1.setEnabled(false);

            Legend legend = bar.getLegend();
            legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

            bar.animateY(2000);

            int layoutHeight = screenUtility.getWidth() > screenUtility.getHeight() ? (int) screenUtility.getHeight() : (int) screenUtility.getWidth();
            if(screenUtility.getHeight()>screenUtility.getWidth()) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) screenUtility.getWidth() - 20, layoutHeight);
                bar.getRootView().setLayoutParams(new ViewGroup.LayoutParams(params));
            }else{
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (screenUtility.getWidth()/2) - 20, layoutHeight);
                bar.getRootView().setLayoutParams(new ViewGroup.LayoutParams(params));
            }

            bar.setNoDataText("No Data is Available");

//Tried adding Textview Here
            xAxisName = new TextView(getActivity());
            xAxisName.setText("Date");
            xAxisName.setGravity(Gravity.BOTTOM);
            container.addView(xAxisName);
            return bar.getRootView();
     */

}
