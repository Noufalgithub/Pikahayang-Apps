package pikahayang.pikahayang;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pd.chocobar.ChocoBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Adapter.CityAdapter;
import pikahayang.pikahayang.Model.CityModel;
import pikahayang.pikahayang.Model.ServiceKurirModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class CheckOutActivity extends AppCompatActivity {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.etNama)
    AppCompatEditText etNama;
    @BindView(R.id.ibClearNama)
    ImageButton ibClearNama;
    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.ibClearEmail)
    ImageButton ibClearEmail;
    @BindView(R.id.etNoTelp)
    AppCompatEditText etNoTelp;
    @BindView(R.id.ibClearNoTelp)
    ImageButton ibClearNoTelp;
    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;
    @BindView(R.id.etAlamat)
    AppCompatEditText etAlamat;
    @BindView(R.id.spinnerKurir)
    Spinner spinnerKurir;
    @BindView(R.id.etCatatan)
    EditText etCatatan;
    @BindView(R.id.btnLanjutPembayaran)
    Button btnLanjutPembayaran;
    @BindView(R.id.pbPilihKota)
    ProgressBar pbPilihKota;

    ProgressDialog progressDialog;
    View view;

    private List<CityModel> cityModelList = new ArrayList<CityModel>();
    private CityAdapter mAdapterCity;

    String total, namaProduk;
    String origin = "149";
    String destination, city_name;
    String weight = "1000";
    String jsonCosts;
    List<ServiceKurirModel> itemList = new ArrayList<ServiceKurirModel>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);

        readCity();
        pbPilihKota.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        total = intent.getExtras().getString("total");
        namaProduk = intent.getExtras().getString("nama_produk");

        etNama.setText(new UserSession(getApplicationContext()).getName());
        etEmail.setText(new UserSession(getApplicationContext()).getEmail());
        etNoTelp.setText(new UserSession(getApplicationContext()).getNoHp());
        etAlamat.setText(new UserSession(getApplicationContext()).getAlamat());

        ibClearNama.setVisibility(View.INVISIBLE);
        ibClearEmail.setVisibility(View.INVISIBLE);
        ibClearNoTelp.setVisibility(View.INVISIBLE);

        etNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ibClearNama.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNoTelp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ibClearNoTelp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        // RAJA ONGKIR CITY
        mAdapterCity = new CityAdapter(CheckOutActivity.this, cityModelList);
        spinnerCity.setAdapter(mAdapterCity);

        // Spinner CITY
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("broadcast"));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            destination = intent.getStringExtra("city_id");
            city_name = intent.getStringExtra("city_name");
        }
    };


    @OnClick(R.id.ibClearNama)
    public void clearNama(){
        etNama.setText(null);
    }
    @OnClick(R.id.ibClearEmail)
    public void clearEmail(){
        etEmail.setText(null);
    }
    @OnClick(R.id.ibClearNoTelp)
    public void clearNoHp(){
        etNoTelp.setText(null);
    }

    private void readCity(){
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.rajaOngkirCity, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject rajaongkir = new JSONObject(response);
                        JSONObject jsonObject = rajaongkir.getJSONObject("rajaongkir");
                        Log.e("RajaOngkir: ", rajaongkir.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("results");


                        List<CityModel> item = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CityModel>>() {
                        }.getType());

                        cityModelList.clear();
                        cityModelList.addAll(item);

                        mAdapterCity.notifyDataSetChanged();

                        pbPilihKota.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null){
                        pbPilihKota.setVisibility(View.VISIBLE);
                        VolleyHandler.handleVolleyError(view, error);
                    }
                }
            });
            AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.btnLanjutPembayaran)
    public void lanjut(final View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.hargaOngkir, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    hideDialog();
                try {
                    JSONObject rajaongkir = new JSONObject(response);
                    JSONObject jsonObject = rajaongkir.getJSONObject("rajaongkir");
                    Log.e("RajaOngkir: ", rajaongkir.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    JSONObject objectCost = jsonArray.getJSONObject(0);
                    JSONArray arrayCost = objectCost.getJSONArray("costs");

                    Gson gson = new Gson();

                    for (int i = 0; i < arrayCost.length(); i++) {
                        try {
                            JSONObject obj = arrayCost.getJSONObject(i);

                            ServiceKurirModel item = new ServiceKurirModel();

                            item.setService(obj.getString("service"));

                            JSONArray arrayCos = obj.getJSONArray("cost");
                            JSONObject objCost = arrayCos.getJSONObject(0);

                            item.setEtd(objCost.getString("etd"));
                            item.setValue(objCost.getString("value"));

                            // menambah item ke array
                            itemList.add(item);
                            jsonCosts = gson.toJson(itemList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    itemList.clear();

                    Bundle bundle = new Bundle();
                    bundle.putString("total", total);
                    bundle.putString("nama_penerima", etNama.getText().toString());
                    bundle.putString("email_penerima", etEmail.getText().toString());
                    bundle.putString("no_hp_penerima", etNoTelp.getText().toString());
                    bundle.putString("alamat_kota_pengiriman", city_name);
                    bundle.putString("alamat_lengkap_pengiriman", etAlamat.getText().toString());
                    bundle.putString("kurir", spinnerKurir.getSelectedItem().toString());
                    bundle.putString("catatan", etCatatan.getText().toString());
                    bundle.putString("costs", jsonCosts);
                    bundle.putString("nama_produk", namaProduk);

                    Intent intent = new Intent(CheckOutActivity.this, PaymentActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

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
        }){
            @Override
            protected Map<String, String> getParams()throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("key", URL.keyRajaOngkir);
                params.put("origin", origin);
                params.put("destination", destination);
                params.put("weight", weight);
                params.put("courier", spinnerKurir.getSelectedItem().toString());

                return  params;
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
