package dev.agustacandi.learn.gitgit.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.agustacandi.learn.gitgit.R
import dev.agustacandi.learn.gitgit.adapter.ListUserAdapter
import dev.agustacandi.learn.gitgit.data.remote.response.ItemsItem
import dev.agustacandi.learn.gitgit.databinding.ActivityUserFavoriteBinding
import dev.agustacandi.learn.gitgit.ui.viewmodel.UserFavoriteViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.UserFavoriteViewModelFactory

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var userFavoriteBinding: ActivityUserFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userFavoriteBinding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(userFavoriteBinding.root)

        setSupportActionBar(userFavoriteBinding.topAppBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userFavoriteViewModelFactory = UserFavoriteViewModelFactory.getInstance(this)
        val userFavoriteViewModel: UserFavoriteViewModel by viewModels { userFavoriteViewModelFactory }

        setupRecyclerView()

        userFavoriteViewModel.getListUserFavorite().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map { user ->
                val item = ItemsItem(login = user.username, avatarUrl = user.avatarUrl)
                items.add(item)
            }
            val adapter = ListUserAdapter()
            adapter.submitList(items)
            userFavoriteBinding.rvUserFavorite.adapter = adapter
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        userFavoriteBinding.rvUserFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        userFavoriteBinding.rvUserFavorite.addItemDecoration(itemDecoration)
    }
}