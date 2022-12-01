package com.samsung.finalprojectsamsungtt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AccountAdapter extends ArrayAdapter<Account> {

    DBTickets DBConnector;

    public AccountAdapter(@NonNull Context context, Account[] arr) {
        super(context, R.layout.account_item, arr);
        DBConnector = new DBTickets(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Account acc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item, null);
        }
        ((TextView) convertView.findViewById(R.id.accountListItem)).setText(acc.getEmail());

        CheckBox isAdmin = convertView.findViewById(R.id.accountCheckBox);
        Button saveButton = convertView.findViewById(R.id.saveAccountStatus);

        if (acc.getIsAdmin() == 1) {
            isAdmin.setChecked(true);
        } else {
            isAdmin.setChecked(false);
        }
        isAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc.setIsAdmin(isAdmin.isChecked());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBConnector.updateAcc(acc);
            }
        });

        return convertView;
    }

}
