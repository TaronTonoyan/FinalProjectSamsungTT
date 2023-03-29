package com.samsung.gamingshop.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.models.Order;

import java.util.ArrayList;

public class SureActivity extends AppCompatActivity {

    private DBShop DBConnector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Confirmation");
        TextView text = findViewById(R.id.sureText);
        Button yes = findViewById(R.id.buttonYes);
        Button no = findViewById(R.id.buttonNo);
        DBConnector = new DBShop(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        long id = getIntent().getLongExtra(getString(R.string.yes), -1);
        int code = getIntent().getIntExtra(getString(R.string.no), -1);
        long owner = getIntent().getLongExtra(getString(R.string.account), -1);
        long product = getIntent().getLongExtra(getString(R.string.product), -1);
        int quantity = getIntent().getIntExtra(getString(R.string.quantity), 0);

        if (id == -1) {
            text.setText(getString(R.string.sure_add_to_cart));
        }

        yes.setOnClickListener(v -> {
            mediaPlayer.start();
            switch (code) {
                case 0:
                    DBConnector.deleteAcc(id);
                    break;
                case 1:
                    ArrayList<Order> arr = DBConnector.selectAllOrders();
                    for (int i = 0; i < arr.size(); i++) {
                        if (arr.get(i).getProduct() == id) {
                            DBConnector.deleteOrder(arr.get(i).getId());
                        }
                    }
                    DBConnector.deleteProduct(id);
                    break;
                case 2:
                    DBConnector.deleteOrder(id);
                    break;
                case 3:
                    Order order = new Order(id, owner, product, 0, quantity);
                    DBConnector.updateOrder(order);
                    break;
                case 4:
                    DBConnector.insertOrder(owner, product, 0, quantity);
            }
            setResult(RESULT_OK);
            finish();
        });
        no.setOnClickListener(v -> {
            mediaPlayer.start();
            finish();
        });

    }

}
