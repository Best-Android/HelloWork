package com.best.luchangdie.hellowork.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.best.luchangdie.hellowork.R;
import com.best.luchangdie.hellowork.view.fragment.BoutiqueFragment;
import com.best.luchangdie.hellowork.view.fragment.CartFragment;
import com.best.luchangdie.hellowork.view.fragment.NewGoodFragment;
import com.best.luchangdie.hellowork.view.fragment.PersonalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rb_new_good)
    RadioButton rbNewGood;
    @BindView(R.id.rb_boutique)
    RadioButton rbBoutique;
    @BindView(R.id.rb_cart)
    RadioButton rbCart;
    @BindView(R.id.rb_personal)
    RadioButton rbPersonal;
    @BindView(R.id.layout_fragment)
    LinearLayout layoutFragment;

    Fragment newGoodFragment = new NewGoodFragment();
    Fragment boutiqueFragment = new BoutiqueFragment();
    Fragment cartFragment = new CartFragment();
    Fragment personalFragment = new PersonalFragment();
    Fragment mTempFragment = newGoodFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rbNewGood.setChecked(true);
        rbNewGood.setOnClickListener(this);
        rbBoutique.setOnClickListener(this);
        rbCart.setOnClickListener(this);
        rbPersonal.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment,newGoodFragment).commit();
    }

    public void switchContent (Fragment fragment) {
        if (fragment != mTempFragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mTempFragment).add(R.id.layout_fragment,fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mTempFragment).show(fragment).commit();
            }
            mTempFragment = fragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_new_good:
                switchContent(newGoodFragment);
                break;
            case R.id.rb_boutique:
                switchContent(boutiqueFragment);
                break;
            case R.id.rb_cart:
                switchContent(cartFragment);
                break;
            case R.id.rb_personal:
                switchContent(personalFragment);
                break;
        }
    }
}
