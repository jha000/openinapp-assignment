package com.console.openinapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.console.openinapp.model.Link
import com.console.openinapp.api.RetrofitInstance
import com.console.openinapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class LinkViewModel : ViewModel() {

    private val _overallUrlChart = MutableLiveData<Map<String, Int>>()
    val overallUrlChart: LiveData<Map<String, Int>> get() = _overallUrlChart

    private val _todayClicks = MutableLiveData<Int>()
    val todayClicks: LiveData<Int> get() = _todayClicks

    private val _topLocation = MutableLiveData<String>()
    val topLocation: LiveData<String> get() = _topLocation

    private val _topSource = MutableLiveData<String>()
    val topSource: LiveData<String> get() = _topSource

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> get() = _startTime

    private val _recentLinks = MutableLiveData<List<Link>>()
    val recentLinks: LiveData<List<Link>> get() = _recentLinks

    private val _topLinks = MutableLiveData<List<Link>>()
    val topLinks: LiveData<List<Link>> get() = _topLinks

    private val _supportWhatsappNumber = MutableLiveData<String>()
    val supportWhatsappNumber: LiveData<String> get() = _supportWhatsappNumber

    init {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        val authToken = "Bearer ${Constants.ACCESS_TOKEN}"
        val apiService = RetrofitInstance.api

        apiService.getDashboardData(authToken).enqueue(object : Callback<com.console.openinapp.model.Response> {
            override fun onResponse(call: Call<com.console.openinapp.model.Response>, response: Response<com.console.openinapp.model.Response>) {
                if (response.isSuccessful) {
                    val dashboardData = response.body()
                    if (dashboardData != null) {
                        _todayClicks.value = dashboardData.today_clicks
                        _topLocation.value = dashboardData.top_location
                        _topSource.value = dashboardData.top_source
                        _startTime.value = dashboardData.startTime
                        _recentLinks.value = dashboardData.data.recent_links.map { it.copy(created_at = formatDate(it.created_at)) }
                        _topLinks.value = dashboardData.data.top_links.map { it.copy(created_at = formatDate(it.created_at)) }
                        _supportWhatsappNumber.value = dashboardData.support_whatsapp_number

                        val chartData = dashboardData.data.overall_url_chart
                        _overallUrlChart.value = chartData
                    }
                }
            }

            override fun onFailure(call: Call<com.console.openinapp.model.Response>, t: Throwable) {
                // Handle error here
            }
        })
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: "")
        } catch (e: Exception) {
            ""
        }
    }
}
