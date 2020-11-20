package pikahayang.pikahayang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

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

public class KonfirmasiActivity extends AppCompatActivity {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnKonrimasi)
    Button btnKonrimasi;
    @BindView(R.id.ratingBar)
    AppCompatRatingBar ratingBar;
    @BindView(R.id.btnKirimRating)
    MaterialButton btnKirimRating;
    @BindView(R.id.cvRating)
    CardView cvRating;

    String kdTransaksi, idTransaksi;
    String statusTransaksi = "TRANSAKSI BERHASIL";
    Float ratings;

    ProgressDialog pDialog;

    private static final String TAG = KonfirmasiActivity.class.getSimpleName();
    private static String TAG_STATUS_TRANSAKSI = "status_transaksi";
    private static String TAG_RATING = "rating";
    private static String TAG_KD_TRANSAKSI = "kd_transaksi";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        kdTransaksi = intent.getExtras().getString("kode_otomatis");
        idTransaksi = intent.getExtras().getString("id_transaksi");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings = (Float) ratingBar.getRating();
            }
        });

    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.btnKirimRating)
    public void setBtnKirimRating(){

    }

    @OnClick(R.id.btnKonrimasi)
    public void update(final View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Konfirmasi Pesanan...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.update_status_transaksi + "?kd_transaksi=" + kdTransaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Keranjang : ", jsonObject.toString());

                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {
                        ChocoBar.builder().setActivity(KonfirmasiActivity.this)
                                .setText("Konfirmasi pesanan berhasil")
                                .setDuration(ChocoBar.LENGTH_SHORT)
                                .setIcon(R.drawable.ic_check_white)
                                .setTextColor(getResources().getColor(R.color.putih))
                                .green()  // in built green ChocoBar
                                .show();

                        btnKonrimasi.setEnabled(false);
                        // Menampilkan Rating
                        cvRating.setVisibility(View.VISIBLE);

                    } else {
                        ChocoBar.builder().setActivity(KonfirmasiActivity.this)
                                .setText("ERROR!")
                                .setDuration(ChocoBar.LENGTH_LONG)
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
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                    hideDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_STATUS_TRANSAKSI, statusTransaksi.trim());


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    @OnClick(R.id.btnKirimRating)
    public void setBtnKirimRating(final View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Kirim rating..");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.rating + "?kd_transaksi=" + kdTransaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    Log.e("KONFIRMASI", response);
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(KonfirmasiActivity.this);
                        dialog.setMessage("Berhasil mengirim rating")
                                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                        finish();
                                    }
                                });
                        dialog.show();


//                        ChocoBar.builder().setActivity(KonfirmasiActivity.this)
//                                .setText("Berhasil mengirim rating")
//                                .setDuration(ChocoBar.LENGTH_SHORT)
//                                .setIcon(R.drawable.ic_check_white)
//                                .setTextColor(getResources().getColor(R.color.putih))
//                                .green()  // in built green ChocoBar
//                                .show();

                    } else {
                        ChocoBar.builder().setActivity(KonfirmasiActivity.this)
                                .setText("ERROR!")
                                .setDuration(ChocoBar.LENGTH_LONG)
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
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                    hideDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_KD_TRANSAKSI, kdTransaksi.trim());
                params.put(TAG_RATING, ratings.toString().trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
