package pikahayang.pikahayang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Detail_produk;
import pikahayang.pikahayang.Model.BajuModel;
import pikahayang.pikahayang.Model.ServiceKurirModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;

public class ServiceKurirAdapter extends RecyclerView.Adapter<ServiceKurirAdapter.MyViewHolder> {
    private Context context;
    private List<ServiceKurirModel> serviceKurirModelList;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";

    String service, estimasiHari, biaya;

    DecimalFormat formatRupiah;

    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvService)
        TextView tvService;
        @BindView(R.id.tvEstimasiHari)
        TextView tvEstimasiHari;
        @BindView(R.id.tvOngkir)
        TextView tvOngkir;
        @BindView(R.id.cvServiceKurir)
        CardView cvServiceKurir;
        @BindView(R.id.ivCheck)
        ImageView ivCheck;

        public RadioButton rbService;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);

            rbService = (RadioButton) view.findViewById(R.id.rbService);
            rbService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

    }

    public ServiceKurirAdapter(Context context, List<ServiceKurirModel> items){
        this.context = context;
        this.serviceKurirModelList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_service_kurir, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){

        formatRupiah();

        final ServiceKurirModel item = serviceKurirModelList.get(position);

        holder.rbService.setChecked(lastSelectedPosition == position);

        service = item.getService();
        estimasiHari = item.getEtd();
        biaya = item.getValue();

        holder.tvService.setText(service);
        holder.tvEstimasiHari.setText(estimasiHari);
        holder.tvOngkir.setText(formatRupiah.format(Long.valueOf(biaya)));

        if (holder.rbService.isChecked()) {
            Intent intent = new Intent("broadcast");
            intent.putExtra("biaya", String.valueOf(biaya));
            intent.putExtra("service", service);
            intent.putExtra("duration", estimasiHari);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

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
        return serviceKurirModelList.size();
    }

}