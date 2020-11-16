package com.example.streepjesblad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {
    String name, password;

    EditText nameEditText;
    EditText passwordEditText;
    Button loginButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentSecondLayout = inflater.inflate(R.layout.fragment_second, container, false);

        nameEditText = (EditText) fragmentSecondLayout.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) fragmentSecondLayout.findViewById(R.id.passwordEditText);
        loginButton = (Button) fragmentSecondLayout.findViewById(R.id.loginBtn);
        return fragmentSecondLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                SecondFragmentDirections.ActionSecondFragmentToFirstFragment action = SecondFragmentDirections.actionSecondFragmentToFirstFragment(name);
                NavHostFragment.findNavController(SecondFragment.this).navigate(action);
            }
        });
    }
}