package com.example.cobachatapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class TabProfileAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, userId: String) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var id = userId

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> return Post.newInstance(id, "")
            1 -> return Upload.newInstance(id, "")
        }
        return Post()
    }
}