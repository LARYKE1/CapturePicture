package com.example.capturepicture;

import android.net.Uri;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.holder> {

    List<ImageModel> images;

    public AdapterClass(List<ImageModel> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        ImageModel model=images.get(position);

        holder.img.setImageURI(Uri.parse(model.getImageCaptured()));
        Toast.makeText(holder.itemView.getContext(), "The id is: "+model.getId()+" and the tag is: "+model.getTag(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView img;
        EditText editText;

        public holder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.imgCapture);
            editText=itemView.findViewById(R.id.txtTag);



        }

    }
}
