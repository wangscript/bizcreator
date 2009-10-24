/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.collection;

import com.bizcreator.core.hibernate.proxy.LazyCollectionReference;
import java.io.ObjectStreamException;
import java.util.Map;
import org.hibernate.collection.PersistentMap;
import org.hibernate.engine.SessionImplementor;

/**
 *
 * @author Administrator
 */
public class RemotablePersistentMap extends PersistentMap implements Remotable {
    
    public RemotablePersistentMap(SessionImplementor session, Map map) {
        super(session, map);
    }
    
    public RemotablePersistentMap(SessionImplementor session) {
		super(session);
	}
    
    public Object unwrap() {
        read();
        return map;
    }
    
    Object writeReplace() throws ObjectStreamException {

        if (wasInitialized()) {
            return map;
        }

        LazyCollectionReference ref = new LazyCollectionReference();
        ref.setId(getKey());
        ref.setRole(getRole());
        ref.setCollectionType("map");
        return ref;

    }

}
