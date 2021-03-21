# VideoPreview
Program for creating preview images of video files

OpenCV is used to create thumbnails and the complete preview image.

MediaInfo is used to retrieve information about the file.

Velocity Engine is used to create the info text, which is displayed on the preview image.
This allows customized information to be displayed, by creating your own templates.

# Compatibility
The Program was so far only tested on Windows 7 and Windows 10.
The Program might not work on Linux, Mac or other operating systems

# Install MediaInfo Libraries under Windows
Download MediaInfo libraries and place them into the program root folder:
MediaInfo.dll
MediaInfox64.dll

# Install OpenCV Libraries under Windows
Download OpenCV for Windows from https://opencv.org/releases/ and extract it.
Create the following folder structure and copy files from the extracted OpenCV:
+-- OpenCV
|   +-- bin
|   |   +-- opencv_videoio_ffmpeg451_64.dll
|   +-- etc
|   |   +-- haarcascades
|   |   |   +-- *.*
|   |   +-- lbpcascades
|   |   |   +-- *.*
|   |   +-- licenses
|   |   |   +-- *.*
|   +-- java
|   |   +-- opencv-451.jar
|   |   +-- x64
|   |   |   +-- opencv_java451.dll
|   |   +-- x86
|   |   |   +-- opencv_java451.dll

# Requirements
Java 8 is required.

# License
This project is licensed under the GNU General Public License version 3 (or later).

This product bundles slf4j-api 1.7.25, which is available under a "MIT Software License" license. For details, see http://www.slf4j.org

This product bundles tango-icon-theme 0.8.90, which is in the public domain. For details, see http://tango.freedesktop.org

This product bundles jna 5.3.1, which is available under a "Apache-2.0" License. For details, see https://github.com/java-native-access/jna

This product bundles jna-platform 5.3.1, which is available under a "Apache-2.0" License. For details, see https://github.com/java-native-access/jna

TODO Add missing license information
