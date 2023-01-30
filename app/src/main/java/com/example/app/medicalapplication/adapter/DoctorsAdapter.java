package com.example.app.medicalapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.medicalapplication.R;
import com.example.app.medicalapplication.databinding.DoctorRowBinding;
import com.example.app.medicalapplication.model.UserModel;
import com.example.app.medicalapplication.ui.AddUpdateOrderActivity;


public class DoctorsAdapter extends ListAdapter<UserModel, DoctorsAdapter.OrderViewHolder> {
    private int mode;
    private Context context;

    public static final DiffUtil.ItemCallback<UserModel> ORDER_ITEM_CALLBACK = new DiffUtil.ItemCallback<UserModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
            return false;
        }
    };

    public DoctorsAdapter(Context context) {
        super(ORDER_ITEM_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        UserModel userModel=getItem(position);
        holder.binding.doctorName.setText(userModel.getName());
        holder.binding.doctorSpec.setText(userModel.getSpec());
        holder.binding.addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(context, AddUpdateOrderActivity.class);
              intent.putExtra("doc",userModel.getUserName());
              context.startActivity(intent);
            }
        });


    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public DoctorRowBinding binding;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DoctorRowBinding.bind(itemView);
        }

    }
}
