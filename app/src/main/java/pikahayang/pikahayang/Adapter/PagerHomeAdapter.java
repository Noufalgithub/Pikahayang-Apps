package pikahayang.pikahayang.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pikahayang.pikahayang.FragmentAllProduk;
import pikahayang.pikahayang.FragmentBaju;
import pikahayang.pikahayang.FragmentDesainPlusKain;

public class PagerHomeAdapter extends FragmentStatePagerAdapter {

    public PagerHomeAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FragmentAllProduk();
            case 1: return new FragmentDesainPlusKain();
            case 2: return new FragmentBaju();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Semua Produk";
            case 1: return "Kain";
            case 2: return "Baju";
            default: return null;
        }
    }

}
