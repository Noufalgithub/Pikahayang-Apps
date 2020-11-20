package pikahayang.pikahayang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.pd.chocobar.ChocoBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.ServerSide.UserSession;

public class FragmentAkun extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.etNamaAkun)
    AppCompatEditText etNamaAkun;
    @BindView(R.id.etEmailAkun)
    AppCompatEditText etEmailAkun;
    @BindView(R.id.etAlamatAkun)
    AppCompatEditText etAlamatAkun;
    @BindView(R.id.etNoHpAkun)
    AppCompatEditText etNoHpAkun;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnEditAkun)
    Button btnEditAkun;
    @BindView(R.id.rlAkunAtas)
    RelativeLayout rlAkunAtas;
    @BindView(R.id.rlAkunBiodata)
    RelativeLayout rlAkunBiodata;
    @BindView(R.id.btnMasuk)
    Button btnMasuk;
    @BindView(R.id.btnDaftar)
    Button btnDaftar;

    ProgressDialog progressDialog;

    public static final String TAG_ID_USERS = "id_users";
    public static final String TAG_NAMA = "name";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NO_HP = "no_hp";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    @BindView(R.id.llUbahPassword)
    LinearLayout llUbahPassword;
    @BindView(R.id.llBantuan)
    LinearLayout llBantuan;
    @BindView(R.id.llAtas)
    LinearLayout llAtas;
    @BindView(R.id.svAkun)
    ScrollView svAkun;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_akun, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        if (new UserSession(getActivity()).getIsLogin()) {
            svAkun.setVisibility(View.VISIBLE);
            rlAkunAtas.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            rlAkunAtas.setVisibility(View.VISIBLE);
            svAkun.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }

        etNamaAkun.setText(new UserSession(getActivity()).getName());
        etEmailAkun.setText(new UserSession(getActivity()).getEmail());
        etAlamatAkun.setText(new UserSession(getActivity()).getAlamat());
        etNoHpAkun.setText(new UserSession(getActivity()).getNoHp());

        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.btnMasuk)
    public void pindah() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnDaftar)
    public void pindahkedaftar() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnEditAkun)
    public void editakun() {
        Intent intent = new Intent(getActivity(), SettingAkun.class);
        startActivity(intent);
    }

    @OnClick(R.id.llBantuan)
    public void setLlBantuan(){
        Intent intent = new Intent(getActivity(), BantuanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnLogout)
    public void logout() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Logout")
                .setIcon(R.drawable.ic_info)
                .setMessage("Apakah Anda yakin ingin keluar ?")
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserSession x = new UserSession(getActivity());
                        x.setIsLogin(false);
                        x.clearSession();

                        btnLogout.setVisibility(View.GONE);

                        ChocoBar.builder().setActivity(getActivity())
                                .setText("Berhasil Logout")
                                .setDuration(ChocoBar.LENGTH_SHORT)
                                .setIcon(R.drawable.ic_check_white)
                                .setTextColor(getResources().getColor(R.color.putih))
                                .green()  // in built green ChocoBar
                                .show();

                        Intent intent = new Intent(getActivity(), BerandaActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick(R.id.llUbahPassword)
    public void ubahpass() {
        Intent intent = new Intent(getActivity(), UbahPasswordActivity.class);
        startActivity(intent);

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        null.unbind();
//    }
}