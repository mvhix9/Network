package com.network.countriesinformation;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.network.countriesinformation.Adapter.CustomAdapter;
import com.network.countriesinformation.Entity.Country;
import com.network.countriesinformation.Interface.MainActivityInterface;
import com.network.countriesinformation.Service.ServiceHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MainActivityInterface {

    public enum sortType{
        ASC_COUNTRY,
        DESC_COUNTRY
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CustomAdapter mAdapter;

    private ServiceHandler serviceHandler;

    private List<Country> mAllCountries;
    private static sortType mSortType;
    private static boolean flagForSort = false;
    private static String mSearch;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.i("Hien", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState == null)
        {
            getAllCountries();
        }
    }

    private void initView()
    {
        if (mAllCountries == null)
            mAllCountries = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
                getAllCountries();
            }
        });
    }

    protected boolean checkInternetConnection(){
        Log.i("Hien" , "checkInternetConnection");
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo!= null && networkInfo.isAvailable() && networkInfo.isConnected())
            return true;
        else{
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.i("Hien","onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.options_menu, menu);

        // searchview on action bar
        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        SearchView searchView       = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        // search text
        // if text not empty
        if (!TextUtils.isEmpty(mSearch)) {
            // set the text in SearchView's input
            // uses for storing the text if the rotate happens
            searchView.setQuery(mSearch, false);
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.sortAsc:
                mSortType = sortType.ASC_COUNTRY;
                sortCountries();
                return true;
            case R.id.sortDes:
                mSortType = sortType.DESC_COUNTRY;
                sortCountries();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        mSearch = newText;
        if(flagForSort)
        {
            sortCountries();
        }
        return false;
    }
    
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("Hien", "onSaveInstanceState called");

        // put all into bundle
        ArrayList<Country> allCountries = (ArrayList<Country>) mAdapter.getAllItem();
        ArrayList<Country> presentationCountries = (ArrayList<Country>) mAdapter.getAllPresentationItem();
        outState.putParcelableArrayList("allCountries", allCountries);
        outState.putParcelableArrayList("presentationCountries", presentationCountries);
        outState.putBoolean("sortFlag", flagForSort);
        outState.putSerializable("sortType", mSortType);
        if(!TextUtils.isEmpty(mSearch))
        {
            outState.putString("search", mSearch);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Hien", "onDestroy called");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Hien", "onRestoreInstanceState called");

        // get from bundle
        boolean isSorted = savedInstanceState.getBoolean("sortFlag");
        String search = savedInstanceState.getString("search");
        mAllCountries = savedInstanceState.getParcelableArrayList("allCountries");
        List<Country> presentation = savedInstanceState.getParcelableArrayList("presentationCountries");

        mAdapter = new CustomAdapter(this, presentation, mAllCountries);
        mRecyclerView.setAdapter(mAdapter);
        mSearch = search;
    }

    @Override
    public void setNewAdapterCountryList(List<Country> list) {
        Log.i("Hien", "setNewAdapterCountryList");
        mAllCountries = list;
        mAdapter = new CustomAdapter(this, mAllCountries);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getAllCountries() {
        Log.i("Hien", "getAllCountries");
        if(checkInternetConnection())
        {
            Log.i("Hien", "Internet");
            ContentValues callParams = new ContentValues();
            callParams.put("username","hihi");
            serviceHandler = new ServiceHandler(MainActivity.this, this, callParams);
            serviceHandler.execute(ServiceHandler.DISPLAY);
        }
        else
        {
            Log.i("Hien", "Non-Internet");
            mAllCountries = new ArrayList<>();
            mAdapter = new CustomAdapter(this, mAllCountries);
            mRecyclerView.setAdapter(mAdapter);
            Toast.makeText(this, "Please check your Internet connection" , Toast.LENGTH_SHORT).show();
        }
    }
    
    private void sortCountries()
    {
        flagForSort = true;
        Log.i("Hien","sortCountry");
        if(mSortType == sortType.ASC_COUNTRY)
        {
            Collections.sort(mAdapter.getAllPresentationItem());
        }
        else
        {
            Collections.sort(mAdapter.getAllPresentationItem(), Collections.reverseOrder());
        }
        mRecyclerView.setAdapter(mAdapter);
    }
}
