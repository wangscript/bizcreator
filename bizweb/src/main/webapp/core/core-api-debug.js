/**********************************************************************
 * 
 * Code generated automatically by DirectJNgine
 * Copyright (c) 2009, Pedro Agull¨® Soliveres
 * 
 * DO NOT MODIFY MANUALLY!!
 * 
 **********************************************************************/

Ext.namespace( 'Biz.core');

Biz.core.PROVIDER_BASE_URL=window.location.protocol + '//' + window.location.host + '/' + (window.location.pathname.split('/').length>2 ? window.location.pathname.split('/')[1]+ '/' : '')  + 'djn/directprovider';

Biz.core.POLLING_URLS = {
}

Biz.core.REMOTING_API = {
  url: Biz.core.PROVIDER_BASE_URL,
  type: 'remoting',
  actions: {
    JdbcAction: [
      {
        name: 'find'/*(String, Object) => java.util.Map */,
        len: 2,
        formHandler: false
      }
    ]
  }
}

