package pikahayang.pikahayang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import pikahayang.pikahayang.Adapter.HistoryAdapter;
import pikahayang.pikahayang.Model.HistoryModel;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.UserSession;
import pikahayang.pikahayang.app.AppController;

public class FragmentHistory extends Fragment implements AdapterView.OnItemClickListener {


    @BindView(R.id.llKosongHistory)
    LinearLayout llKosongHistory;
    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rlSearchView)
    RelativeLayout rlSearchView;
    @BindView(R.id.shimmer_history)
    ShimmerFrameLayout shimmerHistory;

    private List<HistoryModel> historyModelList;
    private HistoryAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        historyModelList = new ArrayList<>();
        mAdapter = new HistoryAdapter(getActivity(), historyModelList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(mLayoutManager);
        rvHistory.setAdapter(mAdapter);

        readHistory();

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void readHistory() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_history + "?id_users=" + new UserSession(getActivity()).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("HISTORY: ", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    if (jsonArray.length() == 0) {
                        llKosongHistory.setVisibility(View.VISIBLE);
                    } else {
                        llKosongHistory.setVisibility(View.GONE);
                    }

                    List<HistoryModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HistoryModel>>() {
                    }.getType());

                    historyModelList.clear();
                    historyModelList.addAll(items);

                    mAdapter.notifyDataSetChanged();

                    shimmerHistory.stopShimmerAnimation();
                    shimmerHistory.setVisibility(View.GONE);

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

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        null.unbind();
//    }

    @OnClick({R.id.etSearch, R.id.rlSearchView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                Intent intent = new Intent(getActivity(), SearchHistoryTransaksi.class);
                startActivity(intent);
                break;
            case R.id.rlSearchView:
                Intent intent2 = new Intent(getActivity(), SearchHistoryTransaksi.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerHistory.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmerHistory.stopShimmerAnimation();
        super.onPause();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        null.unbind();
//    }
}
