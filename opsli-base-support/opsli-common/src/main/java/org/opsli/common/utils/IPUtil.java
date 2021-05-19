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

import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 *
 * @author Parker
 * @date 2020-09-19 23:21
 */
public final class IPUtil {

    /** 排除结果 */
    private static final String UNKNOWN = "unknown";
    /** 尝试字段 */
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };


    /***
     * 获取客户端地址
     *
     * @param request request
     * @return String
     */
    public static String getClientAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String address = request.getHeader(header);
            if (StrUtil.isNotBlank(address)
                    && !UNKNOWN.equalsIgnoreCase(address)) {
                return address;
            }
        }
        return request.getRemoteAddr();
    }

    /***
     * 获取客户端地址 (多重代理只获得第一个)
     *
     * @param request request
     * @return String
     */
    public static String getClientAddressBySingle(HttpServletRequest request) {
        String clientAddress = getClientAddress(request);
        return NetUtil.getMultistageReverseProxyIp(clientAddress);
    }

    /***
     * 获取客户端IP
     *
     * @param request request
     * @return String
     */
    public static String getClientId(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (StrUtil.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                String reverseProxyIp = NetUtil.getMultistageReverseProxyIp(ip);
                if(Validator.isIpv4(reverseProxyIp) || Validator.isIpv6(reverseProxyIp)){
                    // 判断是否为IP 返回原始IP
                    return ip;
                }
            }
        }
        // 否则返回 空地址
        return "";
    }

    /***
     * 获取客户端IP (多重代理只获得第一个)
     *
     * @param request request
     * @return String
     */
    public static String getClientIdBySingle(HttpServletRequest request) {
        String clientIp = getClientId(request);
        return NetUtil.getMultistageReverseProxyIp(clientIp);
    }

    // ===============

    private IPUtil(){}
}
