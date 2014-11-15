package edu.mccc.cos210.fp2014.col.client.tools;

import java.util.Map;
import java.util.HashMap;

/**
 * Contains an array of settings to be recalled by any of the ToolViews
 */
public class ToolModel {
	private Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * Default public constructor
	 */
	public ToolModel() {
	}

	/**
	 * Returns the attributes of a given model
	 * @return attributes String array of attributes
	 */
	public Object getAttribute(String keyName) {
		return attributes.get(keyName);
	}

	/**
	 * Takes a new string array of attributes and updates the private model
	 * @param  newAttributes String array of attributes
	 * @return 
	 */
	public void updateAttribute(String keyName, Object keyValue) {
		attributes.put(keyName, keyValue);
	}

	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, Object> entry : this.attributes.entrySet()) {
		    result.append(
		    	"Key: " + entry.getKey() + " | Value: " + entry.getValue()
		    );
		}

		return result.toString();
	}

}