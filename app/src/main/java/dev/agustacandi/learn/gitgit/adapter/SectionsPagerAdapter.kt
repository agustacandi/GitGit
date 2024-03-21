package dev.agustacandi.learn.gitgit.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.agustacandi.learn.gitgit.ui.view.FollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity, usernameParam: String) :
    FragmentStateAdapter(activity) {

    private val username = usernameParam

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putString(FollowFragment.ARG_USERNAME, username)
            putInt(FollowFragment.ARG_POSITION, position + 1)
        }
        return fragment
    }
}