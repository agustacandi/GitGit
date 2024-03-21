package dev.agustacandi.learn.gitgit.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.agustacandi.learn.gitgit.adapter.ListUserAdapter
import dev.agustacandi.learn.gitgit.databinding.FragmentFollowBinding
import dev.agustacandi.learn.gitgit.ui.viewmodel.FollowersViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.FollowingViewModel

class FollowFragment : Fragment() {

    private var _position: Int? = null
    private var _username: String? = null
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding
    private val followersViewModel by viewModels<FollowersViewModel>()
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            _position = it.getInt(ARG_POSITION)
            _username = it.getString(ARG_USERNAME)
        }
        val adapter = ListUserAdapter()

        followersViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding?.root?.rootView!!, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }

        followingViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding?.root?.rootView!!, snackBarText, Snackbar.LENGTH_SHORT).show()
            }
        }

        setupRecyclerView()

        if (_position == 1) {
            showFollowersData(adapter)
        } else {
            showFollowingData(adapter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFollow?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvFollow?.addItemDecoration(itemDecoration)
    }

    private fun showFollowersData(adapter: ListUserAdapter) {
        with(followersViewModel) {
            if (listFollowers.value == null) {
                getUserFollowersByUsername(_username!!)
            }
            noFollowersText.observe(viewLifecycleOwner) {
                binding?.tvFollowNotFound?.text = it
            }
            listFollowers.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding?.rvFollow?.adapter = adapter
            }
            loader.observe(viewLifecycleOwner) {
                showLoader(it)
            }
        }
    }

    private fun showFollowingData(adapter: ListUserAdapter) {
        with(followingViewModel) {
            getUserFollowingByUsername(_username!!)
            listFollowing.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding?.rvFollow?.adapter = adapter
            }
            notFollowingAnyoneText.observe(viewLifecycleOwner) {
                binding?.tvFollowNotFound?.text = it
            }
            loader.observe(viewLifecycleOwner) {
                showLoader(it)
            }
        }
    }

    private fun showLoader(isLoading: Boolean) {
        if (isLoading) {
            binding?.loader?.root?.visibility = View.VISIBLE
            binding?.rvFollow?.visibility = View.INVISIBLE
        } else {
            binding?.loader?.root?.visibility = View.INVISIBLE
            binding?.rvFollow?.visibility = View.VISIBLE
        }
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}