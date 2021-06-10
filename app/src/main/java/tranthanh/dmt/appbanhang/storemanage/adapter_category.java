package tranthanh.dmt.appbanhang.storemanage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tranthanh.dmt.appbanhang.R;
import tranthanh.dmt.appbanhang.dangnhap.Drinks;

public class adapter_category extends RecyclerView.Adapter<adapter_category.ViewHolder>{
    List<Drinks> listsp;
    Context context;
    ionGetName ion;
    public adapter_category(List<Drinks> listsp, Context context) {
        this.listsp = listsp;
        this.context = context;
    }

    public adapter_category(List<Drinks> listsp, Context context, ionGetName ion) {
        this.listsp = listsp;
        this.context = context;
        this.ion = ion;
    }

    @NonNull
    @Override
    public adapter_category.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_category,parent,false);
        adapter_category.ViewHolder viewHolder=new adapter_category.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drinks cup=listsp.get(position);
        holder.btn_sp.setText(cup.getCategory());
        holder.btn_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ion.getNameCatgory(cup.getCategory());
            }
        });
    }


    @Override
    public int getItemCount() {
        return listsp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_sp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_sp=itemView.findViewById(R.id.btn_category);

        }
    }
}
