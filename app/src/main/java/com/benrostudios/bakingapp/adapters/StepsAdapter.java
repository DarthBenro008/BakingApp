package com.benrostudios.bakingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.ShowPlayerActivity;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.benrostudios.bakingapp.ui.StepsFragment;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {
    private List<StepsBean> steps;
    private Context mContext;
    Boolean istwoPane;
    private CustomClickListener mListener;

    public interface CustomClickListener{
        void customClick(View view,int position);
    }

    public StepsAdapter(List<StepsBean> steps, Context mContext, Boolean istwoPane,CustomClickListener listener) {
        this.steps = steps;
        this.mContext = mContext;
        this.istwoPane = istwoPane;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public StepsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, parent, false);
        StepsHolder stepsHolder = new StepsHolder(v,mListener);
        return stepsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsHolder holder, int position) {
        holder.step_number.setText("" + steps.get(position).getId());
        holder.step_content.setText(steps.get(position).getShortDescription());
        if (!istwoPane) {
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShowPlayerActivity.class);
                    intent.putExtra("Steps", (Serializable) steps);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class StepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.step_number)
        TextView step_number;

        @BindView(R.id.step_content)
        TextView step_content;

        @BindView(R.id.steps_item_holder)
        ConstraintLayout constraintLayout;

        private CustomClickListener mListener;

        public StepsHolder(View view,CustomClickListener listener) {
            super(view);
            mListener = listener;
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            mListener.customClick(v,getAdapterPosition());
        }
    }
}
