package com.gogroup.app.gogroupapp.Fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.CustomViews.CustomSpinner;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CategoryResponse;
import com.gogroup.app.gogroupapp.Seller.UserProfile;
import com.gogroup.app.gogroupapp.User.CategoryDetailUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


public class AdsFilterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    ArrayAdapter<String> spinnerAdapter;
    String[] arrCategory1, arrCategory2;
    String[] arrId1, arrId2;
    Calendar calendarStarts, calendarStartsTo, calendarEnds, calendarEndsTo;
    DateFormat dateFormatPost = new SimpleDateFormat("yyyy-MM-dd");
    boolean isStart = false, isStartFrom = false, isEndFrom = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_seller_filter, container, false);
        ButterKnife.bind(this, view);
        setIcons();
        calendarStarts = Calendar.getInstance();
        calendarStartsTo = Calendar.getInstance();
        calendarEnds = Calendar.getInstance();
        calendarEndsTo = Calendar.getInstance();
        layoutSpinner2.setVisibility(View.GONE);

        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.spinner_item));


        getCategory1();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory2(arrId1[spinner1.getSelectedItemPosition()]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return view;
    }

    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
    }


    private void getCategory1() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            RestClient.get().getCategory1(UserPreferences.getInstance().getToken()).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    arrCategory1 = new String[response.body().getData().size() + 1];
                                    arrId1 = new String[response.body().getData().size() + 1];
                                    arrCategory1[0] = getResources().getString(R.string.selectCategory);
                                    arrId1[0] = "";
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        arrCategory1[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                        arrId1[i + 1] = response.body().getData().get(i).getCategoryId();
                                    }
                                    getSpinner(arrCategory1);
                                    spinner1.setAdapter(spinnerAdapter);
                                    for (int i = 0; i < arrId1.length; i++) {
                                        if (arrId1[i].trim().equals(((CategoryDetailUser) getActivity()).postCategoryId)) {
                                            spinner1.setSelection(i);
                                            spinner1.setEnabled(false);
                                            break;
                                        }
                                    }
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            ((BaseActivity) getActivity()).hideProgressBar();
                            Utils.showShortToast(getActivity(), t.getMessage());
                        }
                    });
        }
    }

    private void getCategory2(String id) {

        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            RestClient.get().getCategory2(UserPreferences.getInstance().getToken(), id).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    layoutSpinner2.setVisibility(View.VISIBLE);
                                    if (response.body().getData().size() > 0) {
                                        arrCategory2 = new String[response.body().getData().size() + 1];
                                        arrId2 = new String[response.body().getData().size() + 1];
                                        arrCategory2[0] = getResources().getString(R.string.selectSubCategory1);
                                        arrId2[0] = "";
                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            arrCategory2[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                            arrId2[i + 1] = response.body().getData().get(i).getCategoryId();
                                        }
                                        getSpinner(arrCategory2);
                                        spinner2.setAdapter(spinnerAdapter);
                                    }


                                } else {
                                    layoutSpinner2.setVisibility(View.GONE);
                                }

                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            ((BaseActivity) getActivity()).hideProgressBar();
                            Utils.showShortToast(getActivity(), t.getMessage());
                        }
                    });
        }
    }

    @OnClick(R.id.tvStartDate)
    void setStartDateFrom() {
        isStart = true;
        isStartFrom = true;
        Date newDate = new Date();
        openCalendarStarts(newDate);
    }

    @OnClick(R.id.tvStartDateTo)
    void setStartDateTo() {
        isStart = true;
        isStartFrom = false;
        Date newDate = new Date();
        if (!tvStartDate.getText().toString().equals("")) {
            try {
                newDate = dateFormatPost.parse(tvStartDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        openCalendarStarts(newDate);
    }

    void openCalendarStarts(Date newDate) {
        Calendar calendar = isStartFrom ? calendarStarts : calendarStartsTo;
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (!isStartFrom&&!TextUtils.isEmpty(tvStartDate.getText().toString())) {
            dialog.getDatePicker().setMinDate(newDate.getTime());
        }
        dialog.show();
    }

    @OnClick(R.id.tvEndDate)
    void setEndDateFrom() {
        isStart = false;
        isEndFrom = true;
        Date newDate = new Date();
        openCalendarEnds(newDate);
    }

    @OnClick(R.id.tvEndDateTo)
    void setEndDateTo() {
        isStart = false;
        isEndFrom = false;
        Date newDate = new Date();
        if (!tvEndDate.getText().toString().equals("")) {
            try {
                newDate = dateFormatPost.parse(tvEndDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        openCalendarEnds(newDate);
    }

    void openCalendarEnds(Date newDate) {
        Calendar calendar = isEndFrom ? calendarEnds : calendarEndsTo;
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (!isEndFrom&&!TextUtils.isEmpty(tvEndDate.getText().toString())) {
            dialog.getDatePicker().setMinDate(newDate.getTime());
        }
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (isStart) {
            if (isStartFrom) {
                calendarStarts.set(Calendar.YEAR, year);
                calendarStarts.set(Calendar.MONTH, month);
                calendarStarts.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvStartDate.setText(dateFormatPost.format(calendarStarts.getTime()));
                tvStartDate.setError(null);
                if (!tvStartDateTo.getText().toString().trim().equals("")) {
                    Date sd, ed;
                    try {
                        ed = dateFormatPost.parse(tvStartDateTo.getText().toString().trim());
                        sd = dateFormatPost.parse(tvStartDate.getText().toString().trim());
                        if (sd.after(ed)) {
                            tvStartDateTo.setText("");
                            calendarStartsTo = calendarStarts;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                calendarStartsTo.set(Calendar.YEAR, year);
                calendarStartsTo.set(Calendar.MONTH, month);
                calendarStartsTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvStartDateTo.setText(dateFormatPost.format(calendarStartsTo.getTime()));
                tvStartDateTo.setError(null);

                if (!tvStartDate.getText().toString().trim().equals("")) {
                    Date sd, ed;
                    try {
                        ed = dateFormatPost.parse(tvStartDateTo.getText().toString().trim());
                        sd = dateFormatPost.parse(tvStartDate.getText().toString().trim());
                        if (sd.after(ed)) {
                            tvStartDate.setText("");
                            calendarStarts = calendarStartsTo;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (isEndFrom) {
            calendarEnds.set(Calendar.YEAR, year);
            calendarEnds.set(Calendar.MONTH, month);
            calendarEnds.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tvEndDate.setText(dateFormatPost.format(calendarEnds.getTime()));
            tvEndDate.setError(null);
            if (!tvStartDateTo.getText().toString().trim().equals("")) {
                Date sd, ed;
                try {
                    ed = dateFormatPost.parse(tvEndDateTo.getText().toString().trim());
                    sd = dateFormatPost.parse(tvEndDate.getText().toString().trim());
                    if (sd.after(ed)) {
                        tvEndDateTo.setText("");
                        calendarEndsTo = calendarStarts;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            calendarEndsTo.set(Calendar.YEAR, year);
            calendarEndsTo.set(Calendar.MONTH, month);
            calendarEndsTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tvEndDateTo.setText(dateFormatPost.format(calendarEndsTo.getTime()));
            tvEndDateTo.setError(null);

            if (!tvStartDate.getText().toString().trim().equals("")) {
                Date sd, ed;
                try {
                    ed = dateFormatPost.parse(tvEndDateTo.getText().toString().trim());
                    sd = dateFormatPost.parse(tvEndDate.getText().toString().trim());
                    if (sd.after(ed)) {
                        tvEndDate.setText("");
                        calendarEnds = calendarEndsTo;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @OnClick(R.id.btnBack)
    void back() {
        if (((CategoryDetailUser) getActivity()).drawer.isDrawerOpen(Gravity.RIGHT)) {
            ((CategoryDetailUser) getActivity()).drawer.closeDrawer(Gravity.RIGHT);

        } else {
            ((CategoryDetailUser) getActivity()).drawer.openDrawer(Gravity.RIGHT);
        }
        Utils.hideKeyboard(getActivity());
    }

    @OnClick(R.id.tvReset)
    void resetValues() {
        tvStartDate.setText("");
        tvEndDate.setText("");
        tvStartDateTo.setText("");
        tvEndDateTo.setText("");
        etLocation.setText("");
        etDealName.setText("");

        if (spinner2.getAdapter() != null) {
            spinner2.setSelection(0);
        }
        ((CategoryDetailUser) getActivity()).filterAds.clearValues();
        Utils.hideKeyboard(getActivity());


    }


    @OnClick(R.id.btnApply)
    void apply() {

        if (isValid()) {
            ((CategoryDetailUser) getActivity()).filterAds.setSellerFilterValues(tvStartDate.getText().toString(), tvStartDateTo.getText().toString(),
                    tvEndDate.getText().toString(), tvEndDateTo.getText().toString(),
                    spinner1.getAdapter() != null ? arrId1[spinner1.getSelectedItemPosition()] : "",
                    spinner2.getAdapter() != null ? arrId2[spinner2.getSelectedItemPosition()] : "", etLocation.getText().toString(),etDealName.getText().toString());
            back();
            ((CategoryDetailUser) getActivity()).userAdsFragment.getSellerAdds(false);
        }
    }

    private boolean isValid() {

        if (TextUtils.isEmpty(tvStartDate.getText().toString()) && !TextUtils.isEmpty(tvStartDateTo.getText().toString())) {
            Utils.showShortToast(getActivity(), getString(R.string.validationStartFrom));
            return false;
        } else if (!TextUtils.isEmpty(tvStartDate.getText().toString()) && TextUtils.isEmpty(tvStartDateTo.getText().toString())) {
            Utils.showShortToast(getActivity(), getString(R.string.validationStartTo));
            return false;
        } else if (TextUtils.isEmpty(tvEndDate.getText().toString()) && !TextUtils.isEmpty(tvEndDateTo.getText().toString())) {
            Utils.showShortToast(getActivity(), getString(R.string.validationEndFrom));
            return false;
        } else if (!TextUtils.isEmpty(tvEndDate.getText().toString()) && TextUtils.isEmpty(tvEndDateTo.getText().toString())) {
            Utils.showShortToast(getActivity(), getString(R.string.validationEndTo));
            return false;
        }
        return true;

    }

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.layoutSpinner2)
    LinearLayout layoutSpinner2;
    @BindView(R.id.spinner1)
    CustomSpinner spinner1;
    @BindView(R.id.spinner2)
    CustomSpinner spinner2;
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.tvStartDateTo)
    TextView tvStartDateTo;
    @BindView(R.id.tvEndDateTo)
    TextView tvEndDateTo;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;

    @BindView(R.id.etDealName)
    EditText etDealName;

    public void getSpinner(String[] values) {
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, values) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return true;
                } else {
                    return true;
                }
            }
        };
    }

}
