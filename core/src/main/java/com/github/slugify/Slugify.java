package com.github.slugify;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Slugify {
	private final Properties replacements = new Properties();

	private Map<String, String> customReplacements;
	private boolean lowerCase;

	public Slugify() throws IOException {
		this(true);
	}

	public Slugify(final boolean lowerCase) throws IOException {
		InputStream replacementsStream = getClass().getClassLoader().getResourceAsStream("replacements.properties");
		replacements.load(replacementsStream);
		replacementsStream.close();
		setLowerCase(lowerCase);
	}

	public String slugify(String input) {
		if (input == null) {
			return "";
		}

		input = input.trim();

		Map<String, String> customReplacements = getCustomReplacements();
		if (customReplacements != null) {
			for (Entry<String, String> entry : customReplacements.entrySet()) {
				input = input.replace(entry.getKey(), entry.getValue());
			}
		}

		for (Entry<Object, Object> e : replacements.entrySet()) {
			input = input.replace(e.getKey().toString(), e.getValue().toString());
		}

		input = normalize(input);

		if (getLowerCase()) {
			input = input.toLowerCase();
		}

		return input;
	}
	
	protected String normalize(String input) {
	    input = Normalizer.normalize(input, Normalizer.Form.NFKD)
	    		.replaceAll("[^\\p{ASCII}]+", "")
	    		.replaceAll("(?:[^\\w+]|\\s)+", "-")
	    		.replaceAll("^-|-$", "");
	    return input;
	}
	
	public Map<String, String> getCustomReplacements() {
		return customReplacements;
	}

	public void setCustomReplacements(Map<String, String> customReplacements) {
		this.customReplacements = customReplacements;
	}

	public boolean getLowerCase() {
		return lowerCase;
	}

	public void setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}
}
