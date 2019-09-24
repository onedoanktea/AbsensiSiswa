package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;


public class ScanActivity extends AppCompatActivity  {

    public static TextView textViewScan,textHasil;
    private Button btnScanKartu,btnLihatAbsensi,btnSimpanData,btnScanKartu2,btnlogout;
    private ParseContent parseContent;
    private PreferenceHelper preferenceHelper;
    private final int ScanTask = 1;
    private static ImageView imgScanKartu;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        preferenceHelper = new PreferenceHelper(this);
        parseContent = new ParseContent(this);

        textViewScan = (TextView) findViewById(R.id.textViewScan);
        textHasil = (TextView) findViewById(R.id.textHasil);

        textHasil.setVisibility(View.INVISIBLE);
        textHasil.setText(preferenceHelper.getKodeh());

        btnScanKartu = (Button) findViewById(R.id.btnScanKartu);
        btnScanKartu2 = (Button) findViewById(R.id.btnScanKartu2);
        btnSimpanData = (Button) findViewById(R.id.btnSimpanData);
        btnLihatAbsensi = (Button) findViewById(R.id.btnLihatAbsensi);
        btnlogout = (Button) findViewById(R.id.btnLogout);

        imgScanKartu =  (ImageView) findViewById(R.id.imgScanKartu);

        btnScanKartu.setVisibility(View.INVISIBLE);
        btnScanKartu2.setVisibility(View.INVISIBLE);

        btnScanKartu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, ProsesScanActivity.class);
                startActivity(intent);
                textViewScan.setText("");
            }
        });

        btnScanKartu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, ProsesScan2Activity.class);
                startActivity(intent);
                textViewScan.setText("");
            }
        });

        imgScanKartu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, ProsesScan2Activity.class);
                startActivity(intent);
                textViewScan.setText("");
            }
        });

        btnLihatAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this,ListHadirActivity.class);
                startActivity(intent);
                textViewScan.setText("");
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnSimpanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    kirimhasilscan();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void kirimhasilscan() throws IOException, JSONException {

        if (!AndyUtils.isNetworkAvailable(ScanActivity.this)) {
            Toast.makeText(ScanActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        AndyUtils.showSimpleProgressDialog(ScanActivity.this);
        final HashMap<String, String> map = new HashMap<>();
        map.put(AndyConstants.Hadirs.IDNF, textViewScan.getText().toString());
        map.put(AndyConstants.Hadirs.KODEH, textHasil.getText().toString());

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] hadirs) {
                String response="";
                try {
                    HttpRequest req = new HttpRequest(AndyConstants.ServiceType.INPUT_HADIR);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss", result);
                onScanTaskCompleted(result, ScanTask);
            }
        }.execute();
    }

    private void onScanTaskCompleted(String response,int task) {
        Log.d("responsejson", response);
        AndyUtils.removeSimpleProgressDialog();  //will remove progress dialog
        switch (task) {
            case ScanTask:

                if (parseContent.isSuccess(response)) {

                    parseContent.saveInfo(response);
                    Toast.makeText(ScanActivity.this, "Alhamdulillah "+preferenceHelper.getIdnf()+" sudah hadir", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ScanActivity.this, parseContent.getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah yakin mau logout?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        preferenceHelper.putIsLogin(false);
                        Intent intent = new Intent(ScanActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        ScanActivity.this.finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        preferenceHelper.putIsLogin(true);
                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            moveTaskToBack(true);
        } else {
            Toast.makeText(getBaseContext(), "Tekan sekali lagi untuk menutup aplikasi!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
