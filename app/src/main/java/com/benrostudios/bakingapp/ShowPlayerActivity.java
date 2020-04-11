package com.benrostudios.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.benrostudios.bakingapp.ui.PlayerFragment;
import com.google.android.material.badge.BadgeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPlayerActivity extends AppCompatActivity {
    private List<StepsBean> steps;
    private int mPos = 0;
    private int mLength = 0;
    private final String saved = "PLAYER";

    @BindView(R.id.next_button)
    Button nextButton;

    @BindView(R.id.prev_button)
    Button prevButton;

    @BindView(R.id.step_number_final)
    TextView stepsNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_player);
        ButterKnife.bind(this);
        Intent recieve = getIntent();
        steps = (List<StepsBean>) recieve.getSerializableExtra("Steps");
        mLength = steps.size();

        if(savedInstanceState!=null){
            mPos = savedInstanceState.getInt(saved);
        }
        if(mPos == 0){
            prevButton.setVisibility(View.GONE);
        }
        replaceFragment();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPos != mLength-2){
                    mPos++;
                    if(mPos!=0)
                        prevButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    replaceFragment();
                }else{
                    nextButton.setVisibility(View.GONE);
                    mPos++;
                    replaceFragment();
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPos == 1){
                    prevButton.setVisibility(View.GONE);
                    mPos--;
                    replaceFragment();
                }else{
                    nextButton.setVisibility(View.VISIBLE);
                    prevButton.setVisibility(View.VISIBLE);
                    mPos--;
                    replaceFragment();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(saved,mPos);
        super.onSaveInstanceState(outState);
    }

    private void replaceFragment(){
        PlayerFragment player = PlayerFragment.newInstance(steps.get(mPos),false);
        updateSteps();
        getSupportFragmentManager().beginTransaction().replace(R.id.final_holder,player).commit();
    }
    private void updateSteps(){
        int temp = mLength-1;
        stepsNumber.setText("Step "+ mPos + "/"+temp);
    }
}
