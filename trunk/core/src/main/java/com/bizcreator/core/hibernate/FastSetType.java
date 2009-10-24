/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate;

import java.util.*;
//import javolution.util.FastSet;
import org.hibernate.HibernateException;
import org.hibernate.collection.*;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;
 
public class FastSetType implements UserCollectionType {
 
	public FastSetType() {		
	}
 
	// could be common for all collection implementations.
	public boolean contains(Object collection, Object obj) {
		Set set = (Set)collection;
		return set.contains(obj);
	}
	
	// could be common for all collection implementations.
	public Iterator getElementsIterator(Object collection) {
		return ((Set)collection).iterator();
	}
	
	// common for list-like collections.
	public Object indexOf(Object collection, Object obj) {
		return null;
	}
	// factory method for certain collection type.
	public Object instantiate() {
		return new HashSet();
	}
	
	// standard wrapper for collection type.
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) throws HibernateException {
		// Use hibernate's built in persistent set implementation
		//wrapper
		return new PersistentSet(session);
	}
	
	// could be common implementation for all collection implementations
	public Object replaceElements(Object collectionA, Object collectionB,
		CollectionPersister persister, Object owner,
		Map copyCache, SessionImplementor implementor) throws HibernateException {
        
		Set setA = (Set)collectionA;
		Set setB = (Set)collectionB;
		setB.clear();
		setB.addAll(setA);
        
        return setB;
	}
	
	// standard wrapper for collection type.
	public PersistentCollection wrap(SessionImplementor session, Object colllection) {
		// Use hibernate's built in persistent set implementation
		//wrapper.
		return new PersistentSet(session, (Set)colllection);
	}

   

    public Object instantiate(int size) {
        return new HashSet(8);
    }

   
}
