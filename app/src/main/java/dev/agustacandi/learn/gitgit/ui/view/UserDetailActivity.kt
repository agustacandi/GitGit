package dev.agustacandi.learn.gitgit.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.agustacandi.learn.gitgit.R
import dev.agustacandi.learn.gitgit.data.adapter.SectionsPagerAdapter
import dev.agustacandi.learn.gitgit.data.response.DetailUserResponse
import dev.agustacandi.learn.gitgit.databinding.ActivityUserDetailBinding
import dev.agustacandi.learn.gitgit.ui.viewmodel.DetailViewModel
import java.text.DecimalFormat

class UserDetailActivity : AppCompatActivity() {

    private lateinit var activityUserDetailBinding: ActivityUserDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserDetailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(activityUserDetailBinding.root)

        val username = intent.extras?.getString("username")

        if (detailViewModel.userDetail.value == null) {
            detailViewModel.getUserDetail(username!!)
        }

        detailViewModel.userDetail.observe(this) {
            setUserDetail(it!!)
        }

        detailViewModel.loader.observe(this) {
            showLoader(it)
        }

        detailViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username!!)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        setSupportActionBar(activityUserDetailBinding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios)
        supportActionBar?.elevation = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUserDetail(user: DetailUserResponse) {
        val df = DecimalFormat("#,###")
        val detailUsername = "@${user.login}"
        val detailRepo = "${user.publicRepos} repository"
        val detailFollow =
            "${df.format(user.followers)} Followers - ${df.format(user.following)} Following"
        with(activityUserDetailBinding.detailUserLayout) {
            Glide.with(root.context)
                .load(user.avatarUrl)
                .placeholder(ColorDrawable(Color.LTGRAY))
                .into(detailUserAvatar)
            detailUserFullname.text = user.name ?: "-"
            detailUserUsername.text = detailUsername
            detailUserBio.text = user.bio ?: "There is no bio."
            detailUserFollow.text = detailFollow
            detailUserRepo.text = detailRepo
            detailUserLocation.text = user.location ?: "-"
        }
    }

    private fun showLoader(isLoading: Boolean) {
        with(activityUserDetailBinding) {
            if (isLoading) {
                userDetailSkeleton.root.visibility = android.view.View.VISIBLE
                detailUserLayout.root.visibility = android.view.View.INVISIBLE
            } else {
                userDetailSkeleton.root.visibility = android.view.View.INVISIBLE
                detailUserLayout.root.visibility = android.view.View.VISIBLE
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}