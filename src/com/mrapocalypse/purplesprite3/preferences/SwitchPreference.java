package com.mrapocalypse.purplesprite3.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

import com.mrapocalypse.purplesprite3.R;

/**
 * Created by cedwards on 12/16/2015.
 */
public class SwitchPreference extends BaseSetting implements OnClickListener {

    Switch mSwitch;

    String aDescriptionOn, aDescriptionOff;

    Boolean mChecked = false;


    public SwitchPreference(Context context) {
        this(context, null);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {

            TypedArray typedArray = null;
            try {
                typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckboxSetting);

                mChecked = Boolean.parseBoolean(getDefaultValue());
                aDescriptionOn = typedArray.getString(R.styleable.CheckboxSetting_descriptionOn);
                aDescriptionOff = typedArray.getString(R.styleable.CheckboxSetting_descriptionOff);
            } finally {
                if (typedArray != null) {
                    typedArray.recycle();
                }
            }
        }

        /**
         * Inflate Views
         */
        addView(View.inflate(context, R.layout.setting_switch, mRootView));
        mSwitch = (Switch) findViewById(R.id.the_switch);


        /**
         * Setup initial logic
         */
        updateSummary();

        // The default value of a boolean setting is usually stored as 1 or 0, but support "true" and "false" values
        if (getValue() == null && getDefaultValue() != null) {

            // if this key is not present in the table, try to use the defualt value supplied
            mChecked = Boolean.valueOf(getDefaultValue()) || getDefaultValue().equals("1");

        } else if (getValue() != null) {

            mChecked = getValue().equals("1");

        }
        mSwitch.setChecked(mChecked);

        setOnClickListener(this);
        setFocusable(true);
    }

    private void updateSummary() {
        if (getCurrentSummary() == null) {
            // no summary is set, so let's use the descriptions if we can
            if (aDescriptionOff != null || aDescriptionOn != null) {
                setSummary(isChecked() ? aDescriptionOn : aDescriptionOff);
            } else {
                setSummary(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        setChecked(!isChecked());
        updateSummary();
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        mSwitch.setChecked(checked);
        setValue(checked ? "1" : "0");
    }

    public boolean isChecked() {
        return mChecked;
    }
}
