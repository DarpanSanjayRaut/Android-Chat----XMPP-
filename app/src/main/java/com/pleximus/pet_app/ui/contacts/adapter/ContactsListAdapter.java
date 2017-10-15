package com.pleximus.pet_app.ui.contacts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.core.model.DBUserContact;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pleximus on 06/05/17.
 */

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder> {

    private Context context;
    private List<DBUserContact> dbUserContactList;
    private ConactsListClickListner conactsListClickListner;
    private final LayoutInflater mInflater;

    public ContactsListAdapter(Context context, ConactsListClickListner conactsListClickListner, List<DBUserContact> dbUserContactList, LayoutInflater mInflater) {
        this.context = context;
        this.conactsListClickListner = conactsListClickListner;
        this.dbUserContactList = dbUserContactList;
        this.mInflater = mInflater;
    }

    @Override
    public ContactsListAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(mInflater.inflate(R.layout.row_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        DBUserContact dbUserContact = dbUserContactList.get(position);
        if (dbUserContact.getContactName() != null){
            holder.lblContactName.setText(dbUserContact.getContactName());
        } else {
            holder.lblContactName.setText(dbUserContact.getContactJID());
        }

        holder.lblPresence.setText(dbUserContact.getConatctPresence() + " ");
    }


    @Override
    public int getItemCount() {
        if (dbUserContactList != null && dbUserContactList.size() > 0) {
            return dbUserContactList.size();
        }
        return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.lbl_contactname)
        TextView lblContactName;
        @BindView(R.id.lbl_presence)
        TextView lblPresence;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            conactsListClickListner.onContactClick(getAdapterPosition(), dbUserContactList.get(getAdapterPosition()));
        }
    }

    public interface ConactsListClickListner {
        void onContactClick(int position, DBUserContact contact);
    }

}
