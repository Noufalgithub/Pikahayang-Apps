package pikahayang.pikahayang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.pd.chocobar.ChocoBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pikahayang.pikahayang.Adapter.SearchAdapter;
import pikahayang.pikahayang.Model.SearchModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.app.AppController;

public class SearchBatikDesainpluskain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        android.support.v7.widget.SearchView.OnQueryTextListener{
    @BindView(R.id.swipeSearch)
    SwipeRefreshLayout swipeSearch;
    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;

    ProgressDialog pDialog;


    private static final String TAG = SearchBatikDesainpluskain.class.getSimpleName();
    private List<SearchModel> searchModelList;
    private SearchAdapter mAdapter;

    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_JENIS_BATIK = "jenis_batik";
    private static String TAG_JENIS_KELAMIN = "jenis_kelamin";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";
    public static final String TAG_VALUE = "value";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_batik);
        ButterKnife.bind(this);


        swipeSearch.setOnRefreshListener(this);

        searchModelList = new ArrayList<>();
        mAdapter = new SearchAdapter(getApplicationContext(),searchModelList);
        rvSearch.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rvSearch.setAdapter(mAdapter);

        swipeSearch.post(new Runnable() {
            @Override
            public void run() {
                swipeSearch.setRefreshing(true);
                callData();
            }
        });
    }

    private void callData(){
        searchModelList.clear();
        mAdapter.notifyDataSetChanged();
        swipeSearch.setRefreshing(true);

            JsonArrayRequest jArr = new JsonArrayRequest(URL.data_desainpluskain, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.e(TAG, response.toString());

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);

                            SearchModel item = new SearchModel();

                            item.setId(jsonObject.getString(TAG_ID));
                            item.setId_kategori(jsonObject.getString(TAG_ID_KATEGORI));
                            item.setNama_produk(jsonObject.getString(TAG_NAMA_PRODUK));
                            item.setKode_produk(jsonObject.getString(TAG_KODE_PRODUK));
                            item.setJenis_batik(jsonObject.getString(TAG_JENIS_BATIK));
                            item.setDeskripsi(jsonObject.getString(TAG_DESKRIPSI));
                            item.setHarga(jsonObject.getString(TAG_HARGA));
                            item.setImage(jsonObject.getString(TAG_IMAGE));

                            searchModelList.add(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    swipeSearch.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e(TAG, "Error: " + error.getMessage());
                    Snackbar.make(findViewById(android.R.id.content), error.getMessage(), Snackbar.LENGTH_LONG).show();
//                Toast.makeText(SearchBatik.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    swipeSearch.setRefreshing(false);
                }
            });
            AppController.getInstance().addToRequestQueue(jArr);
    }

    @Override
    public void onRefresh() {
        callData();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        cariData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari produk desain + kain..");
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void cariData(final String keyword){
        pDialog = new ProgressDialog(SearchBatikDesainpluskain.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.cari_data, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response: ", response.toString());

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int value = jsonObject.getInt(TAG_VALUE);

                        if (value == 1) {
                            searchModelList.clear();
                            mAdapter.notifyDataSetChanged();

                            String getObject = jsonObject.getString(TAG_RESULTS);
                            JSONArray jsonArray = new JSONArray(getObject);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                SearchModel data = new SearchModel();

                                data.setId(object.getString(TAG_ID));
                                data.setId_kategori(object.getString(TAG_ID_KATEGORI));
                                data.setNama_produk(object.getString(TAG_NAMA_PRODUK));
                                data.setKode_produk(object.getString(TAG_KODE_PRODUK));
                                data.setJenis_batik(object.getString(TAG_JENIS_BATIK));
                                data.setDeskripsi(object.getString(TAG_DESKRIPSI));
                                data.setHarga(object.getString(TAG_HARGA));
                                data.setImage(object.getString(TAG_IMAGE));

                                searchModelList.add(data);
                            }
                        } else {
                            ChocoBar.builder().setActivity(SearchBatikDesainpluskain.this)
                                    .setText("Data yang Anda cari tidak ada!")
                                    .setDuration(ChocoBar.LENGTH_INDEFINITE)
                                    .setActionText(android.R.string.ok)
                                    .red()  // in built green ChocoBar
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                    pDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e(TAG, "Error: " + error.getMessage());
                    Snackbar.make(findViewById(android.R.id.content), error.getMessage(), Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keyword", keyword);

                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(stringRequest);

    }

//    @Override
//    public void onBackPressed(){
//        Intent intent = new Intent(SearchBatik.this, BerandaActivity.class);
//        startActivity(intent);
//        finish();
//        super.onBackPressed();
//    }
}
