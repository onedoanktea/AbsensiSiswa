package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvname,tvhobby;
    private Button btnlogout,btnScanQR,btnLihatAbsensi,btnCekAbsen,btnStatistikHadir;
    private ParseContent parseContent;
    private PreferenceHelper preferenceHelper;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        parseContent = new ParseContent(this);
        preferenceHelper = new PreferenceHelper(this);

        //tvhobby = (TextView) findViewById(R.id.tvhobby);
        tvname = (TextView) findViewById(R.id.tvname);
        btnlogout = (Button) findViewById(R.id.btn);
        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnLihatAbsensi = (Button) findViewById(R.id.btnLihatAbsensi);
        btnCekAbsen = (Button) findViewById(R.id.btnCekAbsen);
        btnStatistikHadir = (Button) findViewById(R.id.btnStatistikHadir);

        btnLihatAbsensi.setVisibility(View.INVISIBLE);
        btnStatistikHadir.setVisibility(View.INVISIBLE);

        tvname.setText("Selamat Datang "+preferenceHelper.getName());
        //tvhobby.setText("Lokasi "+preferenceHelper.getHobby()+"("+preferenceHelper.getKodeLokasi()+")");

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnCekAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,CekAbsenActivity.class);
                startActivity(intent);
            }
        });

        btnStatistikHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,StatistikActivity.class);
                startActivity(intent);
            }
        });

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });

        btnLihatAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,SearchHadirActivity.class);
                startActivity(intent);
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
                        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
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
