package linc.com.alarmclockforprogrammers.model.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesAlarm {

    // todo replace hardcoded IDs to constants

    private Context context;

    public PreferencesAlarm(Context context) {
        this.context = context;
    }

    /** Questions*/
    public void saveLocalQuestionsVersion(String version) {
        getPrefsEditor().putString("LOCAL_QUESTIONS_VERSION", version)
                .apply();
    }

    public String getLocalQuestionsVersion() {
        return getDefaultPreferences().getString("LOCAL_QUESTIONS_VERSION", "0");
    }

    /** Achievements*/
    public void saveLocalAchievementsVersion(String version) {
        getPrefsEditor().putString("LOCAL_ACHIEVEMENTS_VERSION", version)
                .apply();
    }

    public String getLocalAchievementsVersion() {
        return getDefaultPreferences().getString("LOCAL_ACHIEVEMENTS_VERSION", "0");
    }


    /** Balance*/
    public void saveBalance(int balance) {
        getPrefsEditor().putInt("BALANCE", balance)
                .apply();
    }

    public int getBalance() {
        return getDefaultPreferences().getInt("BALANCE", 20);
    }

    private SharedPreferences.Editor getPrefsEditor(){
        return PreferenceManager
                .getDefaultSharedPreferences(this.context)
                .edit();
    }

    private SharedPreferences getDefaultPreferences(){
        return PreferenceManager
                .getDefaultSharedPreferences(this.context);
    }

}
