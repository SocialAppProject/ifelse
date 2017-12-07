package com.socialappproject.ifelse;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class StatisticFragment extends Fragment {
    private static final String TAG = "StatisticFragment";

    private DatabaseReference userRef = DatabaseManager.databaseReference.child("USER");
    private DatabaseReference articleRef = DatabaseManager.databaseReference.child("ARTICLE");

    BarChart _barChart;
    LineChart _lineChart;
    PieChart _pieChart;
    ScatterChart _scatterChart;


    private static final BarData null_data = new BarData();

    private static int CHART_FLAG = 100;

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

        view.findViewById(R.id.new_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.chart_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), which + "", Toast.LENGTH_SHORT).show();
                        if(which == 0) { // 막대 그래프
                            drawChart();
                        } else if(which == 1) { // 원 그래프

                        } else if(which == 2) { // 선 그래프

                        } else {
                            _barChart.clear();
                            _lineChart.clear();
                            _pieChart.clear();
                            _scatterChart.clear();
                        }
                    }
                }).show();
            }
        });

        _barChart = (BarChart) view.findViewById(R.id.bar_chart);
        _lineChart = (LineChart) view.findViewById(R.id.line_chart);
        _pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        _scatterChart = (ScatterChart) view.findViewById(R.id.scatter_chart);

        _barChart.setNoDataText("");
        _lineChart.setNoDataText("");
        _pieChart.setNoDataText("");
        _scatterChart.setNoDataText("");

        /*
        시나리오1: 성별 수 비율 - 파이차트, 바차트
        시나리오2: 연령 수 비율 - 파이차트, 바차트
        시나리오3: 성별 + 연령 수 비율 - 바차트
        시나리오4: 카테고리 별 게시물 수 비율 - 파이차트, 바차트
        시나리오5: 카테고리 별 투표 수 비율 - 파이차트, 바차트
        시나리오6: 하루 시간대 별 게시물 업데이트 수 - 라인차트
        시나리오7: 하루 시간대 별 투표 업데이트 수 - 라인차트
        시나리오8: 연령 별 카테고리 게시물 수 비율 - 스택바차트
        시나리오9: 성별 별 카테고리 게시물 수 비율 - 스택바차트
        시나이로10: 성별 + 연령 별 카테고리 게시물 수 비율 - 스택바차트
         */




        return view;
    }

    private void drawChart() {
        BarData data = new BarData(getXAxisValues(), getDataSet());
        _barChart.setData(data);

        _barChart.setDrawValuesForWholeStack(false);
        _barChart.setDrawBarShadow(false);
        _barChart.setDrawValueAboveBar(true);

        _barChart.setHighlightEnabled(false);
        _barChart.setDrawGridBackground(true);
        _barChart.setDescription("");

        XAxis x = _barChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yLabels_left = _barChart.getAxisLeft();
        yLabels_left.setDrawLabels(false);
        yLabels_left.setAxisMinValue(0);
        yLabels_left.setAxisMaxValue(100);
        YAxis yLabels_right = _barChart.getAxisRight();
        yLabels_right.setDrawLabels(false);

        Legend legend = _barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

        _barChart.animateXY(2000, 2000);
        _barChart.invalidate();

        Log.d(TAG, "asdf");
    }


    private ArrayList<BarDataSet> getDataSet() {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> valueSet0 = new ArrayList<>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        //int women_sum = 0, men_sum = 0;

        final BarEntry v0e0 = new BarEntry(0, 0); // 10대 미만 여
        final BarEntry v0e1 = new BarEntry(0, 1); // 10대 여
        final BarEntry v0e2 = new BarEntry(0, 2); // 20대 여
        final BarEntry v0e3 = new BarEntry(0, 3); // 30대 여
        final BarEntry v0e4 = new BarEntry(0, 4); // 40대 여
        final BarEntry v0e5 = new BarEntry(0, 5); // 50대 여
        final BarEntry v0e6 = new BarEntry(0, 6); // 60대 이상 여

        final BarEntry v1e0 = new BarEntry(0, 0); // 10대 미만 남
        final BarEntry v1e1 = new BarEntry(0, 1); // 10대 남
        final BarEntry v1e2 = new BarEntry(0, 2); // 20대 남
        final BarEntry v1e3 = new BarEntry(0, 3); // 30대 남
        final BarEntry v1e4 = new BarEntry(0, 4); // 40대 남
        final BarEntry v1e5 = new BarEntry(0, 5); // 50대 남
        final BarEntry v1e6 = new BarEntry(0, 6); // 60대 이상 남


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String snaped_gender = postSnapshot.child("gender").getValue().toString();
                    int snaped_old = Integer.parseInt((postSnapshot.child("old").getValue().toString()));

                    switch (snaped_gender) {
                        case "0": {
                            if (snaped_old < 10) {
                                v0e0.setVal(v0e0.getVal() + 1);
                            } else if(snaped_old < 20) {
                                v0e1.setVal(v0e1.getVal() + 1);
                            } else if(snaped_old < 30) {
                                v0e2.setVal(v0e2.getVal() + 1);
                            } else if(snaped_old < 40) {
                                v0e3.setVal(v0e3.getVal() + 1);
                            } else if(snaped_old < 50) {
                                v0e4.setVal(v0e4.getVal() + 1);
                            } else if(snaped_old < 60) {
                                v0e5.setVal(v0e5.getVal() + 1);
                            } else {
                                v0e6.setVal(v0e6.getVal() + 1);
                            }
                            break;
                        }
                        case "1": {
                            if (snaped_old < 10) {
                                v1e0.setVal(v1e0.getVal() + 1);
                            } else if(snaped_old < 20) {
                                v1e1.setVal(v1e1.getVal() + 1);
                            } else if(snaped_old < 30) {
                                v1e2.setVal(v1e2.getVal() + 1);
                            } else if(snaped_old < 40) {
                                v1e3.setVal(v1e3.getVal() + 1);
                            } else if(snaped_old < 50) {
                                v1e4.setVal(v1e4.getVal() + 1);
                            } else if(snaped_old < 60) {
                                v1e5.setVal(v1e5.getVal() + 1);
                            } else {
                                v1e6.setVal(v1e6.getVal() + 1);
                            }
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
                float women_count = v0e0.getVal() + v0e1.getVal() + v0e2.getVal() + v0e3.getVal() + v0e4.getVal() + v0e5.getVal() +v0e6.getVal();
                float men_count = v1e0.getVal() + v1e1.getVal() + v1e2.getVal() + v1e3.getVal() + v1e4.getVal() + v1e5.getVal() +v1e6.getVal();

                v0e0.setVal(v0e0.getVal()/women_count*100);
                v0e1.setVal(v0e1.getVal()/women_count*100);
                v0e2.setVal(v0e2.getVal()/women_count*100);
                v0e3.setVal(v0e3.getVal()/women_count*100);
                v0e4.setVal(v0e4.getVal()/women_count*100);
                v0e5.setVal(v0e5.getVal()/women_count*100);
                v0e6.setVal(v0e6.getVal()/women_count*100);

                v1e0.setVal(v1e0.getVal()/men_count*100);
                v1e1.setVal(v1e1.getVal()/men_count*100);
                v1e2.setVal(v1e2.getVal()/men_count*100);
                v1e3.setVal(v1e3.getVal()/men_count*100);
                v1e4.setVal(v1e4.getVal()/men_count*100);
                v1e5.setVal(v1e5.getVal()/men_count*100);
                v1e6.setVal(v1e6.getVal()/men_count*100);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        valueSet0.add(v0e0);
        valueSet0.add(v0e1);
        valueSet0.add(v0e2);
        valueSet0.add(v0e3);
        valueSet0.add(v0e4);
        valueSet0.add(v0e5);
        valueSet0.add(v0e6);

        valueSet1.add(v1e0);
        valueSet1.add(v1e1);
        valueSet1.add(v1e2);
        valueSet1.add(v1e3);
        valueSet1.add(v1e4);
        valueSet1.add(v1e5);
        valueSet1.add(v1e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "남성");
        barDataSet1.setDrawValues(false);
        barDataSet1.setColor(ContextCompat.getColor(this.getContext(), R.color.men));
        BarDataSet barDataSet2 = new BarDataSet(valueSet0, "여성");
        barDataSet2.setDrawValues(false);
        barDataSet2.setColor(ContextCompat.getColor(this.getContext(), R.color.women));

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




}
