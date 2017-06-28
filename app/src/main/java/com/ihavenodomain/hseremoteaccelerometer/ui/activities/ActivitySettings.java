package com.ihavenodomain.hseremoteaccelerometer.ui.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihavenodomain.hseremoteaccelerometer.R;
import com.ihavenodomain.hseremoteaccelerometer.data.Const;
import com.ihavenodomain.hseremoteaccelerometer.data.preferences.PreferencesManager;
import com.ihavenodomain.hseremoteaccelerometer.ui.customDialogs.SettingsItemDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySettings extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.llUserName) LinearLayout llUserName;
    @BindView(R.id.llPassword) LinearLayout llPassword;
    @BindView(R.id.llDbName) LinearLayout llDbName;
    @BindView(R.id.llIpAddress) LinearLayout llIpAddress;
    @BindView(R.id.llSendIntervals) LinearLayout llSendIntervals;
    @BindView(R.id.btnRestoreDefault) Button btnRestoreDefault;

    @BindView(R.id.tvUserName) TextView tvUserName;
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

    final String DIALOG = "1";

    @Override
    public void onClick(View v) {
        SettingsItemDialogFragment fragment = new SettingsItemDialogFragment();
        switch (v.getId()) {
            case R.id.llUserName:
                fragment.setDialogType(Const.TYPE_USERNAME);
                fragment.setCurrentValue(tvUserName.getText().toString());
                fragment.show(getSupportFragmentManager(), DIALOG);
                break;
            case R.id.llPassword:
                fragment.setDialogType(Const.TYPE_PASSWORD);
                fragment.setCurrentValue(tvPassword.getText().toString());
                fragment.show(getSupportFragmentManager(), DIALOG);
                break;
            case R.id.llDbName:
                fragment.setDialogType(Const.TYPE_DB_NAME);
                fragment.setCurrentValue(tvDbName.getText().toString());
                fragment.show(getSupportFragmentManager(), DIALOG);
                break;
            case R.id.llIpAddress:
                fragment.setDialogType(Const.TYPE_IP);
                fragment.setCurrentValue(tvIpAddress.getText().toString());
                fragment.show(getSupportFragmentManager(), DIALOG);
                break;
            case R.id.llSendIntervals:
                fragment.setDialogType(Const.TYPE_INTERVAL);
                fragment.setCurrentValue(intervals);
                fragment.setHasSeekBar(true);
                fragment.show(getSupportFragmentManager(), DIALOG);
                break;
            case R.id.btnRestoreDefault:
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettings.this);
                builder.setTitle(R.string.defaults_header);
                builder.setMessage(R.string.default_confirm);
                builder.setPositiveButton(R.string.yes, (dialog, which) -> restoreDefaults());
                builder.setNegativeButton(R.string.No, (dialog, which) -> dialog.dismiss());
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

    public void setNewData(int type, String newValue) {
        PreferencesManager manager = new PreferencesManager(this);
        switch (type) {
            case Const.TYPE_USERNAME:
                tvUserName.setText(newValue);
                manager.setLogin(newValue);
                break;
            case Const.TYPE_PASSWORD:
                tvPassword.setText(newValue);
                manager.setPassword(newValue);
                break;
            case Const.TYPE_DB_NAME:
                tvDbName.setText(newValue);
                manager.setDbName(newValue);
                break;
            case Const.TYPE_IP:
                tvIpAddress.setText(newValue);
                manager.setIpAddress(newValue);
                break;
            case Const.TYPE_INTERVAL:
                intervals = newValue;
                tvIntervals.setText(newValue + " sec");
                manager.setInterval(newValue);
                break;
            default:
                break;
        }
    }

    String intervals;

    private void initSettings() {
        PreferencesManager manager = new PreferencesManager(this);
        tvUserName.setText(manager.getLogin());
        tvPassword.setText(manager.getPassword());
        tvDbName.setText(manager.getDbName());
        tvIpAddress.setText(manager.getIpAddress());

        intervals = manager.getInterval();
        tvIntervals.setText(intervals + " sec");
    }

    private void restoreDefaults() {
        PreferencesManager manager = new PreferencesManager(this);
        manager.setLogin(Const.DEFAULT_USERNAME);
        manager.setPassword(Const.DEFAULT_PASSWORD);
        manager.setDbName(Const.DEFAULT_DB_NAME);
        manager.setIpAddress(Const.DEFAULT_IP);
        manager.setInterval(Const.DEFAULT_INTERVAL);
        initSettings();
    }
}
