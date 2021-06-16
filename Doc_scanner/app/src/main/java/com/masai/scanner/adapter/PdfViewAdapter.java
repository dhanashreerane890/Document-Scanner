package com.masai.scanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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

    public PdfViewAdapter(Context context, List<File> pdfFiles) {
        this.context = context;
        this.pdfFiles = pdfFiles;
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
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
