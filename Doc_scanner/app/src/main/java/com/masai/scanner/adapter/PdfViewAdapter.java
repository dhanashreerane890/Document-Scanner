package com.masai.scanner.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masai.scanner.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PdfViewAdapter extends RecyclerView.Adapter<PdfViewHolder> implements Filterable {


    private Context context;
    private List<File> pdfFiles;
    private OnPdfSelectListner listener;
    private List<File> listOfAllFiles;
    private Communicationlistener communicationlistener;


    public PdfViewAdapter(Context context, List<File> pdfFiles, OnPdfSelectListner listener) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
        this.listOfAllFiles = new ArrayList<>(pdfFiles);
    }

    @NotNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull PdfViewHolder holder, int position) {
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<File> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(listOfAllFiles);
            } else {
                for (File files : listOfAllFiles) {
                    if (files.toString().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(files);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            pdfFiles.clear();
            pdfFiles.addAll((Collection<? extends File>) filterResults.values);
            notifyDataSetChanged();
        }
    };



}
