package pikahayang.pikahayang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pd.chocobar.ChocoBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.VolleyHandler;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.etNamaRegis)
    AppCompatEditText etNamaRegis;
    @BindView(R.id.etEmailRegis)
    AppCompatEditText etEmailRegis;
    @BindView(R.id.etPasswordRegis)
    AppCompatEditText etPasswordRegis;
    @BindView(R.id.etConfirmPassRegis)
    AppCompatEditText etConfirmPassRegis;
    @BindView(R.id.ibCrossXNamaRegis)
    ImageButton ibCrossXNamaRegis;
    @BindView(R.id.ibCrossXEmailRegis)
    ImageButton ibCrossXEmailRegis;
    @BindView(R.id.ibCrossXPasswordRegis)
    ImageButton ibCrossXPasswordRegis;
    @BindView(R.id.ibCrossXConfirmPassRegis)
    ImageButton ibCrossXConfirmPassRegis;
    @BindView(R.id.btnRegis)
    Button btnRegis;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.etAlamatRegis)
    AppCompatEditText etAlamatRegis;
    @BindView(R.id.ibCrossXAlamatRegis)
    ImageButton ibCrossXAlamatRegis;
    @BindView(R.id.ibCrossXNoHpRegis)
    ImageButton ibCrossXNoHpRegis;
    @BindView(R.id.etNoHpRegis)
    AppCompatEditText etNoHpRegis;

    public static final String TAG_NAMA = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_NO_HP = "no_hp";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_CONFIRM_PASSWORD = "confirm_password";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_SUCCESS = "success";

    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;

    String newToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegisterActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

            }
        });


        ibCrossXNamaRegis.setVisibility(View.GONE);
        ibCrossXEmailRegis.setVisibility(View.GONE);
        ibCrossXNoHpRegis.setVisibility(View.GONE);
        ibCrossXAlamatRegis.setVisibility(View.GONE);
        ibCrossXPasswordRegis.setVisibility(View.GONE);
        ibCrossXConfirmPassRegis.setVisibility(View.GONE);


        etNamaRegis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibCrossXNamaRegis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmailRegis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibCrossXEmailRegis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNoHpRegis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibCrossXNoHpRegis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAlamatRegis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibCrossXAlamatRegis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPasswordRegis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibCrossXPasswordRegis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etConfirmPassRegis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibCrossXConfirmPassRegis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.ibCrossXNamaRegis)
    public void hapusNama(){
        etNamaRegis.getText().clear();
    }
    @OnClick(R.id.ibCrossXEmailRegis)
    public void hapusEmail(){
        etEmailRegis.getText().clear();
    }
    @OnClick(R.id.ibCrossXNoHpRegis)
    public void hapusNoHp(){
        etNoHpRegis.getText().clear();
    }
    @OnClick(R.id.ibCrossXAlamatRegis)
    public void hapusAlamat(){
        etAlamatRegis.getText().clear();
    }
    @OnClick(R.id.ibCrossXPasswordRegis)
    public void hapusPassword(){
        etPasswordRegis.getText().clear();
    }
    @OnClick(R.id.ibCrossXConfirmPassRegis)
    public void hapusConfirmPassword(){
        etConfirmPassRegis.getText().clear();
    }

    @OnClick(R.id.btnRegis)
    public void daftar(final View view){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.register, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("REGISTER", response);
                    hideDialog();
                    try {
                        JSONObject datasRegister = new JSONObject(response);
                        if (datasRegister.getInt(TAG_SUCCESS) == 1) {
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                            dialog.setIcon(R.drawable.ic_info)
                                    .setTitle("INFORMASI")
                                    .setCancelable(false)
                                    .setMessage(datasRegister.getString(TAG_MESSAGE))
                                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            etNamaRegis.getText().clear();
                                            etEmailRegis.getText().clear();
                                            etNoHpRegis.getText().clear();
                                            etAlamatRegis.getText().clear();
                                            etPasswordRegis.getText().clear();
                                            etConfirmPassRegis.getText().clear();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }
                                    });
                            dialog.show();
                        } else {
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                            dialog.setIcon(R.drawable.ic_info)
                                    .setCancelable(false)
                                    .setTitle("INFORMASI")
                                    .setMessage(datasRegister.getString(TAG_MESSAGE))
                                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            dialog.show();
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
                        Log.e("Error", error.getMessage());
                        hideDialog();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams()throws AuthFailureError {

                    Map<String, String> paramsRegister = new HashMap<>();

                    paramsRegister.put(TAG_NAMA, etNamaRegis.getText().toString().trim());
                    paramsRegister.put(TAG_EMAIL, etEmailRegis.getText().toString().trim());
                    paramsRegister.put(TAG_NO_HP, etNoHpRegis.getText().toString().trim());
                    paramsRegister.put(TAG_ALAMAT, etAlamatRegis.getText().toString().trim());
                    paramsRegister.put(TAG_PASSWORD, etPasswordRegis.getText().toString().trim());
                    paramsRegister.put(TAG_TOKEN, newToken);
                    paramsRegister.put(TAG_CONFIRM_PASSWORD, etConfirmPassRegis.getText().toString().trim());

                    return paramsRegister;
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

    @OnClick(R.id.btnBack)
    public void pindah(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
