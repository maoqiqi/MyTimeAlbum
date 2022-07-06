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

package com.codearms.maoqiqi.timealbum.vm;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codearms.maoqiqi.timealbum.bean.Album;
import com.codearms.maoqiqi.timealbum.loader.AlbumLoader;
import com.codearms.maoqiqi.timealbum.utils.ExifInterfaceUtils;
import com.codearms.maoqiqi.timealbum.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main View model
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 15:26
 * version v1.0.0
 */
public class MainViewModel extends ViewModel {

    public final List<String> dates = new ArrayList<>();
    public final MutableLiveData<Map<String, List<Album>>> albums = new MutableLiveData<>();

    public void getList(Context context) {
        new Thread(() -> {
            List<Album> list = AlbumLoader.getList(context);
            Map<String, List<Album>> map = new HashMap<>();
            for (Album album : list) {
                String key = TimeUtils.getDateFormat(album.getDateAdded() * 1000);
                List<Album> value = map.get(key);
                if (value == null) value = new ArrayList<>();
                value.add(album);
                if (!dates.contains(key)) dates.add(key);
                map.put(key, value);
                album.setCurrentPosition(ExifInterfaceUtils.getLatLong(context, album.getData()));
            }
            albums.postValue(map);
        }).start();
    }
}