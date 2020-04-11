package com.benrostudios.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.network.response.IngredientsBean;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends  RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder>{

    List<IngredientsBean> ingredients;

    public IngredientsAdapter(List<IngredientsBean> ingredients){
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item,parent,false);
        IngredientsHolder ingredientsHolder = new IngredientsHolder(v);
        return ingredientsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsHolder holder, int position) {
        holder.ingre_name.setText(ingredients.get(position).getIngredient());
        holder.ingre_quantity.setText(ingredients.get(position).getQuantity()+" "+ingredients.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public static class IngredientsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name)
        TextView ingre_name;

        @BindView(R.id.ingredient_quantity)
        TextView ingre_quantity;


        public IngredientsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
