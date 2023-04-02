package com.example.ulkeuyqulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.ulkeuyqulamasi.R
import com.example.ulkeuyqulamasi.databinding.ItemCountryBinding
import com.example.ulkeuyqulamasi.model.Country
import com.example.ulkeuyqulamasi.util.downloadFromUrl
import com.example.ulkeuyqulamasi.util.placeholderProgressBar
import com.example.ulkeuyqulamasi.view.FeedFragmentDirections

class CountryAdapter(val countryList: ArrayList<Country>):RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {


    class CountryViewHolder(val view:ItemCountryBinding):RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemCountryBinding = ItemCountryBinding.inflate(layoutInflater, parent, false)
        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.view.name.text=countryList[position].countryName
        holder.view.region.text=countryList[position].countryRegion

        holder.view.imageView.setOnClickListener {
            val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)

            Navigation.findNavController(it).navigate(action)
        }
        holder.view.imageView.downloadFromUrl(countryList[position].imageUrl,
            placeholderProgressBar(holder.view.root.context)
        )


    }
    fun updateCountryList(newCountryList:List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()//adapteri yenilemek ucun istifade elediyimiz method
    }

}