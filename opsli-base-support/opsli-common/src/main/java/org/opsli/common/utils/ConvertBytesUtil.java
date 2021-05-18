/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.common.utils;

/**
 * 字节转换
 *
 * @author Parker
 * @date 2020-09-19 23:21
 */
public final class ConvertBytesUtil {

    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSizeToString(long size) {
        float fileSize = convertFileSize(size);
        if (size >= GB) {
            return String.format("%.1f GB" , fileSize);
        } else if (size >= MB) {
            return String.format(fileSize > 100 ? "%.0f MB" : "%.1f MB" , fileSize);
        } else if (size >= KB) {
            float f = (float) size / KB;
            return String.format(fileSize > 100 ? "%.0f KB" : "%.1f KB" , fileSize);
        } else {
            return String.format("%d B" , size);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static float convertFileSize(long size) {
        if (size >= GB) {
            return (float) size / GB;
        } else if (size >= MB) {
            return (float) size / MB;
        } else if (size >= KB) {
            return (float) size / KB;
        } else {
            return size;
        }
    }


    private ConvertBytesUtil(){}

}
