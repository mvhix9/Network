package com.network.countriesinformation;

import android.app.SearchManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.network.countriesinformation.Adapter.CustomAdapter;
import com.network.countriesinformation.Service.ServiceHandler;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CustomAdapter mAdapter;
    private ServiceHandler serviceHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        getAllCountries();
    }

    private void getAllCountries() {
        ContentValues callParams = new ContentValues();
        callParams.put("username","hí hí");
        serviceHandler = new ServiceHandler(MainActivity.this, mRecyclerView, callParams,mAdapter);
        serviceHandler.execute(ServiceHandler.DISPLAY);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        // searchview on action bar
        SearchManager searchManager = (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
        SearchView searchView       = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.sortAsc:
                // TO-DO
                return true;
            case R.id.sortDes:
                // TO-DO
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
        return false;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}
