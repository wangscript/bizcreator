
//Ext.namespace('BIZ', 'BIZ.wui', 'BIZ.base');

Env = {contextPath: '/bizweb', invokePath: '/bizweb/BsfInvokerServlet'};

var BIZ = {};

BIZ.util = {
    isString: function(o) {
        return typeof o === 'string';
    },
    isNumber: function(o) {
        return typeof o === 'number' && isFinite(o);
    },
    isInt: function(obj) {
        if (!this.isNumber(obj)) return false;
        return obj.toString().indexOf(".")==-1;
    },
    isFloat: function(obj) {
        if (!this.isNumber(obj)) return false;
        return obj.toString().indexOf(".")>-1;
    },
    isBoolean: function(o) {
        return typeof o === 'boolean';
    },
    isPrimitive : function(o){
        var t = typeof o;
        return t == 'string' || t == 'number' || t == 'boolean';
    },
    isDate: function(obj){
        return (typeof obj == 'object') && obj.constructor==Date;
    },
    isArray: function(o) {
        return Object.prototype.toString.apply(o) === '[object Array]';
    },
    isObject: function(o) {
        return (o && (typeof o === 'object' || this.isFunction(o))) || false;
    },
    isFunction: function(o) {
        return Object.prototype.toString.apply(o) === '[object Function]';
    },
    isNull: function(o) {
        return o === null;
    },
    isUndefined: function(o) {
        return typeof o === 'undefined';
    },

    getTypeName: function(obj) {
        if (this.isString(obj)) return 'String';
        else if (this.isInt(obj)) return 'Integer';
        else if (this.isFloat(obj)) return 'Float';
        else if (this.isBoolean(obj)) return 'Boolean';
        else if (this.isDate(obj)) return 'Date';
        else if (this.isArray(obj)) return 'Array';
        else if (this.isFunction(obj)) return 'Function';
        else if (this.isObject(obj)) {
            if (obj.__TYPE__) {
                //obj.__TYPE__是服务器端对应的类型，如：com.kingdee.youshang.gl.Account
                return obj.__TYPE__
            }
            else {
                return 'Object';
            }
        }
        else return 'undefined';
    },
    //压缩列表，消除相同的项
    reduceList: function(a) {
        if (a.length>1) {
            var prev = a[0];
            for (var i=1; i<a.length; i++) {
                if (a[i] == prev) {
                    a.splice(i, 1);
                }
                prev = a[i];
            }
        }
        return a;
    }
};


Ext.lib.Sjax = function() {
    var activeX = ['MSXML2.XMLHTTP.3.0',
    'MSXML2.XMLHTTP',
    'Microsoft.XMLHTTP'];

    // private
    function setHeader(o) {
        var conn = o.conn,
        prop;

        function setTheHeaders(conn, headers){
            for (prop in headers) {
                if (headers.hasOwnProperty(prop)) {
                    conn.setRequestHeader(prop, headers[prop]);
                }
            }
        }

        if (pub.defaultHeaders) {
            setTheHeaders(conn, pub.defaultHeaders);
        }

        if (pub.headers) {
            setTheHeaders(conn, pub.headers);
            pub.headers = null;
        }
    }

    // private
    function initHeader(label, value) {
        (pub.headers = pub.headers || {})[label] = value;
    }

    // private
    function createExceptionObject(tId) {
        return {
            tId : tId,
            status : 0,
            statusText : 'communication failure'
        };
    }

    // private
    function createResponseObject(o) {
        var headerObj = {},
        headerStr,
        conn = o.conn;

        try {
            headerStr = o.conn.getAllResponseHeaders();
            Ext.each(headerStr.split('\n'), function(v){
                var t = v.indexOf(':');
                headerObj[v.substr(0, t)] = v.substr(t + 1);
            });
        } catch(e) {}

        return {
            tId : o.tId,
            status : conn.status,
            statusText : conn.statusText,
            getResponseHeader : function(header){
                return headerObj[header];
            },
            getAllResponseHeaders : function(){
                return headerStr
            },
            responseText : conn.responseText,
            responseXML : conn.responseXML
        };
    }

    // private
    function releaseObject(o) {
        o.conn = null;
        o = null;
    }

    // private
    function getResponseObject(o) {

        var httpStatus, responseObject = null;

        try {
            if (o.conn.status !== undefined && o.conn.status != 0) {
                httpStatus = o.conn.status;
            }
            else {
                httpStatus = 13030;
            }
        }
        catch(e) {
            httpStatus = 13030;
        }

        if ((httpStatus >= 200 && httpStatus < 300) || (Ext.isIE && httpStatus == 1223)) {
            responseObject = createResponseObject(o);
        }
        else {
            switch (httpStatus) {
                case 12002:
                case 12029:
                case 12030:
                case 12031:
                case 12152:
                case 13030:
                    responseObject = createExceptionObject(o.tId);
                    break;
                default:
                    responseObject = createResponseObject(o);
            }
        }

        releaseObject(o);
        return responseObject;
    }

    // private
    function syncRequest(method, uri, postData) {
        var o = getConnectionObject() || null;

        if (o) {
            o.conn.open(method, uri, false);

            if (pub.useDefaultXhrHeader) {
                initHeader('X-Requested-With', pub.defaultXhrHeader);
            }

            if(postData && pub.useDefaultHeader && (!pub.headers || !pub.headers['Content-Type'])){
                initHeader('Content-Type', pub.defaultPostHeader);
            }

            if (pub.defaultHeaders || pub.headers) {
                setHeader(o);
            }
            o.conn.send(postData || null);

            return getResponseObject(o);
        }
        return null;
    }

    // private
    function getConnectionObject() {
        var o;
        try {
            if (o = createXhrObject(pub.transactionId)) {
                pub.transactionId++;
            }
        } catch(e) {
        } finally {
            return o;
        }
    }

    // private
    function createXhrObject(transactionId) {
        var http;
        try {
            http = new XMLHttpRequest();
        } catch(e) {
            for (var i = 0; i < activeX.length; ++i) {
                try {
                    http = new ActiveXObject(activeX[i]);
                    break;
                } catch(e) {}
            }
        } finally {
            return {
                conn : http,
                tId : transactionId
            };
        }
    }

    var pub = {
        request : function(method, uri, data, options) {
            if(options){
                var me = this,
                xmlData = options.xmlData,
                jsonData = options.jsonData;

                Ext.applyIf(me, options);

                if(xmlData || jsonData){
                    initHeader('Content-Type', xmlData ? 'text/xml' : 'application/json');
                    data = xmlData || (Ext.isObject(jsonData) ? Ext.encode(jsonData) : jsonData);
                }
            }
            return syncRequest(method || options.method || "POST", uri, data);
        },

        useDefaultHeader : true,
        defaultPostHeader : 'application/x-www-form-urlencoded; charset=UTF-8',
        useDefaultXhrHeader : true,
        defaultXhrHeader : 'XMLHttpRequest',
        poll : {},
        timeout : {},
        pollInterval : 50,
        transactionId : 0
    };
    return pub;
}();

