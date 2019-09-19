package ch.supertomcat.mediainfo;

import static java.util.Collections.*;

import java.lang.reflect.Method;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

/**
 * MediaInfo Implementation
 */
public class MediaInfo implements AutoCloseable {
	/**
	 * Media Info Library Name for loading library
	 */
	public static String mediaInfoLibraryName = Platform.isWindows() && Platform.is64Bit() ? "MediaInfox64" : "MediaInfo";

	static {
		// libmediainfo for linux depends on libzen
		try {
			// We need to load dependencies first, because we know where our native libs are (e.g. Java Web Start Cache).
			// If we do not, the system will look for dependencies, but only in the library path.
			String os = System.getProperty("os.name");
			if (os != null && !os.toLowerCase().startsWith("windows") && !os.toLowerCase().startsWith("mac")) {
				final ClassLoader loader = MediaInfo.class.getClassLoader();
				final String localPath;
				if (loader != null) {
					localPath = loader.getResource(MediaInfo.class.getName().replace('.', '/') + ".class").getPath().replace("MediaInfo.class", "");
					try {
						NativeLibrary.getInstance(localPath + "libzen.so.0");
					} catch (LinkageError e) {
						NativeLibrary.getInstance("zen");
					}
				} else {
					localPath = "";
					NativeLibrary.getInstance("zen");
				}
				if (!localPath.isEmpty()) {
					try {
						String libraryName = localPath + "libmediainfo.so.0";
						NativeLibrary.getInstance(libraryName);
						mediaInfoLibraryName = libraryName;
					} catch (LinkageError e) {
					}
				}
			}
		} catch (LinkageError e) {
			// Logger.getLogger(MediaInfo.class.getName()).warning("Failed to preload libzen");
		}
	}

	/**
	 * Handle
	 */
	private Pointer handle = null;

	/**
	 * Constructor
	 */
	public MediaInfo() {
		handle = MediaInfoJNA.INSTANCE.New();
	}

