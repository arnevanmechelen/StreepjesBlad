package com.example.streepjesblad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    Integer amountOfStr = 0;
    TextView amountTextView;
    TextView nameTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        //Get the amount Text View
        amountTextView = fragmentFirstLayout.findViewById(R.id.amountTextView);
        nameTextView = fragmentFirstLayout.findViewById(R.id.nameTextView);
        return fragmentFirstLayout;
    }

    private void addStreepjes(View view, int amount){
        amountOfStr = Integer.parseInt(amountTextView.getText().toString());
        amountOfStr += amount;
        amountTextView.setText(amountOfStr.toString());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String name = FirstFragmentArgs.fromBundle(getArguments()).getMyArg();
        nameTextView.setText(name);


        view.findViewById(R.id.redbullButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast btnToast = Toast.makeText(getActivity(), "1 streepje toegevoegd", Toast.LENGTH_SHORT);
                btnToast.show();
                addStreepjes(view, 1);
            }
        });

        view.findViewById(R.id.frisdrankButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast btnToast = Toast.makeText(getActivity(), "2 streepjes toegevoegd", Toast.LENGTH_SHORT);
                btnToast.show();
                addStreepjes(view, 2);
            }
        });

        view.findViewById(R.id.beerButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast btnToast = Toast.makeText(getActivity(), "2 streepjes toegevoegd", Toast.LENGTH_SHORT);
                btnToast.show();
                addStreepjes(view, 2);
            }
        });

        view.findViewById(R.id.kriekButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast btnToast = Toast.makeText(getActivity(), "3 streepjes toegevoegd", Toast.LENGTH_SHORT);
                btnToast.show();
                addStreepjes(view, 3);
            }
        });

        view.findViewById(R.id.chipsButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast btnToast = Toast.makeText(getActivity(), "1 streepje toegevoegd", Toast.LENGTH_SHORT);
                btnToast.show();
                addStreepjes(view, 1);
            }
        });

        view.findViewById(R.id.otherButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast btnToast = Toast.makeText(getActivity(), "1 streepje toegevoegd", Toast.LENGTH_SHORT);
                btnToast.show();
                addStreepjes(view, 1);
            }
        });

    }
}