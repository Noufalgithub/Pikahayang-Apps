package pikahayang.pikahayang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BantuanActivity extends AppCompatActivity {


    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivPeople)
    ImageView ivPeople;
    @BindView(R.id.rlHubungiKami)
    RelativeLayout rlHubungiKami;
    @BindView(R.id.ivKetent)
    ImageView ivKetent;
    @BindView(R.id.rlKetentuan)
    RelativeLayout rlKetentuan;
    @BindView(R.id.ivInfo)
    ImageView ivInfo;
    @BindView(R.id.rlInfoAplikasi)
    RelativeLayout rlInfoAplikasi;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.ibBack, R.id.rlHubungiKami, R.id.rlKetentuan, R.id.rlInfoAplikasi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                onBackPressed();
                finish();
                break;
            case R.id.rlHubungiKami:
                intent = new Intent(BantuanActivity.this, HubungiKamiActivity.class);
                startActivity(intent);
                break;
            case R.id.rlKetentuan:
                intent = new Intent(BantuanActivity.this, KetentuanKebijakanActivity.class);
                startActivity(intent);
                break;
            case R.id.rlInfoAplikasi:
                intent = new Intent(BantuanActivity.this, InfoAplikasiActivity.class);
                startActivity(intent);
                break;
        }
    }
}
