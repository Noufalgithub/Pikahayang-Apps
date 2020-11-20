package pikahayang.pikahayang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pd.chocobar.ChocoBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class SettingAkun extends AppCompatActivity {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    ProgressDialog progressDialog;

    private static String TAG_SUCCESS = "success";
    private static String TAG_ID_USERS = "id_users";
    private static String TAG_NAME = "name";
    private static String TAG_EMAIL = "email";
    private static String TAG_ALAMAT = "alamat";
    private static String TAG_NO_HP = "no_hp";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.ibClearNama)
    ImageButton ibClearNama;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.ibClearEmail)
    ImageButton ibClearEmail;
    @BindView(R.id.etAlamat)
    EditText etAlamat;
    @BindView(R.id.ibClearAlamat)
    ImageButton ibClearAlamat;
    @BindView(R.id.etNoHp)
    EditText etNoHp;
    @BindView(R.id.ibClearNoHp)
    ImageButton ibClearNoHp;
    @BindView(R.id.rlBottom)
    RelativeLayout rlBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_akun);
        ButterKnife.bind(this);

        etNama.setText(new UserSession(getApplicationContext()).getName());
        etEmail.setText(new UserSession(getApplicationContext()).getEmail());
        etAlamat.setText(new UserSession(getApplicationContext()).getAlamat());
        etNoHp.setText(new UserSession(getApplicationContext()).getNoHp());

        ibClearNama.setVisibility(View.INVISIBLE);
        ibClearEmail.setVisibility(View.INVISIBLE);
        ibClearAlamat.setVisibility(View.INVISIBLE);
        ibClearNoHp.setVisibility(View.INVISIBLE);
        btnSimpan.setBackgroundResource(R.drawable.button_nonaktif);
        btnSimpan.setEnabled(false);

        etNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ibClearNama.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSimpan.setBackgroundResource(R.color.hitam);
                btnSimpan.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ibClearEmail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSimpan.setBackgroundResource(R.color.hitam);
                btnSimpan.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAlamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ibClearAlamat.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSimpan.setBackgroundResource(R.color.hitam);
                btnSimpan.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNoHp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ibClearNoHp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSimpan.setBackgroundResource(R.color.hitam);
                btnSimpan.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick({R.id.ibClearNama, R.id.ibClearEmail, R.id.ibClearAlamat, R.id.ibClearNoHp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibClearNama:
                etNama.setText(null);
                break;
            case R.id.ibClearEmail:
                etEmail.setText(null);
                break;
            case R.id.ibClearAlamat:
                etAlamat.setText(null);
                break;
            case R.id.ibClearNoHp:
                etNoHp.setText(null);
                break;
        }
    }

    @OnClick(R.id.btnSimpan)
    public void updateAkun(final View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.edit_akun + "?id=" + new UserSession(getApplicationContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("EDIT AKUN", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {

                        new UserSession(getApplicationContext()).setName(etNama.getText().toString().trim());
                        new UserSession(getApplicationContext()).setEmail(etEmail.getText().toString().trim());
                        new UserSession(getApplicationContext()).setAlamat(etAlamat.getText().toString().trim());
                        new UserSession(getApplicationContext()).setNoHp(etNoHp.getText().toString().trim());

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(SettingAkun.this);
                        dialog.setMessage("Sukses Update Akun")
                                .setCancelable(false)
                                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                        finish();
                                    }
                                });
                        dialog.show();

                    } else {
                        ChocoBar.builder()
                                .setText("ERROR!")
                                .centerText()
                                .setActivity(SettingAkun.this)
                                .setDuration(ChocoBar.LENGTH_INDEFINITE)
                                .setActionText(android.R.string.ok)
                                .red()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyHandler.handleVolleyError(view, error);
                Log.e("Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_NAME, etNama.getText().toString().trim());
                params.put(TAG_EMAIL, etEmail.getText().toString().trim());
                params.put(TAG_ALAMAT, etAlamat.getText().toString().trim());
                params.put(TAG_NO_HP, etNoHp.getText().toString().trim());

                return params;
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
