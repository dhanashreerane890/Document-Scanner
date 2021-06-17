package com.masai.scanner.home_tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.masai.scanner.R
import com.masai.scanner.dialog_box.EnterFolderName
import com.masai.scanner.local_database.FolderDatabase
import com.masai.scanner.local_database.FoldersEntity
import com.masai.scanner.repository.FolderRepository
import com.masai.scanner.viewmodels.FolderViewModel
import com.masai.scanner.viewmodels.FolderViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_folders.*

class MyFoldersFragment : Fragment(), EnterFolderName.DialogListener {

    private val foldersDAO by lazy {
        val roomDatabase = FolderDatabase.getDatabase(requireContext())
        roomDatabase.getFolderDao()
    }
    val repository by lazy {
        FolderRepository(foldersDAO)
    }
    private lateinit var viewModel: FolderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel()
    }

    private fun settingViewModel() {
        val viewModelFactory = FolderViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FolderViewModel::class.java)

        addFolders.setOnClickListener {
            openDialog()

        }

    }

    private fun openDialog() {
        val folderDialog = EnterFolderName()
        activity?.let { folderDialog.show(it.supportFragmentManager, "dialog") }

    }

    override fun applyText(folderName: String?) {
        val foldersEntity = folderName?.let { FoldersEntity(it, 0) }
        foldersEntity?.let { viewModel.addFolder(it) }
    }


}