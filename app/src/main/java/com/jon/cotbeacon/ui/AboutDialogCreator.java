package com.jon.cotbeacon.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jon.cotbeacon.BuildConfig;
import com.jon.cotbeacon.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class AboutDialogCreator {
    private AboutDialogCreator() {
    }

    static void show(Context context) {
        List<String> titles = Arrays.asList("Version", "Build Time", "Github Repository");
        List<String> items = Arrays.asList(
                BuildConfig.VERSION_NAME,
                new SimpleDateFormat("HH:mm:ss dd MMM yyyy z", Locale.ENGLISH).format(BuildConfig.BUILD_TIME),
                "https://github.com/jonapoul/cotbeacon"
        );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_2, android.R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                text1.setText(titles.get(position));
                text2.setText(items.get(position));
                return view;
            }
        };
        ListView listView = (ListView) View.inflate(context, R.layout.about_listview, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 2) { // github repo URL
                /* Open the URL in the browser */
                String url = items.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
        new MaterialAlertDialogBuilder(context)
                .setTitle("About")
                .setView(listView)
                .setPositiveButton(android.R.string.ok, (dialog, buttonId) -> dialog.dismiss())
                .show();
    }
}
