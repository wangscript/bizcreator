YUI.add('ytemplate', function(Y) {
	
	var L = Y.Lang,
		UA = Y.UA,
	    O = Y.Object,
	    Node = Y.Node;
	
	/**
	 * @class Y.Template
	 * Represents an HTML fragment template. Templates can be precompiled for greater performance.
	 * For a list of available format functions, see {@link Ext.util.Format}.<br />
	 * Usage:
	<pre><code>
	var t = new Ext.Template(
	    '&lt;div name="{id}"&gt;',
	        '&lt;span class="{cls}"&gt;{name:trim} {value:ellipsis(10)}&lt;/span&gt;',
	    '&lt;/div&gt;'
	);
	t.append('some-element', {id: 'myid', cls: 'myclass', name: 'foo', value: 'bar'});
	</code></pre>
	 * @constructor
	 * @param {String/Array} html The HTML fragment or an array of fragments to join("") or multiple arguments to join("")
	 */
	Template = function(html){
	    var me = this,
	    	a = arguments,
	    	buf = [];

	    if (L.isArray(html)) {
	        html = html.join("");
	    } else if (a.length > 1) {
		    L.Array.each(a, function(v) {
	            if (L.isObject(v)) {
	                Y.merge(me, v);
	            } else {
	                buf.push(v);
	            }
	        });
	        html = buf.join('');
	    }

	    /**@private*/
	    me.html = html;
	    if (me.compiled) {
	        me.compile();
	    }
	};
	
	Template.prototype = {
	    /**
	     * Returns an HTML fragment of this template with the specified values applied.
	     * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @return {String} The HTML fragment
	     */
	    applyTemplate : function(values){
			var me = this;

	        return me.compiled ?
	        		me.compiled(values) :
					me.html.replace(me.re, function(m, name){
			        	return values[name] !== undefined ? values[name] : "";
			        });
		},

	    /**
	     * Sets the HTML used as the template and optionally compiles it.
	     * @param {String} html
	     * @param {Boolean} compile (optional) True to compile the template (defaults to undefined)
	     * @return {Ext.Template} this
	     */
	    set : function(html, compile){
		    var me = this;
	        me.html = html;
	        me.compiled = null;
	        return compile ? me.compile() : me;
	    },

	    /**
	    * The regular expression used to match template variables
	    * @type RegExp
	    * @property
	    */
	    re : /\{([\w-]+)\}/g,

	    /**
	     * Compiles the template into an internal function, eliminating the RegEx overhead.
	     * @return {Ext.Template} this
	     */
	    compile : function(){
	        var me = this,
	        	sep = UA.gecko > 0 ? "+" : ",";

	        function fn(m, name){                        
		        name = "values['" + name + "']";
		        return "'"+ sep + '(' + name + " == undefined ? '' : " + name + ')' + sep + "'";
	        }
	                
	        eval("this.compiled = function(values){ return " + (UA.gecko > 0 ? "'" : "['") +
	             me.html.replace(/\\/g, '\\\\').replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn) +
	             (UA.gecko > 0 ?  "';};" : "'].join('');};"));
	        return me;
	    },

	    /**
	     * Applies the supplied values to the template and inserts the new node(s) as the first child of el.
	     * @param {Mixed} el The context element
	     * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @param {Boolean} returnElement (optional) true to return a Ext.Element (defaults to undefined)
	     * @return {HTMLElement/Ext.Element} The new node or Element
	     */
	    insertFirst: function(node, values){
	        return this.doInsert(0, node, values);
	    },

	    /**
	     * Applies the supplied values to the template and inserts the new node(s) before el.
	     * @param {Mixed} el The context element
	     * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @param {Boolean} returnElement (optional) true to return a Ext.Element (defaults to undefined)
	     * @return {HTMLElement/Ext.Element} The new node or Element
	     */
	    insertBefore: function(node, values){
	        return this.doInsert('before', node, values);
	    },

	    /**
	     * Applies the supplied values to the template and inserts the new node(s) after el.
	     * @param {Mixed} el The context element
	     * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @param {Boolean} returnElement (optional) true to return a Ext.Element (defaults to undefined)
	     * @return {HTMLElement/Ext.Element} The new node or Element
	     */
	    insertAfter : function(node, values){
	        return this.doInsert('after', node, values);
	    },

	    /**
	     * Applies the supplied values to the template and appends the new node(s) to el.
	     * @param {Mixed} el The context element
	     * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @param {Boolean} returnElement (optional) true to return a Ext.Element (defaults to undefined)
	     * @return {HTMLElement/Ext.Element} The new node or Element
	     */
	    append : function(node, values){
	        return this.doInsert(null, node, values);
	    },
	    
	    /**
	     * Applies the supplied values to the template and overwrites the content of el with the new node(s).
	     * @param {Mixed} el The context element
	     * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @param {Boolean} returnElement (optional) true to return a Ext.Element (defaults to undefined)
	     * @return {HTMLElement/Ext.Element} The new node or Element
	     */
	    overwrite : function(node, values){
	    	return this.doInsert('replace', node, values);
	    },
	    
	    doInsert : function(where, node, values) {
	    	if (typeof node === 'string') {
	    		node = Y.one(node);
	    	}
	    	node.insert(this.applyTemplate(values), where);
	    	if (where === 'replace' || where == 0) {
	    		return node.get('firstChild');
	    	}
	    	else if (typeof where == 'number') {
	    		var kid = node._node.childNodes[where];
	    		return Y.one('#' + kid.id);
	    	}
	    	else if (where === 'before') {
	    		return node.get('previousSibling');
	    	}
	    	else if (where === 'after') {
	    		return node.get('nextSibling');
	    	}
	    	else {
	    		return node.get('lastChild');
	    	}
	    }
	    
	};
	/**
	 * Alias for {@link #applyTemplate}
	 * Returns an HTML fragment of this template with the specified values applied.
	 * @param {Object/Array} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	 * @return {String} The HTML fragment
	 * @member Ext.Template
	 * @method apply
	 */
	Template.prototype.apply = Template.prototype.applyTemplate;

	/**
	 * Creates a template from the passed element's value (<i>display:none</i> textarea, preferred) or innerHTML.
	 * @param {String/HTMLElement} el A DOM element or its id
	 * @param {Object} config A configuration object
	 * @return {Ext.Template} The created template
	 * @static
	 */
	Template.from = function(node, config){
		if (typeof node === 'string') {
			node = Y.one(node);
		}
	    return new Template(node.get('value') || node.get('innerHTML'), config || '');
	};
	
	Y.Template = Template;
	
	XTemplate = function(){
	    XTemplate.superclass.constructor.apply(this, arguments);

	    var me = this,
	    	s = me.html,
	    	re = /<tpl\b[^>]*>((?:(?=([^<]+))\2|<(?!tpl\b[^>]*>))*?)<\/tpl>/,
	    	nameRe = /^<tpl\b[^>]*?for="(.*?)"/,
	    	ifRe = /^<tpl\b[^>]*?if="(.*?)"/,
	    	execRe = /^<tpl\b[^>]*?exec="(.*?)"/,
	    	m,
	    	id = 0,
	    	tpls = [],
	    	VALUES = 'values',
	    	PARENT = 'parent',
	    	XINDEX = 'xindex',
	    	XCOUNT = 'xcount',
	    	RETURN = 'return ',
	    	WITHVALUES = 'with(values){ ';

	    s = ['<tpl>', s, '</tpl>'].join('');

	    while((m = s.match(re))){
	       	var m2 = m[0].match(nameRe),
				m3 = m[0].match(ifRe),
	       		m4 = m[0].match(execRe),
	       		exp = null,
	       		fn = null,
	       		exec = null,
	       		name = m2 && m2[1] ? m2[1] : '';

	       if (m3) {
	           exp = m3 && m3[1] ? m3[1] : null;
	           if(exp){
	               fn = new Function(VALUES, PARENT, XINDEX, XCOUNT, WITHVALUES + RETURN +(Ext.util.Format.htmlDecode(exp))+'; }');
	           }
	       }
	       if (m4) {
	           exp = m4 && m4[1] ? m4[1] : null;
	           if(exp){
	               exec = new Function(VALUES, PARENT, XINDEX, XCOUNT, WITHVALUES +(Ext.util.Format.htmlDecode(exp))+'; }');
	           }
	       }
	       if(name){
	           switch(name){
	               case '.': name = new Function(VALUES, PARENT, WITHVALUES + RETURN + VALUES + '; }'); break;
	               case '..': name = new Function(VALUES, PARENT, WITHVALUES + RETURN + PARENT + '; }'); break;
	               default: name = new Function(VALUES, PARENT, WITHVALUES + RETURN + name + '; }');
	           }
	       }
	       tpls.push({
	            id: id,
	            target: name,
	            exec: exec,
	            test: fn,
	            body: m[1]||''
	        });
	       s = s.replace(m[0], '{xtpl'+ id + '}');
	       ++id;
	    }
		L.Array.each(tpls, function(t) {
	        me.compileTpl(t);
	    });
	    me.master = tpls[tpls.length-1];
	    me.tpls = tpls;
	};
	
	Y.extend(XTemplate, Template, {
	    // private
	    re : /\{([\w-\.\#]+)(?:\:([\w\.]*)(?:\((.*?)?\))?)?(\s?[\+\-\*\\]\s?[\d\.\+\-\*\\\(\)]+)?\}/g,
	    // private
	    codeRe : /\{\[((?:\\\]|.|\n)*?)\]\}/g,

	    // private
	    applySubTemplate : function(id, values, parent, xindex, xcount){
	        var me = this,
	        	len,
	        	t = me.tpls[id],
	        	vs,
	        	buf = [];
	        if ((t.test && !t.test.call(me, values, parent, xindex, xcount)) ||
	            (t.exec && t.exec.call(me, values, parent, xindex, xcount))) {
	            return '';
	        }
	        vs = t.target ? t.target.call(me, values, parent) : values;
	        len = vs.length;
	        parent = t.target ? values : parent;
	        if(t.target && L.isArray(vs)){
		        L.Array.each(vs, function(v, i) {
	                buf[buf.length] = t.compiled.call(me, v, parent, i+1, len);
	            });
	            return buf.join('');
	        }
	        return t.compiled.call(me, vs, parent, xindex, xcount);
	    },

	    // private
	    compileTpl : function(tpl){
	        var fm = Ext.util.Format,
	       		useF = this.disableFormats !== true,
	            sep = Ext.isGecko ? "+" : ",",
	            body;

	        function fn(m, name, format, args, math){
	            if(name.substr(0, 4) == 'xtpl'){
	                return "'"+ sep +'this.applySubTemplate('+name.substr(4)+', values, parent, xindex, xcount)'+sep+"'";
	            }
	            var v;
	            if(name === '.'){
	                v = 'values';
	            }else if(name === '#'){
	                v = 'xindex';
	            }else if(name.indexOf('.') != -1){
	                v = name;
	            }else{
	                v = "values['" + name + "']";
	            }
	            if(math){
	                v = '(' + v + math + ')';
	            }
	            if (format && useF) {
	                args = args ? ',' + args : "";
	                if(format.substr(0, 5) != "this."){
	                    format = "fm." + format + '(';
	                }else{
	                    format = 'this.call("'+ format.substr(5) + '", ';
	                    args = ", values";
	                }
	            } else {
	                args= ''; format = "("+v+" === undefined ? '' : ";
	            }
	            return "'"+ sep + format + v + args + ")"+sep+"'";
	        }

	        function codeFn(m, code){
	            return "'"+ sep +'('+code+')'+sep+"'";
	        }

	        // branched to use + in gecko and [].join() in others
	        if(Ext.isGecko){
	            body = "tpl.compiled = function(values, parent, xindex, xcount){ return '" +
	                   tpl.body.replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn).replace(this.codeRe, codeFn) +
	                    "';};";
	        }else{
	            body = ["tpl.compiled = function(values, parent, xindex, xcount){ return ['"];
	            body.push(tpl.body.replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn).replace(this.codeRe, codeFn));
	            body.push("'].join('');};");
	            body = body.join('');
	        }
	        eval(body);
	        return this;
	    },

	    /**
	     * Returns an HTML fragment of this template with the specified values applied.
	     * @param {Object} values The template values. Can be an array if your params are numeric (i.e. {0}) or an object (i.e. {foo: 'bar'})
	     * @return {String} The HTML fragment
	     */
	    applyTemplate : function(values){
	        return this.master.compiled.call(this, values, {}, 1, 1);
	    },

	    /**
	     * Compile the template to a function for optimized performance.  Recommended if the template will be used frequently.
	     * @return {Function} The compiled function
	     */
	    compile : function(){return this;}

	    /**
	     * @property re
	     * @hide
	     */
	    /**
	     * @property disableFormats
	     * @hide
	     */
	    /**
	     * @method set
	     * @hide
	     */

	});
	
	XTemplate.prototype.apply = XTemplate.prototype.applyTemplate;

	/**
	 * Creates a template from the passed element's value (<i>display:none</i> textarea, preferred) or innerHTML.
	 * @param {String/HTMLElement} el A DOM element or its id
	 * @return {Ext.Template} The created template
	 * @static
	 */
	XTemplate.from = function(el){
		if (typeof node === 'string') {
			node = Y.one(node);
		}
	    return new XTemplate(node.get('value') || node.get('innerHTML'));
	};
	
	Y.XTemplate = XTemplate;
	
}, '3.0.0' ,{requires:['node']});