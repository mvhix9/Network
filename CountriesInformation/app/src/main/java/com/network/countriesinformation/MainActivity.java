package com.network.countriesinformation;

import android.app.SearchManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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
    private List<Country> mAllCountry = new ArrayList<>();
    private static sortType mSortType;
    private static boolean flagForSort = false;
    private static String mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Hien", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mAllCountry == null)
            mAllCountry = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null)
        {

        }
        else
        {
            getAllCountries();
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
        if (!TextUtils.isEmpty(mSearch)) {

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
                sortCountry();
                return true;
            case R.id.sortDes:
                mSortType = sortType.DESC_COUNTRY;
                sortCountry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TO-DO
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // TO-DO
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        mSearch = newText;
        if(flagForSort)
        {
            sortCountry();
        }
        return false;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("Hien", "onSaveInstanceState called");
        ArrayList<Country> allCountry = (ArrayList<Country>) mAdapter.getAllItem(); // all countries not country
        ArrayList<Country> presentationCountry = (ArrayList<Country>) mAdapter.getAllPresentationItem(); // presentationCountries
        outState.putParcelableArrayList("allCountry", allCountry);
        outState.putParcelableArrayList("presentationCountry", presentationCountry);
        outState.putBoolean("Sort", flagForSort);
        outState.putSerializable("SortType", mSortType);
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
        boolean isSorted = savedInstanceState.getBoolean("Sort");
        String search = savedInstanceState.getString("search");
        mAllCountry = savedInstanceState.getParcelableArrayList("allCountry");
        mAdapter = new CustomAdapter(this,mAllCountry);
        List<Country> presentation = savedInstanceState.getParcelableArrayList("presentationCountry");
        mAdapter = new CustomAdapter(this, presentation,mAllCountry);
        mRecyclerView.setAdapter(mAdapter);
        mSearch = search;
//        if(isSorted)
//        {
//            mSortType =  (sortType) savedInstanceState.getSerializable("SortType");
//            sortCountry();
//        }
//        if(!TextUtils.isEmpty(search))
//        {
//            mAdapter.filter(search);
//            mRecyclerView.setAdapter(mAdapter);
//        }
    }

    private void getAllCountries() {
        Log.i("Hien", "getAllCountries");
        ContentValues callParams = new ContentValues();
        callParams.put("username","wtf");
        serviceHandler = new ServiceHandler(MainActivity.this, this, callParams);
        serviceHandler.execute(ServiceHandler.DISPLAY);
    }

    @Override
    public void setNewAdaterCountryList(List<Country> list) {
        Log.i("Hien", "setNewAdapterCountryList");
        mAllCountry = list;
        mAdapter = new CustomAdapter(this, mAllCountry);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void sortCountry()
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
