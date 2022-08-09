package org.opsli.modulars.system.logs.factory;

import cn.hutool.core.util.StrUtil;
import org.opsli.api.wrapper.system.logs.LoginLogsModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.IPUtil;
import org.opsli.core.utils.UserUtil;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Parker
 * @date 2022/3/18 14:34
 */
public final class UserLoginLogFactory {

	/**
	 * 获取日志记录实体对象
	 * @param user 用户信息
	 * @return 日志信息
	 */
	public static LoginLogsModel getUserLoginModel(HttpServletRequest request, UserModel user, boolean isLogin){
		//记录登入登出日志
		LoginLogsModel loginInfo = new LoginLogsModel();
		loginInfo.setUsername(user.getUsername());
		loginInfo.setRealName(user.getRealName());
		// *** 需要确保 user对象的ip信息是有值的
		loginInfo.setRemoteAddr(IPUtil.getClientAddressBySingle(request));
		String header = request.getHeader("User-Agent");
		loginInfo.setUserAgent(header);

		loginInfo.setIzManual(true);
		loginInfo.setCreateBy(user.getId());
		loginInfo.setUpdateBy(user.getId());
		UserOrgRefModel orgByUserId = UserUtil.getUserDefOrgByUserId(user.getId());
		loginInfo.setOrgIds(ObjectUtils.isEmpty(orgByUserId)? "0" : orgByUserId.getOrgIds() );
		loginInfo.setTenantId(StrUtil.blankToDefault(user.getTenantId(),null));

		if(isLogin){
			loginInfo.setType(DictType.LOGIN_LOG_TYPE_LOGIN.getValue());
		}else {
			loginInfo.setType(DictType.LOGIN_LOG_TYPE_LOGOUT.getValue());
		}
		return loginInfo;
	}

}
