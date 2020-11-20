package pikahayang.pikahayang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pikahayang.pikahayang.Detail_produk;
import pikahayang.pikahayang.Model.BajuModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;

public class BajuAdapter extends RecyclerView.Adapter<BajuAdapter.MyViewHolder> {
    private Context context;
    private List<BajuModel> bajuModelList;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_JENIS_BATIK = "jenis_batik";
    private static String TAG_JENIS_KELAMIN = "jenis_kelamin";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";
    private static String TAG_RATE = "rate";
    private static String TAG_STATUS_WISHLIST = "status_wishlist";

    String id, id_kategori, nama_produk, rating, jenis_batik, jenis_kelamin, harga, image;

    DecimalFormat formatRupiah;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNamaProduk)
        TextView tvNamaProduk;
        @BindView(R.id.tvHarga)
        TextView tvHarga;
        @BindView(R.id.ivBaju)
        ImageView ivBaju;
        @BindView(R.id.cvBaju)
        CardView cvBaju;
        @BindView(R.id.tvJenisBatik)
        TextView tvJenisBatik;
        @BindView(R.id.tvRating)
        TextView tvRating;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public BajuAdapter(Context context, List<BajuModel> bajuModels){
        this.context = context;
        this.bajuModelList = bajuModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_baju, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){

        formatRupiah();

        final BajuModel bajuModel = bajuModelList.get(position);

        id = bajuModel.getId();
        id_kategori = bajuModel.getId_kategori();
        nama_produk = bajuModel.getNama_produk();
        harga = bajuModel.getHarga();
        jenis_kelamin = bajuModel.getJenis_kelamin();
        jenis_batik = bajuModel.getJenis_batik();
        rating = bajuModel.getRate();
        image = URL.image + bajuModel.getImage();

        holder.tvNamaProduk.setText(bajuModel.getNama_produk());
        holder.tvHarga.setText(formatRupiah.format(Long.valueOf(bajuModel.getHarga())));
        holder.tvJenisBatik.setText(bajuModel.getJenis_batik());
        holder.tvRating.setText(rating);
        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(holder.ivBaju);
        holder.cvBaju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail_produk.class);
                intent.putExtra(TAG_ID, bajuModel.getId())
                        .putExtra(TAG_ID_KATEGORI, bajuModel.getId_kategori())
                        .putExtra(TAG_NAMA_PRODUK, bajuModel.getNama_produk())
                        .putExtra(TAG_KODE_PRODUK, bajuModel.getKode_produk())
                        .putExtra(TAG_DESKRIPSI, bajuModel.getDeskripsi())
                        .putExtra(TAG_HARGA, bajuModel.getHarga())
                        .putExtra(TAG_JENIS_KELAMIN, bajuModel.getJenis_kelamin())
                        .putExtra(TAG_JENIS_BATIK, bajuModel.getJenis_batik())
                        .putExtra(TAG_IMAGE, URL.image + bajuModel.getImage());
                context.startActivity(intent);
            }
        });

        formatRupiah();

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
        return bajuModelList.size();
    }

}