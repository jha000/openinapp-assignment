package com.console.openinapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.console.openinapp.view.Fragments.ProfileFragment
import com.console.openinapp.R
import com.console.openinapp.view.Fragments.CampaignFragment
import com.console.openinapp.view.Fragments.CourseFragment
import com.console.openinapp.view.Fragments.LinkFragment
import com.console.openinapp.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val linkFragment = LinkFragment()
    private val courseFragment = CourseFragment()
    private val campaignFragment = CampaignFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.links -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, linkFragment)
                        .commit()
                    true
                }
                R.id.courses -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, courseFragment)
                        .commit()
                    true
                }
                R.id.campaigns -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, campaignFragment)
                        .commit()
                    true
                }
                R.id.user -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, profileFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, linkFragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is CourseFragment || currentFragment is CampaignFragment || currentFragment is ProfileFragment) {
            supportFragmentManager.beginTransaction()
                .apply {
                    replace(R.id.container, LinkFragment()).commit()
                    binding.bottomNavigation.selectedItemId = R.id.links
                }
        } else {
            super.onBackPressed()
        }
    }
}
