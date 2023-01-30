package com.example.app.medicalapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.medicalapplication.R;
import com.example.app.medicalapplication.databinding.OrderRowBinding;
import com.example.app.medicalapplication.model.Order;
import com.example.app.medicalapplication.ui.DoctorOrderDetailsActivity;
import com.example.app.medicalapplication.ui.UserViewOrderDetailsActivity;


public class OrdersAdapter extends ListAdapter<Order, OrdersAdapter.OrderViewHolder> {
    private int mode;
    private Context context;

    public static final DiffUtil.ItemCallback<Order> ORDER_ITEM_CALLBACK = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return false;
        }
    };

    public OrdersAdapter(Context context, int mode) {
        super(ORDER_ITEM_CALLBACK);
        this.context = context;
        this.mode = mode;
        Log.d("SSSS", "OrdersAdapter: "+mode);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order item = getItem(position);
        holder.binding.name.setText("Name : " + item.getName());
        holder.binding.address.setText("Address : " + item.getAddress());
        holder.binding.phone.setText("Phone : " + item.getPhone());
        holder.binding.describe.setText("Description : " + item.getDescribe());
        holder.binding.actionBtn.setText("Check");


        switch (this.mode) {
            case 1: {
                // from user
                holder.binding.actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getPharmacyName() == null && item.getPharmacyPhone() == null) {
                            Toast.makeText(context, "Not Checked By Doctor", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent=new Intent(context, UserViewOrderDetailsActivity.class);
                            intent.putExtra("data",item);
                            context.startActivity(intent);

//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setTitle("Delivery Info");
//                            builder.setMessage("Delivery Name : " + item.getDeliveryName() + "\n" + "Delivery Phone : " + item.getDeliveryPhone());
//                            builder.setNeutralButton("Call Delivery", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    ((ViewOrdersActivity) context).callPhone(item.getDeliveryPhone());
//
//                                }
//                            }).create().show();
                        }
                    }
                });
                break;
            }
            case 2: {

                holder.binding.actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, DoctorOrderDetailsActivity.class);
                        intent.putExtra("data",item);
                        context.startActivity(intent);
                    }
                });
                break;
            }
            case 3: {

                holder.binding.actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, UserViewOrderDetailsActivity.class);
                        intent.putExtra("data",item);
                        intent.putExtra("from","pharmacy");
                        context.startActivity(intent);
                    }
                });
                break;
            }

        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public OrderRowBinding binding;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = OrderRowBinding.bind(itemView);
        }

    }
}
