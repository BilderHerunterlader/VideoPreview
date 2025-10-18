package ch.supertomcat.videopreview.templates;

import java.io.File;
import java.io.FileFilter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.IncludeRelativePath;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import ch.supertomcat.supertomcatutils.application.ApplicationMain;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;

/**
 * Manager for templates
 */
public class TemplateManager {
	/**
	 * Velocity Engine
	 */
	private final VelocityEngine velocityEngine = new VelocityEngine();

	/**
	 * Folder
	 */
	private final File folder;

	/**
	 * Folder for Includes
	 */
	private final File includesFolder;

	/**
	 * Folder for Footers
	 */
	private final File footersFolder;

	/**
	 * File Filter
	 */
	private FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.isFile() && pathname.getName().endsWith(".vm");
		}
	};

	/**
	 * Constructor
	 * Templates are only loaded from class path
	 */
	public TemplateManager() {
		this(null, true);
	}

	/**
	 * Constructor
	 * No templates are loaded from class path
	 * 
	 * @param folder Templates Folder or null for default folder
	 */
	public TemplateManager(File folder) {
		this(folder != null ? folder : new File(ApplicationProperties.getProperty(ApplicationMain.APPLICATION_PATH), "templates/"), false);
	}

	/**
	 * Constructor
	 * 
	 * @param folder Templates Folder or null
	 * @param classPath True if templates from class path needs to be loaded, false otherwise
	 */
	public TemplateManager(File folder, boolean classPath) {
		if (folder == null && classPath == false) {
			throw new IllegalArgumentException("folder is null and classPath is false. One of them needs to be enabled");
		}

		this.folder = folder;
		if (this.folder == null) {
			this.includesFolder = null;
			this.footersFolder = null;
		} else {
			this.includesFolder = new File(folder, "includes");
			this.footersFolder = new File(folder, "footers");
		}

		StringJoiner sjResourceLoaders = new StringJoiner(",");
		if (classPath) {
			sjResourceLoaders.add("classpath");
			velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER_CACHE, "false");
			velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
			velocityEngine.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeRelativePath.class.getName());
		}

		if (folder != null) {
			sjResourceLoaders.add("file");
			velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, folder.getAbsolutePath());
			velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
		}

		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, sjResourceLoaders.toString());

		velocityEngine.init();
	}

	/**
	 * Get templates files
	 * 
	 * @param includes True if includes should be returned also, false otherwise
	 * @param footers True if footers should be returned also, false otherwise
	 * @return Template Files or an empty list if only resources are used
	 */
	public List<File> getTemplateFiles(boolean includes, boolean footers) {
		List<File> templateFiles = new ArrayList<>();
		if (folder != null) {
			templateFiles.addAll(getTemplateFiles(folder));
			if (includes) {
				templateFiles.addAll(getTemplateFiles(includesFolder));
			}
			if (footers) {
				templateFiles.addAll(getTemplateFiles(footersFolder));
			}
		}
		return templateFiles;
	}

	/**
	 * Get main templates files
	 * 
	 * @return Template Files or an empty list if only resources are used
	 */
	public List<File> getMainTemplateFiles() {
		return getTemplateFiles(folder);
	}

	/**
	 * Get include templates files
	 * 
	 * @return Template Files or an empty list if only resources are used
	 */
	public List<File> getIncludeTemplateFiles() {
		return getTemplateFiles(includesFolder);
	}

	/**
	 * Get include templates files
	 * 
	 * @return Template Files or an empty list if only resources are used
	 */
	public List<File> getFooterTemplateFiles() {
		return getTemplateFiles(footersFolder);
	}

	/**
	 * Get templates files from folder
	 * 
	 * @param templateFolder Folder
	 * @return Template Files
	 */
	private List<File> getTemplateFiles(File templateFolder) {
		if (templateFolder == null) {
			return new ArrayList<>();
		}

		File[] foundFiles = templateFolder.listFiles(fileFilter);
		if (foundFiles == null) {
			return new ArrayList<>();
		}

		return Arrays.asList(foundFiles);
	}

	/**
	 * Render Template
	 * 
	 * @param templateName Template Name
	 * @param vars Variables
	 * @return Rendered Template
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws MethodInvocationException
	 */
	public String renderTemplate(String templateName, Map<String, Object> vars) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		Template t = velocityEngine.getTemplate(templateName);

		VelocityContext context = new VelocityContext();
		vars.entrySet().stream().forEach(e -> context.put(e.getKey(), e.getValue()));

		StringWriter stringWriter = new StringWriter();
		t.merge(context, stringWriter);
		return stringWriter.toString();
	}
}
