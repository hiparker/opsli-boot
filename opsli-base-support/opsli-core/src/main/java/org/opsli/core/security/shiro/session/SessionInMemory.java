package org.opsli.core.security.shiro.session;

import org.apache.shiro.session.Session;

import java.util.Date;

/**
 * <p></p>
 *
 * @author sunzhiqiang23
 * @date 2020-04-27 20:52
 */
public class SessionInMemory {
    private Session session;
    private Date createTime;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
