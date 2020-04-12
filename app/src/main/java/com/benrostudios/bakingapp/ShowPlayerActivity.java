package com.benrostudios.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.benrostudios.bakingapp.ui.PlayerFragment;
import com.google.android.exoplayer2.Player;
import com.google.android.material.badge.BadgeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPlayerActivity extends AppCompatActivity {
    private List<StepsBean> steps;
    private int mPos = 0;
    private int mLength = 0;
    private final String saved = "PLAYER";
    private final String frag = "FRAGMENT";
    private PlayerFragment playerFrag;

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

        if (savedInstanceState != null) {
            mPos = savedInstanceState.getInt(saved);
            playerFrag = (PlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, frag);
            getSupportFragmentManager().beginTransaction().replace(R.id.final_holder, playerFrag).commit();
            updateSteps();
        } else {
            if (mPos == 0) {
                replaceFragment();
                updateSteps();
            }
        }
        if (mPos == 0) {
            prevButton.setVisibility(View.GONE);

        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPos != mLength - 2) {
                    mPos++;
                    if (mPos != 0)
                        prevButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    replaceFragment();
                } else {
                    nextButton.setVisibility(View.GONE);
                    mPos++;
                    replaceFragment();
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPos == 1) {
                    prevButton.setVisibility(View.GONE);
                    mPos--;
                    replaceFragment();
                } else {
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
        super.onSaveInstanceState(outState);
        outState.putInt(saved, mPos);
        getSupportFragmentManager().putFragment(outState, frag, playerFrag);
    }

    private void replaceFragment() {
        PlayerFragment player = PlayerFragment.newInstance(steps.get(mPos), false);
        updateSteps();
        playerFrag = player;
        getSupportFragmentManager().beginTransaction().replace(R.id.final_holder, player).commit();
    }

    private void updateSteps() {
        int temp = mLength - 1;
        stepsNumber.setText("Step " + mPos + "/" + temp);
    }
}
