/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $PROACTIVE_INITIAL_DEV$
 */
package org.ow2.proactive.scheduler.core.db.schedulerType;

import java.sql.Types;


/**
 * Hibernate natively maps cloc columns to java.sql.Clob.<br />
 * However, it's sometimes useful to read the whole clob into memory and deal with it as a String array.<br />
 * CharacterLargeOBject is made to fix this issue.
 *
 * @author The ProActive Team
 * @since ProActive Scheduling 0.9.1
 */
public class CharacterLargeOBject extends BinaryLargeOBject {

    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class<?> returnedClass() {
        return String[].class;
    }

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y) {
        return (x == y) || (x != null && y != null && java.util.Arrays.equals((String[]) x, (String[]) y));
    }

    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) {
        if (value == null)
            return null;

        String[] strs = (String[]) value;
        String[] result = new String[strs.length];
        System.arraycopy(strs, 0, result, 0, strs.length);

        return result;
    }

}
