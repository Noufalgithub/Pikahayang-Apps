package pikahayang.pikahayang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.Adapter.DesainPlusKainAdapter;
import pikahayang.pikahayang.Model.DesainPlusKainModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class FragmentDesainPlusKain extends Fragment implements AdapterView.OnItemClickListener {


    View view;
    @BindView(R.id.llKosong)
    LinearLayout llKosong;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rlSearchView)
    RelativeLayout rlSearchView;
    @BindView(R.id.ibArrowDownJB)
    ImageButton ibArrowDownJB;
    @BindView(R.id.ibArrowUpJB)
    ImageButton ibArrowUpJB;
    @BindView(R.id.rbBatikTulis)
    RadioButton rbBatikTulis;
    @BindView(R.id.rbBatikCap)
    RadioButton rbBatikCap;
    @BindView(R.id.rbBatikPrint)
    RadioButton rbBatikPrint;
    @BindView(R.id.rgJB)
    RadioGroup rgJB;
    @BindView(R.id.rlRadioBtnJB)
    RelativeLayout rlRadioBtnJB;
    @BindView(R.id.btnOkeJB)
    Button btnOkeJB;
    @BindView(R.id.filterJenisBatik)
    CardView filterJenisBatik;
    @BindView(R.id.llFiltering)
    LinearLayout llFiltering;
    @BindView(R.id.shimmer_desainpluskain)
    ShimmerFrameLayout shimmer_desainpluskain;
    @BindView(R.id.rvDesainPlusKain)
    RecyclerView rvDesainPlusKain;
    @BindView(R.id.ibArrowDown)
    ImageButton ibArrowDown;
    @BindView(R.id.ibArrowUp)
    ImageButton ibArrowUp;
    @BindView(R.id.rbLakilaki)
    RadioButton rbLakilaki;
    @BindView(R.id.rbPerempuan)
    RadioButton rbPerempuan;
    @BindView(R.id.rgJK)
    RadioGroup rgJK;
    @BindView(R.id.rlRadioBtnJK)
    RelativeLayout rlRadioBtnJK;
    @BindView(R.id.btnOkeJK)
    Button btnOkeJK;
    @BindView(R.id.filterJK)
    CardView filterJK;

    private List<DesainPlusKainModel> desainPlusKainModelList;
    private DesainPlusKainAdapter mAdapter;


    private static final String TAG = FragmentDesainPlusKain.class.getSimpleName();
    private static String TAG_ID = "id";
    private static String TAG_ID_KATEGORI = "id_kategori";
    private static String TAG_NAMA_PRODUK = "nama_produk";
    private static String TAG_KODE_PRODUK = "kode_produk";
    private static String TAG_DESKRIPSI = "deskripsi";
    private static String TAG_HARGA = "harga";
    private static String TAG_IMAGE = "image";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_desain_plus_kain, container, false);
        ButterKnife.bind(this, view);

        etSearch.setText(null);


        desainPlusKainModelList = new ArrayList<>();
        mAdapter = new DesainPlusKainAdapter(getActivity(), desainPlusKainModelList);

        rvDesainPlusKain.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvDesainPlusKain.setAdapter(mAdapter);

        readDesainPlusKain();

        // jenis kelamin
        ibArrowUp.setVisibility(View.GONE);
        rlRadioBtnJK.setVisibility(View.GONE);
        btnOkeJK.setVisibility(View.GONE);

        // jenis batik
        ibArrowUpJB.setVisibility(View.GONE);
        rlRadioBtnJB.setVisibility(View.GONE);
        btnOkeJB.setVisibility(View.GONE);


        return view;
    }

    // JENIS KELAMIN ARROW DOWN
//    @OnClick(R.id.ibArrowDown)
//    public void setIbArrowDown() {
//        ibArrowDown.setVisibility(View.GONE);
//        ibArrowUp.setVisibility(View.VISIBLE);
//        rlRadioBtnJK.setVisibility(View.VISIBLE);
//        btnOkeJK.setVisibility(View.VISIBLE);
//    }

    // JENIS KELAMIN ARROW DOWN
//    @OnClick(R.id.filterJK)
//    public void setFilterJK() {
//        ibArrowDown.setVisibility(View.GONE);
//        ibArrowUp.setVisibility(View.VISIBLE);
//        rlRadioBtnJK.setVisibility(View.VISIBLE);
//        btnOkeJK.setVisibility(View.VISIBLE);
//    }

    // JENIS KELAMIN ARROW UP
