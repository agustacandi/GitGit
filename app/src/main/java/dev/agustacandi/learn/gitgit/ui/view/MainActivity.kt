package dev.agustacandi.learn.gitgit.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.agustacandi.learn.gitgit.data.adapter.ListUserAdapter
import dev.agustacandi.learn.gitgit.data.response.ItemsItem
import dev.agustacandi.learn.gitgit.databinding.ActivityMainBinding
import dev.agustacandi.learn.gitgit.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setupRecyclerView()

        mainViewModel.users.observe(this) { listUserData ->
            if (listUserData != null) {
                setListUserData(listUserData)
            }
        }

        mainViewModel.loader.observe(this) {
            showLoader(it)
        }

        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(window.decorView.rootView, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }

        searchAction()
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