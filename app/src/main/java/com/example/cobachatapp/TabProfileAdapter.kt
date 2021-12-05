package com.example.cobachatapp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabProfileAdapter(fragmentManager: FragmentManager,
                        lifecycle: Lifecycle,
                        user: User, number_tabs: Int,
                        authenticated: Boolean) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var _user = user
    private var authenticated = authenticated
    private var num_tabs = number_tabs

    override fun getItemCount(): Int {
        return num_tabs
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> return Post.newInstance(_user, authenticated)
            1 -> return Upload.newInstance(_user.userId.toString(), "")
        }
        return Post()
    }
}