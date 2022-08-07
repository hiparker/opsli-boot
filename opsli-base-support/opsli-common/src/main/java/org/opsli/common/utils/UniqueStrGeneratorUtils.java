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

import cn.hutool.core.util.RandomUtil;

/**
 * 唯一字符串生成器
 * 参考  <a href="https://huzb.me/2018/03/23/%E7%AE%80%E5%8D%95%E7%9A%84%E5%AF%86%E7%A0%81%E5%AD%A6%E7%94%9F%E6%88%90%E5%94%AF%E4%B8%80%E9%82%80%E8%AF%B7%E7%A0%81">...</a>
 *
 * @author Parker
 * @date 2022年07月11日 14:07
 */
public final class UniqueStrGeneratorUtils {

    private static final String BEGIN_SALT = "opsli_2022_begin";
    private static final String END_SALT = "opsli_2022_end";
    private static final String CONTENT_SALT = "opsli_2022";

    private static final HashIdsUtil DISTURB_HASH_ID_BEGIN = new HashIdsUtil(BEGIN_SALT, 2);
    private static final HashIdsUtil DISTURB_HASH_ID_END = new HashIdsUtil(END_SALT, 3);
    private static final HashIdsUtil CONTENT_HASH_ID = new HashIdsUtil(CONTENT_SALT, 7);


    /** 生成 */
    public static String generator(long i) {
        int begin = RandomUtil.randomInt(0, 10);
        int end = RandomUtil.randomInt(10, 100);
        String disturbHashIdBeginStr = DISTURB_HASH_ID_BEGIN.encode(begin);
        String disturbHashIdEndStr = DISTURB_HASH_ID_END.encode(end);
        String contentHashIdStr = CONTENT_HASH_ID.encode(i);
        return disturbHashIdBeginStr + contentHashIdStr + disturbHashIdEndStr;
    }

}
