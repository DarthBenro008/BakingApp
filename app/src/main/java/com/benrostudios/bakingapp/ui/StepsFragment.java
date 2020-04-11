package com.benrostudios.bakingapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.adapters.StepsAdapter;
import com.benrostudios.bakingapp.network.response.IngredientsBean;
import com.benrostudios.bakingapp.network.response.StepsBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    @BindView(R.id.steps_holder)
    RecyclerView steps_holder;
    private Boolean isTwoPane;
    private List<StepsBean> steps;
    StepsAdapter adapter;
    private static  final String LIST_STEPS = "ListSteps";
    private static final String TWO_PANE_BOOL = "TwoPaneBool";

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this , view);
        steps_holder.setLayoutManager(new LinearLayoutManager(getContext()));

        if(isTwoPane) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container,PlayerFragment.newInstance(steps.get(0),isTwoPane)).commit();
            StepsAdapter.CustomClickListener listener = (v, position) -> {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container,PlayerFragment.newInstance(steps.get(position),isTwoPane)).commit();
            };
             adapter = new StepsAdapter(steps, getContext(), isTwoPane, listener);
        }else{
            adapter = new StepsAdapter(steps, getContext(), isTwoPane, null);
        }
        steps_holder.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steps = (List<StepsBean>) getArguments().getSerializable(LIST_STEPS);
        isTwoPane = getArguments().getBoolean(TWO_PANE_BOOL);
    }

    public static StepsFragment newInstance(List<StepsBean> steps, Boolean isTwoPane) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putSerializable(LIST_STEPS,(Serializable) steps);
        args.putBoolean(TWO_PANE_BOOL,isTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

}
