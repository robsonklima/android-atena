package com.robsonlima.atena;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProjectEditFragment extends Fragment {

    public static TextView etName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_edit_fragment, container, false);

        etName = (TextView) view.findViewById(R.id.etName);

        return view;
    }

}
