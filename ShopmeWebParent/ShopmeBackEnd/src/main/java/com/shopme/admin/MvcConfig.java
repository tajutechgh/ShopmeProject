package com.shopme.admin;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class MvcConfig implements WebMvcConfigurer {

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		
//		String dirName = "user-photos";
//		
//		Path userPhotosDir = Paths.get(dirName);
//		
//		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
//				
//		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:" + userPhotosPath + "/");	
//		
//		
//        String categoryImageDirName = "../category-images";
//		
//		Path categoryImageDir = Paths.get(categoryImageDirName);
//		
//		String categoryImagesPath = categoryImageDir.toFile().getAbsolutePath();
//				
//		registry.addResourceHandler("/category-images/**").addResourceLocations("file:" + categoryImagesPath + "/");
//		
//		
//        String brandLogoDirName = "../brand-logos";
//		
//		Path brandLogoDir = Paths.get(brandLogoDirName);
//		
//		String brandLogoPath = brandLogoDir.toFile().getAbsolutePath();
//				
//		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:" + brandLogoPath + "/");
//	}
	
	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		
//		exposeDirectory("user-photos", registry);
//		exposeDirectory("../category-images", registry);
//		exposeDirectory("../brand-logos", registry);
//		exposeDirectory("../product-images", registry);
//		exposeDirectory("../site-logo", registry);
//	}
//	
//	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
//		
//		Path path = Paths.get(pathPattern);
//		
//		String absolutePath = path.toFile().getAbsolutePath();
//		
//		String logicalPath = pathPattern.replace("../", "") + "/**";
//				
//		registry.addResourceHandler(logicalPath).addResourceLocations("file:" + absolutePath + "/");		
//	}

}
