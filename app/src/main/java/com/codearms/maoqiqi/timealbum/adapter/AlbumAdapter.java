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

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.codearms.maoqiqi.timealbum.R;
import com.codearms.maoqiqi.timealbum.bean.Album;
import com.codearms.maoqiqi.timealbum.databinding.ItemAlbumBinding;
import com.codearms.maoqiqi.timealbum.image.ImageLoader;
import com.codearms.maoqiqi.timealbum.utils.TimeUtils;

import java.util.List;

/**
 * Album adapter
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 16:03
 * version v1.0.0
 */
public class AlbumAdapter extends BaseQuickAdapter<Album, BaseDataBindingHolder<ItemAlbumBinding>> {

    private int width;

    public AlbumAdapter(@Nullable List<Album> data) {
        super(R.layout.item_album, data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemAlbumBinding> holder, Album album) {
        Log.e("info", "item:" + album.toString());
        if (album.getCurrentPosition() != null) Log.e("info", album.getCurrentPosition());
        ItemAlbumBinding binding = holder.getDataBinding();
        if (binding != null) {
            // Glide.with(getContext()).load(album.getData()).placeholder(R.drawable.ic_default).into(binding.ivIcon);
            ImageLoader.getInstance().bindBitmap(album.getData(), getWidth(), getWidth(), binding.ivIcon);
            binding.tvTime.setText(TimeUtils.getTimeFormat(album.getDateAdded() * 1000));
            binding.ivPlay.setVisibility(album.getMimeType().startsWith("video") ? View.VISIBLE : View.GONE);
            binding.ivAddress.setVisibility(album.getCurrentPosition() == null ? View.GONE : View.VISIBLE);
            binding.executePendingBindings();
        }
    }

    private int getWidth() {
        if (width == 0) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            width = (int) ((displayMetrics.widthPixels - displayMetrics.density * 8 * 4) / 3 * 0.85f);
        }
        return width;
    }
}