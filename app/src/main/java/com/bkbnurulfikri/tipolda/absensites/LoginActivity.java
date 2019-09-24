package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etusername, etpassword;
    private Button btnlogin;
 //   private TextView notif;
    private ParseContent parseContent;
    private final int LoginTask = 1;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        parseContent = new ParseContent(this);
        preferenceHelper = new PreferenceHelper(this);

        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);

        btnlogin = (Button) findViewById(R.id.btn);
 //       notif = (TextView) findViewById(R.id.notif);

 //       tvreg.setOnClickListener(new View.OnClickListener() {
 //           @Override
 //           public void onClick(View v) {
 //               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
 //               startActivity(intent);
 //           }
 //       });

        if(preferenceHelper.getIsLogin()) {
            String lev = preferenceHelper.getLevel();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(lev, "top")) {
                    Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                } else if (Objects.equals(lev, "oke")) {
                    Intent intent = new Intent(LoginActivity.this, Scan2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                }
            }
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void login() throws IOException, JSONException {

        if (!AndyUtils.isNetworkAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, "Mohon aktifkan jaringan internet!", Toast.LENGTH_SHORT).show();
            return;
        }
        AndyUtils.showSimpleProgressDialog(LoginActivity.this);
        final HashMap<String, String> map = new HashMap<>();
        map.put(AndyConstants.Params.USERNAME, etusername.getText().toString());
        map.put(AndyConstants.Params.PASSWORD, etpassword.getText().toString());
        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    HttpRequest req = new HttpRequest(AndyConstants.ServiceType.LOGIN);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss", result);
                onTaskCompleted(result,LoginTask);
            }
        }.execute();
    }

    private void onTaskCompleted(String response,int task) {
        Log.d("responsejson", response);
        AndyUtils.removeSimpleProgressDialog();  //will remove progress dialog
        switch (task) {
            case LoginTask:
                if (parseContent.isSuccess(response)) {
                    parseContent.saveInfo(response);
                    preferenceHelper.loginExpired();
                    Toast.makeText(LoginActivity.this, "Login Berhasil !", Toast.LENGTH_SHORT).show();

                    //Intent intent = new Intent(LoginActivity.this,ScanActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //startActivity(intent);
                    //this.finish();

                    String lev = preferenceHelper.getLevel();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if(Objects.equals(lev, "top")) {
                            Intent intent = new Intent(LoginActivity.this,ScanActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            this.finish();
                        } else if(Objects.equals(lev, "oke")) {
                            Intent intent = new Intent(LoginActivity.this,Scan2Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            this.finish();
                        }
                    }

                }else {
                    Toast.makeText(LoginActivity.this, parseContent.getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
        }
    }
}
