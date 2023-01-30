package com.example.app.medicalapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.example.app.medicalapplication.R;
import com.example.app.medicalapplication.databinding.PharmacyRowBinding;
import com.example.app.medicalapplication.model.UserModel;

import java.util.List;

public class PharmacyAdapter extends BaseAdapter {
    private List<UserModel> models;

    public PharmacyAdapter(List<UserModel> models) {
        this.models = models;
    }

    @Override
    public int getCount() {
        return this.models.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacy_row,viewGroup,false);
        PharmacyRowBinding binding=PharmacyRowBinding.bind(view1);
        binding.pName.setText(this.models.get(i).getName());
        return view1;
    }
}
