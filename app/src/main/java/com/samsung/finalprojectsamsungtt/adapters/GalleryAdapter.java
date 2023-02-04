package com.samsung.finalprojectsamsungtt.adapters;

import android.annotation.SuppressLint;
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

import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.activities.AddProductActivity;
import com.samsung.finalprojectsamsungtt.activities.SureActivity;
import com.samsung.finalprojectsamsungtt.activities.ProductActivity;
import com.samsung.finalprojectsamsungtt.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GalleryAdapter extends ArrayAdapter<Product> {

    private Bitmap bitmap;
    private URL url;
    private final long id;

    public GalleryAdapter(@NonNull Context context, Product[] arr, long id) {
        super(context, R.layout.gallery_item, arr);
        this.id = id;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        final Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, null);
        }

        ImageView image = convertView.findViewById(R.id.image);
        ((TextView) convertView.findViewById(R.id.name)).setText(product.getName());
        ((TextView) convertView.findViewById(R.id.price)).setText(product.getPrice() + "$");
        Button view = convertView.findViewById(R.id.view);

        if (id == -1) {
            view.setText(getContext().getString(R.string.edit));
        } else if (id == -2) {
            view.setText(getContext().getString(R.string.delete));
        }

        try {
            url = new URL(product.getImage());
            ImageThread thread = new ImageThread();
            thread.start();
            thread.join();
            image.setImageBitmap(bitmap);
        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }

        view.setOnClickListener(v -> {
            Intent intent;
            if (id == -1) {
                intent = new Intent(getContext(), AddProductActivity.class);
                intent.putExtra(getContext().getString(R.string.product), product.getId());
                getContext().startActivity(intent);
            } else if (id == -2) {
                intent = new Intent(getContext(), SureActivity.class);
                intent.putExtra(getContext().getString(R.string.yes), product.getId());
                intent.putExtra(getContext().getString(R.string.no), 1);
                getContext().startActivity(intent);
            } else {
                intent = new Intent(getContext(), ProductActivity.class);
                intent.putExtra(getContext().getString(R.string.account), id);
                intent.putExtra(getContext().getString(R.string.product), product.getId());
                getContext().startActivity(intent);
            }
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
