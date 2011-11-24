/**
 * Copyright (C) 2011 dCache.org <support@dcache.org>
 *
 * This file is part of xrootd4j.
 *
 * xrootd4j is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xrootd4j is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with xrootd4j.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.dcache.xrootd.plugins;

import java.util.Map;
import java.net.InetSocketAddress;

import java.security.GeneralSecurityException;
import javax.security.auth.Subject;

import org.dcache.xrootd.protocol.XrootdProtocol.FilePerm;

/**
 * The interface to different authorization and path mapping plugins.
 *
 * @author radicke
 */
public interface AuthorizationHandler
{
   /**
     * Authorization and path mapping hook.
     *
     * Called upon any xrootd door operation.
     *
     * Implementations may perform authorization checks for the
     * requested operation.
     *
     * @param subject the user
     * @param requestId xrootd requestId of the operation
     * @param path the file which is checked
     * @param opaque the opaque data from the request
     * @param mode the requested mode
     * @param localAddress local socket address of client connection
     * @throws AccessControlException when the requested access is
     * denied
     * @throws GeneralSecurityException when the process of
     * authorizing fails
     */
    void check(Subject subject,
               int requestId,
               String path,
               Map<String,String> opaque,
               FilePerm mode,
               InetSocketAddress localAddress)
        throws SecurityException, GeneralSecurityException;

    /**
     * If authorization plugin provides the LFN-to-PFN-mapping, this
     * method will return the PFN.
     *
     * @return the PFN or null if no mapping is done by the underlying
     * authorization plugin.
     */
    String getPath();

    /**
     * Returns the authorized subject.
     *
     * SHOULD return null if authorized subject is the same as
     * provided to the check method. If not null then the caller may
     * have to reapply expensive internal mapping and authorization
     * rules.
     *
     * @return the subject or null if not supported
     */
    Subject getSubject();
}
