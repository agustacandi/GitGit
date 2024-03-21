package dev.agustacandi.learn.gitgit.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.agustacandi.learn.gitgit.R
import dev.agustacandi.learn.gitgit.adapter.ListUserAdapter
import dev.agustacandi.learn.gitgit.data.local.datastore.ThemePreferences
import dev.agustacandi.learn.gitgit.data.local.datastore.dataStore
import dev.agustacandi.learn.gitgit.data.remote.response.ItemsItem
import dev.agustacandi.learn.gitgit.databinding.ActivityMainBinding
import dev.agustacandi.learn.gitgit.ui.viewmodel.MainViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.ThemeViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.ThemeViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val pref = ThemePreferences.getInstance(application.dataStore)
        val themeViewModel =
            ViewModelProvider(this, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]
        var isDarkMode = true

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            isDarkMode = if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
        }

        setupRecyclerView()

        mainViewModel.apply {
            users.observe(this@MainActivity) { listUserData ->
                if (listUserData != null) {
                    setListUserData(listUserData)
                }
            }

            usernameNotFoundText.observe(this@MainActivity) {
                activityMainBinding.tvUsernameNotFound.text = it
            }

            loader.observe(this@MainActivity) {
                showLoader(it)
            }

            snackbarText.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        searchAction()

        activityMainBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite_menu -> {
                    Intent(this@MainActivity, UserFavoriteActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }

                R.id.theme_menu -> {
                    themeViewModel.saveThemeSetting(!isDarkMode)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        activityMainBinding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        activityMainBinding.recyclerView.addItemDecoration(itemDecoration)
    }

    private fun setListUserData(users: List<ItemsItem>) {
        val adapter = ListUserAdapter()
        adapter.submitList(users)
        activityMainBinding.recyclerView.adapter = adapter
    }

    private fun searchAction() {
        with(activityMainBinding) {
            tvUsernameNotFound.text = ""
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val usernameQuery =
                    if (searchView.text.isEmpty()) "a" else searchView.text.toString()
                searchView.hide()
                searchBar.setText(searchView.text)
                mainViewModel.searchUserByUsername(usernameQuery)
                false
            }

        }
    }

    private fun showLoader(isLoading: Boolean) {
        with(activityMainBinding) {
            if (isLoading) {
                listUserSkeleton.root.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            } else {
                listUserSkeleton.root.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

}