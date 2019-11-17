package com.direct.attendance.ui.students

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.direct.attendance.R
import com.direct.attendance.constant.ClassTime
import com.direct.attendance.database.State
import com.direct.attendance.extension.errorDialog
import com.direct.attendance.extension.isNotNull
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_update_student.btn_delete_student
import kotlinx.android.synthetic.main.dialog_update_student.btn_submit_student
import kotlinx.android.synthetic.main.dialog_update_student.spinner_class
import kotlinx.android.synthetic.main.dialog_update_student.spinner_class_time
import kotlinx.android.synthetic.main.dialog_update_student.tv_fullname
import kotlinx.android.synthetic.main.dialog_update_student.tv_gender
import kotlinx.android.synthetic.main.dialog_update_student.tv_started_date
import kotlinx.android.synthetic.main.fragment_students.rv_home
import kotlinx.android.synthetic.main.fragment_students.tv_search_bar
import org.koin.android.scope.currentScope

class StudentsFragment: BaseFragment(), StudentsListener {

    private val viewModelFactory: StudentsViewModelFactory by currentScope.inject()
    private lateinit var viewModel: StudentsViewModel
    private lateinit var studentsAdapter: StudentsAdapter
    private var users: List<User> = emptyList()
    private var classes: List<ClassRoom> = emptyList()
    private var classId: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StudentsViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            val args = StudentsFragmentArgs.fromBundle(bundle)
            classId = args.classId
        }

        studentsAdapter = StudentsAdapter(this)

        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.adapter = studentsAdapter

        tv_search_bar.addTextChangedListener {
            searchStudents(it.toString())
        }

        viewModel.updateState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is State.Loading -> isLoading = true
                is State.Error -> renderErrorUpdateState(it)
                is State.Success -> renderSuccessState()
            }
        })

        viewModel.deleteState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is State.Loading -> isLoading = true
                is State.Error -> renderErrorDeleteState(it)
                is State.Success -> renderSuccessState()
            }
        })

        setupStudents()
    }

    override fun onPause() {
        super.onPause()

        val inputMethod = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethod?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun renderErrorUpdateState(state: State.Error<Long>) {
        isLoading = false
        context?.let {
            errorDialog(it, state.message)
        }
    }

    private fun renderErrorDeleteState(state: State.Error<Int>) {
        isLoading = false
        context?.let {
            errorDialog(it, state.message)
        }
    }

    private fun renderSuccessState() {
        isLoading = false
        setupStudents()
    }

    private fun setupStudents() {
        when(classId.isNotNull()) {
            true -> {
                viewModel.allClasses.observe(viewLifecycleOwner, Observer { classes ->
                    this.classes = classes
                    viewModel.usersByClass(classId!!).observe(viewLifecycleOwner, Observer { users ->
                        this.users = users
                        studentsAdapter.submitList(users ?: emptyList(), classes)
                    })
                })
            }
            false -> {
                viewModel.allClasses.observe(viewLifecycleOwner, Observer { classes ->
                    this.classes = classes
                    viewModel.allStudents.observe(viewLifecycleOwner, Observer { users ->
                        this.users = users
                        studentsAdapter.submitList(users ?: emptyList(), classes)
                    })
                })
            }
        }
    }

    private fun searchStudents(text: String) {
        if (text.isBlank()) {
            studentsAdapter.submitList(this.users)
            return
        }

        studentsAdapter.submitList(
            this.users.filter {
                it.name.startsWith(text, true)
            }
        )
    }

    @SuppressLint("DefaultLocale")
    override fun onUpdateStudent(user: User) {
        context?.let { context ->
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_update_student)
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            val maxWidth = Resources.getSystem().displayMetrics.widthPixels
            val params = LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.window?.setLayout(params.width, params.height)

            setupSpinnerClasses(dialog.spinner_class)
            setupSpinnerClassTime(dialog.spinner_class_time)

            dialog.tv_fullname.text = user.fullname
            dialog.tv_started_date.text = user.startedDate
            dialog.tv_gender.text = user.gender.capitalize()
            var classRoomId = user.classRoomId
            var classTime = user.classTime

            dialog.spinner_class.setSelection(classes.indexOfFirst { it.id == classRoomId })
            dialog.spinner_class_time.setSelection(if (classTime == ClassTime.MORNING.name) 0 else 1)

            dialog.spinner_class.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (classes.isEmpty()) return
                    if (classes.size == 1) return
                    classRoomId = classes[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            dialog.spinner_class_time.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    classTime = if (position == 0) ClassTime.MORNING.name else ClassTime.AFTERNOON.name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            dialog.btn_submit_student.setOnClickListener {
                user.classTime = classTime
                user.classRoomId = classRoomId
                viewModel.update(user)
                dialog.dismiss()
            }

            dialog.btn_delete_student.setOnClickListener {
                viewModel.delete(user)
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setupSpinnerClasses(spinner: Spinner) {
        spinner.adapter = ArrayAdapter(
            spinner.context,
            R.layout.item_spinner,
            classes
        )
    }

    @SuppressLint("DefaultLocale")
    private fun setupSpinnerClassTime(spinner: Spinner) {
        spinner.adapter = ArrayAdapter(
            spinner.context,
            R.layout.item_spinner,
            listOf(ClassTime.MORNING.name.capitalize(),
                ClassTime.AFTERNOON.name.capitalize())
        )
    }
}