Remote Lazy Loading

1) 服务器端的Hibernate生成LazyEntityReference到客户端;
2) 客户端通过LazyEntityReference.readResolve方法生成一个CGLIB代理(在EntityProxy中)
3) 在客户端EntityProxy向服务器端序列化时, 如果还未取得过entity时, 则直接序列化LazyEntityReference到服务器端;
4) 服务器端得到LazyEntityReference时, 直接通过LazyLoading从数据库中读取对应的entity来替换;
重点类: EntityProxy, LazyEntityReference;

该包下的类为了swing客户端中实现远程的Lazy loading, 同时对Hibernate 3.2.5做了如下修改:
1. 在hibernate.properties中增加下列属性:
########################################
### Settings for remote lazy loading ###
########################################

## define client type
hibernate.client_type swing
hibernate.proxy_factory com.rhinofield.base.hibernate.remoting.RemotableProxyFactory
hibernate.set_class com.rhinofield.base.hibernate.collection.RemotablePersistentSet
hibernate.list_class com.rhinofield.base.hibernate.collection.RemotablePersistentList
hibernate.bag_class com.rhinofield.base.hibernate.collection.RemotablePersistentBag

2. 修改org.hibernate.tuple.entity.PojoEntityTuplizer.buildProxyFactoryInternal()方法如下:
    protected ProxyFactory buildProxyFactoryInternal(PersistentClass persistentClass, Getter idGetter, Setter idSetter) {
		// TODO : YUCK!!!  finx after HHH-1907 is complete
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String proxyFactoryClassName = PropertiesHelper.getString("hibernate.proxy_factory", props, null);
        
        if ("swing".equals(clientType) && proxyFactoryClassName != null) {
            try {
                Class factoryClass = Class.forName(proxyFactoryClassName);
                return (ProxyFactory) factoryClass.newInstance();
            } catch (Exception ex) {
                return Environment.getBytecodeProvider().getProxyFactoryFactory().buildProxyFactory();
            }
        }
        else {
            return Environment.getBytecodeProvider().getProxyFactoryFactory().buildProxyFactory();
        }
//		return getFactory().getSettings().getBytecodeProvider().getProxyFactoryFactory().buildProxyFactory();
	}

3. 为了使用RemotablePersistentList, 修改org.hibernate.type.ListType.instantiate方法如下:
    public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) {
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String listClassName = PropertiesHelper.getString("hibernate.list_class", props, null);
        
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentListElementHolder(session, persister, key);
		}
        else if ("swing".equals(clientType) && listClassName != null) {
            try {
                Class listClass = Class.forName(listClassName);
                Constructor constructor = listClass.getConstructor(new Class[] {SessionImplementor.class});
                return (PersistentList) constructor.newInstance(new Object[]{session});
            } catch (Exception ex) {
                return new PersistentList(session);
            }
        }
		else {
			return new PersistentList(session);
		}
	}
    
    public PersistentCollection wrap(SessionImplementor session, Object collection) {
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String listClassName = PropertiesHelper.getString("hibernate.list_class", props, null);
        
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentListElementHolder( session, (Element) collection );
		}
        else if ("swing".equals(clientType) && listClassName != null) {
            try {
                Class listClass = Class.forName(listClassName);
                Constructor constructor = listClass.getConstructor(new Class[] {SessionImplementor.class, List.class});
                return (PersistentList) constructor.newInstance(new Object[]{session, (List) collection});
            } catch (Exception ex) {
                return new PersistentList(session, (List) collection);
            }
        }
		else {
			return new PersistentList( session, (List) collection );
		}
	}

4. 为了使用RemotablePersistentSet, 修改org.hibernate.type.SetType如下方法如下:
    public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) {
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String setClassName = PropertiesHelper.getString("hibernate.set_class", props, null);
        
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder(session, persister, key);
		}
        else if ("swing".equals(clientType) && setClassName != null) {
            try {
                Class setClass = Class.forName(setClassName);
                Constructor constructor = setClass.getConstructor(new Class[] {SessionImplementor.class});
                return (PersistentSet) constructor.newInstance(new Object[]{session});
            } catch (Exception ex) {
                return new PersistentSet(session);
            }
        }
		else {
			return new PersistentSet(session);
		}
	}

    public PersistentCollection wrap(SessionImplementor session, Object collection) {
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String setClassName = PropertiesHelper.getString("hibernate.set_class", props, null);
        
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder( session, (Element) collection );
		}
        else if ("swing".equals(clientType) && setClassName != null) {
            try {
                Class setClass = Class.forName(setClassName);
                Constructor constructor = setClass.getConstructor(new Class[] {SessionImplementor.class, java.util.Set.class});
                return (PersistentSet) constructor.newInstance(new Object[]{session, collection});
            } catch (Exception ex) {
                return new PersistentSet(session, (java.util.Set) collection);
            }
        }
		else {
			return new PersistentSet( session, (java.util.Set) collection );
		}
	}

5. 为了使用RemotablePersistentBag, 修改org.hibernate.type.BagType如下方法如下:
public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key)
	throws HibernateException {
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String bagClassName = PropertiesHelper.getString("hibernate.bag_class", props, null);
        
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder(session, persister, key);
		}
        else if ("swing".equals(clientType) && bagClassName != null) {
            try {
                Class bagClass = Class.forName(bagClassName);
                Constructor constructor = bagClass.getConstructor(new Class[] {SessionImplementor.class});
                return (PersistentBag) constructor.newInstance(new Object[]{session});
            } catch (Exception ex) {
                return new PersistentBag(session);
            }
        }
		else {
			return new PersistentBag(session);
		}
	}

	public Class getReturnedClass() {
		return java.util.Collection.class;
	}

	public PersistentCollection wrap(SessionImplementor session, Object collection) {
        Properties props = Environment.getProperties();
        String clientType = PropertiesHelper.getString("hibernate.client_type", props, "web");
        String bagClassName = PropertiesHelper.getString("hibernate.bag_class", props, null);
        
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder( session, (Element) collection );
		}
        else if ("swing".equals(clientType) && bagClassName != null) {
            try {
                Class bagClass = Class.forName(bagClassName);
                Constructor constructor = bagClass.getConstructor(new Class[] {SessionImplementor.class, Collection.class});
                return (PersistentBag) constructor.newInstance(new Object[]{session, (Collection) collection});
            } catch (Exception ex) {
                return new PersistentBag(session, (Collection) collection);
            }
        }
		else {
			return new PersistentBag( session, (Collection) collection );
		}
	}
