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
import pikahayang.pikahayang.Model.DesainPlusKainModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;

public class DesainPlusKainAdapter extends RecyclerView.Adapter<DesainPlusKainAdapter.MyViewHolder> {
    private Context context;
    private List<DesainPlusKainModel> desainPlusKainModelList;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_JENIS_BATIK = "jenis_batik";
    private static String TAG_JENIS_KELAMIN = "jenis_kelamin";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";

    String id, id_kategori, nama_produk, rating, kode_produk, deksripsi, jenis_kelamin, jenis_batik, harga, image;

    DecimalFormat formatRupiah;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNamaProduk)
        TextView tvNamaProduk;
        @BindView(R.id.tvHarga)
        TextView tvHarga;
        @BindView(R.id.ivDesainPlusKain)
        ImageView ivDesainPlusKain;
        @BindView(R.id.cvDesainPlusKain)
        CardView cvDesainPlusKain;
        @BindView(R.id.tvJenisBatik)
        TextView tvJenisBatik;
        @BindView(R.id.tvRating)
        TextView tvRating;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DesainPlusKainAdapter(Context context, List<DesainPlusKainModel> desainPlusKainModels){
        this.context = context;
        this.desainPlusKainModelList = desainPlusKainModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_desainpluskain, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){

        formatRupiah();

        final DesainPlusKainModel desainPlusKainModel = desainPlusKainModelList.get(position);

        id = desainPlusKainModel.getId();
        id_kategori = desainPlusKainModel.getId_kategori();
        nama_produk = desainPlusKainModel.getNama_produk();
        harga = desainPlusKainModel.getHarga();
        jenis_kelamin = desainPlusKainModel.getJenis_kelamin();
        jenis_batik = desainPlusKainModel.getJenis_batik();
        rating = desainPlusKainModel.getRate();
        image = URL.image + desainPlusKainModel.getImage();

        holder.tvNamaProduk.setText(desainPlusKainModel.getNama_produk());
        holder.tvHarga.setText(formatRupiah.format(Long.valueOf(desainPlusKainModel.getHarga())));
        holder.tvJenisBatik.setText(desainPlusKainModel.getJenis_batik());
        holder.tvRating.setText(rating);
        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(holder.ivDesainPlusKain);
        holder.cvDesainPlusKain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail_produk.class);
                intent.putExtra(TAG_ID, desainPlusKainModel.getId())
                        .putExtra(TAG_ID_KATEGORI, desainPlusKainModel.getId_kategori())
                        .putExtra(TAG_NAMA_PRODUK, desainPlusKainModel.getNama_produk())
                        .putExtra(TAG_KODE_PRODUK, desainPlusKainModel.getKode_produk())
                        .putExtra(TAG_HARGA, desainPlusKainModel.getHarga())
                        .putExtra(TAG_DESKRIPSI, desainPlusKainModel.getDeskripsi())
                        .putExtra(TAG_JENIS_KELAMIN, desainPlusKainModel.getJenis_kelamin())
                        .putExtra(TAG_JENIS_BATIK, desainPlusKainModel.getJenis_batik())
                        .putExtra(TAG_IMAGE, URL.image + desainPlusKainModel.getImage());
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
        return desainPlusKainModelList.size();
    }

}