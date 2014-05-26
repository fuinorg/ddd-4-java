/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

/**
 * Meta data stored together with an event.
 */
@XmlRootElement(name = "event-meta-data")
public class EventMetaData implements MetaData, Serializable {

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
    private DateTime cmdCreated;

    @XmlAttribute(name = "cmd-received")
    private DateTime cmdReceived;

    /**
     * Protected default constructor for de-serialization.
     */
    protected EventMetaData() {
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
    public EventMetaData(final String remoteAddr, final Integer remotePort,
            final String remoteUser, final String localAddr,
            final Integer localPort, final String user,
            final DateTime cmdCreated, final DateTime cmdReceived) {
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
    public final DateTime getCmdCreated() {
        return cmdCreated;
    }

    /**
     * Returns time the command was received by the command handler.
     * 
     * @return Date/Time of command receipt.
     */
    public final DateTime getCmdReceived() {
        return cmdReceived;
    }

    @Override
    public final String getType() {
        return getClass().getSimpleName();
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((cmdCreated == null) ? 0 : cmdCreated.hashCode());
        result = prime * result
                + ((cmdReceived == null) ? 0 : cmdReceived.hashCode());
        result = prime * result
                + ((localAddr == null) ? 0 : localAddr.hashCode());
        result = prime * result
                + ((localPort == null) ? 0 : localPort.hashCode());
        result = prime * result
                + ((remoteAddr == null) ? 0 : remoteAddr.hashCode());
        result = prime * result
                + ((remotePort == null) ? 0 : remotePort.hashCode());
        result = prime * result
                + ((remoteUser == null) ? 0 : remoteUser.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventMetaData other = (EventMetaData) obj;
        if (cmdCreated == null) {
            if (other.cmdCreated != null)
                return false;
        } else if (!cmdCreated.equals(other.cmdCreated))
            return false;
        if (cmdReceived == null) {
            if (other.cmdReceived != null)
                return false;
        } else if (!cmdReceived.equals(other.cmdReceived))
            return false;
        if (localAddr == null) {
            if (other.localAddr != null)
                return false;
        } else if (!localAddr.equals(other.localAddr))
            return false;
        if (localPort == null) {
            if (other.localPort != null)
                return false;
        } else if (!localPort.equals(other.localPort))
            return false;
        if (remoteAddr == null) {
            if (other.remoteAddr != null)
                return false;
        } else if (!remoteAddr.equals(other.remoteAddr))
            return false;
        if (remotePort == null) {
            if (other.remotePort != null)
                return false;
        } else if (!remotePort.equals(other.remotePort))
            return false;
        if (remoteUser == null) {
            if (other.remoteUser != null)
                return false;
        } else if (!remoteUser.equals(other.remoteUser))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }
    // CHECKSTYLE:ON

}
