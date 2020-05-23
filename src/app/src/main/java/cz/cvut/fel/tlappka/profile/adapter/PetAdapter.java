package cz.cvut.fel.tlappka.profile.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import cz.cvut.fel.tlappka.R;
import cz.cvut.fel.tlappka.model.Pet;
import cz.cvut.fel.tlappka.profile.PetFragment;
import cz.cvut.fel.tlappka.profile.ProfileFragment;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.MyViewHolder> {
    private Context context;
    private List<IkonkaModelClass> petList;

    public PetAdapter(Context context, List<IkonkaModelClass> petList) {
        this.petList = petList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_icon_pet, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(context).load(petList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String mItem;
        ImageView image;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedPosition = getAbsoluteAdapterPosition();
            notifyDataSetChanged();
            //Toast.makeText(context, " SelectedPosition" + selectedPosition, Toast.LENGTH_SHORT).show();
            if (selectedPosition >= 0) {
                if(petList.get(selectedPosition).isPet()) {
                    PetFragment petFragment = new PetFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("UID", petList.get(selectedPosition).getUID());
                    petFragment.setArguments(arguments);
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, petFragment);
//        fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit();
                }else{
                    ProfileFragment profileFragment = new ProfileFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("UID", petList.get(selectedPosition).getUID());
                    profileFragment.setArguments(arguments);
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, profileFragment);
//        fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit();
                }
            }
        }

        public int selectedPosition = -1;

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

    }
}