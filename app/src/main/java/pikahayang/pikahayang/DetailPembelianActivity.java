package pikahayang.pikahayang;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pd.chocobar.ChocoBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pikahayang.pikahayang.ServerSide.URL;
import pikahayang.pikahayang.ServerSide.VolleyHandler;
import pikahayang.pikahayang.app.AppController;

public class DetailPembelianActivity extends AppCompatActivity {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.btnBatalkanPesanan)
    Button btnBatalkanPesanan;
    //    @BindView(R.id.tvTimer)
//    TextView tvTimer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cvTimer)
    CardView cvTimer;
    @BindView(R.id.tvKdTransaksi)
    TextView tvKdTransaksi;
    @BindView(R.id.tvStatusTransaksi)
    TextView tvStatusTransaksi;
    @BindView(R.id.ivBuktiTransfer)
    ImageView ivBuktiTransfer;
    @BindView(R.id.btnAmbilGambar)
    Button btnAmbilGambar;
    @BindView(R.id.tvKdotomatis)
    TextView tvKdotomatis;
    @BindView(R.id.tvJam)
    TextView tvJam;
    @BindView(R.id.tvMenit)
    TextView tvMenit;
    @BindView(R.id.tvDetik)
    TextView tvDetik;
    @BindView(R.id.txt_Hour)
    TextView txtHour;
    @BindView(R.id.LinearLayout2)
    android.widget.LinearLayout LinearLayout2;
    @BindView(R.id.txt_Minute)
    TextView txtMinute;
    @BindView(R.id.LinearLayout3)
    android.widget.LinearLayout LinearLayout3;
    @BindView(R.id.txt_Second)
    TextView txtSecond;
    @BindView(R.id.LinearLayout4)
    android.widget.LinearLayout LinearLayout4;
    @BindView(R.id.LinearLayout)
    android.widget.LinearLayout LinearLayout;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.btnUploadBukti)
    Button btnUploadBukti;


    private static final String TAG = DetailPembelianActivity.class.getSimpleName();
    private static String TAG_KD_TRANSAKSI = "kd_transaksi";
    private static String TAG_STATUS_TRANSAKSI = "status_transaksi";
    private static String TAG_BUKTI_TRANSAKSI = "bukti_transaksi";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    String statusTransaksi = "DIBATALKAN";
    String statusTransaksiUpload = "MENUNGGU VERIFIKASI";
    String kodeOtomatis, sekarang, batas;

    private Handler handler = new Handler();
    private Runnable runnable;
    View view;

    int Seconds, success;
    Intent intent;
    Uri fileUri;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    ProgressDialog pDialog;

    int bitmap_size = 100; // image quality 1 - 100;
    int max_resolution_image = 800;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembelian);
        ButterKnife.bind(this);

        // dari payment
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            kodeOtomatis = bundle.getString("kode_otomatis");
            tvKdotomatis.setText(kodeOtomatis);
        }

        readTanggal();
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPembelianActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        Intent intent = new Intent(DetailPembelianActivity.this, BerandaActivity.class);
        startActivity(intent);
        finish();
    }

    // UPLOAD BUKTI   
    @OnClick(R.id.btnUploadBukti)
    public void uploadBukti() {
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.upload_bukti_transfer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(DetailPembelianActivity.this);
                                dialog.setMessage("Sukses upload bukti transfer")
                                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        });
                                dialog.show();

                                //method kosong
                                kosong();

                            } else {
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(DetailPembelianActivity.this);
                                dialog.setMessage("Gagal upload bukti transfer, coba ulangi!")
                                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(DetailPembelianActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(TAG_BUKTI_TRANSAKSI, getStringImage(decoded));
                params.put(TAG_KD_TRANSAKSI, kodeOtomatis);
                params.put(TAG_STATUS_TRANSAKSI, statusTransaksiUpload);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void kosong() {
        ivBuktiTransfer.setImageResource(R.drawable.ic_photo_camera);
    }

    private void selectImage() {
        ivBuktiTransfer.setImageResource(0);
        final CharSequence[] items = {"Ambil dari Kamera", "Pilih dari Galeri",
                "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPembelianActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil dari Kamera")) {
                    requestCameraPermission();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Pilih dari Galeri")) {
                    requestStoragePermission();
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(DetailPembelianActivity.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        ivBuktiTransfer.setImageBitmap(decoded);
    }

    // Untuk resize bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Pikahayang");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_PKHY_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @OnClick(R.id.btnAmbilGambar)
    public void ambilGambar() {
        selectImage();
    }

    @OnClick(R.id.btnBatalkanPesanan)
    public void batalPesanan(final View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Membatalkan Pesanan..");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.batalkan_pesanan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("BATALKAN ", jsonObject.toString());

                    if (jsonObject.getInt(TAG_SUCCESS) == 1) {

                        returnStock();

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(DetailPembelianActivity.this);
                        dialog
                                .setMessage("Pesanan Anda telah dibatalkan")
                                .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                });
                        dialog.show();
                    } else {
                        ChocoBar.builder().setActivity(DetailPembelianActivity.this)
                                .setText("Gagal Membatalkan Pesanan!")
                                .setDuration(ChocoBar.LENGTH_INDEFINITE)
                                .setActionText(android.R.string.ok)
                                .red()  // in built green ChocoBar
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    VolleyHandler.handleVolleyError(view, error);
                    Log.e("Error", error.getMessage());
                    hideDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_KD_TRANSAKSI, kodeOtomatis.trim());
                params.put(TAG_STATUS_TRANSAKSI, statusTransaksi.trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void otomatisBatalPesanan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.batalkan_pesanan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("OTOMATIS BATAL ", jsonObject.toString());

                    returnStock();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("Error", error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_KD_TRANSAKSI, kodeOtomatis.trim());
                params.put(TAG_STATUS_TRANSAKSI, statusTransaksi.trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void returnStock() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.update_stock, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("RETUR STOCK ", jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("Error", error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(TAG_KD_TRANSAKSI, kodeOtomatis.trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    // READ TANGGAL
    private void readTanggal() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.read_tanggal + "?kd_transaksi=" + kodeOtomatis, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("READ TANGGAL", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String tanggal = jsonObject.getString("tanggal");

                    countDownStart(tanggal);

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

    // COUNT DOWN
    public void countDownStart(final String batas) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                try {
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();


                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");

                    c2.setTime(dateFormat.parse(batas));
                    c2.add(Calendar.DATE, 1);
                    c2.set(Calendar.SECOND,0);
                    String strdate1 = dateFormat.format(c1.getTime());
                    Date futureDate = c2.getTime();
                    Date currentDate = dateFormat.parse(strdate1);



                    long diff = futureDate.getTime()
                            - currentDate.getTime();
                    long hours = diff / (60 * 60 * 1000);
                    diff -= hours * (60 * 60 * 1000);
                    long minutes = diff / (60 * 1000);
                    diff -= minutes * (60 * 1000);
                    long seconds = diff / 1000;
                    if ((seconds > 0) || (hours > 0) || (minutes > 0)){
                        tvJam.setText("" + String.format("%02d", hours));
                        tvMenit.setText("" + String.format("%02d", minutes));
                        tvDetik.setText("" + String.format("%02d", seconds));
                    } else {
                        tvJam.setText("--");
                        tvMenit.setText("--");
                        tvDetik.setText("--");

                        otomatisBatalPesanan();
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailPembelianActivity.this, BerandaActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
