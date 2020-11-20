package pikahayang.pikahayang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Adapter.DetailTransaksiAdapter;
import pikahayang.pikahayang.Model.DetailTransaksiModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class DetailTransaksiActivity extends AppCompatActivity {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.rvDetailTransaksi)
    RecyclerView rvDetailTransaksi;
    @BindView(R.id.tvTotalHarga)
    TextView tvTotalHarga;

    DecimalFormat formatRupiah;
    @BindView(R.id.pbDetailTransaksi)
    ProgressBar pbDetailTransaksi;
    @BindView(R.id.tvTanggal)
    TextView tvTanggal;

    private List<DetailTransaksiModel> detailTransaksiModelList;
    private DetailTransaksiAdapter mAdapter;

    String kdTransaksi, tanggal;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        kdTransaksi = intent.getExtras().getString("kode_otomatis");
        tanggal = intent.getExtras().getString("tanggal");

        tvTanggal.setText(tanggal);

        detailTransaksiModelList = new ArrayList<>();
        mAdapter = new DetailTransaksiAdapter(DetailTransaksiActivity.this, detailTransaksiModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvDetailTransaksi.setLayoutManager(mLayoutManager);
        rvDetailTransaksi.setAdapter(mAdapter);

        // format rupiah
        formatRupiah();
        readDetail();
        readTotal();
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
        finish();
    }

    // format rupiah
    private void formatRupiah() {
        formatRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols(localeID);
        formatRp.setCurrencySymbol("Rp. ");
        formatRupiah.setDecimalFormatSymbols(formatRp);
    }

    private void readDetail() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_detail_transaksi + "?kd_transaksi=" + kdTransaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("DETAIL TRS: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    List<DetailTransaksiModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DetailTransaksiModel>>() {
                    }.getType());

                    detailTransaksiModelList.clear();
                    detailTransaksiModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    pbDetailTransaksi.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                    pbDetailTransaksi.setVisibility(View.VISIBLE);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void readTotal() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_total_harga + "?kd_transaksi=" + kdTransaksi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());

                    String total = jsonObject.getString("total_harga");
                    tvTotalHarga.setText(formatRupiah.format(Long.valueOf(total)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
