package com.shopme.admin.customer;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Customer;

import jakarta.servlet.http.HttpServletResponse;

public class CustomerCsvExporter extends AbstractExporter {
	
	public void export(List<Customer> listCustomers, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv", "customers_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"Customer ID", "E-mail", "First Name", "Last Name", "City", "State", "Country", "Enabled"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "city", "state", "country", "enabled"};

		csvWriter.writeHeader(csvHeader);

		for (Customer customer : listCustomers) {

			csvWriter.write(customer, fieldMapping);
		}

		csvWriter.close();
	}

}
