package com.finance.sampatika.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.finance.sampatika.R
import com.finance.sampatika.adapter.WatchlistAdapter
import com.finance.sampatika.apis.ApiInterface
import com.finance.sampatika.apis.ApiUtilities
import com.finance.sampatika.databinding.FragmentWatchlistBinding
import com.finance.sampatika.room.AppDatabase
import com.finance.sampatika.room.WatchlistEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WatchlistFragment : Fragment() {
    private lateinit var binding: FragmentWatchlistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadWatchlist()
    }

    private fun loadWatchlist() {
        val dao = AppDatabase.getDatabase(requireContext()).watchlistDao()
        binding.spinKitView.visibility = View.VISIBLE
        binding.watchlistRecyclerView.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
        lifecycleScope.launch(Dispatchers.IO) {
            val list = dao.getWatchlist()

            withContext(Dispatchers.Main) {
                binding.spinKitView.visibility = View.GONE
                if (list.isEmpty()) {
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.watchlistRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.watchlistRecyclerView.visibility = View.VISIBLE
                    binding.watchlistRecyclerView.adapter =
                        WatchlistAdapter(requireContext(), list) { entity ->
                            openDetailsFromWatchlist(entity)
                        }
                }
            }
        }
    }

    private fun openDetailsFromWatchlist(entity: WatchlistEntity) {

        lifecycleScope.launch(Dispatchers.IO) {

            // Fetch latest market data
            val response = ApiUtilities.getInstance()
                .create(ApiInterface::class.java)
                .getMarketData()

            val coin = response.body()
                ?.data
                ?.cryptoCurrencyList
                ?.find { it.id == entity.id }

            if (coin != null) {
                withContext(Dispatchers.Main) {
                    findNavController().navigate(
                        WatchlistFragmentDirections
                            .actionWatchlistFragmentToDetailsFragment(coin)
                    )
                }
            }
        }
    }


}