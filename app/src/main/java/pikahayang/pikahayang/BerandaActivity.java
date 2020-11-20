package pikahayang.pikahayang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pd.chocobar.ChocoBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class BerandaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.ibShoppingCart)
    ImageButton ibShoppingCart;
    @BindView(R.id.rlShoppingCart)
    RelativeLayout rlShoppingCart;
    @BindView(R.id.tvCountNotifCart)
    TextView tvCountNotifCart;
    @BindView(R.id.rlCountCart)
    RelativeLayout rlCountCart;


    Handler mHandler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new FragmentHome();
                    break;
                case R.id.navigation_history:
                    fragment = new FragmentHistory();
                    break;
                case R.id.navigation_akun:
                    fragment = new FragmentAkun();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        ButterKnife.bind(this);

        Log.e("TOKEN", new UserSession(getApplicationContext()).getToken());

        readCount();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FragmentHome());

        rlCountCart.setVisibility(View.INVISIBLE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("broadcast"));

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            readCount();
        }
    };


    // method untuk load fragment yang sesuai
    private boolean loadFragment (Fragment fragment){
            if (fragment != null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flBeranda, fragment)
                        .commit();
                return true;
            }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.navigation_home:
                fragment = new FragmentHome();
                break;
            case R.id.navigation_history:
                fragment = new FragmentHistory();
                break;
            case R.id.navigation_akun:
                fragment = new FragmentAkun();
                break;
        }
        return loadFragment(fragment);
    }

    @OnClick(R.id.ibShoppingCart)
    public void pindahcart(){
        Intent intent = new Intent(BerandaActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.rlShoppingCart)
    public void rlpindahcart(){
        Intent intent = new Intent(BerandaActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        readCount();
        super.onResume();
    }

    public void readCount(){
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.count_cart + "?id=" + new UserSession(getApplicationContext()).getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("Sukses: ", jsonObject.toString());

                        String total = jsonObject.getString("count(id_users)");
                        tvCountNotifCart.setText(total);

                        int totalInt = Integer.parseInt(total);

                        if (totalInt > 0){
                            rlCountCart.setVisibility(View.VISIBLE);
                        } else {
                            rlCountCart.setVisibility(View.INVISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            AppController.getInstance().addToRequestQueue(stringRequest);
        }


}
