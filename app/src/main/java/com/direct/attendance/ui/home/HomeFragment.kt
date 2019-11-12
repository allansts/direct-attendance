package com.direct.attendance.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.direct.attendance.R
import com.direct.attendance.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.rv_home
import org.koin.android.scope.currentScope

class HomeFragment: BaseFragment() {

    private val viewModelFactory: HomeViewModelFactory by currentScope.inject()
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = HomeAdapter()

        viewModel.allStudents.observe(viewLifecycleOwner, Observer {
            homeAdapter.submitList(it ?: emptyList())
        })

        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.adapter = homeAdapter

    }

}