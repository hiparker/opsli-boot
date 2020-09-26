package org.opsli.core.security.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * <p></p>
 *
 * @author sunzhiqiang23
 * @date 2020-04-25 17:29
 */
public class MySessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        System.out.println("会话创建：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        System.out.println("会话退出：" + session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("会话过期：" + session.getId());

    }
}
