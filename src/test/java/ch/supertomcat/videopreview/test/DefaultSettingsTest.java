package ch.supertomcat.videopreview.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import ch.supertomcat.videopreview.settingsconfig.ObjectFactory;
import ch.supertomcat.videopreview.settingsconfig.Settings;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Default Settings Test
 */
@SuppressWarnings("javadoc")
public class DefaultSettingsTest {
	@Test
	public void testTemplate() throws IOException, SAXException, JAXBException {
		Settings settings = loadSettingsFile("/ch/supertomcat/videopreview/settingsconfig/default-settings.xml");
		assertNotNull(settings.getDirectorySettings());
		assertNotNull(settings.getGuiSettings());
		assertNotNull(settings.getLogLevel());
	}

	private Settings loadSettingsFile(String resourceFile) throws IOException, SAXException, JAXBException {
		try (InputStream in = getClass().getResourceAsStream(resourceFile)) {
			if (in == null) {
				throw new IllegalArgumentException("Resource not found: " + resourceFile);
			}

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(getClass().getResource("/ch/supertomcat/videopreview/settingsconfig/settings.xsd"));

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setSchema(schema);
			return (Settings)unmarshaller.unmarshal(in);
		}
	}
}
