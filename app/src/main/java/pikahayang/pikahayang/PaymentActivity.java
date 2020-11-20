package pikahayang.pikahayang;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pd.chocobar.ChocoBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Adapter.PaymentAdapter;
import pikahayang.pikahayang.Adapter.ServiceKurirAdapter;
import pikahayang.pikahayang.Model.PaymentModel;
import pikahayang.pikahayang.Model.ServiceKurirModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class PaymentActivity extends AppCompatActivity {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.rvPayment)
    RecyclerView rvPayment;
    @BindView(R.id.btnBayar)
    Button btnBayar;
    @BindView(R.id.pbPayment)
    ProgressBar pbPayment;
    @BindView(R.id.tvTotalHarga)
    TextView tvTotalHarga;
    @BindView(R.id.tvNamaKurir)
    TextView tvNamaKurir;
    @BindView(R.id.rvService)
    RecyclerView rvService;
    @BindView(R.id.tvJumlahTotal)
    TextView tvJumlahTotal;
    @BindView(R.id.tvOngkir)
    TextView tvOngkir;

    DecimalFormat formatRupiah;
    ProgressDialog pDialog;
    View view;

    private List<PaymentModel> paymentModelList;
    private PaymentAdapter mAdapter;

    private List<ServiceKurirModel> serviceKurirModelList;
    private ServiceKurirAdapter mAdapterService;

    String totalHarga, totalHargaa, totalBerat, kurir, rajaongkir, namaPenerima, biaya, biayaa, service, duration, emailPenerima, noHpPenerima, kota, alamatLengkap, catatan;
    String statusTransaksi = "BELUM MENGIRIM BUKTI TRANSFER";
    int jumlahTotal = 0;
    String berat = "1000";


    private static final String TAG = PaymentActivity.class.getSimpleName();
    private static String TAG_ID_USERS = "id_users";
    private static String TAG_NAMA_PENERIMA = "nama_penerima";
    private static String TAG_EMAIL_PENERIMA = "email_penerima";
    private static String TAG_NOHP_PENERIMA = "no_hp_penerima";
    private static String TAG_TANGGAL = "tanggal";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_CATATAN = "catatan";
    private static String TAG_QTY_TRANSAKSI = "qty_transaksi";
    private static String TAG_ONGKIR = "ongkos_kirim";
    private static String TAG_TOTAL_BERAT = "total_berat";
    private static String TAG_TOTAL_HARGA = "total_harga";
    private static String TAG_STATUS_TRANSAKSI = "status_transaksi";
    private static String TAG_ALAMAT_KOTA_PENGIRIMAN = "alamat_kota_pengiriman";
    private static String TAG_ALAMAT_LENGKAP_PENGIRIMAN = "alamat_lengkap_pengiriman";
    private static String TAG_KURIR = "kurir";
    private static String TAG_SERVICE = "service";
    private static String TAG_DURATION = "duration";
    private static String TAG_BUKTI_TRANSAKSI = "bukti_transaksi";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

