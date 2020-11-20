package pikahayang.pikahayang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pd.chocobar.ChocoBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Adapter.GalleryAdapter;
import pikahayang.pikahayang.Adapter.SizeAdapter;
import pikahayang.pikahayang.Model.GalleryModel;
import pikahayang.pikahayang.Model.SizeModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class Detail_produk extends AppCompatActivity {
    @BindView(R.id.tvNamaProdukDetail)
    TextView tvNamaProdukDetail;
    @BindView(R.id.tvHargaDetail)
    TextView tvHargaDetail;
    @BindView(R.id.ivProdukDetail)
    ImageView ivProdukDetail;
    @BindView(R.id.tvDeskripsiProdukDetail)
    TextView tvDeskripsiProdukDetail;
    @BindView(R.id.spinnerSize)
    Spinner spinnerSize;
    @BindView(R.id.tvResultSize)
    TextView tvResultSize;
    @BindView(R.id.tvResultQuantity)
    TextView tvResultQuantity;
    @BindView(R.id.tvResultStock)
    TextView tvStock;
    @BindView(R.id.tvKodeProduk)
    TextView tvKodeProduk;
    @BindView(R.id.btnTambahKeKeranjang)
    Button btnTambahKeKeranjang;
    @BindView(R.id.btnBeliSekarang)
    Button btnBeliSekarang;
    @BindView(R.id.rvGallery)
    RecyclerView rvGallery;
    @BindView(R.id.rlShoppingCart)
    RelativeLayout rlShoppingCart;
    @BindView(R.id.rlCountCart)
    RelativeLayout rlCountCart;
    @BindView(R.id.tvCountNotifCart)
    TextView tvCountNotifCart;
    @BindView(R.id.pbDetailProduk)
    ProgressBar pbDetailProduk;
    @BindView(R.id.ibPlus)
    ImageButton ibPlus;
    @BindView(R.id.ibMinus)
    ImageButton ibMinus;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.ibArrowUp)
    ImageButton ibArrowUp;
    @BindView(R.id.ibArrowDown)
    ImageButton ibArrowDown;
    @BindView(R.id.rlArrowDown)
    RelativeLayout rlArrowDown;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvBerat)
    TextView tvBerat;
    @BindView(R.id.tvJenisBatik)
    TextView tvJenisBatik;

    private List<SizeModel> sizeModelList = new ArrayList<SizeModel>();
    private SizeAdapter mAdapterSize;

    private List<GalleryModel> galleryModelList;
    private GalleryAdapter mAdapterGallery;

    ProgressDialog pDialog;

    String id, idKategori, namaProduk, kodeProduk, harga, jenisKelamin, jenisBatik, deskripsi, image, statusWishList;
    int status = 1;

    private static final String TAG = Detail_produk.class.getSimpleName();
    private static String TAG_ID = "id";
    private static String TAG_ID_PRODUK = "id_produk";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";
    private static String TAG_JENIS_BATIK = "jenis_batik";
    private static String TAG_QUANTITY = "quantity";
    private static String TAG_STATUS_WISHLIST = "status_wishlist";
    private static String TAG_ID_USERS = "id_users";
    private static String TAG_SIZE = "size";
    private static String TAG_STOCK = "stock";
    private static String TAG_BERAT = "berat";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";

    int quantity = 1;

    DecimalFormat formatRupiah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        ButterKnife.bind(this);

        readCount();

        // format rupiah
        formatRupiah();

        pbDetailProduk.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        id = intent.getExtras().getString(TAG_ID);
        idKategori = intent.getExtras().getString(TAG_ID_KATEGORI);
        namaProduk = intent.getExtras().getString(TAG_NAMA_PRODUK);
        kodeProduk = intent.getExtras().getString(TAG_KODE_PRODUK);
        harga = intent.getExtras().getString(TAG_HARGA);
        deskripsi = intent.getExtras().getString(TAG_DESKRIPSI);
        jenisBatik = intent.getExtras().getString(TAG_JENIS_BATIK);
        image = intent.getExtras().getString(TAG_IMAGE);

        tvNamaProdukDetail.setText(namaProduk);
        tvHargaDetail.setText(formatRupiah.format(Long.valueOf(harga)));
        tvDeskripsiProdukDetail.setText(deskripsi);
        tvKodeProduk.setText(kodeProduk);
        tvJenisBatik.setText(jenisBatik);
        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(ivProdukDetail);

        // Spinner SIZE
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvResultSize.setText(sizeModelList.get(position).getSize());
                tvStock.setText(sizeModelList.get(position).getStock());
                tvBerat.setText(sizeModelList.get(position).getBerat());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // size
        mAdapterSize = new SizeAdapter(Detail_produk.this, sizeModelList);
        spinnerSize.setAdapter(mAdapterSize);


        // gallery
        galleryModelList = new ArrayList<>();
        mAdapterGallery = new GalleryAdapter(Detail_produk.this, galleryModelList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvGallery.setLayoutManager(gridLayoutManager);
        rvGallery.setHasFixedSize(true);
        rvGallery.setAdapter(mAdapterGallery);

        // count notif cart
        rlCountCart.setVisibility(View.INVISIBLE);

        // arrow
        ibArrowUp.setVisibility(View.GONE);
        tvDeskripsiProdukDetail.setVisibility(View.GONE);

        // inisialisasi method
        readSize();
        readGallery();

        tvQuantity.setText("1");
        tvResultQuantity.setText("1");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("broadcast"));
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
        finish();
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String image = intent.getStringExtra("gambar");
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.ic_error)
                    .into(ivProdukDetail);
            readCount();
        }
    };

    // format rupiah
    private void formatRupiah() {
        formatRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols(localeID);
        formatRp.setCurrencySymbol("Rp. ");
        formatRupiah.setDecimalFormatSymbols(formatRp);
    }

    @Override
    protected void onResume() {
        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(ivProdukDetail);
        super.onResume();
    }

    @OnClick(R.id.ibArrowDown)
    public void arrowDown() {
        ibArrowUp.setVisibility(View.VISIBLE);
        ibArrowDown.setVisibility(View.GONE);
        tvDeskripsiProdukDetail.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rlArrowDown)
    public void rlDown() {
        ibArrowUp.setVisibility(View.VISIBLE);
        ibArrowDown.setVisibility(View.GONE);
        tvDeskripsiProdukDetail.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ibArrowUp)
    public void arrowUp() {
        ibArrowDown.setVisibility(View.VISIBLE);
        ibArrowUp.setVisibility(View.GONE);
        tvDeskripsiProdukDetail.setVisibility(View.GONE);
    }

    @OnClick(R.id.ibPlus)
    public void increment(View view) {
        int stock = Integer.parseInt(tvStock.getText().toString());
        if (quantity == stock) {
            ChocoBar.builder().setView(view)
                    .setText("Tidak boleh lebih dari Stock!")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .setIcon(R.drawable.ic_info_grey)
                    .orange()  // in built green ChocoBar
                    .setActionTextColor(getResources().getColor(R.color.putih))
                    .show();
        } else {
            quantity = quantity + 1;
            tvQuantity.setText(String.valueOf(quantity));
            tvResultQuantity.setText(String.valueOf(quantity));
        }
    }

    @OnClick(R.id.ibMinus)
    public void decrement(View view) {
        int quantityNow = Integer.parseInt(tvQuantity.getText().toString());
        if (quantity == 1) {
            ChocoBar.builder().setActivity(Detail_produk.this)
                    .setText("Minimal 1 Produk Pembelian")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .setIcon(R.drawable.ic_info_grey)
                    .orange()  // in built green ChocoBar
                    .setActionTextColor(getResources().getColor(R.color.putih))
                    .show();
        } else if (quantity > 1) {
            quantity = quantityNow - 1;
            tvQuantity.setText(String.valueOf(quantity));
            tvResultQuantity.setText(String.valueOf(quantity));
        }
    }

    private void readSize() {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.size + "?id_produk=" + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("Sukses: ", jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        List<SizeModel> item = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SizeModel>>() {
                        }.getType());

                        sizeModelList.clear();
                        sizeModelList.addAll(item);

                        mAdapterSize.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void readGallery() {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_gallery + "?id_produk=" + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("Sukses Gallery: ", jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        List<GalleryModel> item = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<GalleryModel>>() {
                        }.getType());

                        galleryModelList.clear();
                        galleryModelList.addAll(item);

                        mAdapterGallery.notifyDataSetChanged();

                        pbDetailProduk.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pbDetailProduk.setVisibility(View.VISIBLE);
                }
            });
            AppController.getInstance().addToRequestQueue(stringRequest);


    }

    private void readCount() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.count_cart + "?id=" + new UserSession(getApplicationContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("COUNT CART ", response);
                    JSONObject jsonObject = new JSONObject(response);

                    String total = jsonObject.getString("count(id_users)");
                    tvCountNotifCart.setText(total);

                    int totalInt = Integer.parseInt(total);

                    if (totalInt > 0) {
                        rlCountCart.setVisibility(View.VISIBLE);
                    } else {
                        rlCountCart.setVisibility(View.INVISIBLE);
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Count", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @OnClick(R.id.btnTambahKeKeranjang)
    public void tambahKeranjang(final View view) {
        if (new UserSession(getApplicationContext()).getIsLogin()) {
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Menambahkan ke Keranjang...");
            showDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.tambahKeranjang, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("ADD KERANJANG ", response);
                    hideDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getInt(TAG_SUCCESS) == 1) {
                            readCount();
                            ChocoBar.builder().setActivity(Detail_produk.this)
                                    .setText("Sukses ditambahkan ke keranjang")
                                    .setDuration(ChocoBar.LENGTH_SHORT)
                                    .setIcon(R.drawable.ic_check_white)
                                    .setTextColor(getResources().getColor(R.color.putih))
                                    .green()  // in built green ChocoBar
                                    .show();
//                            Snackbar.make(findViewById(android.R.id.content), "Berhasil ditambahkan ke keranjang", Snackbar.LENGTH_LONG).show();
                        } else {
                            ChocoBar.builder().setActivity(Detail_produk.this)
                                    .setText("ERROR!")
                                    .setDuration(ChocoBar.LENGTH_LONG)
                                    .red()  // in built green ChocoBar
                                    .show();
//                            Snackbar.make(findViewById(android.R.id.content), "ERROR!", Snackbar.LENGTH_LONG).show();
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
                        Log.e("Error", error.getMessage());
                        hideDialog();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();

                    params.put(TAG_ID_USERS, new UserSession(getApplicationContext()).getId());
                    params.put(TAG_ID_PRODUK, id.trim());
                    params.put(TAG_NAMA_PRODUK, tvNamaProdukDetail.getText().toString().trim());
                    params.put(TAG_KODE_PRODUK, tvKodeProduk.getText().toString().trim());
                    params.put(TAG_SIZE, tvResultSize.getText().toString().trim());
                    params.put(TAG_STOCK, tvStock.getText().toString().trim());
                    params.put(TAG_BERAT, tvBerat.getText().toString().trim());
                    params.put(TAG_HARGA, harga.trim());
                    params.put(TAG_QUANTITY, tvResultQuantity.getText().toString().trim());

                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Anda belum login, silahkan login terlebih dahulu")
                    .setIcon(R.drawable.ic_info)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Detail_produk.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    @OnClick(R.id.btnBeliSekarang)
    public void tambahBeli(final View view) {
        if (new UserSession(getApplicationContext()).getIsLogin()) {
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Memproses...");
            showDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.tambahKeranjang, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("BELI SEKARANG ", response);
                    hideDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getInt(TAG_SUCCESS) == 1) {
                            Intent intent = new Intent(Detail_produk.this, ShoppingCartActivity.class);
                            startActivity(intent);
                            readCount();
                        } else {
                            ChocoBar.builder().setActivity(Detail_produk.this)
                                    .setText("ERROR!")
                                    .setDuration(ChocoBar.LENGTH_LONG)
                                    .red()  // in built green ChocoBar
                                    .show();
//                            Snackbar.make(findViewById(android.R.id.content), "ERROR!", Snackbar.LENGTH_LONG).show();
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
                        Log.e("Error", error.getMessage());
                        hideDialog();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();

                    params.put(TAG_ID_USERS, new UserSession(getApplicationContext()).getId());
                    params.put(TAG_ID_PRODUK, id.trim());
                    params.put(TAG_NAMA_PRODUK, tvNamaProdukDetail.getText().toString().trim());
                    params.put(TAG_KODE_PRODUK, tvKodeProduk.getText().toString().trim());
                    params.put(TAG_SIZE, tvResultSize.getText().toString().trim());
                    params.put(TAG_STOCK, tvStock.getText().toString().trim());
                    params.put(TAG_BERAT, tvBerat.getText().toString().trim());
                    params.put(TAG_HARGA, harga.trim());
                    params.put(TAG_QUANTITY, tvResultQuantity.getText().toString().trim());


                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Anda belum login, silahkan login terlebih dahulu")
                    .setIcon(R.drawable.ic_info)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Detail_produk.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    @OnClick(R.id.ibShoppingCart)
    public void pindahcart() {
        Intent intent = new Intent(Detail_produk.this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rlShoppingCart)
    public void rlpindahcart() {
        Intent intent = new Intent(Detail_produk.this, ShoppingCartActivity.class);
        startActivity(intent);
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
