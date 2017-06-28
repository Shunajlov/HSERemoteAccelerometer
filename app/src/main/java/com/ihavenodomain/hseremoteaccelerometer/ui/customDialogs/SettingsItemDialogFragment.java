package com.ihavenodomain.hseremoteaccelerometer.ui.customDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ihavenodomain.hseremoteaccelerometer.R;
import com.ihavenodomain.hseremoteaccelerometer.data.Const;
import com.ihavenodomain.hseremoteaccelerometer.ui.activities.ActivitySettings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsItemDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvDialogOk) TextView tvOk;
    @BindView(R.id.tvDialogCancel) TextView tvCancel;
    @BindView(R.id.etValue) EditText etValue;
    @BindView(R.id.seekBar) SeekBar seekBar;

    private int dialogType;
    private String currentValue;
    private boolean hasSeekBar;

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void setHasSeekBar(boolean hasSeekBar) {
        this.hasSeekBar = hasSeekBar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings_change, null);
        ButterKnife.bind(this, view);

        if(hasSeekBar)
            seekBar.setVisibility(View.VISIBLE);
        else
            seekBar.setVisibility(View.GONE);

        if (currentValue != null && !currentValue.equals("")) {
            etValue.setText(currentValue);
            if(hasSeekBar && Integer.valueOf(currentValue) > 0 && Integer.valueOf(currentValue) < 61)
                seekBar.setProgress(Integer.valueOf(currentValue));
        }

        if(dialogType == Const.TYPE_PASSWORD)
            etValue.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvOk.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDialogOk:
                ((ActivitySettings) getActivity()).setNewData(dialogType, etValue.getText().toString());
                dismiss();
                break;
            case R.id.tvDialogCancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}
