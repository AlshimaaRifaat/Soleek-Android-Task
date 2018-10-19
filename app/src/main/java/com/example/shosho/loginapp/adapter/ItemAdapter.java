package com.example.shosho.loginapp.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shosho.loginapp.R;
import com.example.shosho.loginapp.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> items;
    private Context context;
    public ItemAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( context ).inflate( R.layout.row_country, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int i) {
        viewHolder.country_name.setText( items.get( i ).getName() );

        Picasso.with(context)
                .load(items.get(i).getFlagUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.country_image_flag);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView country_name;
        private ImageView country_image_flag;


        public ViewHolder(View view) {
            super(view);
            country_name =  view.findViewById(R.id.row_country_name);
            country_image_flag =  view.findViewById(R.id.row_image_flag);
        }
    }
}


