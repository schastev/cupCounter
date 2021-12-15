package com.example.cupcounter.customerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cupcounter.R;
import com.example.cupcounter.database.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private List<Customer> customers;
    private final LayoutInflater inflater;
    private OnItemClickListener clickListener;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name, phoneNumber;

        public void setClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        OnItemClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            name = (TextView) view.findViewById(R.id.name);
            phoneNumber = (TextView) view.findViewById(R.id.phoneNumber);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public TextView getName() {
            return name;
        }

        public TextView getPhoneNumber() {
            return phoneNumber;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAbsoluteAdapterPosition());
        }

    }

    public CustomerAdapter(Context context, List<Customer> dataSet) {
        this.customers = dataSet;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomerAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Customer customer = customers.get(position);
        viewHolder.setClickListener(clickListener);
        viewHolder.name.setText(customer.getName());
        viewHolder.phoneNumber.setText(customer.getPhoneNumber());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}

