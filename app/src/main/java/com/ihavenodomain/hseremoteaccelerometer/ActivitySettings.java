package com.ihavenodomain.hseremoteaccelerometer;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihavenodomain.hseremoteaccelerometer.data.Const;
import com.ihavenodomain.hseremoteaccelerometer.data.preferences.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySettings extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.llUserName) LinearLayout llUserName;
    @BindView(R.id.llPassword) LinearLayout llPassword;
    @BindView(R.id.llDbName) LinearLayout llDbName;
    @BindView(R.id.llIpAddress) LinearLayout llIpAddress;
    @BindView(R.id.llSendIntervals) LinearLayout llSendIntervals;
    @BindView(R.id.btnRestoreDefault) Button btnRestoreDefault;

    @BindView(R.id.tvUserName) TextView tvLogin;
    @BindView(R.id.tvUserPassword) TextView tvPassword;
    @BindView(R.id.tvDbName) TextView tvDbName;
    @BindView(R.id.tvIpAddress) TextView tvIpAddress;
    @BindView(R.id.tvSendIntervals) TextView tvIntervals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSettings();

        llUserName.setOnClickListener(this);
        llPassword.setOnClickListener(this);
        llDbName.setOnClickListener(this);
        llIpAddress.setOnClickListener(this);
        llSendIntervals.setOnClickListener(this);
        btnRestoreDefault.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llUserName:
                break;
            case R.id.llPassword:
                break;
            case R.id.llDbName:
                break;
            case R.id.llIpAddress:
                break;
            case R.id.btnRestoreDefault:
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettings.this);
                builder.setTitle("Restore defaults");
                builder.setMessage("Are you sure? All changed settings will be lost");
                builder.setPositiveButton("Yes", (dialog, which) -> restoreDefaults());
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initSettings() {
        PreferencesManager manager = new PreferencesManager(this);
        tvLogin.setText(manager.getLogin());
        tvPassword.setText(manager.getPassword());
        tvDbName.setText(manager.getDbName());
        tvIpAddress.setText(manager.getIpAddress());

        String intervals = manager.getInterval() + " sec";
        tvIntervals.setText(intervals);
    }

    private void restoreDefaults() {
        PreferencesManager manager = new PreferencesManager(this);
        manager.setLogin(Const.DEFAULT_LOGIN);
        manager.setPassword(Const.DEFAULT_PASSWORD);
        manager.setDbName(Const.DEFAULT_DB_NAME);
        manager.setIpAddress(Const.DEFAULT_IP);
        manager.setInterval(Const.DEFAULT_INTERVAL);
        initSettings();
    }
}
