package com.cepheid.cloud.skel.constant;

import java.util.HashMap;
import java.util.Map;

public class ItemConstants {
	public static final Map<String, String> errorStore = new HashMap<String, String>();

	static {
		errorStore.put("110000", "Something went wrong, please try again later.");
		errorStore.put("110001", "Bad request, please check request.");
		errorStore.put("110002", "IOException Occured");
		errorStore.put("110003", "Not Found in DB , Please try again later with different parameter");
		errorStore.put("110004", "Error - Unique Key Violation!! -  Foreign Key Violation");
		errorStore.put("110005", "Deleted Item Successfully ..");
	}
}
