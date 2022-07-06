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

package com.codearms.maoqiqi.timealbum.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.codearms.maoqiqi.timealbum.bean.Album;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Album loader
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 16:06
 * version v1.0.0
 */
public class AlbumLoader {

    public static List<Album> getList(Context context) {
        return getListForCursor(getCursor(context));
    }

    private static Cursor getCursor(Context context) {
        Uri uri = MediaStore.Files.getContentUri("external");
        // 查询字段
        final String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.WIDTH,
                MediaStore.Files.FileColumns.HEIGHT,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATE_MODIFIED
        };
        // 帅选条件
        String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + " =? " +
                " AND " + MediaStore.MediaColumns.MIME_TYPE + " != 'image/gif'" +
                " AND " + MediaStore.MediaColumns.MIME_TYPE + "!='image/*'" +
                " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + " =?) " +
                " AND " + MediaStore.MediaColumns.SIZE + " >0 ";
        // 帅选条件参数
        String[] selectionArgs = {
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
        };
        // 排序
        String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private static List<Album> getListForCursor(@NotNull Cursor cursor) {
        ArrayList<Album> list = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                int title = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE);
                int displayName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                int data = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                int size = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                int width = cursor.getColumnIndex(MediaStore.Files.FileColumns.WIDTH);
                int height = cursor.getColumnIndex(MediaStore.Files.FileColumns.HEIGHT);
                int mimeType = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE);
                int dateAdded = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED);
                int dateModified = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                do {
                    String path = cursor.getString(data);
                    if (path.isEmpty()) continue;
                    if (!new File(path).exists()) continue;
                    Album file = new Album();
                    file.setId(cursor.getLong(id));
                    file.setTitle(cursor.getString(title));
                    file.setDisplayName(cursor.getString(displayName));
                    file.setData(path);
                    file.setSize(cursor.getLong(size));
                    file.setWidth(cursor.getInt(width));
                    file.setHeight(cursor.getInt(height));
                    file.setMimeType(cursor.getString(mimeType));
                    file.setDateAdded(cursor.getLong(dateAdded));
                    file.setDateModified(cursor.getLong(dateModified));
                    list.add(file);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
}
