package com.samsung.finalprojectsamsungtt.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Order;

public class SureActivity extends AppCompatActivity {

    DBShop DBConnector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure);
        initViews();
    }

    private void initViews() {
        TextView text = findViewById(R.id.sureText);
        Button yes = findViewById(R.id.buttonYes);
        Button no = findViewById(R.id.buttonNo);
        DBConnector = new DBShop(this);
        long id = getIntent().getLongExtra(getString(R.string.yes), -1);
        int code = getIntent().getIntExtra(getString(R.string.no), -1);
        long owner = getIntent().getLongExtra(getString(R.string.account), -1);
        long product = getIntent().getLongExtra(getString(R.string.product), -1);
        int quantity = getIntent().getIntExtra(getString(R.string.quantity), 0);

        if (id == -1) {
            text.setText(getString(R.string.sure_add_to_cart));
        }

        yes.setOnClickListener(v -> {
            switch (code) {
                case 0:
                    DBConnector.deleteAcc(id);
                    break;
                case 1:
                    DBConnector.deleteProduct(id);
                    break;
                case 2:
                    DBConnector.deleteOrder(id);
                    break;
                case 3:
                    Order order = new Order(id, owner, product, 0, quantity);
                    DBConnector.updateOrder(order);
                    break;
                default:
                    DBConnector.insertOrder(owner, product, 0, 1);
            }
            setResult(RESULT_OK);
            finish();
        });
        no.setOnClickListener(v -> finish());

    }

}
