package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Scan2Activity extends AppCompatActivity  {

    public static TextView textViewScan,textJudul;
    private Button btnLihatAbsensi,btnlogout;
    private PreferenceHelper preferenceHelper;
    private static ImageView imgScanKartu;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan2);

        preferenceHelper = new PreferenceHelper(this);

        textViewScan = (TextView) findViewById(R.id.textViewScan);
        textJudul = (TextView) findViewById(R.id.textJudul);

        //textHasil.setVisibility(View.INVISIBLE);
        textJudul.setText("ABSENSI "+preferenceHelper.getName());

        btnLihatAbsensi = (Button) findViewById(R.id.btnLihatAbsensi);
        btnlogout = (Button) findViewById(R.id.btnLogout);

        imgScanKartu =  (ImageView) findViewById(R.id.imgScanKartu);

        imgScanKartu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scan2Activity.this, ProsesScan3Activity.class);
                startActivity(intent);
                textViewScan.setText("");
            }
        });

        btnLihatAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scan2Activity.this,ListHadir2Activity.class);
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
                        Intent intent = new Intent(Scan2Activity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Scan2Activity.this.finish();
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
