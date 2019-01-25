package com.franklincbc.motoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.franklincbc.motoon.R;
import com.franklincbc.motoon.model.Chat;

import java.util.List;

/**
 * Created by Priscila on 19/01/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH>{

    private List<Chat> mChatList;
    private Context mContext;

    public ChatAdapter(Context ctx, List<Chat> chatList){
        this.mContext = ctx;
        this.mChatList = chatList;
    }

    @Override
    public ChatAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lst_chat, parent, false);
        final ChatAdapter.VH viewHolder = new ChatAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.VH holder, final int position) {
        final Chat chat = mChatList.get(position);
        holder.txtNome.setText(chat.getNome());
        holder.txtTexto.setText(chat.getTexto());
        //holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.em_Atendimento));
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }



    class VH extends RecyclerView.ViewHolder {
        TextView txtNome;
        TextView txtTexto;
        View view;

        public VH(View itemView) {
            super(itemView);
            txtNome = (TextView)itemView.findViewById(R.id.item_lst_chat_nome);
            txtTexto = (TextView)itemView.findViewById(R.id.item_lst_chat_texto);
            view = itemView;
        }
    }

}
