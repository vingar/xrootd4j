/**
 * Copyright (C) 2011-2014 dCache.org <support@dcache.org>
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
package org.dcache.xrootd.protocol.messages;
import org.dcache.xrootd.protocol.XrootdProtocol;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectResponse extends AbstractXrootdResponse
{
    private static final Logger _logger =
        LoggerFactory.getLogger(RedirectResponse.class);

    private final String host;
    private final int port;
    private final String opaque;
    private final String token;

    public RedirectResponse(XrootdRequest request, String host, int port)
    {
        this(request, host, port, "", "");
    }

    public RedirectResponse(XrootdRequest request, String host, int port, String opaque, String token)
    {
        super(request, XrootdProtocol.kXR_redirect);

        this.host = host;
        this.port = port;
        this.opaque = opaque;
        this.token = token;

        _logger.info("Sending the following host information to the client: {}", host);
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public String getOpaque()
    {
        return opaque;
    }

    public String getToken()
    {
        return token;
    }

    @Override
    protected int getLength()
    {
        return super.getLength() + 4 + host.length() + opaque.length() + token.length() + 2;
    }

    @Override
    protected void getBytes(ByteBuf buffer)
    {
        super.getBytes(buffer);
        buffer.writeInt(port);
        buffer.writeBytes(host.getBytes(Charsets.US_ASCII));

        if (!opaque.isEmpty()) {
            buffer.writeByte('?');
            buffer.writeBytes(opaque.getBytes(Charsets.US_ASCII));
        }

        if (!token.isEmpty()) {
            if (opaque.isEmpty()) {
                buffer.writeByte('?');
            }

            buffer.writeByte('?');
            buffer.writeBytes(token.getBytes(Charsets.US_ASCII));
        }
    }

    @Override
    public String toString()
    {
        return String.format("redirect[%s:%d,%s,%s]", host, port, opaque, token);
    }
}
