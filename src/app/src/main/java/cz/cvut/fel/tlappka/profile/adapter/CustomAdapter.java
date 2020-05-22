package cz.cvut.fel.tlappka.profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cz.cvut.fel.tlappka.MainActivity;
import cz.cvut.fel.tlappka.R;
import cz.cvut.fel.tlappka.profile.EditProfileActivity;
import cz.cvut.fel.tlappka.profile.PetFragment;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<IkonkaModelClass> items;
    public CustomAdapter(Context context, ArrayList<IkonkaModelClass> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_icon_pet, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //holder.itemTitle.setText(items.get(position).getTitle());
        Picasso.with(context).load(items.get(position).getImage()).into(holder.itemImage);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public int selectedPosition = -1;

        public int getSelectedPosition()
        {
            return selectedPosition;
        }

        public void setSelectedPosition(int selectedPosition)
        {
            this.selectedPosition = selectedPosition;
        }

        private ImageView itemImage;
        private TextView itemTitle;
        //private TextView itemTitle;
        public CustomViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.item_image);

            itemTitle = view.findViewById(R.id.item_title);
        }

        @Override
        public void onClick(View view) {
            selectedPosition = getAbsoluteAdapterPosition();
            notifyDataSetChanged();

            if(selectedPosition >= 0) {

            }
        }
    }

}