package pikahayang.pikahayang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import pikahayang.pikahayang.Adapter.SearchHistoryAdapter;
import pikahayang.pikahayang.Model.SearchHistoryModel;
import pikahayang.pikahayang.Model.SearchModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class SearchHistoryTransaksi extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        android.support.v7.widget.SearchView.OnQueryTextListener{
    @BindView(R.id.swipeSearchHistory)
    SwipeRefreshLayout swipeSearch;
    @BindView(R.id.rvSearchHistory)
    RecyclerView rvSearch;

    ProgressDialog pDialog;


    private static final String TAG = SearchHistoryTransaksi.class.getSimpleName();
    private List<SearchHistoryModel> searchModelList;
    private SearchHistoryAdapter mAdapter;

    private static String TAG_KD_TRANSAKSI = "kd_transaksi";
    private static String TAG_TANGGAL = "tanggal";
    private static String TAG_STATUS_TRANSAKSI = "status_transaksi";
    public static final String TAG_VALUE = "value";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        ButterKnife.bind(this);


        swipeSearch.setOnRefreshListener(this);


        searchModelList = new ArrayList<>();
        mAdapter = new SearchHistoryAdapter(SearchHistoryTransaksi.this, searchModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvSearch.setLayoutManager(mLayoutManager);
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

            JsonArrayRequest jArr = new JsonArrayRequest(URL.data_transaksi +"?id_users="+ new UserSession(getApplicationContext()).getId(), new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.e(TAG, response.toString());

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);

                            SearchHistoryModel item = new SearchHistoryModel();

                            item.setKd_transaksi(jsonObject.getString(TAG_KD_TRANSAKSI));
                            item.setStatus_transaksi(jsonObject.getString(TAG_STATUS_TRANSAKSI));
                            item.setTanggal(jsonObject.getString(TAG_TANGGAL));

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
        searchView.setQueryHint("Cari Kode Transaksi..");
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void cariData(final String keyword){
        pDialog = new ProgressDialog(SearchHistoryTransaksi.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.cari_data_transaksi, new Response.Listener<String>() {
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

                                SearchHistoryModel data = new SearchHistoryModel();

                                data.setKd_transaksi(object.getString(TAG_KD_TRANSAKSI));
                                data.setStatus_transaksi(object.getString(TAG_STATUS_TRANSAKSI));
                                data.setTanggal(object.getString(TAG_TANGGAL));

                                searchModelList.add(data);
                            }
                        } else {
                            ChocoBar.builder().setActivity(SearchHistoryTransaksi.this)
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

}
