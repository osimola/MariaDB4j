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
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

/**
 * Builder for DBConfiguration.
 * Has lot's of sensible default conventions etc.
 */
public class DBConfigurationBuilder {

	private String baseDir = SystemUtils.JAVA_IO_TMPDIR + "/MariaDB4j/base";
	private String dataDir = SystemUtils.JAVA_IO_TMPDIR + "/MariaDB4j/data";
	private String socket = SystemUtils.JAVA_IO_TMPDIR + "/MariaDB4j/mysql.sock";

	private int port = 3306; // this is just the default port - can be changed

	public static DBConfigurationBuilder newBuilder() {
		return new DBConfigurationBuilder();
	}

	protected DBConfigurationBuilder() {
	}

	public static final List<String> MYSQLD_BINARIES = Arrays.asList("bin/mysqld", "sbin/mysqld", "libexec/mysqld");
	public static final File[] basedirCandidates = {
		new File("/usr/local"),
		new File("/usr/")
	};

	@SuppressWarnings("unchecked")
	public static final List<List<String>> requiredFiles = Arrays.asList(
			Arrays.asList("bin/my_print_defaults"),
			Arrays.asList("bin/mysql_install_db"),
			Arrays.asList("bin/mysqlcheck"),
			MYSQLD_BINARIES);


	public String getBaseDir() {
		return baseDir;
	}

	// Todo: Windows directories?
	public void detectBaseDir() {
		for (File baseCandidate: basedirCandidates) {
			boolean found = true;
			for (List<String> filenames : requiredFiles) {
				boolean fileFound = false;
				for (String filename : filenames) {
					if (new File(baseCandidate, filename).exists()) {
						fileFound = true;
						break;
					}
				}
				if (!fileFound)
					found = false;
			}

			if (found) {
				setBaseDir(baseCandidate.getAbsolutePath());
				return;
			}
		}

		throw new RuntimeException("Could not find MariaDB / MySQL installation");
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public int getPort() {
		return port;
	}

	/**
	 * Sets the port number.
	 * @param port port number, or 0 to use detectFreePort()
	 */
	public void setPort(int port) {
	    if (port == 0) {
	    	detectFreePort();
	    } else {
	    	this.port = port;
	    	String portStr = String.valueOf(port);
	    	try {
				File socketFile = File.createTempFile("mariadb4j-" + portStr + "-", ".sock");
				socketFile.delete();
		    	setSocket(socketFile.getAbsolutePath());
			} catch (IOException e) {
				throw new RuntimeException("Error creating temp file");
			}
	    }
	}

	public void detectFreePort() {
		try {
			ServerSocket ss = new ServerSocket(0);
			setPort(ss.getLocalPort());
			ss.setReuseAddress(true);
			ss.close();
		} catch (IOException e) {
			// This should never happen
			throw new RuntimeException(e);
		}
	}

	public String getSocket() {
		return socket;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public DBConfiguration build() {
		return new DBConfiguration.Impl(port, socket, null, baseDir, dataDir);
	}
}
