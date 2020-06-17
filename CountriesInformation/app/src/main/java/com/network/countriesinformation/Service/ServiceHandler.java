package com.network.countriesinformation.Service;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.network.countriesinformation.Adapter.CustomAdapter;
import com.network.countriesinformation.Entity.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceHandler extends AsyncTask<String, Void, ArrayList> {
    String URL_GETCOUNTRIES = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=aporter&style=full";
    ContentValues callParams;

    ProgressDialog dialog;
    RecyclerView recyclerView;
    ServiceCaller serviceCaller = new ServiceCaller();

    Context context;
    ArrayList<Country> countries;
    CustomAdapter adapter;

    public ServiceHandler(Context context, RecyclerView recyclerView, ContentValues callParams, CustomAdapter customAdapter) {
        this.context = context;
        this.callParams = callParams;
        this.recyclerView = recyclerView;
        this.adapter = customAdapter;
    }

    public static final String DISPLAY = "display";
    static final String CREATE = "create";
    static final String UPDATE = "update";
    static final String DELETE = "delete";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Proccesing...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected ArrayList doInBackground(String... strings) {
        switch (strings[0]) {
            case DISPLAY:
                countries = new ArrayList<>();
                String json = serviceCaller.call(URL_GETCOUNTRIES, serviceCaller.GET, callParams);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject != null) {
                            JSONArray accounts = jsonObject.getJSONArray("geonames");
                            for (int i = 0; i < accounts.length(); i++) {
                                JSONObject object = (JSONObject) accounts.get(i);
                                Country country = new Country(object.getString("countryName"),
                                        Double.parseDouble(object.getString("population")),
                                        Double.parseDouble(object.getString("areaInSqKm")),
                                        object.getString("countryCode"));
                                countries.add(country);
                            }
                        } else {
                            Log.d("JSON Data", "JSON data's format is incorrect!");
                            Country country = new Country("JSON data's format is incorrect!", 0, 0, "0");
                            countries.add(country);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            case UPDATE:
                break;
            case CREATE:
                break;
            case DELETE:
                break;
            default:
                break;
        }
        return countries;
    }

    @Override
    protected void onPostExecute(ArrayList ret) {
        super.onPostExecute(ret);
        if (dialog.isShowing())
            dialog.dismiss();
        if (ret == null)
            Toast.makeText(context, "Error - Refresh again",
                    Toast.LENGTH_SHORT).show();
        loadData();
        Log.i("hien", countries.toString());
    }

    private void loadData() {
        if (countries == null) {
            return;
        }
//        List<String> data = new ArrayList<String>();
//        for (int i = 0; i < countries.size(); i++) {
//            Country country = countries.get(i);
//            data.add(country.countryCode + "-" + country.countryName + ": " + country.countryPopulation);
//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        // Tạo adapter cho listivew
        adapter = new CustomAdapter(context, countries);
        // Gắn adapter cho listview
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        // }
    }
}