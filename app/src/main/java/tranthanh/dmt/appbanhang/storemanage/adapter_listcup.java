package tranthanh.dmt.appbanhang.storemanage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tranthanh.dmt.appbanhang.R;
import tranthanh.dmt.appbanhang.dangnhap.Drinks;

public class adapter_listcup extends RecyclerView.Adapter<adapter_listcup.ViewHolder>  {
    List<Drinks> listsp;
    Context context;
    ionAddMinus ion;
    int number;
    public adapter_listcup(List<Drinks> listsp, Context context) {
        this.listsp = listsp;
        this.context = context;
    }

    public adapter_listcup(List<Drinks> listsp, Context context, ionAddMinus ion) {
        this.listsp = listsp;
        this.context = context;
        this.ion = ion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_cup,parent,false);
        adapter_listcup.ViewHolder viewHolder=new adapter_listcup.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drinks cup=listsp.get(position);
        holder.txt_money.setText(StoreManage.moneyFormat(cup.getCost())+"");
        holder.txt_name.setText(cup.getName());
        number=1;
        if(StoreManage.numbersl==true) {
            holder.txt_sl.setText("1");
            holder.txt_sl2.setText(holder.txt_sl.getText().toString());
        }
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number =Integer.valueOf(holder.txt_sl.getText().toString())+1;
                holder.txt_sl.setText(""+number);
                holder.txt_sl2.setText(""+number);
                ion.add(cup.getName(),number);

            }
        });
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( Integer.valueOf(holder.txt_sl.getText().toString())>1) {
                    number =Integer.valueOf(holder.txt_sl.getText().toString())-1;
                    holder.txt_sl.setText(""+number);
                    holder.txt_sl2.setText(""+number);
                    ion.minus(cup.getName(),number);
                }


            }
        });
        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl=Integer.valueOf(holder.txt_sl.getText().toString());
                ion.delete(cup.getName(),sl,position);
                holder.txt_sl.setText("1");
                holder.txt_sl2.setText(holder.txt_sl.getText().toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return listsp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btn_add,btn_minus;
        TextView txt_sl,txt_name,txt_money,txt_delete,txt_sl2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_delete = itemView.findViewById(R.id.txt_delete);
            btn_add = itemView.findViewById(R.id.btn_add);
            btn_minus= itemView.findViewById(R.id.btn_minus);
            txt_sl = itemView.findViewById(R.id.txt_sl);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_money = itemView.findViewById(R.id.txt_money);
            txt_sl2=itemView.findViewById(R.id.txt_numbercup);
        }
    }
}
