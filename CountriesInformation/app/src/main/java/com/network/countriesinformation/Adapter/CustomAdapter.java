package com.network.countriesinformation.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.DataViewHolder> {

    private Context mContext;
    private List<Country> mCountries;
    private List<Country> mOriginalList;

    public CustomAdapter() { }

    public CustomAdapter(Context context, List<Country> countries)
    {
        this.mContext = context;
        this.mOriginalList = new ArrayList<>();
        this.mOriginalList.addAll(countries);
        this.mCountries = new ArrayList<>();
        this.mCountries.addAll(countries);
    }

    public CustomAdapter(Context context, List<Country> presentation, List<Country> original)
    {
        this.mContext = context;
        this.mOriginalList = new ArrayList<>();
        this.mOriginalList.addAll(original);
        this.mCountries = new ArrayList<>();
        this.mCountries.addAll(presentation);
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
        final String imgLink = "https://img.geonames.org/flags/l/" + country.getmCode().toLowerCase() + ".gif";
        Picasso.with(this.mContext).load(imgLink).resize(60,36).into(holder.imgFlag);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaLog(country, imgLink);
            }
        });
    }

    void showDiaLog(Country country, String url)
    {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.country_detailed_info);

        // mapping views
        ImageView imgFlag = dialog.findViewById(R.id.imgFlag);
        TextView txtCountryCode = dialog.findViewById(R.id.txtCountryCode);
        TextView txtCountryName = dialog.findViewById(R.id.txtCountryName);
        TextView txtContinentName = dialog.findViewById(R.id.txtContinentName);
        TextView txtArea = dialog.findViewById(R.id.txtArea);
        TextView txtCurrencyCode = dialog.findViewById(R.id.txtCurrencyCode);
        TextView txtCapital = dialog.findViewById(R.id.txtCapital);
        TextView txtPopulation = dialog.findViewById(R.id.txtPopulation);

        // set views
        Picasso.with(mContext).load(url).resize(200,120).into(imgFlag);
        txtCountryCode.setText(country.getmCode());
        txtCountryName.setText(country.getmName());
        txtContinentName.setText(country.getmContinentName());
        txtCapital.setText(country.getmCapital());
        txtArea.setText(String.valueOf(country.getmArea()));
        txtCurrencyCode.setText(country.getmCurrencyCode());
        txtPopulation.setText(String.valueOf(country.getmPopulation()));

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mCountries == null? 0 : mCountries.size();
    }

    public int getSize() {
        return this.mOriginalList.size();
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

    public void setOriginalList(List<Country> list)  {
        this.mOriginalList.addAll(list);
    }

    public void setPresentationList(List<Country> list)  {
        this.mCountries.addAll(list);
    }

    public List<Country> getAllPresentationItem()
    {
        return mCountries;
    }

    public List<Country> getAllItem()
    {
        return mOriginalList;
    }

    //Search
    public void filter(String text)
    {
        Log.i("Hien", "filter");
        mCountries.clear();
        if(TextUtils.isEmpty(text))
        {
            mCountries.addAll(mOriginalList);
        }
        else
        {
            for(Country country : mOriginalList)
            {
                String name = country.getmName().toLowerCase();
                String continentName = country.getmContinentName();
                if(name.contains(text.toLowerCase()) || continentName.contains(text.toLowerCase()))
                {
                    mCountries.add(country);
                }
            }
        }
        notifyDataSetChanged();
    }
}
