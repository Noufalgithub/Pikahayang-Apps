package pikahayang.pikahayang;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Adapter.CartItemsAdapter;
import pikahayang.pikahayang.Model.CartItemsModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class ShoppingCartActivity extends AppCompatActivity {
    @BindView(R.id.rvItemCart)
    RecyclerView rvItemCart;
    @BindView(R.id.tvCountNotifCart)
    TextView tvCountNotifCart;
    @BindView(R.id.rlCountCart)
    RelativeLayout rlCountCart;
    @BindView(R.id.pbcart)
    ProgressBar progressBar;
    @BindView(R.id.btnLanjutPembayaran)
    Button btnLanjutPembayaran;
    @BindView(R.id.svCart)
    ScrollView svCart;
    @BindView(R.id.rlKosongCart)
    RelativeLayout rlKosongCart;
    @BindView(R.id.cvLanjutKePembayaran)
    CardView cvLanjutKePembayaran;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvTotalBottom)
    TextView tvTotalBottom;
    @BindView(R.id.ibBack)
    ImageButton ibBack;

    ProgressDialog progressDialog;
    DecimalFormat formatRupiah;
    View view;

    private static final String TAG = ShoppingCartActivity.class.getSimpleName();
    private List<CartItemsModel> cartItemsModelList;
    private CartItemsAdapter mAdapter;

    private static String TAG_SUCCESS = "success";
    private static String TAG_ID = "id";
    String jumlahTotal, namaProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);


        cartItemsModelList = new ArrayList<>();
        mAdapter = new CartItemsAdapter(ShoppingCartActivity.this, cartItemsModelList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvItemCart.setLayoutManager(mLayoutManager);
        rvItemCart.setAdapter(mAdapter);

        rlCountCart.setVisibility(View.INVISIBLE);

        readItemCart();
        readNotifKeranjang();
        formatRupiah();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("broadcast"));

    }

    @OnClick(R.id.ibBack)
    public void back(){
        onBackPressed();
        finish();
    }

        public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String jumlah = intent.getStringExtra("jumlah");
                namaProduk = intent.getStringExtra("nama_produk");
                jumlahTotal=jumlah;
                readNotifKeranjang();
                if (jumlahTotal!=null){
                    tvTotal.setText(formatRupiah.format(Long.valueOf(jumlahTotal)));
                    tvTotalBottom.setText(formatRupiah.format(Long.valueOf(jumlahTotal)));
                }
            }
        };

    // format rupiah
    private void formatRupiah(){
        formatRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols(localeID);
        formatRp.setCurrencySymbol("Rp. ");
        formatRupiah.setDecimalFormatSymbols(formatRp);
    }

    private void readNotifKeranjang(){
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.count_cart + "?id=" + new UserSession(getApplicationContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());

                    String total = jsonObject.getString("count(id_users)");
                    tvCountNotifCart.setText(total);

                    int totalInt = Integer.parseInt(total);

                    if (totalInt > 0){
                        rlCountCart.setVisibility(View.VISIBLE);
                    } else {
                        rlCountCart.setVisibility(View.INVISIBLE);
                        rlKosongCart.setVisibility(View.VISIBLE);
                        svCart.setVisibility(View.GONE);
                        cvLanjutKePembayaran.setVisibility(View.GONE);

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
                }
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void readItemCart(){
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_cart_item +"?id_users="+ new UserSession(getApplicationContext()).getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Log.e("READ ITEM CART", response);
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        List<CartItemsModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CartItemsModel>>(){
                        }.getType());

                        cartItemsModelList.clear();
                        cartItemsModelList.addAll(items);

                        mAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        VolleyHandler.handleVolleyError(view, error);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    if (error != null) {
                        VolleyHandler.handleVolleyError(view, error);
                    }
                }
            });
            AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @OnClick(R.id.btnLanjutPembayaran)
    public void toCheckOut(){
        Intent intent = new Intent(ShoppingCartActivity.this, CheckOutActivity.class);
        intent.putExtra("total", jumlahTotal);
        intent.putExtra("nama_produk", namaProduk);
        startActivity(intent);
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
