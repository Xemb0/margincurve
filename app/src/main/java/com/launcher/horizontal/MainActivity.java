package com.launcher.horizontal;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycalview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager mGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL); // 2 is number of items per row
        CustomItemDecoration itemDecoration = new CustomItemDecoration(200, 4); // 20sp margin for the first item in each column, 4 columns
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.setLayoutManager(mGridLayoutManager); // deafult
        PackageManager pm = this.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);
        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(pm));
        Adapter adapter = new Adapter(this, launchables, pm);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mGridLayoutManager);
    }
}