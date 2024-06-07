package com.example.lokalin.ui.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lokalin.ui.categories.CategoriesFragment
import com.example.lokalin.ui.profile.ProfileFragment
import com.example.lokalin.ui.recycle.RecycleFragment
import com.example.lokalin.ui.search.SearchFragment

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RecycleFragment()
            1 -> fragment = ProfileFragment()
            2 -> fragment = SearchFragment()
            3 -> fragment = CategoriesFragment()

        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 4
    }
}