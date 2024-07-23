package com.console.openinapp.view.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.console.openinapp.LinkAdapter
import com.console.openinapp.viewmodel.LinkViewModel
import com.console.openinapp.R
import com.console.openinapp.utils.ChartUtils
import com.console.openinapp.databinding.FragmentLinkBinding
import com.console.openinapp.utils.Constants
import java.util.Calendar

class LinkFragment : Fragment() {

    private lateinit var binding: FragmentLinkBinding
    private lateinit var viewModel: LinkViewModel
    private var whatsappNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_link, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this)[LinkViewModel::class.java]
        binding.viewModel = viewModel

        ChartUtils.setupChart(requireContext(), binding.lineChart)

        viewModel.overallUrlChart.observe(viewLifecycleOwner) { chartData ->
            ChartUtils.updateChart(requireContext(), binding.lineChart, chartData)
            binding.progressBar2.visibility = View.GONE
            binding.graphLayout.visibility = View.VISIBLE
        }

        val adapter = LinkAdapter()
        binding.recentLinksRecyclerView.adapter = adapter
        binding.recentLinksRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.todayClicks.observe(viewLifecycleOwner) {
            binding.TodayClicks.text = it?.toString() ?: "0"
        }

        viewModel.topLocation.observe(viewLifecycleOwner) {
            binding.TopLocation.text = it ?: "N/A"
        }

        viewModel.topSource.observe(viewLifecycleOwner) {
            binding.TopSource.text = it ?: "N/A"
        }

        viewModel.startTime.observe(viewLifecycleOwner) {
            binding.Time.text = it ?: "N/A"
        }

        viewModel.topLinks.observe(viewLifecycleOwner) { topLinks ->
            topLinks?.let {
                adapter.submitList(it)
                binding.progressBar.visibility = View.GONE
                binding.recentLinksRecyclerView.visibility = View.VISIBLE
            }
        }

        val activeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val inactiveTextColor = ContextCompat.getColor(requireContext(), R.color.grey)

        binding.recentLinksButton.setOnClickListener {
            binding.recentLinksButton.apply {
                setBackgroundResource(R.drawable.blue_rectangle)
                setTextColor(activeTextColor)
            }
            binding.topLinksButton.apply {
                background = null
                setTextColor(inactiveTextColor)
            }
            viewModel.recentLinks.observe(viewLifecycleOwner) { recentLinks ->
                recentLinks?.let {
                    adapter.submitList(it)
                    binding.progressBar.visibility = View.GONE
                    binding.recentLinksRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        binding.topLinksButton.setOnClickListener {
            binding.topLinksButton.apply {
                setBackgroundResource(R.drawable.blue_rectangle)
                setTextColor(activeTextColor)
            }
            binding.recentLinksButton.apply {
                background = null
                setTextColor(inactiveTextColor)
            }
            viewModel.topLinks.observe(viewLifecycleOwner) { topLinks ->
                topLinks?.let {
                    adapter.submitList(it)
                    binding.progressBar.visibility = View.GONE
                    binding.recentLinksRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        binding.greeting.text = getGreetingText()

        viewModel.supportWhatsappNumber.observe(viewLifecycleOwner) {
            whatsappNumber = it
        }

        binding.openWhatsapp.setOnClickListener {
            whatsappNumber?.let {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$it")))
            }
        }

        binding.faq.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FAQ_URL)))
        }

        return binding.root
    }

    private fun getGreetingText(): String {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hourOfDay) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
