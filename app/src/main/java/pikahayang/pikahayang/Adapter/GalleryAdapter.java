package pikahayang.pikahayang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pikahayang.pikahayang.Detail_produk;
import pikahayang.pikahayang.Model.GalleryModel;
import pikahayang.pikahayang.R;
import pikahayang.pikahayang.ServerSide.URL;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private Context context;
    private List<GalleryModel> galleryModelList;

//    private static String TAG_IMAGE = "image";

    String image;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvListGallery)
        CardView cvListGallery;
        @BindView(R.id.ivListGaleri)
        ImageView ivListGaleri;

        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GalleryAdapter(Context context, List<GalleryModel> galleryModels){
        this.context = context;
        this.galleryModelList = galleryModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_gallery, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, final int position){

        final GalleryModel item = galleryModelList.get(position);

        image = URL.image + item.getImage();

        Picasso.with(context)
                .load(image)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_error)
                .into(holder.ivListGaleri);
        holder.cvListGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image = URL.image + item.getImage();
                Intent intent = new Intent("broadcast");
                intent.putExtra("gambar", image);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryModelList.size();
    }

}