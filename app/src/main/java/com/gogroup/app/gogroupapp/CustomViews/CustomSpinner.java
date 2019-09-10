package com.gogroup.app.gogroupapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by dmlabs-storage on 30/3/17.
 */

public class CustomSpinner extends android.support.v7.widget.AppCompatSpinner{
    OnItemSelectedListener listener;
    int prevPos = -1;
    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (position == getSelectedItemPosition() && prevPos == position) {
       if(getOnItemSelectedListener()!=null) {
           getOnItemSelectedListener().onItemSelected(null, null, position, 0);
       }
        }
        prevPos = position;
    }
}