//    ArrayList<String> service = new ArrayList<>();
//    ArrayList<String> description = new ArrayList<>();

    String date, namaProduk;
    String statusButton = "aktif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        readItemCart();

        // format rupiah
        formatRupiah();

        paymentModelList = new ArrayList<>();
        mAdapter = new PaymentAdapter(PaymentActivity.this, paymentModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPayment.setLayoutManager(mLayoutManager);
        rvPayment.setAdapter(mAdapter);

        serviceKurirModelList = new ArrayList<>();
        mAdapterService = new ServiceKurirAdapter(PaymentActivity.this, serviceKurirModelList);
        RecyclerView.LayoutManager mLayoutManagerService = new LinearLayoutManager(getApplicationContext());
        rvService.setLayoutManager(mLayoutManagerService);
        rvService.setAdapter(mAdapterService);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            totalHarga = bundle.getString("total");
            kurir = bundle.getString("kurir");
            namaPenerima = bundle.getString("nama_penerima");
            emailPenerima = bundle.getString("email_penerima");
            noHpPenerima = bundle.getString("no_hp_penerima");
            kota = bundle.getString("alamat_kota_pengiriman");
            alamatLengkap = bundle.getString("alamat_lengkap_pengiriman");
            catatan = bundle.getString("catatan");
            rajaongkir = bundle.getString("costs");
            namaProduk = bundle.getString("nama_produk");

            List<ServiceKurirModel> items = new Gson().fromJson(rajaongkir, new TypeToken<List<ServiceKurirModel>>() {
            }.getType());

            serviceKurirModelList.clear();
            serviceKurirModelList.addAll(items);

            mAdapterService.notifyDataSetChanged();
        }

        // date
        date = getDateTimeFromTimeStamp(System.currentTimeMillis(),DATE_FORMAT);

        if (kurir.equals("jne")) {
            tvNamaKurir.setText("JNE");
        } else if (kurir.equals("tiki")) {
            tvNamaKurir.setText("TIKI");
        } else if (kurir.equals("pos")) {
            tvNamaKurir.setText("POS");
        }
        totalHargaa = totalHarga;
        if (totalHarga != null){
            tvTotalHarga.setText(formatRupiah.format(Long.valueOf(totalHargaa)));
        }


        pbPayment.setVisibility(View.VISIBLE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("broadcast"));

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            biaya = intent.getStringExtra("biaya");
            service = intent.getStringExtra("service");
            duration = intent.getStringExtra("duration");
            biayaa = biaya;
            if (biaya != null) {
                tvOngkir.setText(formatRupiah.format(Long.valueOf(biayaa)));

                //Menghitung jumlah total
                jumlahTotal = 0;
                jumlahTotal = Integer.parseInt(totalHarga) + Integer.parseInt(biayaa);
                tvJumlahTotal.setText(formatRupiah.format(Long.valueOf(jumlahTotal)));
            }
        }
    };

    // DATE TIME
    public static String getDateTimeFromTimeStamp(Long time, String mDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Date dateTime = new Date(time);
        return dateFormat.format(dateTime);
    }

    // BACK
    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.btnBayar)
    public void pesanPikahayang(final View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Membuat Pesanan..");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.pesan_pikahayang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    Log.e("PESANAN ", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String kodeOtomatis = jsonObject.getString("kode_otomatis");

                    Bundle bundle = new Bundle();
                    bundle.putString("kode_otomatis", kodeOtomatis);

                    Intent intent = new Intent(PaymentActivity.this, DetailPembelianActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                    Log.e("Error", error.getMessage());
                    hideDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_ID_USERS, new UserSession(getApplicationContext()).getId());
                params.put(TAG_NAMA_PENERIMA, namaPenerima.trim());
                params.put(TAG_EMAIL_PENERIMA, emailPenerima.trim());
                params.put(TAG_NOHP_PENERIMA, noHpPenerima.trim());
                params.put(TAG_ALAMAT_KOTA_PENGIRIMAN, kota.trim());
                params.put(TAG_ALAMAT_LENGKAP_PENGIRIMAN, alamatLengkap.trim());
                params.put(TAG_KURIR, kurir.trim());
                params.put(TAG_ONGKIR, biayaa.trim());
                params.put(TAG_SERVICE, service.trim());
                params.put(TAG_DURATION, duration.trim());
                params.put(TAG_TOTAL_HARGA, String.valueOf(jumlahTotal).trim());
                params.put(TAG_TOTAL_BERAT, berat.trim());
                params.put(TAG_CATATAN, catatan.trim());
                params.put(TAG_TANGGAL, date.trim());
                params.put(TAG_NAMA_PRODUK, namaProduk.trim());
                params.put(TAG_STATUS_TRANSAKSI, statusTransaksi.trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    // format rupiah
    private void formatRupiah() {
        formatRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols(localeID);
        formatRp.setCurrencySymbol("Rp. ");
        formatRupiah.setDecimalFormatSymbols(formatRp);
    }

    private void readItemCart() {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_cart_item + "?id_users=" + new UserSession(getApplicationContext()).getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        List<PaymentModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<PaymentModel>>() {
                        }.getType());

                        paymentModelList.clear();
                        paymentModelList.addAll(items);

                        mAdapter.notifyDataSetChanged();

                        pbPayment.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        VolleyHandler.handleVolleyError(view, error);
                        pbPayment.setVisibility(View.VISIBLE);
                    }
                }
            });
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
