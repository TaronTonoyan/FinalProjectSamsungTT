package com.samsung.finalprojectsamsungtt.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.activities.SureActivity;
import com.samsung.finalprojectsamsungtt.models.Order;
import com.samsung.finalprojectsamsungtt.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WishlistAdapter extends ArrayAdapter<Order> {

    private final DBShop DBConnector;
    private Bitmap bitmap;
    private URL url;

    public WishlistAdapter(@NonNull Context context, Order[] arr) {
        super(context, R.layout.wishlist_item, arr);
        DBConnector = new DBShop(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Order order = getItem(position);
        final Product product = DBConnector.selectProduct(order.getProduct());

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wishlist_item, null);
        }

        ImageView image = convertView.findViewById(R.id.image);
        ((TextView) convertView.findViewById(R.id.name)).setText(product.getName());
        ((TextView) convertView.findViewById(R.id.price)).setText(product.getPrice() + "$");
        Button removeFromWishlist = convertView.findViewById(R.id.removeWishlistItem);
        Button addToCart = convertView.findViewById(R.id.addToCart);

        try {
            url = new URL(product.getImage());
            WishlistAdapter.ImageThread thread = new WishlistAdapter.ImageThread();
            thread.start();
            thread.join();
            image.setImageBitmap(bitmap);
        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }

        removeFromWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SureActivity.class);
            intent.putExtra(getContext().getString(R.string.yes), order.getId());
            intent.putExtra(getContext().getString(R.string.no), 2);
            getContext().startActivity(intent);
        });
        if (existsInCart(order.getOwner(), order.getProduct())) {
            addToCart.setText(getContext().getString(R.string.added_to_cart));
        } else {
            addToCart.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SureActivity.class);
                intent.putExtra(getContext().getString(R.string.account), order.getOwner());
                intent.putExtra(getContext().getString(R.string.product), order.getProduct());
                intent.putExtra(getContext().getString(R.string.no), 4);
                getContext().startActivity(intent);
            });
        }

        return convertView;
    }

    private boolean existsInCart(long accId, long productId) {
        ArrayList<Order> arr = DBConnector.selectAllOrders();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == accId && arr.get(i).getProduct() == productId && arr.get(i).getIsWishlist() == 0) {
                return true;
            }
        }
        return false;
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
