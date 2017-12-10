package com.socialappproject.ifelse;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
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
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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

        _barChart = (BarChart) view.findViewById(R.id.bar_chart);
        _lineChart = (LineChart) view.findViewById(R.id.line_chart);
        _pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        _scatterChart = (ScatterChart) view.findViewById(R.id.scatter_chart);

        _barChart.setNoDataText("");
        _lineChart.setNoDataText("");
        _pieChart.setNoDataText("");
        _scatterChart.setNoDataText("");

        view.findViewById(R.id.new_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.chart_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), which + "", Toast.LENGTH_SHORT).show();
                        if(which == 0) { // 막대 그래프
                            _lineChart.clear();
                            _pieChart.clear();
                            _scatterChart.clear();
                            _barChart.bringToFront();
                            drawChart(which, 10000);
                        } else if(which == 1) { // 원 그래프
                            _barChart.clear();
                            _lineChart.clear();
                            _scatterChart.clear();
                            //TODO: 원을 돌리는 것을 포함 시키려면 .setOnChartGestureListener(new OnChartGestureListener()) 구현 필요.
                            _pieChart.setOnTouchListener(null);
                            _pieChart.setClickable(true);
                            _pieChart.bringToFront();
                            drawChart(which, 10000);
                        } else if(which == 2) { // 선 그래프
                            _barChart.clear();
                            _pieChart.clear();
                            _scatterChart.clear();
                            _lineChart.bringToFront();
                            drawChart(which, 10000);
                        } else if(which == 3) { // 산점도 그래프
                            _barChart.clear();
                            _lineChart.clear();
                            _pieChart.clear();
                            _scatterChart.bringToFront();
                            drawChart(which, 10000);
                        } else {
                            _barChart.clear();
                            _pieChart.clear();
                            _lineChart.clear();
                            _scatterChart.clear();
                        }
                    }
                }).show();
            }
        });

        _barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "이 그래프는 막대 그래프입니다", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.bar_chart_ary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawChart(0, which);
                    }
                }).show();
            }
        });

        _pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "이 그래프는 원 그래프입니다", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.pie_chart_ary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawChart(1, which);
                    }
                }).show();
            }
        });


        _lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "이 그래프는 선 그래프입니다", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.line_chart_ary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawChart(2, which);
                    }
                }).show();
            }
        });


        _scatterChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "이 그래프는 산점도 그래프입니다", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.scatter_chart_ary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawChart(3, which);
                    }
                }).show();
            }
        });

        return view;
    }

    /*
    <string-array name="chart_ary">
        <item>막대 그래프</item>
        <item>원 그래프</item>
        <item>선 그래프</item>
        <item>산점도 그래프</item>
    </string-array>
    <string-array name="bar_chart_ary">
        <item>성별 비율</item>
        <item>연령 비율</item>
        <item>성별 + 연령 비율</item>
        <item>카테고리별 게시물 비율</item>
        <item>성별 + 카테고리 별 게시물 비율</item>
        <item>연령 + 카테고리 별 게시물 비율</item>
        <item>성별 + 연령 + 카테고리 별 게시물 비율</item>
    </string-array>
    <string-array name="pie_chart_ary">
        <item>성별 비율</item>
        <item>연령 비율</item>
        <item>카테고리별 게시물 비율</item>
    </string-array>
    <string-array name="line_chart_ary">
        <item>시간대 별 게시물 업데이트 수</item>
        <item>시간대 별 투표 업데이트 수</item>
    </string-array>
    <string-array name="scatter_chart_ary">
        <item>아직 구현 중입니다</item>
    </string-array>
     */

    private void drawChart(int chart_flag, int data_flag) {
        if(data_flag == 10000) {
            switch(chart_flag) {
                case 0:
                    ArrayList<BarEntry> nullBarEntry = new ArrayList<>();
                    BarDataSet nullBarDataSet = new BarDataSet(nullBarEntry, "NA");
                    ArrayList<BarDataSet> nullBarDataSets = new ArrayList<>();
                    nullBarDataSets.add(nullBarDataSet);

                    ArrayList<String> nullXAxisValues_bar = new ArrayList<>();

                    BarData nullBarData = new BarData(nullXAxisValues_bar, nullBarDataSets);

                    YAxis yLabels_left_bar = _barChart.getAxisLeft();
                    yLabels_left_bar.setDrawLabels(true);
                    yLabels_left_bar.setAxisMinValue(0);
                    yLabels_left_bar.setAxisMaxValue(100);

                    _barChart.setData(nullBarData);
                    _barChart.invalidate();
                    break;
                case 1:
                    ArrayList<Entry> nullPieEntries = new ArrayList<>();
                    ArrayList<String> nullPiexVals_pie = new ArrayList<>();
                    nullPiexVals_pie.add("0");
                    nullPieEntries.add(new Entry(0.0f, 1));

                    PieDataSet nullPieDataSet = new PieDataSet(nullPieEntries, "NA");

                    PieData nullPieData = new PieData(nullPiexVals_pie, nullPieDataSet);
                    nullPieDataSet.setSliceSpace(2f);

                    _pieChart.setData(nullPieData);
                    _pieChart.invalidate();
                    break;
                case 2:
                    ArrayList<Entry> nullLineEntry = new ArrayList<>();
                    LineDataSet nullLineDataSet = new LineDataSet(nullLineEntry, "NA");
                    ArrayList<LineDataSet> nullLineDataSets = new ArrayList<>();
                    nullLineDataSets.add(nullLineDataSet);

                    ArrayList<String> nullXAxisValues_line = new ArrayList<>();

                    LineData nullLineData = new LineData(nullXAxisValues_line, nullLineDataSets);

                    _lineChart.setData(nullLineData);
                    _lineChart.invalidate();
                    break;
                case 3:
                    _scatterChart.invalidate();
                    break;
                default:
            }
        } else {
            switch(chart_flag) {
                case 0:
                    BarData barData = new BarData(getXAxisValues(chart_flag, data_flag), getBarDataSet(data_flag));
                    _barChart.setData(barData);

                    _barChart.setDrawValuesForWholeStack(false);
                    _barChart.setDrawBarShadow(false);
                    _barChart.setDrawValueAboveBar(true);

                    _barChart.setHighlightEnabled(false);
                    _barChart.setDrawGridBackground(true);
                    _barChart.setDescription("");

                    XAxis x_bar = _barChart.getXAxis();
                    x_bar.setPosition(XAxis.XAxisPosition.BOTTOM);

                    YAxis yLabels_left_bar = _barChart.getAxisLeft();
                    yLabels_left_bar.setDrawLabels(true);
                    yLabels_left_bar.setAxisMinValue(0);
                    yLabels_left_bar.setAxisMaxValue(100);
                    YAxis yLabels_right_bar = _barChart.getAxisRight();
                    yLabels_right_bar.setDrawLabels(true);
                    yLabels_right_bar.setAxisMinValue(0);
                    yLabels_right_bar.setAxisMaxValue(1);

                    Legend legend_bar = _barChart.getLegend();
                    legend_bar.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

                    _barChart.animateXY(2000, 2000);
                    _barChart.invalidate();

                    break;
                case 1:
                    PieData pieData = new PieData(getXAxisValues(chart_flag, data_flag), getPieDataSet(data_flag));
                    _pieChart.setData(pieData);

                    _pieChart.setDrawHoleEnabled(true);
                    _pieChart.setHoleColor(Color.WHITE);

                    _pieChart.setHoleRadius(58f);
                    _pieChart.setTransparentCircleRadius(61f);

                    _pieChart.setDrawCenterText(true);

                    _pieChart.setRotationAngle(0);
                    // enable rotation of the chart by touch
                    _pieChart.setRotationEnabled(true);

                    Legend legend_pie = _pieChart.getLegend();
                    legend_pie.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

                    _pieChart.animateY(1400);
                    _pieChart.invalidate();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
            }

        }

    }

    private PieDataSet getPieDataSet(int data_flag) {

        PieDataSet pieDataSet = new PieDataSet(null, null);

        if(data_flag == 0) {
            final ArrayList<Entry> valueSet0 = new ArrayList<>();

            final Entry v0e0 = new Entry(1, 0); // 여
            final Entry v1e0 = new Entry(1, 1); // 남

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String snaped_gender = postSnapshot.child("gender").getValue().toString();
                        Log.d(TAG, "snaped gender is " + snaped_gender);
                        switch (snaped_gender) {
                            case "0": {
                                v0e0.setVal(v0e0.getVal() + 1);
                                break;
                            }
                            case "1": {
                                v1e0.setVal(v1e0.getVal() + 1);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                    float all_count = v0e0.getVal() + v1e0.getVal();

                    v0e0.setVal(v0e0.getVal()/all_count*100);
                    v1e0.setVal(v1e0.getVal()/all_count*100);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(TAG, "Failed to read app title value.", error.toException());
                }
            });


            valueSet0.add(v0e0);
            valueSet0.add(v1e0);

            Log.d(TAG, "" + v0e0.getVal());
            Log.d(TAG, "" + v1e0.getVal());

            pieDataSet = new PieDataSet(valueSet0, "");

            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            pieDataSet.setColors(colors);
            pieDataSet.setDrawValues(false);


        } else if(data_flag == 1) {

        } else if(data_flag == 2) {

        } else {
            ArrayList<Entry> nullPieEntries = new ArrayList<>();
            ArrayList<String> nullPiexVals_pie = new ArrayList<>();
            nullPiexVals_pie.add("0");
            nullPieEntries.add(new Entry(0.0f, 1));

            PieDataSet nullPieDataSet = new PieDataSet(nullPieEntries, "NA");

            PieData nullPieData = new PieData(nullPiexVals_pie, nullPieDataSet);
            nullPieDataSet.setSliceSpace(2f);

            _pieChart.setData(nullPieData);
            _pieChart.invalidate();
            Toast.makeText(getContext(), "아직 개발 중입니다.", Toast.LENGTH_SHORT).show();
        }
        return pieDataSet;
    }


    private ArrayList<BarDataSet> getBarDataSet(int data_flag) {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();

        if(data_flag == 0) {
            ArrayList<BarEntry> valueSet0 = new ArrayList<>();
            ArrayList<BarEntry> valueSet1 = new ArrayList<>();

            final BarEntry v0e0 = new BarEntry(0, 0); // 여
            final BarEntry v1e0 = new BarEntry(0, 1); // 남

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            String snaped_gender = postSnapshot.child("gender").getValue().toString();
                            Log.d(TAG, "snaped gender is " + snaped_gender);
                            switch (snaped_gender) {
                                case "0": {
                                    v0e0.setVal(v0e0.getVal() + 1);
                                    break;
                                }
                                case "1": {
                                    v1e0.setVal(v1e0.getVal() + 1);
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                        }
                        float all_count = v0e0.getVal() + v1e0.getVal();

                        v0e0.setVal(v0e0.getVal()/all_count*100);
                        v1e0.setVal(v1e0.getVal()/all_count*100);
                    }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(TAG, "Failed to read app title value.", error.toException());
                }
            });

            valueSet0.add(v0e0);
            valueSet1.add(v1e0);

            Log.d(TAG, "" + v0e0.getVal());
            Log.d(TAG, "" + v1e0.getVal());

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "남성");
            barDataSet1.setDrawValues(false);
            barDataSet1.setColor(ContextCompat.getColor(this.getContext(), R.color.men));
            BarDataSet barDataSet2 = new BarDataSet(valueSet0, "여성");
            barDataSet2.setDrawValues(false);
            barDataSet2.setColor(ContextCompat.getColor(this.getContext(), R.color.women));

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);

        } else if(data_flag == 1) {

            ArrayList<BarEntry> valueSet0 = new ArrayList<>();

            final BarEntry v0e0 = new BarEntry(0, 0); // 10대 미만
            final BarEntry v0e1 = new BarEntry(0, 1); // 10대
            final BarEntry v0e2 = new BarEntry(0, 2); // 20대
            final BarEntry v0e3 = new BarEntry(0, 3); // 30대
            final BarEntry v0e4 = new BarEntry(0, 4); // 40대
            final BarEntry v0e5 = new BarEntry(0, 5); // 50대
            final BarEntry v0e6 = new BarEntry(0, 6); // 60대 이상


            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        int snaped_old = Integer.parseInt((postSnapshot.child("old").getValue().toString()));

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

                    }
                    float all_count = v0e0.getVal() + v0e1.getVal() + v0e2.getVal() + v0e3.getVal() + v0e4.getVal() + v0e5.getVal() +v0e6.getVal();

                    v0e0.setVal(v0e0.getVal()/all_count*100);
                    v0e1.setVal(v0e1.getVal()/all_count*100);
                    v0e2.setVal(v0e2.getVal()/all_count*100);
                    v0e3.setVal(v0e3.getVal()/all_count*100);
                    v0e4.setVal(v0e4.getVal()/all_count*100);
                    v0e5.setVal(v0e5.getVal()/all_count*100);
                    v0e6.setVal(v0e6.getVal()/all_count*100);
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

            BarDataSet barDataSet2 = new BarDataSet(valueSet0, "");
            barDataSet2.setDrawValues(false);
            barDataSet2.setColor(ContextCompat.getColor(this.getContext(), R.color.chartPrimary));

            dataSets.add(barDataSet2);

        } else if(data_flag == 2) {

            ArrayList<BarEntry> valueSet0 = new ArrayList<>();
            ArrayList<BarEntry> valueSet1 = new ArrayList<>();

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

        } else if(data_flag == 3) {
            ArrayList<BarEntry> valueSet0 = new ArrayList<>();


            final BarEntry v0e0 = new BarEntry(0, 0); // 음식
            final BarEntry v0e1 = new BarEntry(0, 1); // 패션
            final BarEntry v0e2 = new BarEntry(0, 2); // 연애
            final BarEntry v0e3 = new BarEntry(0, 3); // 진로 및 학업
            final BarEntry v0e4 = new BarEntry(0, 4); // 엔터테인먼트
            final BarEntry v0e5 = new BarEntry(0, 5); // 장소
            final BarEntry v0e6 = new BarEntry(0, 6); // 뷰티
            final BarEntry v0e7 = new BarEntry(0, 7); // 기타



            articleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        int snpaed_category = Integer.parseInt(postSnapshot.child("category").getValue().toString());

                        if (snpaed_category == 0) {
                            v0e0.setVal(v0e0.getVal() + 1);
                        } else if(snpaed_category == 1) {
                            v0e1.setVal(v0e1.getVal() + 1);
                        } else if(snpaed_category == 2) {
                            v0e2.setVal(v0e2.getVal() + 1);
                        } else if(snpaed_category == 3) {
                            v0e3.setVal(v0e3.getVal() + 1);
                        } else if(snpaed_category == 4) {
                            v0e4.setVal(v0e4.getVal() + 1);
                        } else if(snpaed_category == 5) {
                            v0e5.setVal(v0e5.getVal() + 1);
                        } else if(snpaed_category == 6){
                            v0e6.setVal(v0e6.getVal() + 1);
                        } else if(snpaed_category == 7){
                            v0e6.setVal(v0e6.getVal() + 1);
                        }

                    }
                    float category_count = v0e0.getVal() + v0e1.getVal() + v0e2.getVal() + v0e3.getVal() + v0e4.getVal() + v0e5.getVal() + v0e6.getVal() + v0e7.getVal();

                    v0e0.setVal(v0e0.getVal()/category_count*100);
                    v0e1.setVal(v0e1.getVal()/category_count*100);
                    v0e2.setVal(v0e2.getVal()/category_count*100);
                    v0e3.setVal(v0e3.getVal()/category_count*100);
                    v0e4.setVal(v0e4.getVal()/category_count*100);
                    v0e5.setVal(v0e5.getVal()/category_count*100);
                    v0e6.setVal(v0e6.getVal()/category_count*100);
                    v0e7.setVal(v0e7.getVal()/category_count*100);

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
            valueSet0.add(v0e7);


            BarDataSet barDataSet1 = new BarDataSet(valueSet0, "카테고리");
            barDataSet1.setDrawValues(false);
            ArrayList<Integer> colors = new ArrayList<>();
            //TODO: get color method
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            barDataSet1.setColors(colors);

            dataSets.add(barDataSet1);

        } else {
            ArrayList<BarEntry> nullBarEntry = new ArrayList<>();
            BarDataSet nullBarDataSet = new BarDataSet(nullBarEntry, "NA");
            ArrayList<BarDataSet> nullBarDataSets = new ArrayList<>();
            nullBarDataSets.add(nullBarDataSet);

            ArrayList<String> nullXAxisValues_bar = new ArrayList<>();

            BarData nullBarData = new BarData(nullXAxisValues_bar, nullBarDataSets);

            _barChart.setData(nullBarData);
            _barChart.invalidate();
            Toast.makeText(getContext(), "아직 구현 중 입니다.", Toast.LENGTH_SHORT).show();
        }

        return dataSets;

    }

    private ArrayList<String> getXAxisValues(int chart_flag, int data_flag) {

        ArrayList<String> xAxis = new ArrayList<>();

        if(chart_flag == 0) {
            if(data_flag == 0) {
                xAxis.add("남자");
                xAxis.add("여자");
            } else if(data_flag == 1 || data_flag == 2) {
                xAxis.add("10대 미만");
                xAxis.add("10대");
                xAxis.add("20대");
                xAxis.add("30대");
                xAxis.add("40대");
                xAxis.add("50대");
                xAxis.add("60대 이상");
            } else if(data_flag == 3) {
                xAxis.add("음식");
                xAxis.add("패션");
                xAxis.add("연애");
                xAxis.add("진로 및 학업");
                xAxis.add("엔터테인먼트");
                xAxis.add("장소");
                xAxis.add("뷰티");
                xAxis.add("기타");
            }
        } else if(chart_flag == 1) {
            if(data_flag == 0) {
                xAxis.add("남자");
                xAxis.add("여자");
            } else if(data_flag == 1) {
                xAxis.add("10대 미만");
                xAxis.add("10대");
                xAxis.add("20대");
                xAxis.add("30대");
                xAxis.add("40대");
                xAxis.add("50대");
                xAxis.add("60대 이상");
            } else if(data_flag == 2) {
                xAxis.add("음식");
                xAxis.add("패션");
                xAxis.add("연애");
                xAxis.add("진로 및 학업");
                xAxis.add("엔터테인먼트");
                xAxis.add("장소");
                xAxis.add("뷰티");
                xAxis.add("기타");
            }
        } else if(chart_flag == 2) {

        } else if(chart_flag == 3) {

        } else {

        }
        return xAxis;
    }




}
