package com.mrapocalypse.purplesprite3.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SlimSeekBarPreference;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrapocalypse.purplesprite3.R;

//import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.util.cm.PowerMenuConstants;

import static com.android.internal.util.cm.PowerMenuConstants.*;
import com.mrapocalypse.purplesprite3.preferences.NumberPickerPreference;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Powermenu extends PreferenceFragment implements Preference.OnPreferenceChangeListener {


    final static String TAG = "PowerMenuActions";

    private static final String PREF_ON_THE_GO_ALPHA = "on_the_go_alpha";
    private static final String SCREENSHOT_DELAY = "screenshot_delay";
    private SwitchPreference mRebootPref;
    private SwitchPreference mScreenshotPref;
    private SwitchPreference mScreenrecordPref;
    private SwitchPreference mAirplanePref;
    //private SwitchPreference mUsersPref;
    private SwitchPreference mSettingsPref;
    private SwitchPreference mLockdownPref;
    private SwitchPreference mBugReportPref;
    private SwitchPreference mSilentPref;
    private SlimSeekBarPreference mOnTheGoAlphaPref;

    Context mContext;
    private ArrayList<String> mLocalUserConfig = new ArrayList<String>();
    private String[] mAvailableActions;
    private String[] mAllActions;

    private NumberPickerPreference mScreenshotDelay;

    private ContentResolver mCr;
    private PreferenceScreen mPrefSet;

    private static final int MIN_DELAY_VALUE = 1;
    private static final int MAX_DELAY_VALUE = 30;
	
	public Powermenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_powermenu);
        mContext = getActivity().getApplicationContext();

        mPrefSet = getPreferenceScreen();

        mCr = getActivity().getContentResolver();

        mAvailableActions = getActivity().getResources().getStringArray(
                R.array.power_menu_actions_array);
        mAllActions = PowerMenuConstants.getAllActions();

        for (String action : mAllActions) {
        // Remove preferences not present in the overlay
            if (!isActionAllowed(action)) {
                getPreferenceScreen().removePreference(findPreference(action));
                continue;
            }

            if (action.equals(GLOBAL_ACTION_KEY_REBOOT)) {
                mRebootPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_REBOOT);
            } else if (action.equals(GLOBAL_ACTION_KEY_SCREENSHOT)) {
                mScreenshotPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_SCREENSHOT);
            } else if (action.equals(GLOBAL_ACTION_KEY_SCREENRECORD)) {
                mScreenrecordPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_SCREENRECORD);
            } else if (action.equals(GLOBAL_ACTION_KEY_AIRPLANE)) {
                mAirplanePref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_AIRPLANE);
            //} else if (action.equals(GLOBAL_ACTION_KEY_USERS)) {
            //    mUsersPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_USERS);
            } else if (action.equals(GLOBAL_ACTION_KEY_SETTINGS)) {
                mSettingsPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_SETTINGS);
            } else if (action.equals(GLOBAL_ACTION_KEY_LOCKDOWN)) {
                mLockdownPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_LOCKDOWN);
            } else if (action.equals(GLOBAL_ACTION_KEY_BUGREPORT)) {
                mBugReportPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_BUGREPORT);
            } else if (action.equals(GLOBAL_ACTION_KEY_SILENT)) {
                mSilentPref = (SwitchPreference) findPreference(GLOBAL_ACTION_KEY_SILENT);
            }
        }

        mOnTheGoAlphaPref = (SlimSeekBarPreference) findPreference(PREF_ON_THE_GO_ALPHA);
        mOnTheGoAlphaPref.setDefault(50);
        mOnTheGoAlphaPref.setInterval(1);
        mOnTheGoAlphaPref.setOnPreferenceChangeListener(this);

        mScreenshotDelay = (NumberPickerPreference) mPrefSet.findPreference(
                SCREENSHOT_DELAY);
        mScreenshotDelay.setOnPreferenceChangeListener(this);
        mScreenshotDelay.setMinValue(MIN_DELAY_VALUE);
        mScreenshotDelay.setMaxValue(MAX_DELAY_VALUE);
        int ssDelay = Settings.System.getInt(mCr,
                Settings.System.SCREENSHOT_DELAY, 1);
        mScreenshotDelay.setCurrentValue(ssDelay);

        //getUserConfig();

    }
	
	@Override
    public void onStart() {
        super.onStart();

        if (mRebootPref != null) {
            mRebootPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_REBOOT));
        }

        if (mScreenshotPref != null) {
            mScreenshotPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_SCREENSHOT));
        }

        if (mScreenrecordPref != null) {
            mScreenrecordPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_SCREENRECORD));
        }

        if (mAirplanePref != null) {
            mAirplanePref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_AIRPLANE));
        }
		
		/*
        if (mUsersPref != null) {
            if (!UserHandle.MU_ENABLED || !UserManager.supportsMultipleUsers()) {
                getPreferenceScreen().removePreference(findPreference(GLOBAL_ACTION_KEY_USERS));
            } else {
                List<UserInfo> users = ((UserManager) mContext.getSystemService(
                        Context.USER_SERVICE)).getUsers();
                boolean enabled = (users.size() > 1);
                mUsersPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_USERS) && enabled);
                mUsersPref.setEnabled(enabled);
            }
        }
		*/

        if (mSettingsPref != null) {
            mSettingsPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_SETTINGS));
        }

        if (mLockdownPref != null) {
            mLockdownPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_LOCKDOWN));
        }

        if (mBugReportPref != null) {
            mBugReportPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_BUGREPORT));
        }

        if (mSilentPref != null) {
            mSilentPref.setChecked(settingsArrayContains(GLOBAL_ACTION_KEY_SILENT));
        }

        updatePreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePreferences();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mRebootPref) {
            value = mRebootPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_REBOOT);

        } else if (preference == mScreenshotPref) {
            value = mScreenshotPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_SCREENSHOT);

        } else if (preference == mScreenrecordPref) {
            value = mScreenrecordPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_SCREENRECORD);

        } else if (preference == mAirplanePref) {
            value = mAirplanePref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_AIRPLANE);
		/*
        } else if (preference == mUsersPref) {
            value = mUsersPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_USERS);
		*/
        } else if (preference == mSettingsPref) {
            value = mSettingsPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_SETTINGS);

        } else if (preference == mLockdownPref) {
            value = mLockdownPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_LOCKDOWN);

        } else if (preference == mBugReportPref) {
            value = mBugReportPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_BUGREPORT);

        } else if (preference == mSilentPref) {
            value = mSilentPref.isChecked();
            updateUserConfig(value, GLOBAL_ACTION_KEY_SILENT);

        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mOnTheGoAlphaPref) {
            float val = Float.parseFloat((String) newValue);
            Settings.System.putFloat(mCr, Settings.System.ON_THE_GO_ALPHA,
                    val / 100);
            return true;
        } else if (preference == mScreenshotDelay) {
            int value = Integer.parseInt(newValue.toString());
            Settings.System.putInt(mCr, Settings.System.SCREENSHOT_DELAY,
                    value);
            return true;
        }
        return false;
    }

    private boolean settingsArrayContains(String preference) {
        return mLocalUserConfig.contains(preference);
    }

    private boolean isActionAllowed(String action) {
        if (Arrays.asList(mAvailableActions).contains(action)) {
            return true;
        }
        return false;
    }
	
	
    private void updateUserConfig(boolean enabled, String action) {
        if (enabled) {
            if (!settingsArrayContains(action)) {
                mLocalUserConfig.add(action);
            }
        } else {
            if (settingsArrayContains(action)) {
                mLocalUserConfig.remove(action);
            }
        }
        saveUserConfig();
    }

    private void updatePreferences() {
        boolean bugreport = Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.BUGREPORT_IN_POWER_MENU, 0) != 0;
        if (mBugReportPref != null) {
            mBugReportPref.setEnabled(bugreport);
            if (bugreport) {
                mBugReportPref.setSummary(null);
            } else {
                mBugReportPref.setSummary(R.string.power_menu_bug_report_disabled);
            }
        }
    }

    private void getUserConfig() {
        mLocalUserConfig.clear();
        String[] defaultActions;
        String savedActions = Settings.Global.getStringForUser(mContext.getContentResolver(),
                Settings.Global.POWER_MENU_ACTIONS, UserHandle.USER_CURRENT);

        if (savedActions == null) {
            defaultActions = mContext.getResources().getStringArray(
                    com.android.internal.R.array.config_globalActionsList);
            for (String action : defaultActions) {
                mLocalUserConfig.add(action);
            }
        } else {
            for (String action : savedActions.split("\\|")) {
                mLocalUserConfig.add(action);
            }
        }
    }

    private void saveUserConfig() {
        StringBuilder s = new StringBuilder();

        // TODO: Use DragSortListView
        ArrayList<String> setactions = new ArrayList<String>();
        for (String action : mAllActions) {
            if (settingsArrayContains(action) && isActionAllowed(action)) {
                setactions.add(action);
            } else {
                continue;
            }
        }

        for (int i = 0; i < setactions.size(); i++) {
            s.append(setactions.get(i).toString());
            if (i != setactions.size() - 1) {
                s.append("|");
            }
        }

        Settings.Global.putStringForUser(getActivity().getContentResolver(),
                 Settings.Global.POWER_MENU_ACTIONS, s.toString(), UserHandle.USER_CURRENT);
        updatePowerMenuDialog();
    }

    private void updatePowerMenuDialog() {
        Intent u = new Intent();
        u.setAction(Intent.UPDATE_POWER_MENU);
        mContext.sendBroadcastAsUser(u, UserHandle.ALL);
    }
}
