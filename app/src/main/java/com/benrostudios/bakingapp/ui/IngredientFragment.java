package com.benrostudios.bakingapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.adapters.IngredientsAdapter;
import com.benrostudios.bakingapp.network.response.IngredientsBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment {
    @BindView(R.id.ingredients_holder)
    RecyclerView ingredients_holder;
    private Unbinder unbinder;
    private List<IngredientsBean> ingredients;

    public IngredientFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        unbinder = ButterKnife.bind(this, view);
        ingredients_holder.setLayoutManager(new LinearLayoutManager(getContext()));
        IngredientsAdapter adapter = new IngredientsAdapter(ingredients);
        ingredients_holder.setAdapter(adapter);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredients = (List<IngredientsBean>) getArguments().getSerializable("ListIngredients");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static IngredientFragment newInstance(List<IngredientsBean> ingredients) {

        Bundle args = new Bundle();
        args.putSerializable("ListIngredients",(Serializable) ingredients);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
