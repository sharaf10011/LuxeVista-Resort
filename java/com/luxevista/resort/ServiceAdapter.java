package com.luxevista.resort;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private OnServiceClickListener onServiceClickListener;

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }

    public ServiceAdapter(List<Service> serviceList, OnServiceClickListener listener) {
        this.serviceList = serviceList;
        this.onServiceClickListener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivServiceImage;
        private TextView tvServiceName, tvServiceDescription, tvServicePrice, tvServiceCategory;
        private Button btnReserveService;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ivServiceImage = itemView.findViewById(R.id.ivServiceImage);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServiceDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvServiceCategory = itemView.findViewById(R.id.tvServiceCategory);
            btnReserveService = itemView.findViewById(R.id.btnReserveService);
        }

        public void bind(Service service) {
            tvServiceName.setText(service.getName());
            tvServiceDescription.setText(service.getDescription());
            tvServicePrice.setText(String.format("$%.0f", service.getPrice()));
            tvServiceCategory.setText(service.getCategory());
            ivServiceImage.setImageResource(service.getImageResource());

            btnReserveService.setOnClickListener(v -> {
                if (onServiceClickListener != null) {
                    onServiceClickListener.onServiceClick(service);
                }
            });
        }
    }
}
