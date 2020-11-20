package pikahayang.pikahayang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pikahayang.pikahayang.DetailPembelianActivity;
import pikahayang.pikahayang.DetailTransaksiActivity;
import pikahayang.pikahayang.KonfirmasiActivity;
import pikahayang.pikahayang.Model.HistoryModel;
import pikahayang.pikahayang.Model.SearchHistoryModel;
import pikahayang.pikahayang.R;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<SearchHistoryModel> searchHistoryModelList;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";
    private static String TAG_STATUS_WISHLIST = "status_wishlist";

    String kdTransaksi, statusTransaksi, tglTransaksi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewHistory)
        View viewHistory;
        @BindView(R.id.tvStatusTransaksi)
        TextView tvStatusTransaksi;
        @BindView(R.id.tvKdTransaksi)
        TextView tvKdTransaksi;
        @BindView(R.id.tvTglTransaksi)
        TextView tvTglTransaksi;
        @BindView(R.id.tvStatusPesanan)
        TextView tvStatusPesanan;
        @BindView(R.id.tvLihatDetailPesanan)
        TextView tvLihatDetailPesanan;
        @BindView(R.id.cvSearchHistory)
        CardView cvSearchHistory;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SearchHistoryAdapter(Context context, List<SearchHistoryModel> items){
        this.context = context;
        this.searchHistoryModelList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){


        final SearchHistoryModel item = searchHistoryModelList.get(position);

        kdTransaksi = item.getKd_transaksi();
        statusTransaksi = item.getStatus_transaksi();
        tglTransaksi = item.getTanggal();

        holder.tvKdTransaksi.setText(kdTransaksi);
        holder.tvTglTransaksi.setText(tglTransaksi);
        if (statusTransaksi.equals("BELUM MENGIRIM BUKTI TRANSFER")){
            holder.tvStatusPesanan.setText(statusTransaksi);
            holder.tvStatusPesanan.setTextColor(Color.parseColor("#D12F2B"));
            holder.tvStatusTransaksi.setTextColor(Color.parseColor("#D12F2B"));
            holder.tvStatusTransaksi.setText("Transaksi belum di verifikasi");
            holder.viewHistory.setBackgroundColor(Color.parseColor("#D12F2B"));
            holder.tvLihatDetailPesanan.setTextColor(Color.parseColor("#D12F2B"));
        } else if (statusTransaksi.equals("DIBATALKAN")){
            holder.tvStatusPesanan.setText(statusTransaksi);
            holder.tvStatusPesanan.setTextColor(Color.parseColor("#8b8b8b"));
            holder.tvStatusTransaksi.setTextColor(Color.parseColor("#8b8b8b"));
            holder.viewHistory.setBackgroundColor(Color.parseColor("#8b8b8b"));
            holder.tvStatusTransaksi.setText("Transaksi dibatalkan");
            holder.tvLihatDetailPesanan.setTextColor(Color.parseColor("#8b8b8b"));
        } else if (statusTransaksi.equals("TRANSAKSI BERHASIL")){
            holder.tvStatusPesanan.setText(statusTransaksi);
            holder.tvStatusPesanan.setTextColor(Color.parseColor("#009305"));
            holder.tvStatusTransaksi.setTextColor(Color.parseColor("#009305"));
            holder.viewHistory.setBackgroundColor(Color.parseColor("#009305"));
            holder.tvStatusTransaksi.setText("Transaksi berhasil");
            holder.tvLihatDetailPesanan.setTextColor(Color.parseColor("#009305"));
        } else if (statusTransaksi.equals("MENUNGGU VERIFIKASI")){
            holder.tvStatusPesanan.setText(statusTransaksi);
            holder.tvStatusPesanan.setTextColor(Color.parseColor("#f49a13"));
            holder.tvStatusTransaksi.setTextColor(Color.parseColor("#f49a13"));
            holder.viewHistory.setBackgroundColor(Color.parseColor("#f49a13"));
            holder.tvStatusTransaksi.setText("Menunggu Verifikasi dari Admin");
            holder.tvLihatDetailPesanan.setTextColor(Color.parseColor("#f49a13"));
        }  else if (statusTransaksi.equals("PESANAN SEDANG DI ANTAR")){
            holder.tvStatusPesanan.setText(statusTransaksi);
            holder.tvStatusPesanan.setTextColor(Color.parseColor("#29abda"));
            holder.tvStatusTransaksi.setTextColor(Color.parseColor("#29abda"));
            holder.viewHistory.setBackgroundColor(Color.parseColor("#29abda"));
            holder.tvStatusTransaksi.setText("Pesanan Anda sedang di antar");
            holder.tvLihatDetailPesanan.setTextColor(Color.parseColor("#29abda"));
        }

        holder.cvSearchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvStatusPesanan.getText().equals("BELUM MENGIRIM BUKTI TRANSFER")){
                    Intent intent = new Intent(context, DetailPembelianActivity.class);
                    intent.putExtra("kode_otomatis", item.getKd_transaksi());
                    context.startActivity(intent);
                } else if (holder.tvStatusPesanan.getText().equals("DIBATALKAN")){
                    Intent intent = new Intent(context, DetailTransaksiActivity.class);
                    intent.putExtra("kode_otomatis", item.getKd_transaksi());
                    intent.putExtra("tanggal", item.getTanggal());
                    context.startActivity(intent);
                } else if (holder.tvStatusPesanan.getText().equals("TRANSAKSI BERHASIL")){
                    Intent intent = new Intent(context, DetailTransaksiActivity.class);
                    intent.putExtra("kode_otomatis", item.getKd_transaksi());
                    intent.putExtra("tanggal", item.getTanggal());
                    context.startActivity(intent);
                } else if (holder.tvStatusPesanan.getText().equals("MENUNGGU VERIFIKASI")){
                    Intent intent = new Intent(context, DetailTransaksiActivity.class);
                    intent.putExtra("kode_otomatis", item.getKd_transaksi());
                    intent.putExtra("tanggal", item.getTanggal());
                    context.startActivity(intent);
                } else if (holder.tvStatusPesanan.getText().equals("PESANAN SEDANG DI ANTAR")){
                    Intent intent = new Intent(context, KonfirmasiActivity.class);
                    intent.putExtra("kode_otomatis", item.getKd_transaksi());
                    intent.putExtra("tanggal", item.getTanggal());
                    context.startActivity(intent);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return searchHistoryModelList.size();
    }

}