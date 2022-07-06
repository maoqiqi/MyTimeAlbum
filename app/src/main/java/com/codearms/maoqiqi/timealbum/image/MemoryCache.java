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

package com.codearms.maoqiqi.timealbum.image;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Memory cache
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 16:58
 * version v1.0.0
 */
public class MemoryCache {

    private final LruCache<String, Bitmap> mMemoryCache;

    private MemoryCache() {
        // 获取当前进程的可用内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 完成bitmap对象大小的计算
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    private static final class MemoryCacheHolder {
        static final MemoryCache instance = new MemoryCache();
    }

    public static MemoryCache getInstance() {
        return MemoryCacheHolder.instance;
    }

    public void addBitmapToMemoryCache(String path, Bitmap bitmap) {
        mMemoryCache.put(path, bitmap);
    }

    public Bitmap getBitmapFromMemoryCache(String path) {
        return mMemoryCache.get(path);
    }
}
