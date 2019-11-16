package com.direct.attendance.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.direct.attendance.R
import com.direct.attendance.database.State
import com.direct.attendance.extension.errorDialog
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_attendance.rv_attendance
import org.koin.android.scope.currentScope

class AttendanceFragment : BaseFragment(), AttendanceListener {

    private val viewModelFactory: AttendanceViewModelFactory by currentScope.inject()

    private lateinit var viewModel: AttendanceViewModel
    private lateinit var adapter: AttendanceAdapter
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AttendanceViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            val args = AttendanceFragmentArgs.fromBundle(bundle)
            user = args.user
        }

        adapter = AttendanceAdapter(this)

        adapter.submitList(user?.attendances?.reversed() ?: emptyList())
        rv_attendance.layoutManager = LinearLayoutManager(context)
        rv_attendance.adapter = adapter

        viewModel.updateState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is State.Loading -> isLoading = true
                is State.Error -> renderErrorState(it)
                is State.Success -> isLoading = false
            }
        })

    }

    private fun renderErrorState(state: State.Error<Long>) {
        context?.let {
            errorDialog(it, state.message)
        }
        isLoading = false
    }

    override fun updateUser() {
        user?.let {
            viewModel.updateUser(it)
        }
    }
}