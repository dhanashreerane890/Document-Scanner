package com.masai.scanner.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.masai.scanner.R;

import org.jetbrains.annotations.NotNull;

public class PdfViewHolder extends RecyclerView.ViewHolder {
    public TextView textName;
    public CardView cardView;

    public PdfViewHolder(@NotNull View itemView) {
        super(itemView);
        textName=itemView.findViewById(R.id.pdf_txtName);
        cardView=itemView.findViewById(R.id.pdf_cardView);
    }
}
