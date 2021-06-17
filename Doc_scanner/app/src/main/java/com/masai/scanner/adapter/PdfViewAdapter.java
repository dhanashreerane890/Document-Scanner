package com.masai.scanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masai.scanner.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class PdfViewAdapter extends RecyclerView.Adapter<PdfViewHolder> {

    private Context context;
    private List<File> pdfFiles;
    private OnPdfSelectListner listener;


    public PdfViewAdapter(Context context, List<File> pdfFiles, OnPdfSelectListner listener) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
    }

    @NotNull
    @Override
    public PdfViewHolder onCreateViewHolder( @NotNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder( @NotNull PdfViewHolder holder, int position) {
         holder.textName.setText(pdfFiles.get(position).getName());
         holder.textName.setSelected(true);
         holder.cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.onPdfSelected(pdfFiles.get(position));
             }
         });
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
