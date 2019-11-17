package com.direct.attendance.ui.settings

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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.direct.attendance.R
import com.direct.attendance.constant.ClassTime
import com.direct.attendance.constant.Constants.Companion.BR_LOCALE
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.constant.Gender
import com.direct.attendance.constant.UserType
import com.direct.attendance.extension.NAME_PATTERN
import com.direct.attendance.extension.errorDialog
import com.direct.attendance.extension.isNotNull
import com.direct.attendance.extension.isNull
import com.direct.attendance.extension.isNullOrBlank
import com.direct.attendance.extension.lessThanToday
import com.direct.attendance.extension.toDate
import com.direct.attendance.extension.yearsLeftFrom
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseFragment
import com.direct.attendance.utils.DateTextWatcher
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.dialog_add_class.btn_submit
import kotlinx.android.synthetic.main.dialog_add_class.et_name
import kotlinx.android.synthetic.main.dialog_add_class.til_name
import kotlinx.android.synthetic.main.dialog_add_student.btn_submit_student
import kotlinx.android.synthetic.main.dialog_add_student.et_first_name
import kotlinx.android.synthetic.main.dialog_add_student.et_last_name
import kotlinx.android.synthetic.main.dialog_add_student.et_started_date
import kotlinx.android.synthetic.main.dialog_add_student.spinner_class
import kotlinx.android.synthetic.main.dialog_add_student.spinner_class_time
import kotlinx.android.synthetic.main.dialog_add_student.spinner_gender
import kotlinx.android.synthetic.main.dialog_add_student.til_first_name
import kotlinx.android.synthetic.main.dialog_add_student.til_last_name
import kotlinx.android.synthetic.main.dialog_add_student.til_started_date
import kotlinx.android.synthetic.main.fragment_settings.rv_settings
import org.koin.android.scope.currentScope
import java.util.Date

class SettingsFragment: BaseFragment(), SettingsListener {

    private val viewModelFactory: SettingsViewModelFactory by currentScope.inject()

    private lateinit var viewModel: SettingsViewModel
    private lateinit var sectionList: List<SettingsSection>

    private var settingsAdapter: SectionedRecyclerViewAdapter? = null

    private var classes: List<ClassRoom> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sectionList = listOf(
                SettingsSection(
                    R.string.setting_add,
                    listOf(
                        R.string.setting_add_student,
                        R.string.setting_add_class,
                        R.string.settings_version
                    ),
                    this
                )
        )

        if (settingsAdapter.isNull()) {
            settingsAdapter = SectionedRecyclerViewAdapter()
            for (it in sectionList) {
                settingsAdapter?.addSection(it)
            }
        }

        rv_settings.layoutManager = LinearLayoutManager(context)
        rv_settings.adapter = settingsAdapter

        viewModel.allClasses.observe(viewLifecycleOwner, Observer {
            classes = if (it.isNullOrEmpty()) emptyList() else it
        })
    }

    @SuppressLint("DefaultLocale")
    override fun onAddStudentClick() {
        if (classes.isEmpty()) {
            context?.let { context ->
                errorDialog(context, getString(R.string.error_not_class_found))
            }
            return
        }

        var name: String
        var surname: String
        var startedDate: String
        var gender = Gender.FEMALE.name
        var classTime = ClassTime.MORNING.name
        var classRoomId = classes[0].id
        val type: String = UserType.STUDENT.name

        context?.let {context ->
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_add_student)
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            val maxWidth = Resources.getSystem().displayMetrics.widthPixels
            val params = LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.window?.setLayout(params.width, params.height)

            dialog.et_started_date.addTextChangedListener(
                DateTextWatcher(dialog.et_started_date)
            )

            setupSpinnerGender(dialog.spinner_gender)
            setupSpinnerClasses(dialog.spinner_class)
            setupSpinnerClassTime(dialog.spinner_class_time)

            dialog.spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    gender = if (position == 0) Gender.FEMALE.name else Gender.MALE.name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

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
                if (!isValid(dialog)) return@setOnClickListener
                name = dialog.et_first_name.text.toString()
                surname = dialog.et_last_name.text.toString()
                startedDate = dialog.et_started_date.text.toString()
                val user = User(
                    name, surname, startedDate,
                    gender, classTime, classRoomId,
                    type
                )
                viewModel.insertOrUpdateUser(user)

                Toast.makeText(context, R.string.setting_student_saved_message, Toast.LENGTH_SHORT).show()
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

    private fun setupSpinnerGender(spinner: Spinner) {
        spinner.adapter = ArrayAdapter(
            spinner.context,
            R.layout.item_spinner,
            resources.getStringArray(R.array.gender_array).toList()
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

    override fun onAddClassClick() {
        context?.let { context ->
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_add_class)
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            val maxWidth = Resources.getSystem().displayMetrics.widthPixels - 200
            val params = LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.window?.setLayout(params.width, params.height)

            dialog.btn_submit.setOnClickListener {
                if (!isNullOrBlank(dialog.et_name, dialog.til_name)) {
                    val classRoom = ClassRoom(
                        dialog.et_name.text.toString()
                    )
                    viewModel.insertOrUpdateClass(classRoom)
                    Toast.makeText(context, R.string.setting_class_saved_message, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }


    override fun onDeleteAllStudentsClick() {
        viewModel.deleteAllStudents()
    }

    private fun isValid(dialog: Dialog): Boolean {
        val etFirstName = dialog.et_first_name
        val etLastName = dialog.et_last_name
        val etStartedDate = dialog.et_started_date

        if (isNullOrBlank(etFirstName, dialog.til_first_name)) return false
        if (isNullOrBlank(etLastName, dialog.til_last_name)) return false
        if (isNullOrBlank(etStartedDate, dialog.til_started_date)) return false


        if(!NAME_PATTERN.matcher(etFirstName.text).matches()) {
            dialog.til_first_name.error = getString(R.string.error_invalid_names)
            dialog.til_first_name.requestFocus()
            return false
        }

        if(!NAME_PATTERN.matcher(etLastName.text).matches()) {
            dialog.til_last_name.error = getString(R.string.error_invalid_names)
            dialog.til_last_name.requestFocus()
            return false
        }

        val startedDate = etStartedDate.text.toString().toDate(DatePatterns.ddMMyyyy, BR_LOCALE)

        if (startedDate.isNotNull()) {
            startedDate?.let { dt ->
                if (!dt.lessThanToday(withoutTime = true)) {
                    dialog.til_started_date.error = getString(R.string.error_invalid_date_greater_than_today)
                    dialog.til_started_date.requestFocus()
                    return false
                }

                if (Date().yearsLeftFrom(dt, true) > 1) {
                    dialog.til_started_date.error = getString(R.string.error_invalid_date_greater_than)
                    dialog.til_started_date.requestFocus()
                    return false
                }
            }
        } else {
            dialog.til_started_date.error = getString(R.string.error_invalid_started_date)
            dialog.til_started_date.requestFocus()
            return false
        }

        return true
    }

}