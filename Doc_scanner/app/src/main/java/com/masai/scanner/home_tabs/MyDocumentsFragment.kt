package com.masai.scanner.home_tabs


import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.masai.scanner.PdfActivity
import com.masai.scanner.R
import com.masai.scanner.adapter.OnPdfSelectListner
import com.masai.scanner.adapter.PdfViewAdapter
import kotlinx.android.synthetic.main.fragment_my_documents.*
import java.io.File
import java.util.*


class MyDocumentsFragment : Fragment(), OnPdfSelectListner {
    var pdfAdapter: PdfViewAdapter? = null
    private var pdfList: List<File>? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_documents, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewForFile)
        displayPdf()
        searchFiles()



        button2.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "hey try this amazing app!!  https://github.com/varunwani22/Document-Scanner"
            )
            startActivity(Intent.createChooser(intent, "Share this Image"))
        }
    }

    private fun searchFiles() {
        etSearchFiles.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                pdfAdapter?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    fun findPdf(file: File): ArrayList<File>? {
        val arrayList = ArrayList<File>()
        val files = file.listFiles()
        for (singleFile in files) {
            if (singleFile.isDirectory && !singleFile.isHidden) {
                arrayList.addAll(findPdf(singleFile)!!)
            } else {
                if (singleFile.name.endsWith(".pdf")) {
                    arrayList.add(singleFile)
                }
            }
        }
        return arrayList
    }

    fun displayPdf() {
        recyclerView?.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = linearLayoutManager
        pdfList = ArrayList()
        findPdf(Environment.getExternalStorageDirectory())?.let {
            (pdfList as ArrayList<File>).addAll(
                it
            )
        }
        pdfAdapter = PdfViewAdapter(context, pdfList, this)
        recyclerView?.adapter = pdfAdapter
    }

    override fun onPdfSelected(file: File?) {
        if (file != null) {
            startActivity(
                Intent(activity, PdfActivity::class.java)
                    .putExtra("path", file.absolutePath)
            )
        }
    }


}