package com.skripsi.ridho.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.skripsi.ridho.Hotel;
import com.skripsi.ridho.Password;
import com.skripsi.ridho.Posisi;
import com.skripsi.ridho.R;
import com.skripsi.ridho.SessionManager;
import com.skripsi.ridho.User;
import com.skripsi.ridho.app.AppController;
import com.skripsi.ridho.model.ModelMenu;

import java.util.List;

/**
 * Created by gandhi on 7/26/17.
 */

public class ListMenu extends RecyclerView.Adapter<ListMenu.ViewHolder> {
    private List<ModelMenu> modelMenus;
    Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SessionManager sessionManager;
    public ListMenu(Context context, List<ModelMenu> modelMenus){
        this.context = context;
        this.modelMenus = modelMenus;
    }
    @Override
    public ListMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;    }

    @Override
    public void onBindViewHolder(ListMenu.ViewHolder holder, int position) {
        ModelMenu modelMenu = modelMenus.get(position);
        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();
        holder.icon.setImageUrl(modelMenu.getIcon(),imageLoader);
        holder.namaMenu.setText(modelMenu.getNamaMenu());
    }
    @Override
    public int getItemCount() {
        return  this.modelMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView icon;
        public TextView namaMenu;
        public ViewHolder(final View itemView) {
            super(itemView);
            icon = (NetworkImageView) itemView.findViewById(R.id.icon);
            namaMenu = (TextView) itemView.findViewById(R.id.namaMenu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    switch (position){
                        case 0:
                            Intent hotel = new Intent(v.getContext(), Hotel.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(hotel);
                            break;
                        case 1:
                            Intent user = new Intent(v.getContext(), User.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(user);
                            break;
                        case 2:
                            Intent password = new Intent(v.getContext(), Password.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(password);
                            break;
                        case 3:
                            Intent posisi = new Intent(v.getContext(), Posisi.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(posisi);
                            break;
                        case 4:
                            sessionManager = new SessionManager(itemView.getContext());
                            sessionManager.logoutUser();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

}
