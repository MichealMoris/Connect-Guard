package com.genius.connectguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.genius.constants.constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class adminPaswword extends Fragment
{
    private View view ;
    private EditText adminPassworrd ;
    private Button adminBtn ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.admin_password,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews()
    {
        adminPassworrd = view.findViewById(R.id.admin_password);
        adminBtn = view.findViewById(R.id.password_btn);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              String password =   adminPassworrd.getText().toString();

              adminPassworrd.setText(password);

              if (password.equals("123456") )
              {
                  constants.replaceFragment(adminPaswword.this , new AdminAddProduct() , false );
              }else
                  {
                      constants.showToast(getContext(), "try again");
                  }



            }
        });
    }
}
