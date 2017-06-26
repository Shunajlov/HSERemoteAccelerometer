package com.ihavenodomain.hseremoteaccelerometer.data.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Для хранения чего-либо в SharedPreferences
 */
public class PreferencesManager {
    private static final String LOGIN = "user_name"; // имя пользователя
    private static final String PASSWORD = "user_password";
    private static final String DB_NAME = "db_name";
    private static final String IP_ADDRESS = "ip_address";
    private static final String INTERVAL = "send_interval";

    private Context ctx;
    private int mode = Context.MODE_PRIVATE;
//    private String prefPath = ctx.getString(R.string.preferencesPath);

    public PreferencesManager(Context ctx) {
        this.ctx = ctx;
    }

    public String getLogin() {
        return readStringProperty(LOGIN);
    }

    public void setLogin(String login) {
        saveStringProperty(LOGIN, login);
    }

    public String getPassword() {
        return readStringProperty(PASSWORD);
    }

    public void setPassword(String password) {
        saveStringProperty(PASSWORD, password);
    }

    public String getDbName() {
        return readStringProperty(DB_NAME);
    }

    public void setDbName(String dbName) {
        saveStringProperty(DB_NAME, dbName);
    }

    public String getIpAddress() {
        return readStringProperty(IP_ADDRESS);
    }

    public void setIpAddress(String ipAddress) {
        saveStringProperty(IP_ADDRESS, ipAddress);
    }

    public String getInterval() {
        return readStringProperty(INTERVAL);
    }

    public void setInterval(String interval) {
        saveStringProperty(INTERVAL, interval);
    }

    /**
     * Сохранить в настройках строковое значение
     * @param tag идентификатор, по которому потом можно прочитать настройку
     * @param str значение, которое нужно сохранить
     */
    private void saveStringProperty(String tag, String str) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        //((AppCompatActivity) ctx).getPreferences(mode);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(tag, str);
        editor.apply();
    }

    /**
     * Прочитать из настроек строковое значение
     * @param tag идентификатор, по которому определяется нужное значение
     * @return хранимая строка
     */
    private String readStringProperty(String tag) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        //((AppCompatActivity) ctx).getPreferences(mode);
        return pref.getString(tag, "");
    }
}
