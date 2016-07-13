/** PersonKey.java -
* @version      $Name$
* @module       my.apache.ignite.examples.collocation
* 
* @purpose
* @see
*
* @author   Kamal (kamal@nmsworks.co.in)
*
* @created  Jun 7, 2016
* $Id$
*
* @bugs
*
* Copyright 2016-2017 NMSWorks Software Pvt Ltd. All rights reserved.
* NMSWorks PROPRIETARY/CONFIDENTIAL. Use is subject to licence terms.
*/ 

package my.apache.ignite.examples.collocation;

import java.io.Serializable;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class PersonKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
	@AffinityKeyMapped
	private int companyId;
	
	public PersonKey() {
	}
	
	public PersonKey(String name) {
		this.name = name;
	}
	
	public PersonKey(String name, int companyId) {
		this.name = name;
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "PersonKey [name=" + name + ", companyId=" + companyId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PersonKey other = (PersonKey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}


/**
 * $Log$
 *  
 */
