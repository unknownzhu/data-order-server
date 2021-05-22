/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.web.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.datagear.management.domain.Role;
import org.datagear.management.service.CreateUserEntityService;
import org.datagear.util.StringUtil;
import org.datagear.web.security.AnonymousAuthenticationFilterExt;
import org.datagear.web.security.AuthUser;
import org.datagear.web.security.AuthenticationSuccessHandlerImpl;
import org.datagear.web.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * 安全配置。
 * 
 * @author datagear@163.com
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	/**
	 * 授权角色：(登录用户 或 系统管理员) 且 数据管理员
	 */
	protected static final String AUTH_USER_ADMIN_AND_DATA_ADMIN = "hasAnyAuthority('" + AuthUser.ROLE_USER + "','"
			+ AuthUser.ROLE_ADMIN + "') and hasAuthority('" + Role.ROLE_DATA_ADMIN + "')";

	/**
	 * 授权角色：(登录用户 或 系统管理员) 且 (数据管理员 或 数据分析员)
	 */
	protected static final String AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST = "hasAnyAuthority('" + AuthUser.ROLE_USER
			+ "','" + AuthUser.ROLE_ADMIN + "') and hasAnyAuthority('" + Role.ROLE_DATA_ADMIN + "','"
			+ Role.ROLE_DATA_ANALYST + "')";

	/**
	 * 授权角色：(匿名用户 或 登录用户 或 系统管理员) 且 数据管理员
	 */
	protected static final String AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN = "hasAnyAuthority('"
			+ AuthUser.ROLE_ANONYMOUS + "','" + AuthUser.ROLE_USER + "','" + AuthUser.ROLE_ADMIN
			+ "') and hasAuthority('" + Role.ROLE_DATA_ADMIN + "')";

	/**
	 * 授权角色：(匿名用户 或 登录用户 或 系统管理员) 且 (数据管理员 或 数据分析员)
	 */
	protected static final String AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST = "hasAnyAuthority('"
			+ AuthUser.ROLE_ANONYMOUS + "','" + AuthUser.ROLE_USER + "','" + AuthUser.ROLE_ADMIN
			+ "') and hasAnyAuthority('" + Role.ROLE_DATA_ADMIN + "','" + Role.ROLE_DATA_ANALYST + "')";

	/**
	 * 授权角色：系统管理员
	 */
	protected static final String AUTH_ADMIN = "hasAuthority('" + AuthUser.ROLE_ADMIN + "')";

	/**
	 * 授权角色：登录用户 或 系统管理员
	 */
	protected static final String AUTH_USER_ADMIN = "hasAnyAuthority('" + AuthUser.ROLE_USER + "','"
			+ AuthUser.ROLE_ADMIN + "')";

	/**
	 * 授权角色：匿名用户 或 登录用户 或 系统管理员
	 */
	protected static final String AUTH_ANONYMOUS_USER_ADMIN = "hasAnyAuthority('" + AuthUser.ROLE_ANONYMOUS + "','"
			+ AuthUser.ROLE_USER + "','" + AuthUser.ROLE_ADMIN + "')";

	/**
	 * 授权角色：匿名用户
	 */
	protected static final String AUTH_ANONYMOUS = "hasAuthority('" + AuthUser.ROLE_ANONYMOUS + "')";

	private CoreConfig coreConfig;

	private Environment environment;

	@Autowired
	public SecurityConfig(CoreConfig coreConfig, Environment environment)
	{
		super();
		this.coreConfig = coreConfig;
		this.environment = environment;
	}

	public CoreConfig getCoreConfig()
	{
		return coreConfig;
	}

	public void setCoreConfig(CoreConfig coreConfig)
	{
		this.coreConfig = coreConfig;
	}

	public Environment getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}

	/**
	 * 是否禁用匿名用户使用系统。
	 * 
	 * @return
	 */
	protected boolean isDisableAnonymous()
	{
		String disableStr = this.environment.getProperty("disableAnonymous");
		return (disableStr == null ? false : Boolean.TRUE.toString().equals(disableStr));
	}

	protected AuthenticationSuccessHandler getAuthenticationSuccessHandler()
	{
		AuthenticationSuccessHandlerImpl bean = new AuthenticationSuccessHandlerImpl();

		List<CreateUserEntityService> createUserEntityServices = Arrays.asList(this.coreConfig.schemaService(),
				this.coreConfig.dataSetEntityService(), this.coreConfig.htmlChartWidgetEntityService(),
				this.coreConfig.htmlTplDashboardWidgetEntityService(), this.coreConfig.analysisProjectService());

		bean.setCreateUserEntityServices(createUserEntityServices);

		return bean;
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		// 静态资源
		web.ignoring().antMatchers("/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		CDaoAuthenticationProvider cDaoAuthenticationProvider = new CDaoAuthenticationProvider();
		cDaoAuthenticationProvider.setUserDetailsService(userDetailsService());
		http.authenticationProvider(cDaoAuthenticationProvider);
		boolean disableAnonymous = isDisableAnonymous();

		// 默认是开启CSRF的，系统目前没有提供相关支持，因此需禁用
		http.csrf().disable();

		// 默认"X-Frame-Options"值为"DENY"，这会导致系统的图表/看板展示页面无法被其他应用嵌入iframe，因此需禁用
		http.headers().frameOptions().disable();

		http.authorizeRequests()

				// 切换主题
				.antMatchers("/changeThemeData/**").permitAll()

				// 展示图表和看板
				// 注意：无论系统是否允许匿名用户访问，它们都应允许匿名用户访问，用于支持外部系统iframe嵌套场景
				.antMatchers("/analysis/chartPlugin/icon/*", "/analysis/chartPlugin/chartPluginManager.js",
						"/analysis/chart/show/**", "/analysis/chart/showData", "/analysis/dashboard/show/**",
						"/analysis/dashboard/showData", "/analysis/dashboard/loadChart",
						"/analysis/dashboard/heartbeat")
				.access(AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)


				// 数据源
				// 编辑
				.antMatchers("/schema/add", "/schema/saveadd", "/schema/edit", "/schema/saveedit", "/schema/delete")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN : AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN)
				// 其他
		/*		.antMatchers("/schema/**")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)
*/
				.antMatchers("/orders/**").access(AUTH_USER_ADMIN)



				// 数据源数据管理、导入导出、SQL工作台、SQL编辑器
				// 用户针对数据源数据的所有操作都已受其所属数据源权限控制，所以不必再引入数据管理员/数据分析员权限
				.antMatchers("/data/**").access(disableAnonymous ? AUTH_USER_ADMIN : AUTH_ANONYMOUS_USER_ADMIN)
				.antMatchers("/dataexchange/**").access(disableAnonymous ? AUTH_USER_ADMIN : AUTH_ANONYMOUS_USER_ADMIN)
				.antMatchers("/sqlpad/**").access(disableAnonymous ? AUTH_USER_ADMIN : AUTH_ANONYMOUS_USER_ADMIN)
				.antMatchers("/sqlEditor/**").access(disableAnonymous ? AUTH_USER_ADMIN : AUTH_ANONYMOUS_USER_ADMIN)

				// 数据集
				// 编辑
				.antMatchers("/analysis/dataSet/addFor*", "/analysis/dataSet/saveAddFor*", "/analysis/dataSet/edit",
						"/analysis/dataSet/saveEditFor*", "/analysis/dataSet/delete", "/analysis/dataSet/uploadFile")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN : AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN)
				// 其他
				.antMatchers("/analysis/dataSet/**")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)

				// 图表
				// 编辑
				.antMatchers("/analysis/chart/add", "/analysis/chart/edit", "/analysis/chart/save",
						"/analysis/chart/delete")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN : AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN)
				// 其他
				.antMatchers("/analysis/chart/**")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)

				// 看板
				// 编辑
				.antMatchers("/analysis/dashboard/add", "/analysis/dashboard/edit", "/analysis/dashboard/save",
						"/analysis/dashboard/saveTemplateNames", "/analysis/dashboard/deleteResource",
						"/analysis/dashboard/uploadResourceFile", "/analysis/dashboard/saveResourceFile",
						"/analysis/dashboard/import", "/analysis/dashboard/uploadImportFile",
						"/analysis/dashboard/saveImport", "/analysis/dashboard/delete")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN : AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN)
				// 其他
				.antMatchers("/analysis/dashboard/**")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)

				// 数据分析项目
				// 编辑
				.antMatchers("/analysis/project/add", "/analysis/project/edit", "/analysis/project/save",
						"/analysis/project/delete")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN : AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN)
				// 其他
				.antMatchers("/analysis/project/**")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)

				// 图表插件
				// 选择
				.antMatchers("/analysis/chartPlugin/select", "/analysis/chartPlugin/selectData")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)
				// 管理
				.antMatchers("/analysis/chartPlugin/**").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				// 数据集资源
				// 选择
				.antMatchers("/dataSetResDirectory/view", "/dataSetResDirectory/select",
						"/dataSetResDirectory/pagingQueryData", "/dataSetResDirectory/listFiles")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)
				// 管理
				.antMatchers("/dataSetResDirectory/**").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				// 看板全局资源
				// 选择
				.antMatchers("/dashboardGlobalRes/queryData")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)
				// 管理
				.antMatchers("/dashboardGlobalRes/**").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				// 数据授权
				.antMatchers("/authorization/**").access(AUTH_USER_ADMIN_AND_DATA_ADMIN)

				// 驱动程序
				// 选择
				.antMatchers("/driverEntity/view", "/driverEntity/select", "/driverEntity/queryData",
						"/driverEntity/downloadDriverFile", "/driverEntity/listDriverFile")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)
				// 管理
				.antMatchers("/driverEntity/**").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				// 数据源URL构建器
				// 构建
				.antMatchers("/schemaUrlBuilder/buildUrl")
				.access(disableAnonymous ? AUTH_USER_ADMIN_AND_DATA_ADMIN_ANALYST
						: AUTH_ANONYMOUS_USER_ADMIN_AND_DATA_ADMIN_ANALYST)
				// 管理
				.antMatchers("/schemaUrlBuilder/*").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				// 用户
				// 个人设置
				.antMatchers("/user/personalSet", "/user/savePersonalSet").access(AUTH_USER_ADMIN)
				// 选择
				.antMatchers("/user/select", "/user/pagingQueryData").access(AUTH_USER_ADMIN)
				// 管理
				.antMatchers("/user/**").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				// 角色
				// 选择
				.antMatchers("/role/select", "/role/pagingQueryData").access(AUTH_USER_ADMIN)
				// 管理
   				.antMatchers("/role/**").access(AUTH_USER_ADMIN)  //AUTH_ADMIN

				//
				.antMatchers("/login/**", "/register/**", "/resetPassword/**").access(AUTH_ANONYMOUS)

				//
				.antMatchers("/**").access(disableAnonymous ? AUTH_USER_ADMIN : AUTH_ANONYMOUS_USER_ADMIN)


				.and().formLogin().loginPage("/login").loginProcessingUrl("/login/doLogin").usernameParameter("name")
				.passwordParameter("password").successHandler(getAuthenticationSuccessHandler())

				.and().logout().logoutUrl("/logout").invalidateHttpSession(true).logoutSuccessUrl("/")

				.and().rememberMe().key("REMEMBER_ME_KEY").tokenValiditySeconds(60 * 60 * 24 * 365)
				.rememberMeParameter("rememberMe").rememberMeCookieName("REMEMBER_ME");

		configureAnonymous(http);
	}

	/**
	 * 将默认的{@linkplain AnonymousAuthenticationFilter}配置改为{@linkplain AnonymousAuthenticationFilterExt}。
	 * 
	 * @param http
	 * @throws Exception
	 */
	protected void configureAnonymous(HttpSecurity http) throws Exception
	{
		String anonymousAuthKey = UUID.randomUUID().toString();

		String[] anonymousRoleIds = StringUtil.split(this.environment.getProperty("defaultRole.anonymous"), ",", true);
		Set<String> anonymousRoleIdSet = new HashSet<>();
		anonymousRoleIdSet.addAll(Arrays.asList(anonymousRoleIds));

		AnonymousAuthenticationFilterExt anonymousAuthenticationFilter = new AnonymousAuthenticationFilterExt(
				anonymousAuthKey);
		anonymousAuthenticationFilter.setAnonymousRoleIds(anonymousRoleIdSet);
		anonymousAuthenticationFilter.setRoleService(this.coreConfig.roleService());

		http.anonymous().authenticationProvider(new AnonymousAuthenticationProvider(anonymousAuthKey))
				.authenticationFilter(anonymousAuthenticationFilter);
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService()
	{
		UserDetailsService bean = new UserDetailsServiceImpl(this.coreConfig.userService());
		return bean;
	}

	@Bean
	public StrictHttpFirewall httpFirewall()
	{
		StrictHttpFirewall firewall = new StrictHttpFirewall();

		// 看板有些功能需要URL中允许分号（;）
		// 参考：AbstractDataAnalysisController.addJsessionidParam(String, String)，
		// 因此这里需要设置为允许，不然功能将无法使用
		firewall.setAllowSemicolon(true);
		return firewall;
	}
}
