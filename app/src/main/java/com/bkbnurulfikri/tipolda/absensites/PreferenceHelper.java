package com.bkbnurulfikri.tipolda.absensites;

/**
 * Created by Irwan.
 */
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class PreferenceHelper {

    private final String INTRO = "intro";
    private final String NAMA = "nama";
    private final String KODE = "kodelokasi";
    private final String LEVEL = "level";
    private final String IDNF = "nonf";
    private final String KODEH = "kodelokasi";
    private final String EXPIRES = "expires";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.commit();
    }
    public boolean getIsLogin() {
        //return app_prefs.getBoolean(INTRO, false);

        Date currentDate = new Date();
        long millis = app_prefs.getLong(EXPIRES, 0);
        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
            //return app_prefs.getBoolean(INTRO, false);
        }
        Date expiryDate = new Date(millis);
        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    public void putName(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAMA, loginorout);
        edit.commit();
    }
    public String getName() {
        return app_prefs.getString(NAMA, "");
    }

    public void putKodeLokasi(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(KODE, loginorout);
        edit.commit();
    }
    public String getKodeLokasi() {
        return app_prefs.getString(KODE, "");
    }

    public void putLevel(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LEVEL, loginorout);
        edit.commit();
    }
    public String getLevel() {
        return app_prefs.getString(LEVEL, "");
    }

    public void putIdnf(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(IDNF, loginorout);
        edit.commit();
    }
    public String getIdnf() {
        return app_prefs.getString(IDNF, "");
    }

    public void putKodeh(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(KODEH, loginorout);
        edit.commit();
    }
    public String getKodeh() {
        return app_prefs.getString(KODEH, "");
    }

    public void loginExpired() {
        Date date = new Date();
        //Set user session for next 7 days (7 * 24 * 60 * 60 * 1000)
        long millis = date.getTime() + (5 * 60 * 60 * 1000);
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putLong(EXPIRES, millis);
        edit.commit();
    }
 }