//    @OnClick(R.id.ibArrowUp)
//    public void setIbArrowUp() {
//        ibArrowDown.setVisibility(View.VISIBLE);
//        ibArrowUp.setVisibility(View.GONE);
//        rlRadioBtnJK.setVisibility(View.GONE);
//        btnOkeJK.setVisibility(View.GONE);
//    }

    // JENIS BATIK ARROW DOWN
    @OnClick(R.id.ibArrowDownJB)
    public void setIbArrowDownJB() {
        ibArrowDownJB.setVisibility(View.GONE);
        ibArrowUpJB.setVisibility(View.VISIBLE);
        rlRadioBtnJB.setVisibility(View.VISIBLE);
        btnOkeJB.setVisibility(View.VISIBLE);
    }

    // JENIS BATIK ARROW DOWN
    @OnClick(R.id.filterJenisBatik)
    public void setFilterJenisBatik() {
        ibArrowDownJB.setVisibility(View.GONE);
        ibArrowUpJB.setVisibility(View.VISIBLE);
        rlRadioBtnJB.setVisibility(View.VISIBLE);
        btnOkeJB.setVisibility(View.VISIBLE);
    }

    // JENIS BATIK ARROW UP
    @OnClick(R.id.ibArrowUpJB)
    public void setIbArrowUpJB() {
        ibArrowDownJB.setVisibility(View.VISIBLE);
        ibArrowUpJB.setVisibility(View.GONE);
        rlRadioBtnJB.setVisibility(View.GONE);
        btnOkeJB.setVisibility(View.GONE);
    }
    // jenis kelamin button
//    @OnClick(R.id.btnOkeJK)
//    public void setFilterJk() {
//        int id = rgJK.getCheckedRadioButtonId();
//        switch (id) {
//            case R.id.rbLakilaki:
//                filterLakilaki();
//                break;
//            case R.id.rbPerempuan:
//                filterPerempuan();
//                break;
//        }
//    }

    // jenis batik button
    @OnClick(R.id.btnOkeJB)
    public void setFilterJB() {
        int id = rgJB.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbBatikTulis:
                filterBatikTulis();
                break;
            case R.id.rbBatikCap:
                filterBatikCap();
                break;
            case R.id.rbBatikPrint:
                filterBatikPrint();
                break;
        }
    }

    private void filterLakilaki() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_all_produk_lakilaki, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosong.setVisibility(View.VISIBLE);
                    } else {
                        llKosong.setVisibility(View.GONE);
                    }

                    List<DesainPlusKainModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DesainPlusKainModel>>() {
                    }.getType());

                    desainPlusKainModelList.clear();
                    desainPlusKainModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    shimmer_desainpluskain.stopShimmerAnimation();
                    shimmer_desainpluskain.setVisibility(View.GONE);
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

    private void filterBatikTulis() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_kain_batiktulis, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosong.setVisibility(View.VISIBLE);
                    } else {
                        llKosong.setVisibility(View.GONE);
                    }

                    List<DesainPlusKainModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DesainPlusKainModel>>() {
                    }.getType());

                    desainPlusKainModelList.clear();
                    desainPlusKainModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    shimmer_desainpluskain.stopShimmerAnimation();
                    shimmer_desainpluskain.setVisibility(View.GONE);

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

    private void filterBatikCap() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_kain_batikcap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosong.setVisibility(View.VISIBLE);
                    } else {
                        llKosong.setVisibility(View.GONE);
                    }

                    List<DesainPlusKainModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DesainPlusKainModel>>() {
                    }.getType());

                    desainPlusKainModelList.clear();
                    desainPlusKainModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    shimmer_desainpluskain.stopShimmerAnimation();
                    shimmer_desainpluskain.setVisibility(View.GONE);

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

    private void filterBatikPrint() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_kain_batikprint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosong.setVisibility(View.VISIBLE);
                    } else {
                        llKosong.setVisibility(View.GONE);
                    }

                    List<DesainPlusKainModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DesainPlusKainModel>>() {
                    }.getType());

                    desainPlusKainModelList.clear();
                    desainPlusKainModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    shimmer_desainpluskain.stopShimmerAnimation();
                    shimmer_desainpluskain.setVisibility(View.GONE);

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


    private void filterPerempuan() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_all_produk_perempuan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosong.setVisibility(View.VISIBLE);
                    } else {
                        llKosong.setVisibility(View.GONE);
                    }

                    List<DesainPlusKainModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DesainPlusKainModel>>() {
                    }.getType());

                    desainPlusKainModelList.clear();
                    desainPlusKainModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    shimmer_desainpluskain.stopShimmerAnimation();
                    shimmer_desainpluskain.setVisibility(View.GONE);


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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void readDesainPlusKain() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_desainpluskain, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Sukses: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosong.setVisibility(View.VISIBLE);
                    } else {
                        llKosong.setVisibility(View.GONE);
                    }

                    List<DesainPlusKainModel> desainPlusKainModels = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DesainPlusKainModel>>() {
                    }.getType());

                    desainPlusKainModelList.clear();
                    desainPlusKainModelList.addAll(desainPlusKainModels);

                    mAdapter.notifyDataSetChanged();

                    shimmer_desainpluskain.stopShimmerAnimation();
                    shimmer_desainpluskain.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_desainpluskain.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_desainpluskain.stopShimmerAnimation();
        super.onPause();
    }

    @OnClick(R.id.etSearch)
    public void pindahdesainpluskain(View view) {
        Intent intent = new Intent(getActivity(), SearchBatikDesainpluskain.class);
        startActivity(intent);
    }



//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        null.unbind();
//    }
}