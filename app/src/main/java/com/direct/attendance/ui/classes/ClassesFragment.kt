package com.direct.attendance.ui.classes

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.direct.attendance.R
import com.direct.attendance.database.State
import com.direct.attendance.extension.errorDialog
import com.direct.attendance.extension.isNullOrBlank
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.ui.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_add_class.btn_submit
import kotlinx.android.synthetic.main.dialog_add_class.et_name
import kotlinx.android.synthetic.main.dialog_add_class.til_name
import kotlinx.android.synthetic.main.fragment_classes.rv_classes
import org.koin.android.scope.currentScope

class ClassesFragment : BaseFragment(), ClassesListener {

    private val viewModelFactory: ClassesViewModelFactory by currentScope.inject()

    private lateinit var viewModel: ClassesViewModel
    private lateinit var adapter: ClassesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_classes, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClassesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClassesAdapter(this)

        rv_classes.layoutManager = GridLayoutManager(
            context,
            2,
            RecyclerView.VERTICAL,
            false
        )
        rv_classes.adapter = adapter

        viewModel.allClasses.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

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
    }

    override fun onUpdateClass(classRoom: ClassRoom) {
        context?.let { context ->
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_add_class)
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            val maxWidth = Resources.getSystem().displayMetrics.widthPixels - 200
            val params = LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.window?.setLayout(params.width, params.height)

            dialog.btn_submit.setText(R.string.update)
            dialog.et_name.setText(classRoom.name)

            dialog.btn_submit.setOnClickListener {
                if (!isNullOrBlank(dialog.et_name, dialog.til_name)) {
                    classRoom.name = dialog.et_name.text.toString()
                    viewModel.update(classRoom)
                    Toast.makeText(context, R.string.classe_updated_message, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }
}