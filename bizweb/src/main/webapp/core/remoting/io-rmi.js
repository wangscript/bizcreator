
RmiCtx = {contextPath: '/bizweb', invokePath: '/bizweb/BsfInvokerServlet'};

YUI.add('io-rmi', function(Y) {

    var L = Y.Lang,
        JSON = Y.JSON,
        Array = Y.Array,
        w = Y.config.win,
        _headers = {
            'X-Requested-With' : 'XMLHttpRequest'
   	};

    /**
         * Takes an object and converts it to an encoded URL. e.g. Ext.urlEncode({foo: 1, bar: 2}); would return "foo=1&bar=2".  Optionally, property values can be arrays, instead of keys and the resulting string that's returned will contain a name/value pair for each array value.
         * @param {Object} o
         * @param {String} pre (optional) A prefix to add to the url encoded string
         * @return {String}
         */
        function urlEncode(o, pre){
            var undef, buf = [], key, e = encodeURIComponent, a;

            for(key in o){
                undef =  typeof o[key] == 'undefined';
                a = undef ? key : o[key];
                if (!L.isEmpty(a, true)) {
                    if (!L.isArray(a) || L.isPrimitive(a)) {
                        a = [a];
                    }
                    Array.each(a, function(val, i){
                        buf.push("&", e(key), "=", (val != key || !undef) ? e(val) : "");
                    });
                }
            }
            if(!pre){
                buf.shift();
                pre = "";
            }
            return pre + buf.join('');
        }

        /**
         * Takes an encoded URL and and converts it to an object. Example: <pre><code>
Ext.urlDecode("foo=1&bar=2"); // returns {foo: "1", bar: "2"}
Ext.urlDecode("foo=1&bar=2&bar=3&bar=4", false); // returns {foo: "1", bar: ["2", "3", "4"]}
</code></pre>
         * @param {String} string
         * @param {Boolean} overwrite (optional) Items of the same name will overwrite previous values instead of creating an an array (Defaults to false).
         * @return {Object} A literal with members
         */
    function urlDecode(string, overwrite){
        var obj = {},
        pairs = string.split('&'),
        d = decodeURIComponent,
        name,
        value;
        Array.each(pairs, function(pair) {
            pair = pair.split('=');
            name = d(pair[0]);
            value = d(pair[1]);
            obj[name] = overwrite || !obj[name] ? value :
            [].concat(obj[name]).concat(value);
        });
        return obj;
    }

     function _setHeader(l, v) {
        if (v) {
            _headers[l] = v;
        }
        else {
            delete _headers[l];
        }
    }

    /**
    * @description Method that sets all HTTP headers to be sent in a transaction.
    *
    * @method _setHeaders
    * @private
    * @static
    * @param {object} o - XHR instance for the specific transaction.
    * @param {object} h - HTTP headers for the specific transaction, as defined
    *                     in the configuration object passed to YUI.io().
    * @return void
    */
    function _setHeaders(o, h) {
        var p;

        for (p in _headers) {
            if (_headers.hasOwnProperty(p)) {
                if (h[p]) {
                    // Configuration headers will supersede IO preset headers,
                    // if headers match.
                    break;
                }
                else {
                    h[p] = _headers[p];
                }
            }
        }

        for (p in h) {
            if (h.hasOwnProperty(p)) {
                o.setRequestHeader(p, h[p]);
            }
        }
    }

     /**
     * options: {
     *     method: 'POST', //请求方法(post or get)
     *     headers: {...},   //发送的请求头
     *     success: fns,    //请求成功时回调
     *     failure: fnf,    //请求失败时回调
     *     versionKey: 'customer' //
     * }
     */
    Y.mix(Y.io, {
        _rmi: function(service, mn, args, options) {

             //1. 编码调用方法的参数、参数类型
            var types = [];
            if (args) {
                for (var i=0; i<args.length; i++) {
                    types[i] = L.getTypeName(args[i]);
                }
                args = JSON.stringify(args);
                types = JSON.stringify(types);
            }

            //2. 设置请求参数
            var o = {
                params: {
                    'service': service,
                    'mn' : mn,
                    'args': args,
                    'types': types
                }
            };

            //3. 设置请求的url、头部、方法、是否缓存等
            var url = RmiCtx.invokePath;
            if (options) {
                if (options.headers) {
                    o.headers = options.headers
                }
                var verKey = options.versionKey;
                if (verKey && window[verKey + 'Version']) {
                    url += ("?ver=" + window[verKey + 'Version']);
                    o.method = "GET";
                }
                if (options.method) o.method = options.method;
                o.disableCaching = options.versionKey ? false : true;
            }
            o.url = url;

            //4 将参数o.params编码成get参数(如：service=core.userMgr&mn=findById&args=[]&types=[]等)
            var p = o.params,
                m = o.method || "POST",
                data = typeof p == 'object' ? urlEncode(p) : p;

            if (o.method && o.method.toUpperCase() == "GET") {
                url += (/\?/.test(url) ? '&' : '?') + data;
                data = '';
            }

            //5 设置头部信息
            if (data && m === 'POST') {
                _setHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
            }

            options = options || {};

            //6. 同步调用并处理返回结果
            if (!options || !options.success) {
                //6.1 创建http请求
                var xhr = w.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');

                //6.2 打开url链接
                xhr.open(m, url, false);

                _setHeaders(xhr, options.headers || {});

                //6.3 发送数据
                xhr.send(data);
                if (xhr.status == 200) {
                    var result = JSON.parse(xhr.responseText);
                    return result.value;
                }
                else {
                    Y.log("Error " + xhr.status + ": " + xhr.statusText);
                }
            }
            //5. 异步调用
            else {
                Y.io(url, {
                    method : m,
                    data : data,
                    headers: options.headers,
                    on : {
                        success: function(id, o, args) {
                            var result = JSON.parse(o.responseText),
                                v = result.value;
                             if (options.success) {
                                 options.success(v);
                             }
                        },
                        failure : function(id, o, args) {
                            Y.log("status: " + o.status);
                        }
                    }
                });
            }
        }
    }, true);

    Y.rmi = Y.io._rmi;

}, '3.0.0' ,{requires:['io-base', 'json', 'node-base']});