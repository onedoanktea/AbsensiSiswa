package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;


public class ProsesScan3Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    private ZXingScannerView mScannerView;
    private ParseContent parseContent;
    private PreferenceHelper preferenceHelper;
    private final int Scan3Task = 1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] perms = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //Toast.makeText(this, "Opening camera", Toast.LENGTH_SHORT).show();
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
            mScannerView.setAutoFocus(true);
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions camera",
                    123, perms);
        }
        //mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        //mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        preferenceHelper = new PreferenceHelper(this);
        parseContent = new ParseContent(this);

        final MediaPlayer scanSoundMediaPlayer = MediaPlayer.create(this, R.raw.beep);

        Scan2Activity.textViewScan.setText(rawResult.getText());
        onBackPressed();
        scanSoundMediaPlayer.start();

        try {
            kirimhasilscan2();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    @SuppressLint("StaticFieldLeak")
    private void kirimhasilscan2() throws IOException, JSONException {

        if (!AndyUtils.isNetworkAvailable(ProsesScan3Activity.this)) {
            Toast.makeText(ProsesScan3Activity.this, "Mohon aktifkan koneksi internet untuk absensi siswa", Toast.LENGTH_SHORT).show();
            return;
        }
        AndyUtils.showSimpleProgressDialog(ProsesScan3Activity.this);
        final HashMap<String, String> map = new HashMap<>();
        map.put(AndyConstants.Hadirs.IDNF, Scan2Activity.textViewScan.getText().toString());
        map.put(AndyConstants.Hadirs.KODEH, preferenceHelper.getKodeh());

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] hadirs) {
                String response="";
                try {
                    HttpRequest req = new HttpRequest(AndyConstants.ServiceType.UPDATE_HADIR);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss", result);
                onScan2TaskCompleted(result, Scan3Task);
            }
        }.execute();
    }

    private void onScan2TaskCompleted(String response,int task) {
        Log.d("responsejson", response);
        AndyUtils.removeSimpleProgressDialog();  //will remove progress dialog
        switch (task) {
            case Scan3Task:

                if (parseContent.isSuccess(response)) {

                    parseContent.saveInfo(response);
                    Toast.makeText(ProsesScan3Activity.this, "Alhamdulillah ID "+Scan2Activity.textViewScan.getText()+" sudah hadir", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ScanActivity.this, ProsesScan2Activity.class);
                    //startActivity(intent);
                    //ScanActivity.textViewScan.setText("");
                }else {
                    Toast.makeText(ProsesScan3Activity.this, parseContent.getErrorMessage(response), Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(ScanActivity.this, ProsesScan2Activity.class);
                    // startActivity(intent);
                    // ScanActivity.textViewScan.setText("");
                }
        }
    }

}
