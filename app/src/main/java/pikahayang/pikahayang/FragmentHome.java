package pikahayang.pikahayang;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pikahayang.pikahayang.Adapter.PagerHomeAdapter;

public class FragmentHome extends Fragment implements AdapterView.OnItemClickListener{
    @BindView(R.id.vpHome)
    ViewPager vpHome;
    @BindView(R.id.tlHome)
    TabLayout tlHome;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);


        PagerHomeAdapter myPagerAdapter = new PagerHomeAdapter(getActivity().getSupportFragmentManager());
        vpHome.setAdapter(myPagerAdapter);

        tlHome.setupWithViewPager(vpHome);
//        tlHome.setTabGravity(TabLayout.GRAVITY_CENTER);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
