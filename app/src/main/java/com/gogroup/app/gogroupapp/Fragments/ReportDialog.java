package com.gogroup.app.gogroupapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackReport;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by atinderpalsingh on 1/16/18.
 */

public class ReportDialog extends DialogFragment {

    private static CallBackReport listener;

    final List<String> list = new ArrayList<String>();

    public static ReportDialog instance(CallBackReport callBackReport) {
        listener = callBackReport;

        return new ReportDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_report, container);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        setSpinner(spinner);
        layoutOther.setVisibility(View.GONE);

        return view;
    }

    void setSpinner(final Spinner spinner) {
        list.add(getString(R.string.selectReason));
        list.add(getString(R.string.report1));
        list.add(getString(R.string.report2));
        list.add(getString(R.string.report3));
        list.add(getString(R.string.report4));
        list.add(getString(R.string.report5));
        list.add(getString(R.string.otherReason));
        final List<String> spinnerList = new ArrayList<String>();

        spinnerList.add(getString(R.string.selectReason));
        for (int i = 1; i < list.size(); i++) {
            spinnerList.add(i + ". " + list.get(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, spinnerList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        dataAdapter.setDropDownViewResource(R.layout.spinner_item2);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                layoutOther.setVisibility(i == 6 ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.tv_cancel)
    void cancel() {
        getDialog().dismiss();
    }

    @OnClick(R.id.tv_ok)
    void submit() {

        String reason;
        if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.reportValidation), Toast.LENGTH_LONG).show();
        } else if (spinner.getSelectedItemPosition() == 6 && etComment.getText().toString().trim().isEmpty()) {
            Utils.setEditTextError(etComment, getResources().getString(R.string.validationComment));

        } else {

            if (spinner.getSelectedItemPosition() < 6) {
                reason = list.get(spinner.getSelectedItemPosition());
            } else {
                reason = etComment.getText().toString();
            }
            listener.onClickReportSubmit(reason);

            getDialog().dismiss();
        }
    }

    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.layoutOther)
    LinearLayout layoutOther;
}
