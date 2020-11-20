package pikahayang.pikahayang.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pd.chocobar.ChocoBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Detail_produk;
import pikahayang.pikahayang.Model.BajuModel;
import pikahayang.pikahayang.Model.CartItemsModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {
    private Context context;
    private List<CartItemsModel> cartItemsModelList;

    private static String TAG_ID_CART = "id_cart";
    private static String TAG_ID_PRODUK = "id_produk";
    private static String TAG_ID_USERS = "id_users";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_SIZE = "size";
    private static String TAG_HARGA = "harga";
    private static String TAG_QUANTITY = "quantity";
    private static String TAG_IMAGE = "image";
    private static String TAG_SUCCESS = "success";

    ProgressDialog progressDialog;

    DecimalFormat formatRupiah;

    int jumlah = 0;

    String id_cart, id_produk, id_users, nama_produk, jenis_batik, kode_produk, quantity, size, harga, image, stock, berat;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProdukItems)
        ImageView ivProdukItems;
        @BindView(R.id.tvNamaProdukItems)
        TextView tvNamaProdukItems;
        @BindView(R.id.ibClearItems)
        ImageButton ibClearItems;
        @BindView(R.id.tvHargaItems)
        TextView tvHargaItems;
        @BindView(R.id.tvUkuranItems)
        TextView tvUkuranItems;
        @BindView(R.id.ibPlus)
        ImageButton ibPlus;
        @BindView(R.id.ibMinus)
        ImageButton ibMinus;
        @BindView(R.id.tvResultQuantity)
        TextView tvResultQuantity;
        @BindView(R.id.tvStockItems)
        TextView tvStockItems;
        @BindView(R.id.tvBerat)
        TextView tvBerat;
        @BindView(R.id.tvJenisBatik)
        TextView tvJenisBatik;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CartItemsAdapter(Context context, List<CartItemsModel> cartItemsModels){
        this.context = context;
        this.cartItemsModelList = cartItemsModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {

        formatRupiah();

        final CartItemsModel item = cartItemsModelList.get(position);

        id_cart = item.getId_cart();
        id_produk = item.getId_produk();
        id_users = new UserSession(context).getId();
        nama_produk = item.getNama_produk();
        kode_produk = item.getKode_produk();
        size = item.getSize();
        stock = item.getStock();
        harga = item.getHarga();
        jenis_batik = item.getJenis_batik();
        quantity = item.getQuantity();
        image = URL.image + item.getImage();
        berat = item.getBerat();

        holder.tvBerat.setText(berat);
        holder.tvNamaProdukItems.setText(nama_produk);
        holder.tvHargaItems.setText(formatRupiah.format(Long.valueOf(harga)));
        holder.tvUkuranItems.setText(size);
        holder.tvStockItems.setText(stock);
        holder.tvJenisBatik.setText(jenis_batik);
        holder.tvResultQuantity.setText(quantity);
        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(holder.ivProdukItems);

        holder.ibClearItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah=0;
                hapusItem(cartItemsModelList.get(position).getId_cart());
                cartItemsModelList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItemsModelList.size());
                notifyDataSetChanged();
                if (cartItemsModelList.isEmpty()){
                    jumlah=0;
                } else {
                    for (int i=0; i<cartItemsModelList.size(); i++){
                        jumlah = jumlah+(Integer.parseInt(cartItemsModelList.get(i).getHarga()) * Integer.parseInt(cartItemsModelList.get(i).getQuantity()));

                    }
                }
                ChocoBar.builder().setView(v)
                        .setText("Sukses Menghapus")
                        .setDuration(ChocoBar.LENGTH_SHORT)
                        .setIcon(R.drawable.ic_check_white)
                        .setTextColor(v.getResources().getColor(R.color.putih))
                        .green()  // in built green ChocoBar
                        .show();

                Intent intent = new Intent("broadcast");
                intent.putExtra("jumlah", String.valueOf(jumlah));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        holder.ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItemsModelList.get(position).getQuantity().equals("1")){
                    ChocoBar.builder().setView(v)
                            .setText("Minimal 1 Produk Pembelian")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .setIcon(R.drawable.ic_info_grey)
                            .orange()  // in built green ChocoBar
                            .setActionTextColor(v.getResources().getColor(R.color.putih))
                            .show();
//                    Toast.makeText(context, "Minimal 1 Produk Pembelian", Toast.LENGTH_LONG).show();
                } else {
                    jumlah=0;
                    int qty = Integer.parseInt(cartItemsModelList.get(position).getQuantity());
                    int jumlahSekarang = qty - 1;
                    cartItemsModelList.get(position).setQuantity(String.valueOf(jumlahSekarang));
                    notifyItemRangeChanged(position, cartItemsModelList.size());
                    notifyDataSetChanged();
                    if (cartItemsModelList.isEmpty()){
                        jumlah=0;
                    } else {
                        for (int i=0; i<cartItemsModelList.size(); i++){
                            jumlah = jumlah+(Integer.parseInt(cartItemsModelList.get(i).getHarga()) * Integer.parseInt(cartItemsModelList.get(i).getQuantity()));
                        }
                    }
                    updateQty(cartItemsModelList.get(position).getId_cart(), cartItemsModelList.get(position).getQuantity());

                    Intent intent = new Intent("broadcast");
                    intent.putExtra("jumlah", String.valueOf(jumlah));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });
        holder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stock = Integer.parseInt(cartItemsModelList.get(position).getStock());
                int qty = Integer.parseInt(cartItemsModelList.get(position).getQuantity());
                if ( qty == stock){
                    ChocoBar.builder().setView(v)
                            .setText("Tidak boleh lebih dari Stock!")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .setIcon(R.drawable.ic_info_grey)
                            .orange()  // in built green ChocoBar
                            .setActionTextColor(v.getResources().getColor(R.color.putih))
                            .show();
                } else {
                    jumlah = 0;
                    int jumlahBeli = Integer.parseInt(cartItemsModelList.get(position).getQuantity());
                    int jumlahSekarang = jumlahBeli + 1;
                    cartItemsModelList.get(position).setQuantity(String.valueOf(jumlahSekarang));
                    notifyItemRangeChanged(position, cartItemsModelList.size());
                    notifyDataSetChanged();
                    if (cartItemsModelList.isEmpty()){
                        jumlah = 0;
                    } else {
                        for (int i=0; i<cartItemsModelList.size(); i++){
                            jumlah = jumlah+(Integer.parseInt(cartItemsModelList.get(i).getHarga()) * Integer.parseInt(cartItemsModelList.get(i).getQuantity()));

                        }
                    }
                    updateQty(cartItemsModelList.get(position).getId_cart(), cartItemsModelList.get(position).getQuantity());

                    Intent intent = new Intent("broadcast");
                    intent.putExtra("jumlah", String.valueOf(jumlah));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }
            }
        });

        jumlah=0;
        if (cartItemsModelList.isEmpty()){
            jumlah=0;
        } else {
            for (int i=0; i<cartItemsModelList.size(); i++){
                jumlah = jumlah+(Integer.parseInt(cartItemsModelList.get(i).getHarga()) * Integer.parseInt(cartItemsModelList.get(i).getQuantity()));
            }
        }

        Intent intent = new Intent("broadcast");
        intent.putExtra("jumlah", String.valueOf(jumlah));
        intent.putExtra("nama_produk", nama_produk);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

    public void updateQty(final String id_cart, final String quantity){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.update_quantity, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Update qty: ", jsonObject.toString());

                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {
                    } else {
                        Toast.makeText(context,"ERROR!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_ID_CART, id_cart);
                params.put(TAG_QUANTITY, quantity);

                return  params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void hapusItem(final String id_cart){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.delete_cart_item + "?id=" + new UserSession(context.getApplicationContext()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Hapus item: ", jsonObject.toString());

                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {

                    } else {
                        Toast.makeText(context,"ERROR!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams()throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_ID_CART, id_cart);

                return  params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    // format rupiah
    private void formatRupiah(){
        formatRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols(localeID);
        formatRp.setCurrencySymbol("Rp. ");
        formatRupiah.setDecimalFormatSymbols(formatRp);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return cartItemsModelList.size();
    }

}