(function() {
    var BEFOREREQUEST = "beforerequest",
        REQUESTCOMPLETE = "requestcomplete",
        REQUESTEXCEPTION = "requestexception",
        UNDEFINED = undefined,
        LOAD = 'load',
        POST = 'POST',
        GET = 'GET',
        WINDOW = window;

    function doFormUpload(o, ps, url){
        var id = Ext.id(),
        doc = document,
        frame = doc.createElement('iframe'),
        form = Ext.getDom(o.form),
        hiddens = [],
        hd;

        frame.id = frame.name = id;
        frame.className = 'x-hidden';
        frame.src = Ext.SSL_SECURE_URL; // for IE
        doc.body.appendChild(frame);

        if(Ext.isIE){
            doc.frames[id].name = id;
        }

        form.target = id;
        form.method = POST;
        form.enctype = form.encoding = 'multipart/form-data';
        form.action = url || "";

        // add dynamic params
        ps = Ext.urlDecode(ps, false);
        for(var k in ps){
            if(ps.hasOwnProperty(k)){
                hd = doc.createElement('input');
                hd.type = 'hidden';
                hd.value = ps[hd.name = k];
                form.appendChild(hd);
                hiddens.push(hd);
            }
        }

        function cb(){
            var me = this,
            // bogus response object
            r = {
                responseText : '',
                responseXML : null,
                argument : o.argument
                },
            doc,
            firstChild;

            try {
                doc = frame.contentWindow.document || frame.contentDocument || WINDOW.frames[id].document;
                if (doc) {
                    if (doc.body) {
                        if (/textarea/i.test((firstChild = doc.body.firstChild || {}).tagName)) { // json response wrapped in textarea
                            r.responseText = firstChild.value;
                        } else {
                            r.responseText = doc.body.innerHTML;
                        }
                    } else {
                        r.responseXML = doc.XMLDocument || doc;
                    }
                }
            }
            catch(e) {}

            Ext.EventManager.removeListener(frame, LOAD, cb, me);

            me.fireEvent(REQUESTCOMPLETE, me, r, o);

            Ext.callback(o.success, o.scope, [r, o]);
            Ext.callback(o.callback, o.scope, [o, true, r]);

            if(!me.debugUploads){
                setTimeout(function(){
                    Ext.removeNode(frame);
                }, 100);
            }
        }

        Ext.EventManager.on(frame, LOAD, cb, this);
        form.submit();

        Ext.each(hiddens, function(h) {
            Ext.removeNode(h);
        });
    }

    Ext.data.Connection.prototype.syncRequest = function(o){

        var me = this;
        if(me.fireEvent(BEFOREREQUEST, me, o)) {
            if (o.el) {
                if(!Ext.isEmpty(o.indicatorText)){
                    me.indicatorText = '<div class="loading-indicator">'+o.indicatorText+"</div>";
                }
                if(me.indicatorText) {
                    Ext.getDom(o.el).innerHTML = me.indicatorText;
                }
                o.success = (Ext.isFunction(o.success) ? o.success : function(){}).createInterceptor(function(response) {
                    Ext.getDom(o.el).innerHTML = response.responseText;
                });
            }

            var p = o.params,
                url = o.url || me.url,
                method,
                form,
                serForm;


            if (Ext.isFunction(p)) {
                p = p.call(o.scope||WINDOW, o);
            }

            p = Ext.urlEncode(me.extraParams, typeof p == 'object' ? Ext.urlEncode(p) : p);

            if (Ext.isFunction(url)) {
                url = url.call(o.scope || WINDOW, o);
            }

            if(form = Ext.getDom(o.form)){
                url = url || form.action;
                if(o.isUpload || /multipart\/form-data/i.test(form.getAttribute("enctype"))) {
                    return doFormUpload.call(me, o, p, url);
                }
                serForm = Ext.lib.Ajax.serializeForm(form);
                p = p ? (p + '&' + serForm) : serForm;
            }

            method = o.method || me.method || ((p || o.xmlData || o.jsonData) ? POST : GET);

            if(method === GET && (me.disableCaching && o.disableCaching !== false) || o.disableCaching === true){
                var dcp = o.disableCachingParam || me.disableCachingParam;
                url += (url.indexOf('?') != -1 ? '&' : '?') + dcp + '=' + (new Date().getTime());
            }

            o.headers = Ext.apply(o.headers || {}, me.defaultHeaders || {});

            if(o.autoAbort === true || me.autoAbort) {
                me.abort();
            }

            if((method == GET || o.xmlData || o.jsonData) && p){
                url += (/\?/.test(url) ? '&' : '?') + p;
                p = '';
            }
            return Ext.lib.Sjax.request(method, url, p, o);
        }else{
            return null;
        }
    };
})();

