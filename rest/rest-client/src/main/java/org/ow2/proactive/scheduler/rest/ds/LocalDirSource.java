/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ActiveEon Team
 *                        http://www.activeeon.com/
 *  Contributor(s):
 *
 * ################################################################
 * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.ow2.proactive.scheduler.rest.ds;

import com.google.common.collect.Lists;
import org.ow2.proactive.scheduler.rest.ds.IDataSpaceClient.ILocalSource;
import org.ow2.proactive_grid_cloud_portal.scheduler.client.utils.Zipper;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class LocalDirSource implements ILocalSource {

    private File source;
    private List<String> includes;
    private List<String> excludes;

    public LocalDirSource(File dir) {
        checkArgument(dir.isDirectory());
        source = dir;
    }

    public LocalDirSource(String path) {
        this(new File(path));
    }

    public void setIncludes(List<String> includes) {
        checkNotNull(includes);
        this.includes = Lists.newArrayList(includes);
    }

    public void setIncludes(String... includes) {
        checkNotNull(includes);
        this.includes = Lists.newArrayList(includes);
    }

    public void setExcludes(List<String> excludes) {
        checkNotNull(excludes);
        this.excludes = Lists.newArrayList(excludes);
    }

    public void setExcludes(String... excludes) {
        checkNotNull(excludes);
        this.excludes = Lists.newArrayList(excludes);
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        Zipper.ZIP.zip(source, includes, excludes, outputStream);
    }

    @Override
    public String getEncoding() throws IOException {
        return "zip";
    }

    @Override
    public String toString() {
        return "LocalDirSource{" +
                "source=" + source +
                ", includes=" + includes +
                ", excludes=" + excludes +
                '}';
    }

}
