package com.samsung.gamingshop.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.activities.SureActivity;
import com.samsung.gamingshop.models.Order;
import com.samsung.gamingshop.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class CartAdapter extends ArrayAdapter<Order> {
    private final DBShop DBConnector;
    private Bitmap bitmap;
    private URL url;
    private final MediaPlayer mediaPlayer;

    public CartAdapter(@NonNull Context context, Order[] arr) {
        super(context, R.layout.cart_item, arr);
        DBConnector = new DBShop(context);
        mediaPlayer = MediaPlayer.create(context, R.raw.click);
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        final Order order = getItem(position);
        final Product product = DBConnector.selectProduct(order.getProduct());

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, null);
        }

        ImageView image = convertView.findViewById(R.id.image);
        ((TextView) convertView.findViewById(R.id.name)).setText(product.getName());
        ((TextView) convertView.findViewById(R.id.price)).setText(product.getPrice() + "$");
        EditText quantity = convertView.findViewById(R.id.quantity);
        Button removeFromCart = convertView.findViewById(R.id.removeCartItem);
        Button save = convertView.findViewById(R.id.save);
        quantity.setText(order.getQuantity() + "");

        try {
            url = new URL(product.getImage());
            CartAdapter.ImageThread thread = new CartAdapter.ImageThread();
            thread.start();
            thread.join();
            image.setImageBitmap(bitmap);
        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(getContext(), SureActivity.class);
            intent.putExtra(getContext().getString(R.string.yes), order.getId());
            intent.putExtra(getContext().getString(R.string.no), 3);
            intent.putExtra(getContext().getString(R.string.account), order.getOwner());
            intent.putExtra(getContext().getString(R.string.product), product.getId());
            if (quantity.getText().toString().equals("")) {
                order.setQuantity(1);
            } else {
                order.setQuantity(Integer.parseInt(quantity.getText().toString()));
            }
            intent.putExtra(getContext().getString(R.string.quantity), order.getQuantity());
            getContext().startActivity(intent);
        });

        removeFromCart.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(getContext(), SureActivity.class);
            intent.putExtra(getContext().getString(R.string.yes), order.getId());
            intent.putExtra(getContext().getString(R.string.no), 2);
            getContext().startActivity(intent);
        });

        return convertView;
    }

    class ImageThread extends Thread{
        @Override
        public void run(){
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}