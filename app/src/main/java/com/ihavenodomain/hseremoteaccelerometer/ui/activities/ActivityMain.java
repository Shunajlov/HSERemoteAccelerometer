package com.ihavenodomain.hseremoteaccelerometer.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ihavenodomain.hseremoteaccelerometer.R;
import com.ihavenodomain.hseremoteaccelerometer.data.Const;
import com.ihavenodomain.hseremoteaccelerometer.data.preferences.PreferencesManager;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity implements SensorEventListener {
    final Handler mHandler = new Handler();

    @BindView(R.id.graphAcc) GraphView graphAcc;
    @BindView(R.id.graphGyro) GraphView graphGyro;
    @BindView(R.id.tvSendIntervals) TextView tvSendIntervals;

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mGyroscope;

    String accX, accY, accZ, gyroX, gyroY, gyroZ;
    Runnable mTimerAcc, mTimerGyro;
    LineGraphSeries<DataPoint> mSeriesAccX, mSeriesAccY, mSeriesAccZ,
                               mSeriesGyroX, mSeriesGyroY, mSeriesGyroZ;
    Integer timePassed = 0;
    Integer timePassed2 = 0;

    // Период отправки берём из настроек
    Integer period = 1;
    String login;
    String password;
    String dbName;
    String ipAddress;
    String interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        initSettings();

        initGraphs();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTimerAcc);
        mHandler.removeCallbacks(mTimerGyro);
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        initSettings();

        String temp = getString(R.string.intervals_of_data_sending) + ": " + interval + " sec";
        tvSendIntervals.setText(temp);

        mTimerAcc = () -> {
            if(accX == null) accX = "0";
            if(accY == null) accY = "0";
            if(accZ == null) accZ = "0";

            mSeriesAccX.appendData(new DataPoint(timePassed, Double.valueOf(accX)), true, 11);
            mSeriesAccY.appendData(new DataPoint(timePassed, Double.valueOf(accY)), true, 11);
            mSeriesAccZ.appendData(new DataPoint(timePassed, Double.valueOf(accZ)), true, 11);

            timePassed += 1;
            mHandler.postDelayed(mTimerAcc, period * 1000);
        };
        mHandler.postDelayed(mTimerAcc, 1000);

        mTimerGyro = () -> {
            if(gyroX == null) gyroX = "0";
            if(gyroY == null) gyroY = "0";
            if(gyroZ == null) gyroZ = "0";

            mSeriesGyroX.appendData(new DataPoint(timePassed2, Double.valueOf(gyroX)), true, 11);
            mSeriesGyroY.appendData(new DataPoint(timePassed2, Double.valueOf(gyroY)), true, 11);
            mSeriesGyroZ.appendData(new DataPoint(timePassed2, Double.valueOf(gyroZ)), true, 11);

            timePassed2 += 1;
            mHandler.postDelayed(mTimerGyro, period * 1000);
        };
        mHandler.postDelayed(mTimerGyro, 1000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                // X - 0, Y - 1, Z - 2
//                event.values[0]
                accX = String.format(Locale.US, "%.2f", event.values[0]);
                accY  = String.format(Locale.US, "%.2f", event.values[1]);
                accZ  = String.format(Locale.US, "%.2f", event.values[2]);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroX = String.format(Locale.US, "%.2f", event.values[0]);
                gyroY  = String.format(Locale.US, "%.2f", event.values[1]);
                gyroZ  = String.format(Locale.US, "%.2f", event.values[2]);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, ActivitySettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initGraphs() {
        mSeriesAccX = new LineGraphSeries<>();
        mSeriesAccY = new LineGraphSeries<>();
        mSeriesAccZ = new LineGraphSeries<>();

        mSeriesAccX.setColor(Color.RED);
        mSeriesAccY.setColor(Color.BLUE);
        mSeriesAccZ.setColor(Color.GREEN);

        graphAcc.addSeries(mSeriesAccX);
        graphAcc.addSeries(mSeriesAccY);
        graphAcc.addSeries(mSeriesAccZ);

        mSeriesGyroX = new LineGraphSeries<>();
        mSeriesGyroY = new LineGraphSeries<>();
        mSeriesGyroZ = new LineGraphSeries<>();

        mSeriesGyroX.setColor(Color.RED);
        mSeriesGyroY.setColor(Color.BLUE);
        mSeriesGyroZ.setColor(Color.GREEN);

        graphGyro.addSeries(mSeriesGyroX);
        graphGyro.addSeries(mSeriesGyroY);
        graphGyro.addSeries(mSeriesGyroZ);

        graphAcc.getViewport().setXAxisBoundsManual(true);
        graphAcc.getViewport().setYAxisBoundsManual(true);
        graphAcc.getViewport().setMinY(-11);
        graphAcc.getViewport().setMaxY(11);
        graphAcc.getViewport().setMinX(0);
        graphAcc.getViewport().setMaxX(10);
        graphAcc.getGridLabelRenderer().setHumanRounding(true);
        graphAcc.getViewport().setScalable(true);

        graphGyro.getViewport().setXAxisBoundsManual(true);
        graphGyro.getViewport().setYAxisBoundsManual(true);
        graphGyro.getViewport().setMinY(-11);
        graphGyro.getViewport().setMaxY(11);
        graphGyro.getViewport().setMinX(0);
        graphGyro.getViewport().setMaxX(10);
        graphGyro.getGridLabelRenderer().setHumanRounding(true);
        graphGyro.getViewport().setScalable(true);

        // Legend
        mSeriesAccX.setTitle("AccX");
        mSeriesAccY.setTitle("AccY");
        mSeriesAccZ.setTitle("AccZ");

        mSeriesGyroX.setTitle("GyroX");
        mSeriesGyroY.setTitle("GyroY");
        mSeriesGyroZ.setTitle("GyroZ");

        graphAcc.getLegendRenderer().setVisible(true);
        graphAcc.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        graphGyro.getLegendRenderer().setVisible(true);
        graphGyro.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
    }

    private void initSettings() {
        PreferencesManager manager = new PreferencesManager(this);
        login = manager.getLogin();
        password = manager.getPassword();
        dbName = manager.getDbName();
        ipAddress = manager.getIpAddress();
        interval = manager.getInterval();

        if(login.equals(""))
            manager.setLogin(Const.DEFAULT_USERNAME);
        if(password.equals(""))
            manager.setPassword(Const.DEFAULT_PASSWORD);
        if(dbName.equals(""))
            manager.setDbName(Const.DEFAULT_DB_NAME);
        if(ipAddress.equals(""))
            manager.setIpAddress(Const.DEFAULT_IP);
        if(interval.equals("")) {
            manager.setInterval(Const.DEFAULT_INTERVAL);
            period = Integer.valueOf(Const.DEFAULT_INTERVAL);
            interval = Const.DEFAULT_INTERVAL;
        } else
            period = Integer.valueOf(interval);
    }
}
