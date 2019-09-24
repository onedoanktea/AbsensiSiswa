package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.media.MediaPlayer;
import com.google.zxing.Result;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;


public class ProsesScan2Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    private ZXingScannerView mScannerView;
    private ParseContent parseContent;
    private PreferenceHelper preferenceHelper;
    private final int Scan2Task = 1;

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

        ScanActivity.textViewScan.setText(rawResult.getText());
        onBackPressed();
        scanSoundMediaPlayer.start();
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

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

        if (!AndyUtils.isNetworkAvailable(ProsesScan2Activity.this)) {
            Toast.makeText(ProsesScan2Activity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        AndyUtils.showSimpleProgressDialog(ProsesScan2Activity.this);
        final HashMap<String, String> map = new HashMap<>();
        map.put(AndyConstants.Hadirs.IDNF, ScanActivity.textViewScan.getText().toString());
        map.put(AndyConstants.Hadirs.KODEH, preferenceHelper.getKodeh());

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
                onScan2TaskCompleted(result, Scan2Task);
            }
        }.execute();
    }

    private void onScan2TaskCompleted(String response,int task) {
        Log.d("responsejson", response);
        AndyUtils.removeSimpleProgressDialog();  //will remove progress dialog
        switch (task) {
            case Scan2Task:

                if (parseContent.isSuccess(response)) {

                    parseContent.saveInfo(response);
                    Toast.makeText(ProsesScan2Activity.this, "Alhamdulillah ID "+ScanActivity.textViewScan.getText()+" sudah hadir", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ScanActivity.this, ProsesScan2Activity.class);
                    //startActivity(intent);
                    //ScanActivity.textViewScan.setText("");
                }else {
                    Toast.makeText(ProsesScan2Activity.this, parseContent.getErrorMessage(response), Toast.LENGTH_SHORT).show();
                   // Intent intent = new Intent(ScanActivity.this, ProsesScan2Activity.class);
                   // startActivity(intent);
                   // ScanActivity.textViewScan.setText("");
                }
        }
    }

}
