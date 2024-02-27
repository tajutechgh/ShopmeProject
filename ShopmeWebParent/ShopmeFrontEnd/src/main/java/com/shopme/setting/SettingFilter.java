package com.shopme.setting;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.shopme.common.Constants;
import com.shopme.common.entity.Menu;
import com.shopme.common.entity.setting.Setting;
import com.shopme.menu.UserMenuService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Order(-123)
public class SettingFilter implements Filter {

	@Autowired
	private UserSettingService settingService;
	
	@Autowired 
	private UserMenuService menuService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		
		String url = servletRequest.getRequestURL().toString();
		
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
			
			chain.doFilter(request, response);
			
			return;
		}
		
		loadGeneralSettings(request);
		loadMenuSettings(request);
		
		chain.doFilter(request, response);

	}
	
	private void loadMenuSettings(ServletRequest request) {
		
		List<Menu> headerMenuItems = menuService.getHeaderMenuItems();
		
		request.setAttribute("headerMenuItems", headerMenuItems);

		List<Menu> footerMenuItems = menuService.getFooterMenuItems();
		
		request.setAttribute("footerMenuItems", footerMenuItems);		
	}

	private void loadGeneralSettings(ServletRequest request) {
		
		List<Setting> generalSettings = settingService.getGeneralSettings();
		
		generalSettings.forEach(setting -> {
			
			request.setAttribute(setting.getKey(), setting.getValue());
			
			System.out.println(setting.getKey() + " == > " + setting.getValue());
		});
		
		request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
	}

}
