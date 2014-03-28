/*
 * Copyright (c) 2012 Michael Vorburger
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */
package ch.vorburger.mariadb4j;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File utilities
 * @author Michael Vorburger
 * @author Michael Seaton
 */
public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	/**
	 * Retrieve the directory located at the given path.
	 * If this does not exist, create it
	 * If it is not a directory, or it is not readable, throw an Exception
	 */
	public static File getDirectory(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			try {
				FileUtils.forceMkdir(dir);
			}
			catch (IOException e) {
				throw new IllegalArgumentException("Unable to create new directory at path: " + path, e);
			}
		}
		if (dir.getAbsolutePath().trim().length() == 0) {
			throw new IllegalArgumentException(path + " is empty");
		}
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException(path + " is not a directory");
		}
		if (!dir.canRead()) {
			throw new IllegalArgumentException(path + " is not a readable directory");
		}
		return dir;
	}

	/**
	 * @return true if the passed directory is within the system temporary directory
	 */
	public static boolean isTemporaryDirectory(String directory) {
		return directory.startsWith(SystemUtils.JAVA_IO_TMPDIR);
	}
}
