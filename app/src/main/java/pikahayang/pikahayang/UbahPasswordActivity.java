package pikahayang.pikahayang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class UbahPasswordActivity extends AppCompatActivity {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.etPassLama)
    EditText etPassLama;
    @BindView(R.id.etPassBaru)
    EditText etPassBaru;
    @BindView(R.id.etPassKonfirm)
    EditText etPassKonfirm;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private static final String TAG = UbahPasswordActivity.class.getSimpleName();
    private static String TAG_ID_USERS = "id_users";
    private static String TAG_PASS_LAMA = "passLama";
    private static String TAG_MASUK_PASS_LAMA = "masukPassLama";
    private static String TAG_PASS_BARU = "passBaru";
    private static String TAG_PASS_KONFIRM = "passKonfirm";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public static final String my_shared_preferences = "my_shared_preferences";

    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    String passLama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        passLama = sharedPreferences.getString(TAG_PASS_LAMA, null);
    }

    @OnClick(R.id.ibBack)
    public void back(){
        onBackPressed();
        finish();
    }

//    @OnClick(R.id.btnSimpan)
//    public void tambahKeranjang(final View view){
//        pDialog = new ProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Merubah Password...");
//        showDialog();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ubah_password, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                hideDialog();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.e("UBAH PASSWORD ", jsonObject.toString());
//
//                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {
//                        ChocoBar.builder().setActivity(UbahPasswordActivity.this)
//                                .setText("Sukses Merubah Password")
//                                .setDuration(ChocoBar.LENGTH_INDEFINITE)
//                                .setActionText(android.R.string.ok)
//                                .setIcon(R.drawable.ic_check_white)
//                                .setTextColor(getResources().getColor(R.color.putih))
//                                .green()  // in built green ChocoBar
//                                .show();
//                    } else {
//                        ChocoBar.builder().setActivity(UbahPasswordActivity.this)
//                                .setText("ERROR!")
//                                .setDuration(ChocoBar.LENGTH_INDEFINITE)
//                                .setActionText(android.R.string.ok)
//                                .red()  // in built green ChocoBar
//                                .show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error != null){
//                    VolleyHandler.handleVolleyError(view, error);
//                    Log.e("Error", error.getMessage());
//                    hideDialog();
//                }
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams()throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//
//                params.put(TAG_ID_USERS, new UserSession(getApplicationContext()).getIdUsers());
//                params.put(TAG_MASUK_PASS_LAMA, etPassLama.getText().toString().trim());
//                params.put(TAG_PASS_BARU, etPassBaru.getText().toString().trim());
//                params.put(TAG_PASS_KONFIRM, etPassKonfirm.getText().toString().trim());
//
//                return  params;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
