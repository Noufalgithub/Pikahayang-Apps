package pikahayang.pikahayang.ServerSide;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class UserSession {

    private final SharedPreferences appPreference;

    public UserSession(Context context) {
        appPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setSession (String key, String value){
        SharedPreferences.Editor editor = appPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearSession (){
        SharedPreferences.Editor editor = appPreference.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public String getSession(String key){
        String value = appPreference.getString(key, "");
        if (TextUtils.isEmpty(value)){
            return "";
        }
        return value;
    }

    public void setIsLogin(boolean stat){
        SharedPreferences.Editor editor = appPreference.edit();
        editor.putBoolean("IS_LOGIN", stat);
        editor.apply();
    }

    public boolean getIsLogin() {
        boolean isLogin = appPreference.getBoolean("IS_LOGIN", false);
        return isLogin;
    }

    public String getId(){
        return getSession("id");
    }
    public void setId(String id){
        setSession("id", id);
    }
    public String getName(){
        return getSession("name");
    }
    public void setName(String name){
        setSession("name", name);
    }
    public String getEmail(){
        return getSession("email");
    }
    public void setEmail(String email){
        setSession("email", email);
    }
    public String getAlamat(){
        return getSession("alamat");
    }
    public void setAlamat(String alamat){
        setSession("alamat", alamat);
    }
    public String getNoHp(){
        return getSession("no_hp");
    }
    public void setNoHp(String noHp){
        setSession("no_hp", noHp);
    }

    public String getToken(){
        return getSession("token");
    }
    public void setToken(String token){
        setSession("token", token);
    }

}
