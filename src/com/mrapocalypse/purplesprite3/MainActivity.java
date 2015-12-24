package com.mrapocalypse.purplesprite3;

import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrapocalypse.purplesprite3.fragments.*;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    DrawerLayout dlDrawer;
    NavigationView nvDrawer;
    ActionBarDrawerToggle drawerToggle;

    TextView welcomeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lock the orientation on portrait
        //We're not ready to rotation on this yet...
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Find our drawer view
        //dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        //Initialize welcome text
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setVisibility(View.VISIBLE);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Depending on what's selected, launch that fragment in place of the default layout

        switch(menuItem.getItemId()) {

            case R.id.ui:
                UI uiFragment = new UI();
                FragmentTransaction uiTransaction = getFragmentManager().beginTransaction();
                uiTransaction.replace(R.id.flContent, uiFragment, "UI");
                uiTransaction.commit();
                break;

            case R.id.notifications:
                Notifications notificationFragment = new Notifications();
                FragmentTransaction notifTransaction = getFragmentManager().beginTransaction();
                notifTransaction.replace(R.id.flContent, notificationFragment, "NOTIFICATIONS");
                notifTransaction.commit();
                break;

            case R.id.sounds:
                Sounds soundsFragment = new Sounds();
                FragmentTransaction soundsTransaction = getFragmentManager().beginTransaction();
                soundsTransaction.replace(R.id.flContent, soundsFragment, "SOUNDS");
                soundsTransaction.commit();
                break;

            case R.id.lockscreen:
                Lockscreen lockFragment = new Lockscreen();
                FragmentTransaction lockTransaction = getFragmentManager().beginTransaction();
                lockTransaction.replace(R.id.flContent, lockFragment, "LOCKSCREEN");
                lockTransaction.commit();
                break;

            case R.id.navigation:
                Navigation navFragment = new Navigation();
                FragmentTransaction navTransaction = getFragmentManager().beginTransaction();
                navTransaction.replace(R.id.flContent, navFragment, "NAVIGATION");
                navTransaction.commit();
                break;

            case R.id.statusbar:
                Statusbar statusbarFragment = new Statusbar();
                FragmentTransaction statTransaction = getFragmentManager().beginTransaction();
                statTransaction.replace(R.id.flContent, statusbarFragment, "STATUSBAR");
                statTransaction.commit();
                break;

            case R.id.recents:
                Recents recFragment = new Recents();
                FragmentTransaction recTransaction = getFragmentManager().beginTransaction();
                recTransaction.replace(R.id.flContent, recFragment, "RECENTS");
                recTransaction.commit();
                break;

            case R.id.powermenu:
                Powermenu powerFragment = new Powermenu();
                FragmentTransaction powerTransaction = getFragmentManager().beginTransaction();
                powerTransaction.replace(R.id.flContent, powerFragment, "POWERMENU");
                powerTransaction.commit();
                break;

            case R.id.lights:
                Lights lightsFragment = new Lights();
                FragmentTransaction lightsTransaction = getFragmentManager().beginTransaction();
                lightsTransaction.replace(R.id.flContent, lightsFragment, "LIGHTS");
                lightsTransaction.commit();
                break;
            default:
                break;

        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        getSupportActionBar().setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();

        welcomeText.setVisibility(View.GONE); //No matter what's selected, kill the welcome text
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //When the user presses the back button, open the drawer if it isn't open already.
    //If the drawer is already open when the user presses back, close the app.
    @Override
    public void onBackPressed() {
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START);
            Snackbar.make(findViewById(R.id.flContent), R.string.press_back_again, Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            super.onBackPressed();
        }
    }


}
