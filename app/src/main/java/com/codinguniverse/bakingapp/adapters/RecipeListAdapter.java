package com.codinguniverse.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codinguniverse.bakingapp.R;
import com.codinguniverse.bakingapp.models.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private List<Recipe> mRecipes;

    private OnClickHandler mOnClickHandler;

    public interface OnClickHandler{
        void onClick(int position);
    }

    public RecipeListAdapter(OnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        String name = mRecipes.get(position).getName();
        holder.mRecipeName.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null){
            return 0;
        }
        return mRecipes.size();
    }

    public void setRecipes(List<Recipe> mRecipeList){
        mRecipes = mRecipeList;
        notifyDataSetChanged();
    }

    public Recipe getRecipe(int position){
        return mRecipes.get(position);
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRecipeName;
        public RecipeListViewHolder(@NonNull View itemView) {
            super(itemView);

            mRecipeName = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickHandler.onClick(getAdapterPosition());
        }
    }
}
