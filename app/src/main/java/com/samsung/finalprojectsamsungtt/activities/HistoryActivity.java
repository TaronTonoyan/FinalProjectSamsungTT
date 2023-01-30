package com.samsung.finalprojectsamsungtt.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView text = findViewById(R.id.orders);
        id = getIntent().getLongExtra(getString(R.string.account), -1);
        DBConnector = new DBShop(this);
        actionBar.setTitle("History");
        text.setText(getHistory());
    }

    private String getHistory() {
        ArrayList<History> arr = DBConnector.selectAllHistory();
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == id) {
                ans.append(arr.get(i).getAddress()).append(": ").append(arr.get(i).getPrice()).append("$\n").append(arr.get(i).getOrders());
            }
        }
        return ans.toString();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
