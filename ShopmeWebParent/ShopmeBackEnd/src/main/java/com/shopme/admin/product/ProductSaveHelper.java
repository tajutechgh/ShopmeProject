package com.shopme.admin.product;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.AmazonS3Util;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
//
//	static void deleteExtraImagesWereRemovedOnForm(Product product) {
//
//		String extraImageDir = "../product-images/" + product.getId() + "/extras";
//
//		Path dirPath = Paths.get(extraImageDir);
//
//		try {
//
//			Files.list(dirPath).forEach(file -> {
//
//				String fileName = file.toFile().getName();
//
//				if (!product.containsImageName(fileName)) {
//
//					try {
//
//						Files.delete(file);
//
//						LOGGER.info("Deleted extra image: " + fileName);
//
//					} catch (IOException e) {
//
//						LOGGER.error("Could not delete extra image: " + fileName);
//					}
//				}
//
//			});
//
//		} catch (IOException ex) {
//
//			LOGGER.error("Could not list directory: " + dirPath);
//		}
//
//	}
	
	static void deleteExtraImagesWereRemovedOnForm(Product product) {
		
		String extraImageDir = "product-images/" + product.getId() + "/extras";
		
		List<String> listObjectKeys = AmazonS3Util.listFolder(extraImageDir);
		
		for (String objectKey : listObjectKeys) {
			
			int lastIndexOfSlash = objectKey.lastIndexOf("/");
			
			String fileName = objectKey.substring(lastIndexOfSlash + 1, objectKey.length());
			
			if (!product.containsImageName(fileName)) {
				
				AmazonS3Util.deleteFile(objectKey);
				
				System.out.println("Deleted extra image: " + objectKey);
			}
		}
	}

	static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

		if (imageIDs == null || imageIDs.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {

			Integer id = Integer.parseInt(imageIDs[count]);

			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

	}

	static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {

		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++) {

			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {

				product.addDetail(id, name, value);

			} else if (!name.isEmpty() && !value.isEmpty()) {

				product.addDetail(name, value);
			}
		}

	}

//	static void saveUploadedImages(MultipartFile mainImageMultiPart, MultipartFile[] extraImageMultiParts,
//			Product savedProduct) throws IOException {
//
//		if (!mainImageMultiPart.isEmpty()) {
//
//			String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
//
//			String uploadDir = "../product-images/" + savedProduct.getId();
//
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultiPart);
//
//		}
//
//		if (extraImageMultiParts.length > 0) {
//
//			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
//
//			for (MultipartFile multipartFile : extraImageMultiParts) {
//
//				if (multipartFile.isEmpty())
//					continue;
//
//				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//
//				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//			}
//		}
//	}
	
	static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		
		if (!mainImageMultipart.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			
			String uploadDir = "product-images/" + savedProduct.getId();
			
			List<String> listObjectKeys = AmazonS3Util.listFolder(uploadDir + "/");
			
			for (String objectKey : listObjectKeys) {
				
				if (!objectKey.contains("/extras/")) {
					
					AmazonS3Util.deleteFile(objectKey);
				}
			}
			
			AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());					
		}
		
		if (extraImageMultiparts.length > 0) {
			
			String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				
				if (multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());	
			}
		}
		
	}

	static void setNewExtraImageNames(MultipartFile[] extraImageMultiParts, Product product) {

		if (extraImageMultiParts.length > 0) {

			for (MultipartFile multipartFile : extraImageMultiParts) {

				if (!multipartFile.isEmpty()) {

					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					if (!product.containsImageName(fileName)) {

						product.addExtraImage(fileName);

					}
				}
			}
		}

	}

	static void setMainImageName(MultipartFile mainImageMultiPart, Product product) {

		if (!mainImageMultiPart.isEmpty()) {

			String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());

			product.setMainImage(fileName);
		}
	}

}
