package com.chandrasalim.jualbuku.pembeli;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.chandrasalim.jualbuku.R;
import com.chandrasalim.jualbuku.adapter.AdapterBuku;
import com.chandrasalim.jualbuku.model.ModelBuku;
import com.chandrasalim.jualbuku.server.BaseURL;
import com.chandrasalim.jualbuku.session.PrefSetting;
import com.chandrasalim.jualbuku.session.SessionManager;
import com.chandrasalim.jualbuku.users.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomePembeliActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    AdapterBuku adapter;
    ListView list;

    ArrayList<ModelBuku> newsList = new ArrayList<ModelBuku>();
    private RequestQueue mRequestQueue;

    FloatingActionButton floatingExit;

    SessionManager session;
    SharedPreferences prefs;
    PrefSetting prefSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pembeli);

        prefSetting = new PrefSetting(this);
        prefs = prefSetting.getSharePreference();

        session = new SessionManager(HomePembeliActivity.this);

        prefSetting.isLogin(session, prefs);

        getSupportActionBar().setTitle("Data Buku");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.array_list);

        floatingExit = (FloatingActionButton) findViewById(R.id.exit);

        newsList.clear();
        adapter = new AdapterBuku(HomePembeliActivity.this, newsList);
        list.setAdapter(adapter);
        getAllBuku();

        floatingExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                session.setSessid(0);
                Intent i = new Intent(HomePembeliActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getAllBuku() {
        // Pass second argument as "null" for GET requests
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, BaseURL.dataBuku, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            boolean status = response.getBoolean("error");
                            if (status == false) {
                                Log.d("data buku = ", response.toString());
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final ModelBuku buku = new ModelBuku();
                                    final String _id = jsonObject.getString("_id");
                                    final String judulBuku = jsonObject.getString("judulBuku");
                                    final String kodeBuku = jsonObject.getString("kodeBuku");
                                    final String pengarng = jsonObject.getString("pengarang");
                                    final String penerbit = jsonObject.getString("penerbit");
                                    final String hargaBuku = jsonObject.getString("hargaBuku");
                                    final String tahunTerbit = jsonObject.getString("tahunTerbit");
                                    final String gamabr = jsonObject.getString("gambar");
                                    buku.setKodeBuku(kodeBuku);
                                    buku.setJudulBuku(judulBuku);
                                    buku.setPengarang(pengarng);
                                    buku.setPenerbit(penerbit);
                                    buku.setHargaBuku(hargaBuku);
                                    buku.setTahunTerbit(tahunTerbit);
                                    buku.setGambar(gamabr);
                                    buku.set_id(_id);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(HomePembeliActivity.this, DetailPembeli.class);
                                            a.putExtra("kodeBuku", newsList.get(position).getKodeBuku());
                                            a.putExtra("_id", newsList.get(position).get_id());
                                            a.putExtra("judulBuku", newsList.get(position).getJudulBuku());
                                            a.putExtra("pengarang", newsList.get(position).getPengarang());
                                            a.putExtra("penerbit", newsList.get(position).getPenerbit());
                                            a.putExtra("hargaBuku", newsList.get(position).getHargaBuku());
                                            a.putExtra("tahunTerbit", newsList.get(position).getTahunTerbit());
                                            a.putExtra("gambar", newsList.get(position).getGambar());
                                            startActivity(a);
                                        }
                                    });
                                    newsList.add(buku);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
