package pikahayang.pikahayang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class SpashScreenActivity extends AppCompatActivity {
//    @BindView(R.id.tvSplashScreen)
//    TextView tvSplashScreen;
    @BindView(R.id.ivLogoSplash)
    ImageView ivLogoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        ButterKnife.bind(this);


        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        ivLogoSplash.startAnimation(myanim);

        final Intent intent = new Intent(this, BerandaActivity.class);
        Thread timer = new Thread(){
            public void run () {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
