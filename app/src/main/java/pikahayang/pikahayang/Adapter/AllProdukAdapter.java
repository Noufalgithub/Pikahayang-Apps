package pikahayang.pikahayang.Adapter;

import android.content.Context;
import android.content.Intent;
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
import pikahayang.pikahayang.Model.AllProdukModel;
import pikahayang.pikahayang.Model.DesainPlusKainModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;

public class AllProdukAdapter extends RecyclerView.Adapter<AllProdukAdapter.MyViewHolder> {
    private Context context;
    private List<AllProdukModel> allProdukModelList;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_JENIS_KELAMIN = "jenis_kelamin";
    private static String TAG_JENIS_BATIK = "jenis_batik";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";

    String id, id_kategori, nama_produk, jenis_kelamin, jenis_batik, rating, kode_produk, deskripsi, harga, image;

    DecimalFormat formatRupiah;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNamaProduk)
        TextView tvNamaProduk;
        @BindView(R.id.tvHarga)
        TextView tvHarga;
        @BindView(R.id.tvJenisBatik)
        TextView tvJenisBatik;
        @BindView(R.id.ivAllProduk)
        ImageView ivAllProduk;
        @BindView(R.id.cvAllProduk)
        CardView cvAllProduk;
        @BindView(R.id.tvRating)
        TextView tvRating;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AllProdukAdapter(Context context, List<AllProdukModel> item){
        this.context = context;
        this.allProdukModelList = item;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_all_produk, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){

        formatRupiah();

        final AllProdukModel items = allProdukModelList.get(position);

        id = items.getId();
        id_kategori = items.getId_kategori();
        nama_produk = items.getNama_produk();
        harga = items.getHarga();
        jenis_batik = items.getJenis_batik();
        jenis_kelamin = items.getJenis_kelamin();
        rating = items.getRate();
        image = URL.image + items.getImage();

        holder.tvNamaProduk.setText(items.getNama_produk());
        holder.tvJenisBatik.setText(items.getJenis_batik());
        holder.tvRating.setText(items.getRate());
        holder.tvHarga.setText(formatRupiah.format(Long.valueOf(items.getHarga())));
        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(holder.ivAllProduk);
        holder.cvAllProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail_produk.class);
                intent.putExtra(TAG_ID, items.getId())
                        .putExtra(TAG_ID_KATEGORI, items.getId_kategori())
                        .putExtra(TAG_NAMA_PRODUK, items.getNama_produk())
                        .putExtra(TAG_KODE_PRODUK, items.getKode_produk())
                        .putExtra(TAG_HARGA, items.getHarga())
                        .putExtra(TAG_DESKRIPSI, items.getDeskripsi())
                        .putExtra(TAG_JENIS_KELAMIN, items.getJenis_kelamin())
                        .putExtra(TAG_JENIS_BATIK, items.getJenis_batik())
                        .putExtra(TAG_IMAGE, URL.image + items.getImage());
                context.startActivity(intent);
            }
        });

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
        return allProdukModelList.size();
    }

}