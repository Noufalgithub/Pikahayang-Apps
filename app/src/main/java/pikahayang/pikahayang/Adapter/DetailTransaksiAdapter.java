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
import pikahayang.pikahayang.Model.DetailTransaksiModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class DetailTransaksiAdapter extends RecyclerView.Adapter<DetailTransaksiAdapter.MyViewHolder> {
    private Context context;
    private List<DetailTransaksiModel> detailTransaksiModelList;

    private static String TAG_ID_CART = "id_cart";
    private static String TAG_ID_PRODUK = "id_produk";
    private static String TAG_ID_USERS = "id_users";
    private static String TAG_NAMA_PRODUK = "nama_produk";

    ProgressDialog progressDialog;

    DecimalFormat formatRupiah;

    int jumlah = 0;

    String namaProduk, quantity, size, harga;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNamaProduk)
        TextView tvNamaProduk;
        @BindView(R.id.tvUkuran)
        TextView tvUkuran;
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;
        @BindView(R.id.tvHarga)
        TextView tvHarga;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DetailTransaksiAdapter(Context context, List<DetailTransaksiModel> items){
        this.context = context;
        this.detailTransaksiModelList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_detail_transaksi, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position) {

        formatRupiah();

        final DetailTransaksiModel item = detailTransaksiModelList.get(position);

        namaProduk = item.getNama_produk();
        quantity = item.getQuantity();
        size = item.getSize();
        harga = item.getHarga();

        holder.tvNamaProduk.setText(namaProduk);
        holder.tvQuantity.setText(quantity);
        holder.tvHarga.setText(formatRupiah.format(Long.valueOf(harga)));
        holder.tvUkuran.setText(size);



    }

    // format rupiah
    private void formatRupiah(){
        formatRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols(localeID);
        formatRp.setCurrencySymbol("Rp. ");
        formatRupiah.setDecimalFormatSymbols(formatRp);
    }

    @Override
    public int getItemCount() {
        return detailTransaksiModelList.size();
    }

}