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

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Meta data stored together with an event.
 */
@XmlRootElement(name = "basic-event-meta-data")
public class BasicEventMetaData extends AbstractEventMetaData {

    private static final long serialVersionUID = 1000L;

    /** Never changing unique name of the type. */
    public static final String TYPE = BasicEventMetaData.class.getSimpleName();

    /**
     * Protected default constructor for de-serialization.
     */
    protected BasicEventMetaData() {
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
    public BasicEventMetaData(final String remoteAddr,
            final Integer remotePort, final String remoteUser,
            final String localAddr, final Integer localPort, final String user,
            final ZonedDateTime cmdCreated, final ZonedDateTime cmdReceived) {
        // CHECKSTYLE:ON
        super(remoteAddr, remotePort, remoteUser, localAddr, localPort, user,
                cmdCreated, cmdReceived);
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((getCmdCreated() == null) ? 0 : getCmdCreated().hashCode());
        result = prime
                * result
                + ((getCmdReceived() == null) ? 0 : getCmdReceived().hashCode());
        result = prime * result
                + ((getLocalAddr() == null) ? 0 : getLocalAddr().hashCode());
        result = prime * result
                + ((getLocalPort() == null) ? 0 : getLocalPort().hashCode());
        result = prime * result
                + ((getRemoteAddr() == null) ? 0 : getRemoteAddr().hashCode());
        result = prime * result
                + ((getRemotePort() == null) ? 0 : getRemotePort().hashCode());
        result = prime * result
                + ((getRemoteUser() == null) ? 0 : getRemoteUser().hashCode());
        result = prime * result
                + ((getUser() == null) ? 0 : getUser().hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEventMetaData other = (AbstractEventMetaData) obj;
        if (getCmdCreated() == null) {
            if (other.getCmdCreated() != null)
                return false;
        } else if (!getCmdCreated().equals(other.getCmdCreated()))
            return false;
        if (getCmdReceived() == null) {
            if (other.getCmdReceived() != null)
                return false;
        } else if (!getCmdReceived().equals(other.getCmdReceived()))
            return false;
        if (getLocalAddr() == null) {
            if (other.getLocalAddr() != null)
                return false;
        } else if (!getLocalAddr().equals(other.getLocalAddr()))
            return false;
        if (getLocalPort() == null) {
            if (other.getLocalPort() != null)
                return false;
        } else if (!getLocalPort().equals(other.getLocalPort()))
            return false;
        if (getRemoteAddr() == null) {
            if (other.getRemoteAddr() != null)
                return false;
        } else if (!getRemoteAddr().equals(other.getRemoteAddr()))
            return false;
        if (getRemotePort() == null) {
            if (other.getRemotePort() != null)
                return false;
        } else if (!getRemotePort().equals(other.getRemotePort()))
            return false;
        if (getRemoteUser() == null) {
            if (other.getRemoteUser() != null)
                return false;
        } else if (!getRemoteUser().equals(other.getRemoteUser()))
            return false;
        if (getUser() == null) {
            if (other.getUser() != null)
                return false;
        } else if (!getUser().equals(other.getUser()))
            return false;
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public final String getType() {
        return getClass().getSimpleName();
    }

}
