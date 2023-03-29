package com.samsung.gamingshop.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.models.Account;

public class AccountAdapter extends ArrayAdapter<Account> {

    private final DBShop DBConnector;
    private final MediaPlayer mediaPlayer;

    public AccountAdapter(@NonNull Context context, Account[] arr) {
        super(context, R.layout.account_item, arr);
        DBConnector = new DBShop(context);
        mediaPlayer = MediaPlayer.create(context, R.raw.click);
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {

        final Account acc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item, null);
        }

        ((TextView) convertView.findViewById(R.id.listItem)).setText(acc.getEmail());
        CheckBox isAdmin = convertView.findViewById(R.id.checkBox);

        isAdmin.setChecked(acc.getIsAdmin() == 1);
        isAdmin.setOnClickListener(v -> {
            mediaPlayer.start();
            acc.setIsAdmin(isAdmin.isChecked());
            DBConnector.updateAcc(acc);
        });

        return convertView;
    }

}
