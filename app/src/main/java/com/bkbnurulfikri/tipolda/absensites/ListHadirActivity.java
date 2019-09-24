package com.bkbnurulfikri.tipolda.absensites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListHadirActivity extends AppCompatActivity  {

    private String TAG = ListHadirActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private PreferenceHelper preferenceHelper;
    SwipeRefreshLayout mSwipeRefreshLayout;

    // URL to get contacts JSON
    private static String url = "https://kb.nurulfikri.id/api/android/absensi_tes/daftarhadir.php?lok=";

    ArrayList<HashMap<String, String>> hadirList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listhadir);
        preferenceHelper = new PreferenceHelper(this);
        hadirList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        // Swipe Refresh Layout
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //new GetHadir().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        new GetHadir().execute();

    }

    /**
     * Async task class to get json by making HTTP call
     */
    @SuppressLint("StaticFieldLeak")
    private class GetHadir extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ListHadirActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url+preferenceHelper.getKodeLokasi());

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString("id");
                        String nonf = c.getString("nopeserta");
                        String nama = c.getString("nama");
                        String jam = c.getString("datetime");

                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> daftarhadir = new HashMap<>();

                        // adding each child node to HashMap key => value
                        daftarhadir.put("id", id);
                        daftarhadir.put("nopeserta", nonf);
                        daftarhadir.put("nama", nama);
                        daftarhadir.put("datetime", jam);

                        // adding contact to contact list
                        hadirList.add(daftarhadir);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Tidak ada data kehadiran hari ini : " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ListHadirActivity.this, hadirList,
                    R.layout.list_hadir, new String[]{"nopeserta", "nama", "datetime"}, new int[]{R.id.nonf,
                    R.id.nama, R.id.jam});

            lv.setAdapter(adapter);

            TextView total = (TextView) findViewById(R.id.total);
            total.setText("Total Hadir : " + String.valueOf(lv.getAdapter().getCount())+ " Peserta");

            mSwipeRefreshLayout.setRefreshing(false);

            Toast.makeText(getApplicationContext(), "Total Hadir : " + String.valueOf(lv.getAdapter().getCount()) + " Peserta", Toast.LENGTH_LONG).show();
        }

    }

}


