package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Brand;
import jakarta.servlet.http.HttpServletResponse;

public class BrandCsvExporter extends AbstractExporter {

	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv", "brands_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Brand ID", "Brand Name" };
		String[] fieldMapping = { "id", "name" };

		csvWriter.writeHeader(csvHeader);

		for (Brand brand : listBrands) {

			brand.setName(brand.getName().replace("--", "  "));

			csvWriter.write(brand, fieldMapping);
		}

		csvWriter.close();
	}

}
