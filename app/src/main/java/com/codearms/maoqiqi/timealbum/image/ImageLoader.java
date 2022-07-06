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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.codearms.maoqiqi.timealbum.R;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Image loader
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 16:58
 * version v1.0.0
 */
public class ImageLoader {

    private final int TAG_KEY_PATH = R.id.image_path;
    private final int MESSAGE_POST_RESULT = 1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;// 核心线程数量
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1; // 最大线程数量
    private static final int KEEP_ALIVE = 10; // 存活时间

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(); // 等待队列

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    // 更新 ImageView
    private final Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_PATH);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.w("info", "set image bitmap,but url has changed, ignored！");
            }
        }
    };

    private ImageLoader() {
    }

    private static final class ImageLoaderHolder {
        static final ImageLoader instance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.instance;
    }

    /**
     * 异步加载
     */
    public void bindBitmap(String path, int reqWidth, int reqHeight, final ImageView imageView) {
        imageView.setTag(TAG_KEY_PATH, path);
        // 从内存缓存中获取bitmap
        Bitmap bitmap = MemoryCache.getInstance().getBitmapFromMemoryCache(path);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = () -> {
            Bitmap b = loadBitmap(path, reqWidth, reqHeight);
            if (b != null) {
                LoaderResult loaderResult = new LoaderResult(imageView, path, b);
                mMainHandler.obtainMessage(MESSAGE_POST_RESULT, loaderResult).sendToTarget();
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 同步加载
     */
    public Bitmap loadBitmap(String path, int reqWidth, int reqHeight) {
        // 从内存缓存中获取bitmap
        Bitmap bitmap = MemoryCache.getInstance().getBitmapFromMemoryCache(path);
        if (bitmap != null) return bitmap;

        // 根据指定的资源文件路径以及指定的宽/高进行等比例缩放
        bitmap = ImageResizer.decodeSampledBitmapFromPathName(path, reqWidth, reqHeight);
        if (bitmap != null) MemoryCache.getInstance().addBitmapToMemoryCache(path, bitmap);
        return bitmap;
    }

    /**
     * 返回结果的封装
     */
    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