	@Override
	public synchronized void close() throws Exception {
		if (handle != null) {
			MediaInfoJNA.INSTANCE.Delete(handle);
			handle = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}

	/**
	 * Open File
	 * 
	 * @param file File
	 * @return 1 if file was opened, 0 if file was not not opened
	 */
	public int open(String file) {
		return MediaInfoJNA.INSTANCE.Open(handle, new WString(file));
	}

	/**
	 * Open Buffer Init
	 * 
	 * @param length Length
	 * @param offset Offset
	 * @return TODO Description
	 */
	public int openButterInit(long length, long offset) {
		return MediaInfoJNA.INSTANCE.Open_Butter_Init(handle, length, offset);
	}

	/**
	 * Open Buffer Continue
	 * 
	 * @param buffer Buffer
	 * @param size Size
	 * @return TODO Description
	 */
	public int openBufferContinue(byte[] buffer, int size) {
		return MediaInfoJNA.INSTANCE.Open_Buffer_Continue(handle, buffer, size);
	}

	/**
	 * Open Buffer GoTo Get
	 * 
	 * @return TODO Description
	 */
	public long openBufferContinueGoToGet() {
		return MediaInfoJNA.INSTANCE.Open_Buffer_Continue_GoTo_Get(handle);
	}

	/**
	 * Open Buffer Finalize
	 * 
	 * @return TODO Description
	 */
	public int openBufferFinalize() {
		return MediaInfoJNA.INSTANCE.Open_Buffer_Finalize(handle);
	}

	/**
	 * Close File
	 */
	public void closeFile() {
		MediaInfoJNA.INSTANCE.Close(handle);
	}

	/**
	 * Get all detail information about a file in a String
	 * 
	 * @return All detail information about a file in a String
	 */
	public String inform() {
		return MediaInfoJNA.INSTANCE.Inform(handle, 0).toString();
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(StreamKind streamKind, int streamNumber, String parameter) {
		return get(streamKind, streamNumber, parameter, InfoKind.TEXT, InfoKind.NAME);
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(StreamKind streamKind, int streamNumber, String parameter, InfoKind infoKind) {
		return get(streamKind, streamNumber, parameter, infoKind, InfoKind.NAME);
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @param searchKind Where to look for parameter
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(StreamKind streamKind, int streamNumber, String parameter, InfoKind infoKind, InfoKind searchKind) {
		return MediaInfoJNA.INSTANCE.Get(handle, streamKind.ordinal(), streamNumber, new WString(parameter), infoKind.ordinal(), searchKind.ordinal()).toString();
	}

	/**
	 * Get a single information about a file (integer parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameterIndex Requested Parameter Index
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(StreamKind streamKind, int streamNumber, int parameterIndex) {
		return get(streamKind, streamNumber, parameterIndex, InfoKind.TEXT);
	}

	/**
	 * Get a single information about a file (integer parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameterIndex Requested Parameter Index
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(StreamKind streamKind, int streamNumber, int parameterIndex, InfoKind infoKind) {
		return MediaInfoJNA.INSTANCE.GetI(handle, streamKind.ordinal(), streamNumber, parameterIndex, infoKind.ordinal()).toString();
	}

	/**
	 * Get number of Streams of the given Stream Kind
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @return Number of Streams of the given Stream Kind
	 */
	public int count(StreamKind streamKind) {
		try {
			return count(streamKind, -1);
		} catch (Exception e) {
			/*
			 * Because this might fail on 64-Bit, try to get it over different method
			 */
			String strStreamCount = get(streamKind, 0, "StreamCount");
			if (strStreamCount == null || strStreamCount.isEmpty()) {
				return 0;
			}
			return Integer.parseInt(strStreamCount);
		}
	}

	/**
	 * Get number of Streams of the given Stream Kind or number of Information of the given Stream
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @return Number of Streams of the given Stream Kind
	 */
	public int count(StreamKind streamKind, int streamNumber) {
		return MediaInfoJNA.INSTANCE.Count_Get(handle, streamKind.ordinal(), streamNumber);
	}

	/**
	 * Configure or get information about MediaInfo
	 * 
	 * @param option Option Name
	 * @return Depending on the option (By Default "" (nothing) means No, other values mean Yes)
	 */
	public String option(String option) {
		return option(option, "");
	}

	/**
	 * Configure or get information about MediaInfo
	 * 
	 * @param option Option Name
	 * @param value Value
	 * @return Depending on the option (By Default "" (nothing) means No, other values mean Yes)
	 */
	public String option(String option, String value) {
		return MediaInfoJNA.INSTANCE.Option(handle, new WString(option), new WString(value)).toString();
	}

	/**
	 * Configure or get information about MediaInfo
	 * 
	 * @param option Option Name
	 * @return Depending on the option (By Default "" (nothing) means No, other values mean Yes)
	 */
	public static String optionStatic(String option) {
		return optionStatic(option, "");
	}

	/**
	 * Configure or get information about MediaInfo
	 * 
	 * @param option Option Name
	 * @param value Value
	 * @return Depending on the option (By Default "" (nothing) means No, other values mean Yes)
	 */
	public static String optionStatic(String option, String value) {
		try (MediaInfo mediaInfo = new MediaInfo()) {
			return mediaInfo.option(option, value);
		} catch (Exception e) {
			throw new RuntimeException("Could not close MediaInfo instance", e);
		}
	}

	/**
	 * JNA Binding Class for MediaInfo Library
	 */
	public interface MediaInfoJNA extends Library {
		/**
		 * INSTANCE
		 */
		MediaInfoJNA INSTANCE = Native.load(mediaInfoLibraryName, MediaInfoJNA.class, singletonMap(OPTION_FUNCTION_MAPPER, new FunctionMapper() {

			@Override
			public String getFunctionName(NativeLibrary library, Method method) {
				return "MediaInfo_" + method.getName();
			}
		}));

		/**
		 * Constructor
		 * 
		 * @return Handle
		 */
		Pointer New();

		/**
		 * Destructor
		 * 
		 * @param Handle Handle
		 */
		void Delete(Pointer Handle);

		/**
		 * Open File
		 * 
		 * @param Handle Handle
		 * @param File File
		 * @return 1 if file was opened, 0 if file was not not opened
		 */
		int Open(Pointer Handle, WString File);

		/**
		 * Open Buffer Init
		 * 
		 * @param Handle Handle
		 * @param Length Length
		 * @param Offset Offset
		 * @return TODO Description
		 */
		int Open_Butter_Init(Pointer Handle, long Length, long Offset);

		/**
		 * Open Buffer Continue
		 * 
		 * @param Handle Handle
		 * @param Buffer Buffer
		 * @param Size Size
		 * @return TODO Description
		 */
		int Open_Buffer_Continue(Pointer Handle, byte[] Buffer, int Size);

		/**
		 * Open Buffer Continue GoTo Get
		 * 
		 * @param Handle Handle
		 * @return TODO Description
		 */
		long Open_Buffer_Continue_GoTo_Get(Pointer Handle);

		/**
		 * Open Buffer Finalize
		 * 
		 * @param Handle Handle
		 * @return TODO Description
		 */
		int Open_Buffer_Finalize(Pointer Handle);

		/**
		 * Close File
		 * 
		 * @param Handle Handle
		 */
		void Close(Pointer Handle);

		/**
		 * Get all detail information about a file in a String
		 * 
		 * @param Handle Handle
		 * @param Reserved Reserved
		 * @return All detail information about a file in a String
		 */
		WString Inform(Pointer Handle, int Reserved);

		/**
		 * Get a single information about a file (String parameter)
		 * 
		 * @param Handle Handle
		 * @param StreamKind Kind of Stream (general, video, audio, ...)
		 * @param StreamNumber Number in Kind of Stream (first, second, ...)
		 * @param Parameter Requested Parameter (Codec, Width, Bitrate, ...)
		 * @param InfoKind Kind for Information about the parameter (text, measure, help,...)
		 * @param SearchKind Where to look for parameter
		 * @return String containing the request information or empty String if there is a problem
		 */
		WString Get(Pointer Handle, int StreamKind, int StreamNumber, WString Parameter, int InfoKind, int SearchKind);

		/**
		 * Get a single information about a file (integer parameter)
		 * 
		 * @param Handle Handle
		 * @param StreamKind Kind of Stream (general, video, audio, ...)
		 * @param StreamNumber Number in Kind of Stream (first, second, ...)
		 * @param ParameterIndex Requested Parameter Index
		 * @param InfoKind Kind for Information about the parameter (text, measure, help,...)
		 * @return String containing the request information or empty String if there is a problem
		 */
		WString GetI(Pointer Handle, int StreamKind, int StreamNumber, int ParameterIndex, int InfoKind);

		/**
		 * Get number of Streams of the given Stream Kind
		 * 
		 * @param Handle Handle
		 * @param StreamKind Kind of Stream (general, video, audio, ...)
		 * @param StreamNumber Number in Kind of Stream (first, second, ...)
		 * @return Number of Streams of the given Stream Kind
		 */
		int Count_Get(Pointer Handle, int StreamKind, int StreamNumber);

		/**
		 * Configure or get information about MediaInfo
		 * 
		 * @param Handle Handle
		 * @param Option Option Name
		 * @param Value Value
		 * @return Depending on the option (By Default "" (nothing) means No, other values mean Yes)
		 */
		WString Option(Pointer Handle, WString Option, WString Value);
	}
}
