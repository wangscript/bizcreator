/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.collection;

import com.bizcreator.core.hibernate.proxy.LazyCollectionReference;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.List;
import org.hibernate.collection.PersistentBag;
import org.hibernate.engine.SessionImplementor;

/**
 *
 * @author Administrator
 */
public class RemotablePersistentBag extends PersistentBag implements Remotable {//
    
    public RemotablePersistentBag(SessionImplementor session, Collection coll) {
        super(session, coll);
    }

    public RemotablePersistentBag(SessionImplementor session) {
        super(session);
    }

    public Collection unwrap() {
        read();
        return bag;
    }

    Object writeReplace() throws ObjectStreamException {

        if (wasInitialized()) {
            return bag;
        }

        LazyCollectionReference ref = new LazyCollectionReference();
        ref.setId(getKey());
        ref.setRole(getRole());
        ref.setCollectionType("bag");
        return ref;
    }
}
