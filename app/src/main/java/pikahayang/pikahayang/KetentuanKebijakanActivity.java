package pikahayang.pikahayang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KetentuanKebijakanActivity extends AppCompatActivity {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketentuan_kebijakan);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ibBack)
    public void onViewClicked() {
        onBackPressed();
        finish();
    }
}
