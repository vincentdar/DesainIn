package com.example.cobachatapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class TabProfileAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, user: User) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var _user = user

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> return Post.newInstance(_user.userId.toString(), _user.userName.toString())
            1 -> return Upload.newInstance(_user.userId.toString(), "")
        }
        return Post()
    }
}