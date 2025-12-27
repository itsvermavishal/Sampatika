package com.finance.sampatika.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finance.sampatika.R
import com.finance.sampatika.databinding.CurrencyItemLayoutBinding
import com.finance.sampatika.room.WatchlistEntity

class WatchlistAdapter(val context: Context, val list: List<WatchlistEntity>, val onItemClick: (WatchlistEntity) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CurrencyItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CurrencyItemLayoutBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol
        holder.binding.currencyPriceTextView.text =
            String.format("$%.04f", item.price)

        // 🔥 LOAD COIN ICON
        Glide.with(context)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png")
            .into(holder.binding.currencyImageView)

        // 🔥 LOAD REALTIME GRAPH
        Glide.with(context)
            .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${item.id}.png")
            .into(holder.binding.currencyChartImageView)

        if (item.percentChange24h > 0) {
            holder.binding.currencyChangeTextView.setTextColor(
                context.getColor(R.color.green)
            )
        } else {
            holder.binding.currencyChangeTextView.setTextColor(
                context.getColor(R.color.red)
            )
        }
        holder.binding.currencyChangeTextView.text =
            String.format("%.02f%%", item.percentChange24h)
    }

    override fun getItemCount(): Int = list.size

}