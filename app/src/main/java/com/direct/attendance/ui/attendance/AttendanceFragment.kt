package com.direct.attendance.ui.attendance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.direct.attendance.R
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.database.State
import com.direct.attendance.extension.errorDialog
import com.direct.attendance.extension.toString
import com.direct.attendance.model.Attendance
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_attendance.rv_attendance
import kotlinx.android.synthetic.main.fragment_attendance.tv_search_bar
import org.koin.android.scope.currentScope
import java.util.Locale

class AttendanceFragment : BaseFragment(), AttendanceListener {

    private val viewModelFactory: AttendanceViewModelFactory by currentScope.inject()

    private lateinit var viewModel: AttendanceViewModel
    private lateinit var adapter: AttendanceAdapter
    private var user: User? = null
    private var attdList: List<Attendance> = emptyList()

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

        attdList = user?.attendances ?: emptyList()

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

        tv_search_bar.addTextChangedListener {
            searchAttendances(it.toString())
        }
    }

    override fun onPause() {
        super.onPause()

        val inputMethod = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethod?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun renderErrorState(state: State.Error<Long>) {
        context?.let {
            errorDialog(it, state.message)
        }
        isLoading = false
    }

    private fun searchAttendances(text: String) {
        if (text.isBlank()) {
            adapter.submitList(attdList.reversed())
            return
        }

        adapter.submitList(
            attdList.filter {
                it.date.toString(DatePatterns.ddMMyyyy, Locale.getDefault()).startsWith(
                    text, true
                )
            }
        )
    }

    override fun updateUser() {
        user?.let {
            viewModel.updateUser(it)
        }
    }
}