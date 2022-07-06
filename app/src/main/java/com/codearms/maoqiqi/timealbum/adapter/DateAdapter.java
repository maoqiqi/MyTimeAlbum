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

package com.codearms.maoqiqi.timealbum.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.codearms.maoqiqi.timealbum.R;
import com.codearms.maoqiqi.timealbum.bean.Album;
import com.codearms.maoqiqi.timealbum.databinding.ItemDateBinding;

import java.util.List;
import java.util.Map;

/**
 * Date adapter
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 16:38
 * version v1.0.0
 */
public class DateAdapter extends BaseQuickAdapter<String, BaseDataBindingHolder<ItemDateBinding>> {

    private Map<String, List<Album>> map;

    public DateAdapter() {
        super(R.layout.item_date);
    }

    public void setMap(Map<String, List<Album>> map) {
        this.map = map;
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemDateBinding> holder, String item) {
        Log.e("info", "item:" + item);
        ItemDateBinding binding = holder.getDataBinding();
        if (binding != null) {
            binding.tvTime.setText(item);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
            binding.recyclerView.setLayoutManager(layoutManager);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setAdapter(new AlbumAdapter(map.get(item)));
            binding.executePendingBindings();
        }
    }
}