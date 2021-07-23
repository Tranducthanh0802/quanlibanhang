package tranthanh.dmt.appbanhang.storemanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tranthanh.dmt.appbanhang.R;
import tranthanh.dmt.appbanhang.dangnhap.Drinks;

public class adapter_listname extends RecyclerView.Adapter<adapter_listname.ViewHolder> implements Filterable {
    List<Drinks> listsp;
    Context context;
    ionGetName ion;
    List<Drinks> listAllsp;
    Customfilter filter;
    List<Drinks> filterArrayList;

    public adapter_listname(List<Drinks> listsp, Context context) {
        this.listsp = listsp;
        this.context = context;

    }

    public adapter_listname(List<Drinks> listsp, Context context, ionGetName ion,List<Drinks> listAllsp) {
        this.listsp = listsp;
        this.context = context;
        this.ion = ion;
        filterArrayList = listAllsp;
    }

    @NonNull
    @Override
    public adapter_listname.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_name,parent,false);
        adapter_listname.ViewHolder viewHolder=new adapter_listname.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_listname.ViewHolder holder, int position) {
            Drinks cup = listsp.get(position);
            holder.btn_sp.setText(cup.getName());
            holder.btn_sp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ion.getname(cup.getName().toString()+"");

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
            btn_sp=itemView.findViewById(R.id.btn_name);


        }
    }
    @Override
    public Filter getFilter() {
        if (filter ==null){
            filter = new Customfilter();
        }
        return filter;
    }
    private class Customfilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if(charSequence !=null && charSequence.length()>0){
                String chuoi=charSequence.toString().toLowerCase().trim();
                ArrayList<Drinks> filters=new ArrayList<>();
                for(int i=0;i<filterArrayList.size();i++){
                    if(filterArrayList.get(i).getName().toLowerCase().trim().contains(chuoi)){
                        Drinks p = filterArrayList.get(i);
                        filters.add(p);
                    }
                }
                results.count=filters.size();
                results.values=filters;

            }
            else{
                results.count=listsp.size();
                results.values=listsp;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listsp= (ArrayList<Drinks>) filterResults.values;
            notifyDataSetChanged();
        }

    }
}
