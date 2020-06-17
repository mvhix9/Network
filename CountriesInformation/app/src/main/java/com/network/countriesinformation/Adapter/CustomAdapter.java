package com.network.countriesinformation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.network.countriesinformation.Entity.Country;
import com.network.countriesinformation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.DataViewHolder> {

    Context mContext;
    List<Country> mCountries;

    public CustomAdapter(Context context, List<Country> countries)
    {
        this.mContext = context;
        this.mCountries = countries;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.country_item, parent,false);
        return new DataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        final Country country = mCountries.get(position);
        holder.txtCountryName.setText(country.getmName());
        String url = "https://img.geonames.org/flags/l/" + country.getmCode().toLowerCase() + ".gif";
        Picasso.with(this.mContext).load(url).resize(50,50).into(holder.imgFlag);
    }

    @Override
    public int getItemCount() {
        return mCountries == null? 0 : mCountries.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFlag;
        private TextView txtCountryName;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlag = (ImageView) itemView.findViewById(R.id.imgFlag);
            txtCountryName = (TextView) itemView.findViewById(R.id.txtCountryName);
        }
    }
}
