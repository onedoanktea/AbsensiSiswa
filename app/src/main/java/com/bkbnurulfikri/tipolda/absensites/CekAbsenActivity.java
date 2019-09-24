package com.bkbnurulfikri.tipolda.absensites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;


public class CekAbsenActivity extends AppCompatActivity  {

    public static TextView textViewSearch,Searching;
    private  Button btnUbahSearch,btnKirimSearch;
    private PreferenceHelper preferenceHelper;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_absen);

        preferenceHelper = new PreferenceHelper(this);

        textViewSearch = (TextView) findViewById(R.id.textViewSearch);
        btnUbahSearch = (Button) findViewById(R.id.btnUbahSearch);
        btnKirimSearch = (Button) findViewById(R.id.btnKirimSearch);

        btnUbahSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosescekabsen();
                textViewSearch.setText("");
            }
        });

        btnKirimSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CekAbsenActivity.this,ListHadir2Activity.class);
                startActivity(intent);
                //textViewSearch.setText("");
            }
        });

    }

    private void prosescekabsen(){
        final EditText textCariSiswa = new EditText(context);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Masukan nonf siswa atau scan kartu");
        alertDialogBuilder.setView(textCariSiswa);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String taskCari = String.valueOf(textCariSiswa.getText());

                        if( textCariSiswa.getText().toString().trim().equals("")){
                            Toast.makeText(CekAbsenActivity.this, "No nf masih kosong!", Toast.LENGTH_SHORT).show();
                            return;

                        }else{
                            Searching = (TextView) findViewById(R.id.textViewSearch);
                            Searching.setText(taskCari);
                            Intent intent = new Intent(CekAbsenActivity.this,ListHadir2Activity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                });

        alertDialogBuilder.setNegativeButton("Scan Kartu",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //preferenceHelper.putIsLogin(true);
                        Intent intent = new Intent(CekAbsenActivity.this, ProsesScan3Activity.class);
                        startActivity(intent);
                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
