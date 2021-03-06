/** Copyright 2014 Filip Bogyai
 *
 * This file is part of certificate-tracker.
 *
 * Certificate-tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.jboss.certificate.tracker.client.dogtag;

import java.math.BigInteger;

/**
 * Class representing the identifier for a particular certificate record. This
 * identifier may be used to retrieve the certificate from the database.
 * 
 * @author Endi S. Dewata
 */
public class CertId {

    protected BigInteger value;

    /**
     * Creates a new CertId from its string representation.
     * 
     * @param id a string containing the decimal or hex value for the identifier.
     */
    public CertId(String id) {
        if (id != null) {
            id = id.trim();
            if (id.startsWith("0x")) { // hex
                value = new BigInteger(id.substring(2), 16);
            } else { // decimal
                value = new BigInteger(id);
            }
        }
    }

    /**
     * Creates a new CertId from its BigInteger representation.
     * 
     * @param id a BigInteger containing the identifier.
     */
    public CertId(BigInteger id) {
        value = id;
    }

    /**
     * Creates a new CertId from its integer representation.
     * 
     * @param id an integer containing the identifier.
     */
    public CertId(int id) {
        value = BigInteger.valueOf(id);
    }

    /**
     * Converts the CertId into its BigInteger representation.
     * 
     * @return a BigInteger containing the identifier.
     */
    public BigInteger toBigInteger() {
        return value;
    }

    /**
     * Converts the CertId into its string representation. The string form can
     * be stored in a database (such as the LDAP directory)
     * 
     * @return a string containing the decimal (base 10) value for the
     *         identifier.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Converts the CertId into its hex string representation. The string form
     * can be stored in a database (such as the LDAP directory)
     * 
     * @return a string containing the hex (hex 16) value for the identifier.
     */
    public String toHexString() {
        return "0x" + value.toString(16);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        CertId other = (CertId) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
