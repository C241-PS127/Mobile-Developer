package com.example.lokalin.ui.Order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lokalin.ui.Order.history.HistoryFragment
import com.example.lokalin.ui.Order.order.OrderFragment

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OrderFragment()
            1 -> fragment = HistoryFragment()

        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}