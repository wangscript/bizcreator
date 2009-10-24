/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.collection;

import com.bizcreator.core.hibernate.proxy.LazyCollectionReference;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Set;
import org.hibernate.collection.PersistentSet;
import org.hibernate.engine.SessionImplementor;

/**
 *
 * @author Administrator
 */
public class RemotablePersistentSet extends PersistentSet implements Remotable {//

    public RemotablePersistentSet(SessionImplementor session, Set set) {
        super(session, set);
    }

    public RemotablePersistentSet(SessionImplementor session) {
        super(session);
    }

    public Collection unwrap() {
        read();
        return set;
    }

    Object writeReplace() throws ObjectStreamException {

        if (wasInitialized()) {
            return set;
        }

        LazyCollectionReference ref = new LazyCollectionReference();
        ref.setId(getKey());
        ref.setRole(getRole());
        ref.setCollectionType("set");
        return ref;

    }
}
