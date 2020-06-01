package com.codinguniverse.bakingapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codinguniverse.bakingapp.R;
import com.codinguniverse.bakingapp.models.Ingredients;
import com.codinguniverse.bakingapp.models.Steps;

import java.util.List;

import static android.content.ContentValues.TAG;

public class DetailRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Steps> mStepsList;
    private List<Ingredients> mIngredientsList;


    private static final int TYPE_INGREDIENT = 1;
    private static final int TYPE_STEPS = 2;

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

    public DetailRecipeAdapter(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_INGREDIENT){
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item, parent, false);
           return new IngredientsViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, parent, false);
            return new StepsViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_INGREDIENT){
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (Ingredients ingredient : mIngredientsList){
                ingredientsBuilder.append(ingredient.getQuantity());
                ingredientsBuilder.append("   ");
                ingredientsBuilder.append(ingredient.getMeasure());
                ingredientsBuilder.append("   ");
                ingredientsBuilder.append(ingredient.getIngredient());
                ingredientsBuilder.append("\n");
            }
            ((IngredientsViewHolder)holder).setIngredients(ingredientsBuilder.toString());
        }else {
            String description = mStepsList.get(position-1).getShortDescription();
            ((StepsViewHolder)holder).setStepName(description);
        }
    }

    @Override
    public int getItemCount() {

        if (mIngredientsList == null && mStepsList == null){
            return 0;
        }

        return mStepsList.size()+1;
    }

    public void setIngredientsList(List<Ingredients> list){
        mIngredientsList = list;
        notifyDataSetChanged();
    }

    public void setStepsList(List<Steps> list){

        mStepsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_INGREDIENT;
        }else{
            return TYPE_STEPS;
        }
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder{
        TextView mIngredients;
        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            mIngredients = itemView.findViewById(R.id.ingredients_text);
        }

        void setIngredients(String ingredients){
            mIngredients.setText(ingredients);
        }
    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mStepName;
        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            mStepName = itemView.findViewById(R.id.recipe_step);

            itemView.setOnClickListener(this);
        }

        void setStepName(String name){
            mStepName.setText(name);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null){
                mItemClickListener.onItemClicked(getAdapterPosition()-1);
            }
        }
    }
}
