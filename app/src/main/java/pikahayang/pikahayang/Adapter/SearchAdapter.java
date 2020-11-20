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
import pikahayang.pikahayang.Model.SearchModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private Context context;
    private List<SearchModel> searchModelList;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_JENIS_BATIK = "jenis_batik";
    private static String TAG_JENIS_KELAMIN = "jenis_kelamin";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";

    DecimalFormat formatRupiah;
    String id, id_kategori, nama_produk, deskripsi, kode_produk, jenis_batik, harga, image;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNamaProdukSearch)
        TextView tvNamaProdukSearch;
        @BindView(R.id.tvJenisBatik)
        TextView tvJenisBatik;
        @BindView(R.id.tvHargaSearch)
        TextView tvHargaSearch;
        @BindView(R.id.ivProdukSearch)
        ImageView ivProdukSearch;
        @BindView(R.id.cvSearch)
        CardView cvSearch;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SearchAdapter (Context context, List<SearchModel> searchModels){
        this.context = context;
        this.searchModelList = searchModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){

        formatRupiah();

        final SearchModel searchModel = searchModelList.get(position);

        id = searchModel.getId();
        id_kategori = searchModel.getId_kategori();
        nama_produk = searchModel.getNama_produk();
        kode_produk = searchModel.getKode_produk();
        jenis_batik = searchModel.getJenis_batik();
        harga = searchModel.getHarga();
        image = URL.image + searchModel.getImage();

        holder.tvNamaProdukSearch.setText(searchModel.getNama_produk());
        holder.tvJenisBatik.setText(jenis_batik);
        holder.tvHargaSearch.setText(formatRupiah.format(Long.valueOf(searchModel.getHarga())));
        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(holder.ivProdukSearch);
        holder.cvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail_produk.class);
                intent.putExtra(TAG_ID, searchModel.getId())
                        .putExtra(TAG_ID_KATEGORI, searchModel.getId_kategori())
                        .putExtra(TAG_NAMA_PRODUK, searchModel.getNama_produk())
                        .putExtra(TAG_KODE_PRODUK, searchModel.getKode_produk())
                        .putExtra(TAG_JENIS_BATIK, searchModel.getJenis_batik())
                        .putExtra(TAG_JENIS_KELAMIN, searchModel.getJenis_kelamin())
                        .putExtra(TAG_DESKRIPSI, searchModel.getDeskripsi())
                        .putExtra(TAG_HARGA, searchModel.getHarga())
                        .putExtra(TAG_IMAGE, URL.image + searchModel.getImage());
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
        return searchModelList.size();
    }

}