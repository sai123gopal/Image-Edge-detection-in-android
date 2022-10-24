package com.example.plotline_assig;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.viewHolder> {

    ArrayList<HistoryModel> list;
    Context context;

    public ResultsAdapter(ArrayList<HistoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_items,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        try {
            holder.Original.setImageBitmap(StringToBitmap(list.get(position).Original));
            holder.Converted.setImageBitmap(StringToBitmap(list.get(position).Converted));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView Original,Converted;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            Original = itemView.findViewById(R.id.original);
            Converted = itemView.findViewById(R.id.converted);
        }
    }

    public Bitmap StringToBitmap(String uri) throws IOException {

        Uri imageUri = Uri.parse(uri);

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), imageUri);
        return bitmap;
    }

}
