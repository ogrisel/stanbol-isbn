package com.example.stanbol.isbn.engine;

import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_END;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_SELECTED_TEXT;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_SELECTION_CONTEXT;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_START;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.clerezza.rdf.core.LiteralFactory;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.PlainLiteralImpl;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.Blob;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementEngine;
import org.apache.stanbol.enhancer.servicesapi.InvalidContentException;
import org.apache.stanbol.enhancer.servicesapi.ServiceProperties;
import org.apache.stanbol.enhancer.servicesapi.helper.ContentItemHelper;
import org.apache.stanbol.enhancer.servicesapi.helper.EnhancementEngineHelper;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detect occurrences of valid ISBN-13 in a text document and annotate those
 * with TextAnnotation.
 */
@Component(metatype = true, immediate = true, label = "%ISBNDetectorEngine.label", description = "%ISBNDetectorEngine.description")
@Service
@Property(name = EnhancementEngine.PROPERTY_NAME, value = "isbn-detector")
public class ISBNDetectorEngine implements EnhancementEngine, ServiceProperties {

	private final Logger log = LoggerFactory
			.getLogger(ISBNDetectorEngine.class);

	protected static final Set<String> SUPPORTED_MIMETYPES = Collections
			.singleton("text/plain");

	protected Pattern isbnPattern = Pattern
			.compile("TODO: write a regexp for ISBN-13 using Wikipedia");

	private String name;

	/**
	 * Activator that reads and validates the
	 * {@link EnhancementEngine#PROPERTY_NAME} property from the
	 * {@link ComponentContext#getProperties()}.
	 * 
	 * @param ctx
	 *            the component context
	 * @throws ConfigurationException
	 *             if the required property
	 *             {@link EnhancementEngine#PROPERTY_NAME} is missing or empty
	 */
	@Activate
	protected void activate(ComponentContext ctx) throws ConfigurationException {
		Object value = ctx.getProperties().get(PROPERTY_NAME);
		if (value instanceof String) {
			name = (String) value;
			if (name.isEmpty()) {
				name = null;
				throw new ConfigurationException(
						PROPERTY_NAME,
						"The configured"
								+ "name of an EnhancementEngine MUST NOT be empty");
			}
		} else {
			throw new ConfigurationException(
					PROPERTY_NAME,
					value == null ? "The name is a required property."
							: "The name of an EnhancementEngine MUST be an non empty String "
									+ "(type: "
									+ value.getClass()
									+ " value: "
									+ value + ")");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Deactivate
	protected void deactivate(ComponentContext ctx) {
		name = null;
	}

	@Override
	public Map<String, Object> getServiceProperties() {
		return Collections.unmodifiableMap(Collections.singletonMap(
				ENHANCEMENT_ENGINE_ORDERING,
				(Object) ServiceProperties.ORDERING_DEFAULT));
	}

	@Override
	public int canEnhance(ContentItem ci) throws EngineException {
		if (ContentItemHelper.getBlob(ci, SUPPORTED_MIMETYPES) != null) {
			return ENHANCE_ASYNC;
		} else {
			return CANNOT_ENHANCE;
		}
	}

	@Override
	public void computeEnhancements(ContentItem ci) throws EngineException {
		Entry<UriRef, Blob> contentPart = ContentItemHelper.getBlob(ci,
				SUPPORTED_MIMETYPES);
		if (contentPart == null) {
			throw new IllegalStateException(
					"No ContentPart with a supported Mime Type"
							+ "found for ContentItem " + ci.getUri()
							+ "(supported: '" + SUPPORTED_MIMETYPES + "')");
		}
		String text;
		try {
			text = ContentItemHelper.getText(contentPart.getValue());
		} catch (IOException e) {
			throw new InvalidContentException(String.format(
					"Unable to extract "
							+ " text from ContentPart %s of ContentItem %s",
					contentPart.getKey(), ci.getUri()), e);
		}
		if (text.trim().length() == 0) {
			log.warn(
					"ContentPart {} of ContentItem does not contain any Text to extract knowledge from",
					contentPart.getKey(), ci);
			return;
		}

		// TODO: use isbnPattern to find all ISBN occurrences in text and call
		// createTextEnhancement for each of them
	}

	public static UriRef createTextEnhancement(String selected, int start,
			int end, EnhancementEngine engine, ContentItem ci, String content) {
		LiteralFactory literalFactory = LiteralFactory.getInstance();
		MGraph model = ci.getMetadata();
		UriRef textAnnotation = EnhancementEngineHelper.createTextEnhancement(
				ci, engine);
		model.add(new TripleImpl(textAnnotation, ENHANCER_SELECTED_TEXT,
				new PlainLiteralImpl(selected)));

		// TODO: add start and end position info as new triples using the
		// literalFactory object for typed literals and the RDF properties:
		// org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_START
		// org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_END

		String context = makeSelectionContext(content, start, end);
		// TODO: add the selection context info as another triple using the RDF
		// property:
		// org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_SELECTION_CONTEXT
		return textAnnotation;
	}

	/**
	 * Compute a window of text of size maximum 500 characters approximately
	 * centered on the selection, cutting the text on white-spaces only.
	 */
	public static String makeSelectionContext(String content, int start, int end) {
		//TODO: implement me:
		return "TODO: return the text surrounding the expression '"
				+ content.substring(start, end)
				+ "' in the original text instead.";
	}

	/**
	 * Return true if the ISBN string follows Wikipedia algorithm to validate
	 * the check sum.
	 */
	public static boolean isISBN13Valid(String isbn) {
		// TODO: write a function that compute the ISBN-13 checksum validation
		return true;
	}

}
