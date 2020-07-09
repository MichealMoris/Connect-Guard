package com.genius.connectguard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.genius.constants.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInFragment extends Fragment {

    //
    TextView createNewAccount;
    TextView forget_password;
    private View view ;
    private Button loginBtn ;
    private EditText emailField ;
    private EditText passwordField ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sign_in, null);


        createNewAccount = view.findViewById(R.id.createAccountText);
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new SignUpFragment());

            }
        });

        forget_password = view.findViewById(R.id.forgetPasswordText);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new ForgetPasswordFragment());

            }
        });

        return view;
    }


    private void setFragemnt(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(R.id.register_framelayout, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        initViews();
        constants.initProgress(requireContext(),"please wait ...");

    }

    private void initViews()
    {
        emailField = view.findViewById(R.id.login_email_field);
        passwordField = view.findViewById(R.id.login_password_field);
        loginBtn = view.findViewById(R.id.login_login_btn);


        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (email.isEmpty() || password.isEmpty())
                {
                    constants.showToast(requireContext(),"invalid data");
                    return;
                }
                constants.showProgress();

                loginFireBase(email,password);


            }
        });
    }

    private void loginFireBase(String email, String password)
    {
        constants.getAuth().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        constants.dissmisProgress();

                        if (task.isSuccessful())
                        {
                            constants.saveUId(requireActivity(),task.getResult().getUser().getUid());

                            setFragemnt(new MainFragment());
                        }else
                        {
                            constants.showToast(requireContext(), task.getException().getMessage());
                        }

                    }
                });
    }


}