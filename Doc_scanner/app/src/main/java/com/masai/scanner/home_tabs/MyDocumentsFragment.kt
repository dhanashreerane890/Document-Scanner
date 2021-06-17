package com.masai.scanner.home_tabs


import android.content.Intent
import android.os.Bundle
import android.os.Environment
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
import java.io.File
import java.util.*


class MyDocumentsFragment : Fragment(),OnPdfSelectListner {
    private var pdfAdapter: PdfViewAdapter? = null
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
        recyclerView=view.findViewById(R.id.recyclerViewForFile)
        displayPdf()
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
        recyclerView?.setHasFixedSize(true);
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager=linearLayoutManager
        pdfList = ArrayList()
       findPdf(Environment.getExternalStorageDirectory())?.let { (pdfList as ArrayList<File>).addAll(it) }
        pdfAdapter= PdfViewAdapter(context,pdfList,this)
        recyclerView?.adapter =pdfAdapter
    }

    override fun onPdfSelected(file: File?) {
        if (file != null) {
            startActivity(Intent(activity,PdfActivity::class.java)
                .putExtra("path",file.absolutePath))
        }
    }


}