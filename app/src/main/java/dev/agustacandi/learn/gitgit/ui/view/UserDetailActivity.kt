package dev.agustacandi.learn.gitgit.ui.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.agustacandi.learn.gitgit.R
import dev.agustacandi.learn.gitgit.adapter.SectionsPagerAdapter
import dev.agustacandi.learn.gitgit.data.remote.response.DetailUserResponse
import dev.agustacandi.learn.gitgit.databinding.ActivityUserDetailBinding
import dev.agustacandi.learn.gitgit.model.UserFavorite
import dev.agustacandi.learn.gitgit.utils.toDecimalFormat
import dev.agustacandi.learn.gitgit.ui.viewmodel.DetailViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.UserFavoriteViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.UserFavoriteViewModelFactory

class UserDetailActivity : AppCompatActivity() {

    private lateinit var activityUserDetailBinding: ActivityUserDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private var userDetailResponse = DetailUserResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserDetailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(activityUserDetailBinding.root)

        val userFavoriteViewModelFactory = UserFavoriteViewModelFactory.getInstance(this)
        val userFavoriteViewModel: UserFavoriteViewModel by viewModels {
            userFavoriteViewModelFactory
        }
        var isUserFavoriteNotEmpty = false

        val username = intent.extras?.getString("username")

        setupSectionPager(username)
        setupAppBar()

        with(detailViewModel) {
            if (userDetail.value == null) {
                getUserDetail(username ?: "a")
            }

            userDetail.observe(this@UserDetailActivity) { detailUserResponse ->
                setUserDetail(detailUserResponse)
                userDetailResponse = detailUserResponse
            }

            loader.observe(this@UserDetailActivity) { isLoading ->
                showLoader(isLoading)
            }

            snackbarText.observe(this@UserDetailActivity) { eventString ->
                eventString.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        userFavoriteViewModel.getUserFavoriteByUsername(username ?: "")
            .observe(this) { userFavorite ->
                isUserFavoriteNotEmpty = userFavorite != null
                if (isUserFavoriteNotEmpty) {
                    activityUserDetailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    activityUserDetailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }

        activityUserDetailBinding.detailUserLayout.btnGithub.setOnClickListener {
            val githubProfileUrl = "https://github.com/${userDetailResponse.login}".toUri()
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = githubProfileUrl
                startActivity(this)
            }
        }

        activityUserDetailBinding.fabFavorite.setOnClickListener {
            val userFavorite =
                UserFavorite(
                    username = userDetailResponse.login ?: "-",
                    avatarUrl = userDetailResponse.avatarUrl
                )
            if (isUserFavoriteNotEmpty) {
                userFavoriteViewModel.deleteUserFavorite(userFavorite)
            } else {
                userFavoriteViewModel.insertUserFavorite(userFavorite)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.share_menu -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "GitGit Github User\nFull Name: ${userDetailResponse.name ?: "-"}\nUsername: ${userDetailResponse.login}\n${userDetailResponse.bio ?: "There is no bio"}"
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }

            else -> false
        }
    }

    private fun setupSectionPager(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username ?: "a")
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setupAppBar() {
        setSupportActionBar(activityUserDetailBinding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios)
        supportActionBar?.elevation = 0f
    }

    private fun setUserDetail(user: DetailUserResponse) {
        val detailUsername = "@${user.login}"
        val detailRepo = "${user.publicRepos} repository"
        with(activityUserDetailBinding.detailUserLayout) {
            detailUserAvatar.load(user.avatarUrl) {
                placeholder(ColorDrawable(Color.LTGRAY))
            }
            detailUserFullname.text = user.name ?: "-"
            detailUserUsername.text = detailUsername
            detailUserBio.text = user.bio ?: "There is no bio."
            detailUserFollow.text = resources.getString(
                R.string.followers_and_following,
                user.followers.toDecimalFormat(),
                user.following.toDecimalFormat()
            )
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