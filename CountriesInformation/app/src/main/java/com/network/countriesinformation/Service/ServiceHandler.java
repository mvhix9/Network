package com.network.countriesinformation.Service;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.network.countriesinformation.Entity.Country;
import com.network.countriesinformation.Interface.MainActivityInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceHandler extends AsyncTask<String, Void, ArrayList> {

    // mấy cái biến như này thì phải để private có phải ko ghi nó cũng auto private ko , ko
    private String URL_GETCOUNTRIES = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&username=aporter&style=full";
    private ContentValues callParams;

    private ProgressDialog dialog;
    private ServiceCaller serviceCaller = new ServiceCaller();

    private Context context;
    private ArrayList<Country> countries;
    private MainActivityInterface mInterface;

    public ServiceHandler(Context context, MainActivityInterface mainActivityInterface, ContentValues callParams) {
        this.context = context;
        this.callParams = callParams;
        this.mInterface = mainActivityInterface;
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
                                        object.getString("countryCode"),object.getString("continentName"),object.getString("currencyCode"),object.getString("capital"));
                                countries.add(country);
                            }
                        } else {
                            Log.d("JSON Data", "JSON data's format is incorrect!");
                            Country country = new Country("JSON data's format is incorrect!", 0, 0, "0","0","0","0");
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
    }

    private void loadData() {
        if (countries == null) {
            return;
        }
        Log.i("Hien","loadData");
        mInterface.setNewAdaterCountryList(countries);
    }
}