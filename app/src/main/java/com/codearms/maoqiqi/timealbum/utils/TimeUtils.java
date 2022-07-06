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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Time utils
 * link: https://github.com/maoqiqi/MyTimeAlbum
 * e-mail: fengqi.mao.march@gmail.com
 * author: March
 * date: 2022/7/5 16:03
 * version v1.0.0
 */
public class TimeUtils {

    public static String getDateFormat(long time) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(time));
    }

    public static String getTimeFormat(long time) {
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(time));
    }
}