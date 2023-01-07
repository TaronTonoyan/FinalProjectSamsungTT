package com.samsung.finalprojectsamsungtt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;

public class AccountAdapter extends ArrayAdapter<Account> {

    private DBShop DBConnector;

    public AccountAdapter(@NonNull Context context, Account[] arr) {
        super(context, R.layout.account_item, arr);
        DBConnector = new DBShop(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Account acc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item, null);
        }

        ((TextView) convertView.findViewById(R.id.accountListItem)).setText(acc.getEmail());
        CheckBox isAdmin = convertView.findViewById(R.id.accountCheckBox);

        isAdmin.setChecked(acc.getIsAdmin() == 1);
        isAdmin.setOnClickListener(v -> {
            acc.setIsAdmin(isAdmin.isChecked());
            DBConnector.updateAcc(acc);
        });

        return convertView;
    }

}
