package com.wealth.sampatika.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wealth.sampatika.R
import com.wealth.sampatika.databinding.FragmentDetailsBinding
import com.wealth.sampatika.fragment.models.CryptoCurrency
import com.wealth.sampatika.room.WatchlistEntity
import com.wealth.sampatika.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class  DetailsFragment : Fragment() {

    lateinit var binding : FragmentDetailsBinding
    lateinit var crypto: CryptoCurrency

    private val item : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        crypto = item.data!!
        setUpDetails(crypto)
        loadChart(crypto)
        setButtonOnClick(crypto)
        clickOnAddToWatchlistButton()
        binding.backStackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun clickOnAddToWatchlistButton() {

        val dao = AppDatabase.getDatabase(requireContext()).watchlistDao()

        // Set initial star state
        lifecycleScope.launch(Dispatchers.IO) {
            val isSaved = dao.isInWatchlist(crypto.id)
            withContext(Dispatchers.Main) {
                binding.addWatchlistButton.setImageResource(
                    if (isSaved)
                        R.drawable.outline_bookmark_star_24
                    else
                        R.drawable.ic_star_outline
                )
            }
        }

        binding.addWatchlistButton.setOnClickListener {

            // 🔥 DISABLE BUTTON immediately (PREVENT DOUBLE CLICK)
            binding.addWatchlistButton.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {

                val exists = dao.isInWatchlist(crypto.id)

                if (!exists) {
                    dao.addToWatchlist(
                        WatchlistEntity(
                            id = crypto.id,
                            name = crypto.name,
                            symbol = crypto.symbol,
                            price = crypto.quotes[0].price,
                            percentChange24h = crypto.quotes[0].percentChange24h
                        )
                    )
                } else {
                    dao.removeFromWatchlist(crypto.id)
                }

                withContext(Dispatchers.Main) {

                    // 🔥 UPDATE ICON
                    binding.addWatchlistButton.setImageResource(
                        if (!exists)
                            R.drawable.outline_bookmark_star_24
                        else
                            R.drawable.ic_star_outline
                    )

                    // 🔥 SHOW TOAST
                    Toast.makeText(
                        requireContext(),
                        if (!exists)
                            "${crypto.name} added to Watchlist ⭐"
                        else
                            "${crypto.name} removed from Watchlist",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 🔥 RE-ENABLE BUTTON
                    binding.addWatchlistButton.isEnabled = true
                }
            }
        }
    }


    private fun setButtonOnClick(item: CryptoCurrency ) {
        val oneMonth = binding.button
        val oneWeek = binding.button
        val oneDay = binding.button
        val fourHour = binding.button
        val oneHour = binding.button
        val fifteenMinute = binding.button

        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMinute.id -> loadChartData(it, "15", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                oneHour.id -> loadChartData(it, "1H", item, oneDay, oneMonth, oneWeek, fourHour, fifteenMinute)
                fourHour.id -> loadChartData(it, "4H", item, oneDay, oneMonth, oneWeek, fifteenMinute, oneHour)
                oneDay.id -> loadChartData(it, "D", item, fifteenMinute, oneMonth, oneWeek, fourHour, oneHour)
                oneWeek.id -> loadChartData(it, "W", item, oneDay, oneMonth, fifteenMinute, fourHour, oneHour)
                oneMonth.id -> loadChartData(it, "M", item, oneDay, fifteenMinute, oneWeek, fourHour, oneHour)

            }
        }

        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
    }

    private fun loadChartData(
        it: View,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButton(oneDay, oneMonth, oneWeek, fourHour, oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "USD&interval=" +s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=" +
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=" +
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }
    private fun disableButton(
        onrDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        onrDay.background = null
        oneMonth.background = null
        oneWeek.background = null
        fourHour.background = null
        oneHour.background = null
    }



    private fun loadChart(item: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=" +
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=" +
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setUpDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text = "${String.format("$%.04f", data.quotes[0].price)}%"

        if (data.quotes!![0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+ ${String.format("%.02f", data.quotes[0].percentChange24h)}%"

        } else{
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeTextView.text = "${String.format("%.02f", data.quotes[0].percentChange24h)}%"
        }

    }

}


