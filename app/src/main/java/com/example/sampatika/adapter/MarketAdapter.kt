package com.example.sampatika.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sampatika.R
import com.example.sampatika.databinding.CurrencyItemLayoutBinding
import com.example.sampatika.fragment.HomeFragmentDirections
import com.example.sampatika.fragment.MarketFragmentDirections
import com.example.sampatika.fragment.models.CryptoCurrency

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    inner class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketAdapter.MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false))
    }

    fun updateData(dataItem: List<CryptoCurrency>){
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MarketAdapter.MarketViewHolder, position: Int) {
        val item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)

        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)

        holder.binding.currencyPriceTextView.text = "${String.format("$%.02f", item.quotes[0].price)}%"

        if (item.quotes!![0].percentChange24h>0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+ ${String.format("%.02f", item.quotes[0].percentChange24h)}%"

        } else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        }

        holder.itemView.setOnClickListener{

            if (type == "home"){
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                )
            }else if (type == "market"){
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}