/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlAttribute;

import org.fuin.objects4j.vo.ValueObject;

/**
 * Base class for typical meta data stored together with an event.
 */
public abstract class AbstractEventMetaData implements MetaData, ValueObject,
        Serializable {

    private static final long serialVersionUID = 1000L;

    @XmlAttribute(name = "remote-addr")
    private String remoteAddr;

    @XmlAttribute(name = "remote-port")
    private Integer remotePort;

    @XmlAttribute(name = "remote-user")
    private String remoteUser;

    @XmlAttribute(name = "local-addr")
    private String localAddr;

    @XmlAttribute(name = "local-port")
    private Integer localPort;

    @XmlAttribute(name = "user")
    private String user;

    @XmlAttribute(name = "cmd-created")
    private ZonedDateTime cmdCreated;

    @XmlAttribute(name = "cmd-received")
    private ZonedDateTime cmdReceived;

    /**
     * Protected default constructor for de-serialization.
     */
    protected AbstractEventMetaData() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param remoteAddr
     *            IP from the HTTP header.
     * @param remotePort
     *            Port from the HTTP header.
     * @param remoteUser
     *            User from the HTTP header.
     * @param localAddr
     *            Local IP address from HTTP request.
     * @param localPort
     *            Local port from HTTP request.
     * @param user
     *            Authenticated user name.
     * @param cmdCreated
     *            Date/Time the command was created.
     * @param cmdReceived
     *            Date/Time the command was received by the command handler.
     */
    // CHECKSTYLE:OFF More than 7 args is OK here
    public AbstractEventMetaData(final String remoteAddr,
            final Integer remotePort, final String remoteUser,
            final String localAddr, final Integer localPort, final String user,
            final ZonedDateTime cmdCreated, final ZonedDateTime cmdReceived) {
        // CHECKSTYLE:ON
        super();
        this.remoteAddr = remoteAddr;
        this.remotePort = remotePort;
        this.remoteUser = remoteUser;
        this.localAddr = localAddr;
        this.localPort = localPort;
        this.user = user;
        this.cmdCreated = cmdCreated;
        this.cmdReceived = cmdReceived;
    }

    /**
     * Returns the remote address.
     * 
     * @return IP from the HTTP header.
     */
    public final String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * Returns the remote port.
     * 
     * @return Port from the HTTP header.
     */
    public final Integer getRemotePort() {
        return remotePort;
    }

    /**
     * Returns the remote user.
     * 
     * @return User from the HTTP header.
     */
    public final String getRemoteUser() {
        return remoteUser;
    }

    /**
     * Returns the local address.
     * 
     * @return Local IP address.
     */
    public final String getLocalAddr() {
        return localAddr;
    }

    /**
     * Returns the local port.
     * 
     * @return Local port.
     */
    public final Integer getLocalPort() {
        return localPort;
    }

    /**
     * Returns the authenticated user.
     * 
     * @return User name.
     */
    public final String getUser() {
        return user;
    }

    /**
     * Returns time the command was created.
     * 
     * @return Date/Time of command creation.
     */
    public final ZonedDateTime getCmdCreated() {
        return cmdCreated;
    }

    /**
     * Returns time the command was received by the command handler.
     * 
     * @return Date/Time of command receipt.
     */
    public final ZonedDateTime getCmdReceived() {
        return cmdReceived;
    }

}
