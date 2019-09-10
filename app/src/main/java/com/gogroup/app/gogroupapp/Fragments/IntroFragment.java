package com.gogroup.app.gogroupapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroFragment extends Fragment {


    ListResponse item;

    public static IntroFragment newInstance(ListResponse listResponse) {
        IntroFragment frag = new IntroFragment();
        Bundle b = new Bundle();
        b.putParcelable(BaseActivity.DATA, listResponse);
        frag.setArguments(b);
        return frag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(BaseActivity.DATA)) {
            item = getArguments().getParcelable(BaseActivity.DATA);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.single_image, container, false);
        ButterKnife.bind(this, view);
        Utils.picasso(getActivity(), item.getImageName(), imageView);
        return view;
    }






    @BindView(R.id.image)
    ImageView imageView;
}
