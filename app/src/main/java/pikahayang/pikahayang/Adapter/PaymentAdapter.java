package pikahayang.pikahayang.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import pikahayang.pikahayang.Model.CartItemsModel;
import pikahayang.pikahayang.Model.PaymentModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private Context context;
    private List<PaymentModel> paymentModelList;

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

    String id_cart, id_produk, id_users, nama_produk, quantity, harga, berat, size;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNama)
        TextView tvNama;
        @BindView(R.id.tvBerat)
        TextView tvBerat;
        @BindView(R.id.tvQty)
        TextView tvQty;
        @BindView(R.id.tvHarga)
        TextView tvHarga;
        @BindView(R.id.tvUkuran)
        TextView tvUkuran;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public PaymentAdapter(Context context, List<PaymentModel> items){
        this.context = context;
        this.paymentModelList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_payment, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {

        formatRupiah();

        final PaymentModel item = paymentModelList.get(position);

        id_cart = item.getId_cart();
        id_produk = item.getId_produk();
        id_users = new UserSession(context).getId();
        nama_produk = item.getNama_produk();
        harga = item.getHarga();
        quantity = item.getQuantity();
        berat = item.getBerat();
        size = item.getSize();

        holder.tvBerat.setText(berat);
        holder.tvNama.setText(nama_produk);
        holder.tvHarga.setText(formatRupiah.format(Long.valueOf(harga)));
        holder.tvUkuran.setText(size);
        holder.tvQty.setText(quantity);

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
        return paymentModelList.size();
    }

}