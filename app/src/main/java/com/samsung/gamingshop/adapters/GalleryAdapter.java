package com.samsung.gamingshop.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.samsung.gamingshop.R;
import com.samsung.gamingshop.activities.AddProductActivity;
import com.samsung.gamingshop.activities.ProductActivity;
import com.samsung.gamingshop.activities.SureActivity;
import com.samsung.gamingshop.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GalleryAdapter extends ArrayAdapter<Product> {

    private Bitmap bitmap;
    private URL url;
    private final long id;
    private final MediaPlayer mediaPlayer;
    private final List<Product> filteredList;
    private final Product[] originalList;

    public GalleryAdapter(@NonNull Context context, Product[] arr, long id) {
        super(context, R.layout.gallery_item, arr);
        mediaPlayer = MediaPlayer.create(context, R.raw.click);
        this.id = id;
        this.originalList = arr;
        this.filteredList = new ArrayList<>(Arrays.asList(arr));
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
            mediaPlayer.start();
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
                ((Activity)getContext()).startActivityForResult(intent, 0);
            }
        });

        return convertView;
    }

    public int getCount() {
        return filteredList.size();
    }

    public Product getItem(int position) {
        return filteredList.get(position);
    }

    public void filter(String searchText) {
        filteredList.clear();
        if (searchText == null || searchText.isEmpty()) {
            filteredList.addAll(Arrays.asList(originalList));
        } else {
            for (Product product : originalList) {
                if (product.getName().toLowerCase().contains(searchText.toLowerCase()) || product.getDescription().toLowerCase().contains(searchText.toLowerCase()) || product.getCategory().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged();
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