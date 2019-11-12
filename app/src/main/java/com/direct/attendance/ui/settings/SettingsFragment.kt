package com.direct.attendance.ui.settings

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.direct.attendance.R
import com.direct.attendance.constant.Constants.Companion.BR_LOCALE
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.constant.Gender
import com.direct.attendance.extension.NAME_PATTERN
import com.direct.attendance.extension.isNotNull
import com.direct.attendance.extension.isNull
import com.direct.attendance.extension.lessThanToday
import com.direct.attendance.extension.toDate
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseFragment
import com.direct.attendance.utils.DateTextWatcher
import com.google.android.material.textfield.TextInputLayout
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.dialog_add_class.btn_submit
import kotlinx.android.synthetic.main.dialog_add_class.et_name
import kotlinx.android.synthetic.main.dialog_add_class.til_name
import kotlinx.android.synthetic.main.dialog_add_student.btn_submit_student
import kotlinx.android.synthetic.main.dialog_add_student.et_first_name
import kotlinx.android.synthetic.main.dialog_add_student.et_last_name
import kotlinx.android.synthetic.main.dialog_add_student.et_started_date
import kotlinx.android.synthetic.main.dialog_add_student.spinner_class
import kotlinx.android.synthetic.main.dialog_add_student.spinner_gender
import kotlinx.android.synthetic.main.dialog_add_student.til_first_name
import kotlinx.android.synthetic.main.dialog_add_student.til_last_name
import kotlinx.android.synthetic.main.dialog_add_student.til_started_date
import kotlinx.android.synthetic.main.fragment_settings.rv_settings
import org.koin.android.scope.currentScope

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
                        R.string.setting_add_class
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

    override fun onAddStudentClick() {
        context?.let {context ->
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_add_student)
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            val maxWidth = Resources.getSystem().displayMetrics.widthPixels - 200
            val params = LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.window?.setLayout(params.width, params.height)

            val user = User()

            dialog.et_started_date.addTextChangedListener(
                DateTextWatcher(dialog.et_started_date)
            )

            dialog.spinner_gender.adapter = ArrayAdapter(
                context,
                R.layout.item_spinner,
                resources.getStringArray(R.array.gender_array).toList()
            )

            dialog.spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    user.gender = if (position == 0) Gender.FEMALE.name else Gender.MALE.name
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            dialog.spinner_class.adapter = ArrayAdapter(
                context,
                R.layout.item_spinner,
                classes
            )

            dialog.spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (classes.isEmpty()) return
                    if (classes.size == 1) return
                    user.classRoom = classes[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            dialog.btn_submit_student.setOnClickListener {
                if (!isValid(dialog)) return@setOnClickListener
                user.name = dialog.et_first_name.text.toString()
                user.surname = dialog.et_last_name.text.toString()
                user.startDate = dialog.et_started_date.text.toString()
                viewModel.insertOrUpdateUser(user)
                Toast.makeText(context, R.string.setting_student_saved_message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
        }
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
                    val classRoom = ClassRoom()
                    classRoom.name = dialog.et_name.text.toString()
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


        if (!etStartedDate.text.isNullOrBlank()) {
            val startedDate = etStartedDate.text.toString().toDate(DatePatterns.ddMMyyyy, BR_LOCALE)
            startedDate?.let { dt ->
                if (!dt.lessThanToday(withoutTime = true)) {
                    dialog.til_started_date.error = getString(R.string.error_invalid_started_date)
                    dialog.til_started_date.requestFocus()
                    return false
                }
            }
        }

        return true
    }

    private fun isNullOrBlank(edit: EditText?, til: TextInputLayout? = null) : Boolean {
        edit?.error = null
        til?.error = null
        if (edit?.text.isNullOrBlank()) {
            if (til.isNotNull()) {
                til?.error = getString(R.string.error_required_field)
                til?.requestFocus()
            } else {
                edit?.error = getString(R.string.error_required_field)
                edit?.requestFocus()
            }
            return true
        }
        return false
    }


}