Ext.Ajax = new Ext.data.Connection({
    autoAbort : false,
    serializeForm : function(form){
        return Ext.lib.Ajax.serializeForm(form);
    }
});

//传入服务名作为参数
BIZ.ServiceProxy = function(service) {
    this.service = service;
}

BIZ.ServiceProxy = Ext.extend(BIZ.ServiceProxy, {
    /**
     * options: {
     *     method: 'POST', //请求方法(post or get)
     *     header: {...},   //发送的请求头
     *     success: fns,    //请求成功时回调
     *     failure: fnf,    //请求失败时回调
     *     versionKey: 'customer' //
     * }
     */
    invoke: function(mn, args, options) {
        //1. 编码调用方法的参数、参数类型
        var types = [];
        if (args) {
            for (var i=0; i<args.length; i++) {
                types[i] = BIZ.util.getTypeName(args[i]);
            }
            args = Ext.encode(args);
            types = Ext.encode(types);
        }

        //2. 设置请求参数
        var o = {
            params: {
                'service': this.service,
                'mn' : mn,
                'args': args,
                'types': types
            }
        };

        //3. 设置请求的url、头部、方法、是否缓存等
        var url = Env.invokePath;
        if (options) {
            if (options.headers) {
                o.headers = options.headers
            }
            var verKey = options.versionKey || this.versionKey;
            if (verKey && window[verKey + 'Version']) {
                url += ("?ver=" + window[verKey + 'Version']);
                o.method = "GET";
            }
            if (options.method) o.method = options.method;
            o.disableCaching = options.versionKey ? false : true;
        }
        o.url = url;

        if (!options || !options.success) {
            //4. 同步调用并处理返回结果
            var response = Ext.Ajax.syncRequest(o);
            this.sessionId = response.getResponseHeader["jsessionid"];
            var result = Ext.decode(response.responseText);
            if (!result || result.error) {
                var error = result ? result.error : 'empty response';
                this.errorHandler(error);
            }
            return result.value;
        }
        else {
            //5. 处理异步调用
            o['success'] = function(response) {
                var result = Ext.decode(response.responseText);
                if (!result || result.error) {
                    var error = result ? result.error : 'empty response';
                    if (options.failure) {
                        options.failure(error);
                    }
                    else {
                       this.errorHandler(error);
                    }
                    return;
                }

                if (options.success) {
                    options.success(result.value);
                }
            };
            o['failure'] = options.failure ? options.failure : this.errorHandler;
            Ext.Ajax.request(o);
            return null;
        }
    },

    invokeAsync: function(m, args, s, f) {
        return this.invoke(m, args, {
            success: s,
            failure: f
        });
    },

    updateVersion: function() {
        window[this.versionKey + 'Version'] = new Date().getTime();
    },

    errorHandler: function(error) {
        throw error;
    }
});

BIZ.ResLoader = function() {
    var resCache = {};
    function loadResource(url) {
        if (!resCache[url]) {
            var req = Ext.ajax.syncRequest({
                url: Env.contextPath + url,
                method: 'GET',
                disableCaching: false
            });
            resCache[url] = req.responseText;
        }
        return resCache[url];
    }

    return {
        loadRes: function(url) {
            return loadResource(url);
        },
        loadDom: function(url) {
            var dom = document.createElement('div');
            dom.innerHTML = loadResource(url);
            return dom;
        },
        loadJS: function(url) {
            var js = loadResource(url);
            eval(js);
        }
    };
}();
