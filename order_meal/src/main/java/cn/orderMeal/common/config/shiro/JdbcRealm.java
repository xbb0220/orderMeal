package cn.orderMeal.common.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import cn.orderMeal.common.service.UserService;
import cn.orderMeal.common.service.impl.UserServiceImpl;

public class JdbcRealm extends AuthorizingRealm {
	UserService userService = UserServiceImpl.service;
	
	/**
	 * 验证用户登录信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		//User user = userService.login(name);
		return new SimpleAuthenticationInfo(null, null, getName());
	}

	/**
	 * 验证用户权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		//User user = (User) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermission("");
		return info;
	}

}
