package com.bizcreator.util;

import java.io.Serializable;

public class KeyNamePair extends NamePair implements Serializable {
    /**
     *	Constructor KeyValue Pair -
     */
    public KeyNamePair(Object key, String name) {
    	super(name);
    	m_key = key;
    }

    /** The Key         */
    private Object m_key;
   

    /**
     *	Get Key
     */
    public Object getKey() {
        return m_key;
    } //	getKey

    /**
     *	Get ID (key as String)
     */
    public String getID() {
        if (m_key == null) {
            return null;
        }
        return m_key.toString();
    } //	getID
    
    public void setKey(Object key) {
    	m_key = key;
    }
   
    
    
    /**
     *	Equals
     */
    public boolean equals(Object obj) {
    	if (obj instanceof KeyNamePair) {
			KeyNamePair pp = (KeyNamePair)obj;
			if ( pp.getKey()!= null && pp.getKey().equals(m_key)
				&& pp.getName() != null
				&& pp.getName().equals(getName()))
				return true;
			return false;
		}
		return false;
    } //	equals

    /**
	 *  Return Hashcode of key
	 *  @return hascode
	 */
	public int hashCode() {
		return m_key.hashCode();
	}   //  hashCode

} //	KeyNamePair

