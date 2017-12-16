package com.socialappproject.ifelse;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StatisticFragment extends Fragment {
    private static final String TAG = "StatisticFragment";

    private DatabaseReference userRef = DatabaseManager.databaseReference.child("USER");
    private DatabaseReference articleRef = DatabaseManager.databaseReference.child("ARTICLE");
    private DatabaseReference voteRef = DatabaseManager.databaseReference.child("VOTED_TIME");

    BarChart _barChart;
    LineChart _lineChart;

    private static int NULL_DATA_FLAG = 10000;

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

        _barChart.setNoDataText("");
        _lineChart.setNoDataText("");
        view.findViewById(R.id.new_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.chart_ary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), which + "", Toast.LENGTH_SHORT).show();
                        if(which == 0) { // 막대 그래프
                            _lineChart.clear();
                            _barChart.bringToFront();
                            drawChart(which, NULL_DATA_FLAG);
                        } else if(which == 1) { // 선 그래프
                            _barChart.clear();
                            _lineChart.bringToFront();
                            drawChart(which, NULL_DATA_FLAG);
                        } else {
                            _barChart.clear();
                            _lineChart.clear();
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


        _lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "이 그래프는 선 그래프입니다", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(R.array.line_chart_ary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawChart(1, which);
                    }
                }).show();
            }
        });

        return view;
    }

    private void drawChart(int chart_flag, int data_flag) {
        if(data_flag == NULL_DATA_FLAG) {
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

                    ArrayList<Entry> nullLineEntry = new ArrayList<>();
                    Entry v0e0 = new Entry(0, 0);
                    Entry v0e1 = new Entry(0, 1);
                    Entry v0e2 = new Entry(0, 2);
                    nullLineEntry.add(v0e0);
                    nullLineEntry.add(v0e1);
                    nullLineEntry.add(v0e2);

                    LineDataSet nullLineDataSet = new LineDataSet(nullLineEntry, "NA");

                    ArrayList<String> nullXAxisValues_line = new ArrayList<>();
                    nullXAxisValues_line.add("0");
                    nullXAxisValues_line.add("1");
                    nullXAxisValues_line.add("2");

                    LineData nullLineData = new LineData(nullXAxisValues_line, nullLineDataSet);

                    YAxis yLabels_left_line = _lineChart.getAxisLeft();
                    yLabels_left_line.setDrawLabels(true);
                    yLabels_left_line.setAxisMinValue(0);
                    yLabels_left_line.setAxisMaxValue(100);
                    YAxis yLabels_right_line = _lineChart.getAxisRight();
                    yLabels_right_line.setDrawLabels(true);
                    yLabels_right_line.setAxisMinValue(0);
                    yLabels_right_line.setAxisMaxValue(1);

                    _lineChart.setData(nullLineData);
                    _lineChart.invalidate();
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
                    LineData lineData = new LineData(getXAxisValues(chart_flag, data_flag), getLineDataSet(data_flag));
                    _lineChart.setData(lineData);

                    _lineChart.setHorizontalFadingEdgeEnabled(false);

                    YAxis yLabels_left_line = _lineChart.getAxisLeft();
                    yLabels_left_line.setDrawLabels(true);
                    yLabels_left_line.setAxisMinValue(0);
                    yLabels_left_line.setAxisMaxValue(100);
                    YAxis yLabels_right_line = _lineChart.getAxisRight();
                    yLabels_right_line.setDrawLabels(true);
                    yLabels_right_line.setAxisMinValue(0);
                    yLabels_right_line.setAxisMaxValue(1);

                    Legend legend_line = _lineChart.getLegend();
                    legend_line.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

                    _lineChart.animateX(2000);
                    _lineChart.invalidate();

                    break;
                default:
            }

        }

    }

    private LineDataSet getLineDataSet(int data_flag) {

        LineDataSet lineDataSet;

        if(data_flag == 0) {
            final ArrayList<Entry> valueSet0 = new ArrayList<>();

            final Entry v0e0 = new Entry(0, 0);     // 0시
            final Entry v0e1 = new Entry(0, 1);     // 1시
            final Entry v0e2 = new Entry(0, 2);     // 2시
            final Entry v0e3 = new Entry(0, 3);     // 3시
            final Entry v0e4 = new Entry(0, 4);     // 4시
            final Entry v0e5 = new Entry(0, 5);     // 5시
            final Entry v0e6 = new Entry(0, 6);     // 6시
            final Entry v0e7 = new Entry(0, 7);     // 7시
            final Entry v0e8 = new Entry(0, 8);     // 8시
            final Entry v0e9 = new Entry(0, 9);     // 9시
            final Entry v0e10 = new Entry(0, 10);    // 10시
            final Entry v0e11 = new Entry(0, 11);    // 11시
            final Entry v0e12 = new Entry(0, 12);    // 12시
            final Entry v0e13 = new Entry(0, 13);    // 13시
            final Entry v0e14 = new Entry(0, 14);    // 14시
            final Entry v0e15 = new Entry(0, 15);    // 15시
            final Entry v0e16 = new Entry(0, 16);    // 16시
            final Entry v0e17 = new Entry(0, 17);    // 17시
            final Entry v0e18 = new Entry(0, 18);    // 18시
            final Entry v0e19 = new Entry(0, 19);    // 19시
            final Entry v0e20 = new Entry(0, 20);    // 20시
            final Entry v0e21 = new Entry(0, 21);    // 21시
            final Entry v0e22 = new Entry(0, 22);    // 22시
            final Entry v0e23 = new Entry(0, 23);    // 23시

            articleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        String snaped_time = postSnapshot.child("time").getValue().toString().substring(6, 8);

                        switch(snaped_time) {
                            case "00": v0e0.setVal(v0e0.getVal() + 1); break;
                            case "01": v0e1.setVal(v0e1.getVal() + 1); break;
                            case "02": v0e2.setVal(v0e2.getVal() + 1); break;
                            case "03": v0e3.setVal(v0e3.getVal() + 1); break;
                            case "04": v0e4.setVal(v0e4.getVal() + 1); break;
                            case "05": v0e5.setVal(v0e5.getVal() + 1); break;
                            case "06": v0e6.setVal(v0e6.getVal() + 1); break;
                            case "07": v0e7.setVal(v0e7.getVal() + 1); break;
                            case "08": v0e8.setVal(v0e8.getVal() + 1); break;
                            case "09": v0e9.setVal(v0e9.getVal() + 1); break;
                            case "10": v0e10.setVal(v0e10.getVal() + 1); break;
                            case "11": v0e11.setVal(v0e11.getVal() + 1); break;
                            case "12": v0e12.setVal(v0e12.getVal() + 1); break;
                            case "13": v0e13.setVal(v0e13.getVal() + 1); break;
                            case "14": v0e14.setVal(v0e14.getVal() + 1); break;
                            case "15": v0e15.setVal(v0e15.getVal() + 1); break;
                            case "16": v0e16.setVal(v0e16.getVal() + 1); break;
                            case "17": v0e17.setVal(v0e17.getVal() + 1); break;
                            case "18": v0e18.setVal(v0e18.getVal() + 1); break;
                            case "19": v0e19.setVal(v0e19.getVal() + 1); break;
                            case "20": v0e20.setVal(v0e20.getVal() + 1); break;
                            case "21": v0e21.setVal(v0e21.getVal() + 1); break;
                            case "22": v0e22.setVal(v0e22.getVal() + 1); break;
                            case "23": v0e23.setVal(v0e23.getVal() + 1); break;
                        }

                    }
                    float all_count = v0e0.getVal() +
                            v0e1.getVal() +
                            v0e2.getVal() +
                            v0e3.getVal() +
                            v0e4.getVal() +
                            v0e5.getVal() +
                            v0e6.getVal() +
                            v0e7.getVal() +
                            v0e8.getVal() +
                            v0e9.getVal() +
                            v0e10.getVal() +
                            v0e11.getVal() +
                            v0e12.getVal() +
                            v0e13.getVal() +
                            v0e14.getVal() +
                            v0e15.getVal() +
                            v0e16.getVal() +
                            v0e17.getVal() +
                            v0e18.getVal() +
                            v0e19.getVal() +
                            v0e20.getVal() +
                            v0e21.getVal() +
                            v0e22.getVal() +
                            v0e23.getVal();

                    v0e0.setVal(v0e0.getVal()/all_count*100);
                    v0e1.setVal(v0e1.getVal()/all_count*100);
                    v0e2.setVal(v0e2.getVal()/all_count*100);
                    v0e3.setVal(v0e3.getVal()/all_count*100);
                    v0e4.setVal(v0e4.getVal()/all_count*100);
                    v0e5.setVal(v0e5.getVal()/all_count*100);
                    v0e6.setVal(v0e6.getVal()/all_count*100);
                    v0e7.setVal(v0e7.getVal()/all_count*100);
                    v0e8.setVal(v0e8.getVal()/all_count*100);
                    v0e9.setVal(v0e9.getVal()/all_count*100);
                    v0e10.setVal(v0e10.getVal()/all_count*100);
                    v0e11.setVal(v0e11.getVal()/all_count*100);
                    v0e12.setVal(v0e12.getVal()/all_count*100);
                    v0e13.setVal(v0e13.getVal()/all_count*100);
                    v0e14.setVal(v0e14.getVal()/all_count*100);
                    v0e15.setVal(v0e15.getVal()/all_count*100);
                    v0e16.setVal(v0e16.getVal()/all_count*100);
                    v0e17.setVal(v0e17.getVal()/all_count*100);
                    v0e18.setVal(v0e18.getVal()/all_count*100);
                    v0e19.setVal(v0e19.getVal()/all_count*100);
                    v0e20.setVal(v0e20.getVal()/all_count*100);
                    v0e21.setVal(v0e21.getVal()/all_count*100);
                    v0e22.setVal(v0e22.getVal()/all_count*100);
                    v0e23.setVal(v0e23.getVal()/all_count*100);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    Log.e(TAG, "Failed to read app title value.", databaseError.toException());
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
            valueSet0.add(v0e8);
            valueSet0.add(v0e9);
            valueSet0.add(v0e10);
            valueSet0.add(v0e11);
            valueSet0.add(v0e12);
            valueSet0.add(v0e13);
            valueSet0.add(v0e14);
            valueSet0.add(v0e15);
            valueSet0.add(v0e16);
            valueSet0.add(v0e17);
            valueSet0.add(v0e18);
            valueSet0.add(v0e19);
            valueSet0.add(v0e20);
            valueSet0.add(v0e21);
            valueSet0.add(v0e22);
            valueSet0.add(v0e23);

            lineDataSet = new LineDataSet(valueSet0, "게시물 수");
            lineDataSet.setDrawValues(false);
            lineDataSet.setColor(ContextCompat.getColor(this.getContext(), R.color.men));
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleColor(Color.WHITE);
            lineDataSet.setCircleSize(3f);
            lineDataSet.setFillAlpha(65);
            lineDataSet.setFillColor(Color.RED);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));

        } else if(data_flag == 1) {
            final ArrayList<Entry> valueSet0 = new ArrayList<>();

            final Entry v0e0 = new Entry(0, 0);     // 0시
            final Entry v0e1 = new Entry(0, 1);     // 1시
            final Entry v0e2 = new Entry(0, 2);     // 2시
            final Entry v0e3 = new Entry(0, 3);     // 3시
            final Entry v0e4 = new Entry(0, 4);     // 4시
            final Entry v0e5 = new Entry(0, 5);     // 5시
            final Entry v0e6 = new Entry(0, 6);     // 6시
            final Entry v0e7 = new Entry(0, 7);     // 7시
            final Entry v0e8 = new Entry(0, 8);     // 8시
            final Entry v0e9 = new Entry(0, 9);     // 9시
            final Entry v0e10 = new Entry(0, 10);    // 10시
            final Entry v0e11 = new Entry(0, 11);    // 11시
            final Entry v0e12 = new Entry(0, 12);    // 12시
            final Entry v0e13 = new Entry(0, 13);    // 13시
            final Entry v0e14 = new Entry(0, 14);    // 14시
            final Entry v0e15 = new Entry(0, 15);    // 15시
            final Entry v0e16 = new Entry(0, 16);    // 16시
            final Entry v0e17 = new Entry(0, 17);    // 17시
            final Entry v0e18 = new Entry(0, 18);    // 18시
            final Entry v0e19 = new Entry(0, 19);    // 19시
            final Entry v0e20 = new Entry(0, 20);    // 20시
            final Entry v0e21 = new Entry(0, 21);    // 21시
            final Entry v0e22 = new Entry(0, 22);    // 22시
            final Entry v0e23 = new Entry(0, 23);    // 23시

            voteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        String snaped_time = postSnapshot.getValue().toString().substring(6, 8);
                        switch(snaped_time) {
                            case "00": v0e0.setVal(v0e0.getVal() + 1); break;
                            case "01": v0e1.setVal(v0e1.getVal() + 1); break;
                            case "02": v0e2.setVal(v0e2.getVal() + 1); break;
                            case "03": v0e3.setVal(v0e3.getVal() + 1); break;
                            case "04": v0e4.setVal(v0e4.getVal() + 1); break;
                            case "05": v0e5.setVal(v0e5.getVal() + 1); break;
                            case "06": v0e6.setVal(v0e6.getVal() + 1); break;
                            case "07": v0e7.setVal(v0e7.getVal() + 1); break;
                            case "08": v0e8.setVal(v0e8.getVal() + 1); break;
                            case "09": v0e9.setVal(v0e9.getVal() + 1); break;
                            case "10": v0e10.setVal(v0e10.getVal() + 1); break;
                            case "11": v0e11.setVal(v0e11.getVal() + 1); break;
                            case "12": v0e12.setVal(v0e12.getVal() + 1); break;
                            case "13": v0e13.setVal(v0e13.getVal() + 1); break;
                            case "14": v0e14.setVal(v0e14.getVal() + 1); break;
                            case "15": v0e15.setVal(v0e15.getVal() + 1); break;
                            case "16": v0e16.setVal(v0e16.getVal() + 1); break;
                            case "17": v0e17.setVal(v0e17.getVal() + 1); break;
                            case "18": v0e18.setVal(v0e18.getVal() + 1); break;
                            case "19": v0e19.setVal(v0e19.getVal() + 1); break;
                            case "20": v0e20.setVal(v0e20.getVal() + 1); break;
                            case "21": v0e21.setVal(v0e21.getVal() + 1); break;
                            case "22": v0e22.setVal(v0e22.getVal() + 1); break;
                            case "23": v0e23.setVal(v0e23.getVal() + 1); break;
                        }

                    }
                    float all_count = v0e0.getVal() +
                            v0e1.getVal() +
                            v0e2.getVal() +
                            v0e3.getVal() +
                            v0e4.getVal() +
                            v0e5.getVal() +
                            v0e6.getVal() +
                            v0e7.getVal() +
                            v0e8.getVal() +
                            v0e9.getVal() +
                            v0e10.getVal() +
                            v0e11.getVal() +
                            v0e12.getVal() +
                            v0e13.getVal() +
                            v0e14.getVal() +
                            v0e15.getVal() +
                            v0e16.getVal() +
                            v0e17.getVal() +
                            v0e18.getVal() +
                            v0e19.getVal() +
                            v0e20.getVal() +
                            v0e21.getVal() +
                            v0e22.getVal() +
                            v0e23.getVal();

                    v0e0.setVal(v0e0.getVal()/all_count*100);
                    v0e1.setVal(v0e1.getVal()/all_count*100);
                    v0e2.setVal(v0e2.getVal()/all_count*100);
                    v0e3.setVal(v0e3.getVal()/all_count*100);
                    v0e4.setVal(v0e4.getVal()/all_count*100);
                    v0e5.setVal(v0e5.getVal()/all_count*100);
                    v0e6.setVal(v0e6.getVal()/all_count*100);
                    v0e7.setVal(v0e7.getVal()/all_count*100);
                    v0e8.setVal(v0e8.getVal()/all_count*100);
                    v0e9.setVal(v0e9.getVal()/all_count*100);
                    v0e10.setVal(v0e10.getVal()/all_count*100);
                    v0e11.setVal(v0e11.getVal()/all_count*100);
                    v0e12.setVal(v0e12.getVal()/all_count*100);
                    v0e13.setVal(v0e13.getVal()/all_count*100);
                    v0e14.setVal(v0e14.getVal()/all_count*100);
                    v0e15.setVal(v0e15.getVal()/all_count*100);
                    v0e16.setVal(v0e16.getVal()/all_count*100);
                    v0e17.setVal(v0e17.getVal()/all_count*100);
                    v0e18.setVal(v0e18.getVal()/all_count*100);
                    v0e19.setVal(v0e19.getVal()/all_count*100);
                    v0e20.setVal(v0e20.getVal()/all_count*100);
                    v0e21.setVal(v0e21.getVal()/all_count*100);
                    v0e22.setVal(v0e22.getVal()/all_count*100);
                    v0e23.setVal(v0e23.getVal()/all_count*100);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    Log.e(TAG, "Failed to read app title value.", databaseError.toException());
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
            valueSet0.add(v0e8);
            valueSet0.add(v0e9);
            valueSet0.add(v0e10);
            valueSet0.add(v0e11);
            valueSet0.add(v0e12);
            valueSet0.add(v0e13);
            valueSet0.add(v0e14);
            valueSet0.add(v0e15);
            valueSet0.add(v0e16);
            valueSet0.add(v0e17);
            valueSet0.add(v0e18);
            valueSet0.add(v0e19);
            valueSet0.add(v0e20);
            valueSet0.add(v0e21);
            valueSet0.add(v0e22);
            valueSet0.add(v0e23);

            lineDataSet = new LineDataSet(valueSet0, "투표 수");
            lineDataSet.setDrawValues(false);
            lineDataSet.setColor(ContextCompat.getColor(this.getContext(), R.color.men));
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleColor(Color.WHITE);
            lineDataSet.setCircleSize(3f);
            lineDataSet.setFillAlpha(65);
            lineDataSet.setFillColor(Color.RED);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));

        } else {
            ArrayList<Entry> nullLineEntry = new ArrayList<>();
            Entry v0e0 = new Entry(0, 0);
            Entry v0e1 = new Entry(0, 1);
            Entry v0e2 = new Entry(0, 2);
            nullLineEntry.add(v0e0);
            nullLineEntry.add(v0e1);
            nullLineEntry.add(v0e2);

            LineDataSet nullLineDataSet = new LineDataSet(nullLineEntry, "NA");

            lineDataSet = nullLineDataSet;

        }

        return lineDataSet;
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

            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            barDataSet2.setColors(colors);

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

            dataSets = nullBarDataSets;
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
            if(data_flag == 0 || data_flag == 1) {
                xAxis.add("0시");
                xAxis.add("1시");
                xAxis.add("2시");
                xAxis.add("3시");
                xAxis.add("4시");
                xAxis.add("5시");
                xAxis.add("6시");
                xAxis.add("7시");
                xAxis.add("8시");
                xAxis.add("9시");
                xAxis.add("10시");
                xAxis.add("11시");
                xAxis.add("12시");
                xAxis.add("13시");
                xAxis.add("14시");
                xAxis.add("15시");
                xAxis.add("16시");
                xAxis.add("17시");
                xAxis.add("18시");
                xAxis.add("19시");
                xAxis.add("20시");
                xAxis.add("21시");
                xAxis.add("22시");
                xAxis.add("23시");
            }

        } else {
            xAxis.add("");
        }
        return xAxis;
    }




}
