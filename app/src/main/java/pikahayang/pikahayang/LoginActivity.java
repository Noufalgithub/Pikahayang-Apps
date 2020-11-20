package pikahayang.pikahayang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.pd.chocobar.ChocoBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.app.AppController;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.ServerSide.VolleyHandler;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etEmailLogin)
    AppCompatEditText etEmailLogin;
    @BindView(R.id.etPasswordLogin)
    AppCompatEditText etPasswordLogin;
    @BindView(R.id.btnMasuk)
    Button btnMasuk;

    public static final String TAG_ID_USERS = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NO_HP = "no_hp";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_SUCCESS = "success";
    public static final String my_shared_preferences = "my_shared_preferences";

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    Boolean session = false;

    String id_users, name, email, alamat, noHape, token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Cek session login jika TRUE maka langsung ke Dashboard
        UserSession userSession = new UserSession(getApplicationContext());
        sharedPreferences = getBaseContext().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = userSession.getIsLogin();
        String id_users = sharedPreferences.getString(TAG_ID_USERS, null);
        if (session){
            Intent intent = new Intent(LoginActivity.this, BerandaActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void pindahkedaftar(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void pindahkelupapassword(View view) {
        Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnMasuk)
    public void login(final View view){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Sukses Login!", response);
                    hideDialog();
                    try {
                        JSONObject datasLogin = new JSONObject(response);
                        int success = datasLogin.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            ChocoBar.builder().setActivity(LoginActivity.this)
                                    .setText(datasLogin.getString(TAG_MESSAGE))
                                    .setDuration(ChocoBar.LENGTH_SHORT)
                                    .setIcon(R.drawable.ic_check_white)
                                    .setTextColor(getResources().getColor(R.color.putih))
                                    .green()  // in built green ChocoBar
                                    .show();
//                            Snackbar.make(findViewById(android.R.id.content), datasLogin.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG).show();

                            id_users = datasLogin.getString(TAG_ID_USERS);
                            name = datasLogin.getString(TAG_NAME);
                            email = datasLogin.getString(TAG_EMAIL);
                            alamat = datasLogin.getString(TAG_ALAMAT);
                            noHape = datasLogin.getString(TAG_NO_HP);
                            token = datasLogin.getString(TAG_TOKEN);

                            // Menyimpan ke SESSION
                            UserSession x = new UserSession(getApplicationContext());
                            x.setIsLogin(true);
                            x.setId(id_users);
                            x.setName(name);
                            x.setEmail(email);
                            x.setAlamat(alamat);
                            x.setNoHp(noHape);
                            x.setToken(token);

                            finish();
                        } else {
                            ChocoBar.builder().setActivity(LoginActivity.this)
                                    .setText(datasLogin.getString(TAG_MESSAGE))
                                    .setDuration(ChocoBar.LENGTH_LONG)
                                    .setIcon(getResources().getDrawable(R.drawable.ic_info_putih))
                                    .red()  // in built green ChocoBar
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null){
                        VolleyHandler.handleVolleyError(view, error);
                        Log.e("Error!", error.getMessage());
                        hideDialog();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> paramsLogin = new HashMap<>();
                    paramsLogin.put(TAG_EMAIL, etEmailLogin.getText().toString().trim());
                    paramsLogin.put(TAG_PASSWORD, etPasswordLogin.getText().toString().trim());

                    return paramsLogin;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
