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

package com.codearms.maoqiqi.timealbum.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * ExifInterface utils
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 18:18
 * version v1.0.0
 */
public class ExifInterfaceUtils {

    public static String getLatLong(Context context, String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // String time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            String latValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                float lat = convertRationalLatLonToFloat(latValue, latRef);
                float lng = convertRationalLatLonToFloat(lngValue, lngRef);
                return getAddress(context, (double) lat, (double) lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static float convertRationalLatLonToFloat(String rationalString, String ref) {
        String[] parts = rationalString.split(",");
        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return (float) -result;
        }
        return (float) result;
    }

    private static String getAddress(Context context, double latitude, double longitude) {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> locationList = gc.getFromLocation(latitude, longitude, 1);
            if (locationList != null && locationList.size() > 0) {
                Address address = locationList.get(0);
                String countryName = address.getCountryName();
                String countryCode = address.getCountryCode();
                String adminArea = address.getAdminArea();
                String locality = address.getLocality();
                String subAdminArea = address.getSubLocality();
                String featureName = address.getFeatureName();

                return "countryName == " + countryName
                        + "\n" + "countryCode == " + countryCode
                        + "\n" + "adminArea == " + adminArea
                        + "\n" + "locality ==" + locality
                        + "\n" + "subAdminArea == " + subAdminArea
                        + "\n" + "featureName == " + featureName;
            } else {
                Log.e("info", "无法获取地址");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
