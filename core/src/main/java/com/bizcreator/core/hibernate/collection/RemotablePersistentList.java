/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.collection;

import org.hibernate.collection.PersistentList;
import com.bizcreator.core.hibernate.proxy.LazyCollectionReference;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.List;
import org.hibernate.engine.SessionImplementor;
/**
 *
 * @author Administrator
 */
public class RemotablePersistentList extends PersistentList implements Remotable {//
    
    public RemotablePersistentList(SessionImplementor session, List list) {
        super(session, list);
    }

    public RemotablePersistentList(SessionImplementor session) {
        super(session);
    }

    public Collection unwrap() {
        read();
        return list;
    }

    Object writeReplace() throws ObjectStreamException {

        if (wasInitialized()) {
            return list;
        }

        LazyCollectionReference ref = new LazyCollectionReference();
        ref.setId(getKey());
        ref.setRole(getRole());
        ref.setCollectionType("list");
        return ref;
    }
}
