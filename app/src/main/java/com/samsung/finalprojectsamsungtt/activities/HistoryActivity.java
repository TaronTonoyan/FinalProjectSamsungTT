package com.samsung.finalprojectsamsungtt.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;
import com.samsung.finalprojectsamsungtt.models.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private TextView text;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView text = findViewById(R.id.orders);
        id = getIntent().getLongExtra(getString(R.string.account), -1);
        DBConnector = new DBShop(this);
        actionBar.setTitle("History");
        text.setText(getHistory());
    }

    private String getHistory() {
        ArrayList<History> arr = DBConnector.selectAllHistory();
        String ans = "";
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == id) {
                ans += arr.get(i).getAddress() + ": " + arr.get(i).getPrice() + "$\n" + arr.get(i).getOrders();
            }
        }
        return ans;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
