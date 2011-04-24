/**
 * Copyright (C) 2008-2010 Matt Gumbley, DevZendo.org <http://devzendo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devzendo.xpfsa;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.devzendo.commoncode.executor.IteratorExecutor;
import org.devzendo.commoncode.logging.LoggingUnittestHelper;
import org.devzendo.commoncode.os.OSTypeDetect;
import org.devzendo.commoncode.os.OSTypeDetect.OSType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


/**
 * Preconditions: Mac OS X file data has been set up in the
 * testdata subdir with src/test/sh/setupmacosxtestdata.sh
 * 
 * @author matt
 *
 */
public final class TestMacOSXFileMetadata {
    private static final Logger LOGGER = Logger
            .getLogger(TestMacOSXFileMetadata.class);
    
    private static OSType osType = OSTypeDetect.getInstance().getOSType();

    @BeforeClass
    public static void setupLogging() {
        LoggingUnittestHelper.setupLogging();
    }
    
    @Rule
    public final TemporaryFolder tempDir = new TemporaryFolder();
    private String mTestDir;

    /**
     * Create a temporary file to hold prefs data, that's deleted after
     * the JVM exits.
     * 
     * @throws IOException on failure
     */
    @Before
    public void setUp() throws IOException {
        Assume.assumeTrue(osType == OSType.MacOSX);

        tempDir.create();
        mTestDir = tempDir.getRoot().getAbsolutePath();
        final IteratorExecutor it = new IteratorExecutor(new String[] 
            {"/bin/sh", "src/test/sh/setupmacosxtestdata.sh", mTestDir});
        while (it.hasNext()) {
            LOGGER.info(">> " + it.next());
        }
        final int exitValue = it.getExitValue();
        MatcherAssert.assertThat(exitValue, Matchers.equalTo(0));
    }
    
    @Test
    public void directoryOwnershipObtained() {
        final File testDir = new File(mTestDir, "directory");
        MatcherAssert.assertThat("'directory' does not exist", testDir.exists(), Matchers.equalTo(true));
    }

    
}