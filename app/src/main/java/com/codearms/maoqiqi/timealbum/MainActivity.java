/*
 * Copyright [2022] [March]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codearms.maoqiqi.timealbum;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codearms.maoqiqi.timealbum.adapter.DateAdapter;
import com.codearms.maoqiqi.timealbum.databinding.ActivityMainBinding;
import com.codearms.maoqiqi.timealbum.vm.MainViewModel;
import com.dylanc.activityresult.launcher.RequestPermissionLauncher;

/**
 * Main activity
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 15:26
 * version v1.0.0
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;
    private final DateAdapter mAdapter = new DateAdapter();
    private final RequestPermissionLauncher mLauncher = new RequestPermissionLauncher(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.albums.observe(this, map -> {
            mAdapter.setMap(map);
            mAdapter.addData(mViewModel.dates);
        });
        binding.recyclerView.setAdapter(mAdapter);
        requestPermission();
    }

    private void requestPermission() {
        mLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE,
                () -> {
                    showToast("Read permission are granted");
                    mViewModel.getList(this.getApplicationContext());
                },
                (settingsLauncher) -> showToast("Need permission"),
                () -> showToast("Need permission")
        );
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}