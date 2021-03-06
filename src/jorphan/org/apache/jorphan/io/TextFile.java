/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jorphan.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to handle text files as a single lump of text.
 * <p>
 * Note this is just as memory-inefficient as handling a text file can be. Use
 * with restraint.
 *
 */
public class TextFile extends File {
    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggerFactory.getLogger(TextFile.class);

    /**
     * File encoding. null means use the platform's default.
     */
    private String encoding = null;

    /**
     * Create a TextFile object to handle the named file with the given
     * encoding.
     *
     * @param filename
     *            File to be read and written through this object.
     * @param encoding
     *            Encoding to be used when reading and writing this file.
     */
    public TextFile(File filename, String encoding) {
        super(filename.toString());
        setEncoding(encoding);
    }

    /**
     * Create a TextFile object to handle the named file with the platform
     * default encoding.
     *
     * @param filename
     *            File to be read and written through this object.
     */
    public TextFile(File filename) {
        super(filename.toString());
    }

    /**
     * Create a TextFile object to handle the named file with the platform
     * default encoding.
     *
     * @param filename
     *            Name of the file to be read and written through this object.
     */
    public TextFile(String filename) {
        super(filename);
    }

    /**
     * Create a TextFile object to handle the named file with the given
     * encoding.
     *
     * @param filename
     *            Name of the file to be read and written through this object.
     * @param encoding
     *            Encoding to be used when reading and writing this file.
     */
    public TextFile(String filename, String encoding) {
        super(filename);
        setEncoding(encoding);
    }

    /**
     * Create the file with the given string as content -- or replace it's
     * content with the given string if the file already existed.
     *
     * @param body
     *            New content for the file.
     */
    public void setText(String body) {
        Writer writer = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(this);
            if (encoding == null) {
                writer = new OutputStreamWriter(outputStream);
            } else {
                writer = new OutputStreamWriter(outputStream, encoding);
            }
            writer.write(body);
            writer.flush();
        } catch (IOException ioe) {
            log.error("", ioe);
        } finally {
            JOrphanUtils.closeQuietly(writer);
            JOrphanUtils.closeQuietly(outputStream);
        }
    }

    /**
     * Read the whole file content and return it as a string.
     *
     * @return the content of the file
     */
    public String getText() {
        String lineEnd = System.getProperty("line.separator"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        Reader reader = null;
        BufferedReader br = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(this);
            if (encoding == null) {
                reader = new InputStreamReader(fileInputStream);                
            } else {
                reader = new InputStreamReader(fileInputStream, encoding);
            }
            br = new BufferedReader(reader);
            String line = "NOTNULL"; //$NON-NLS-1$
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    sb.append(line + lineEnd);
                }
            }
        } catch (IOException ioe) {
            log.error("", ioe); //$NON-NLS-1$
        } finally {
            JOrphanUtils.closeQuietly(br);
            JOrphanUtils.closeQuietly(reader); 
            JOrphanUtils.closeQuietly(fileInputStream); 
        }

        return sb.toString();
    }

    /**
     * @return Encoding being used to read and write this file.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param string
     *            Encoding to be used to read and write this file.
     */
    public void setEncoding(String string) {
        encoding = string;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((encoding == null) ? 0 : encoding.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof TextFile)) {
            return false;
        }
        TextFile other = (TextFile) obj;
        if (encoding == null) {
            return other.encoding == null;
        } 
        return encoding.equals(other.encoding);
    }
}
