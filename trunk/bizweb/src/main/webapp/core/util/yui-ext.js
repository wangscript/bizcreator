YUI.add('yui-ext', function(Y) {
        var L = Y.Lang;

        L.getTypeName = function(obj) {
            if (L.isString(obj)) return 'String';
            else if (L.isNumber(obj) && obj.toString().indexOf(".") == -1) return 'Integer';
            else if (L.isNumber(obj) && obj.toString().indexOf(".") > -1) return 'Float';
            else if (L.isBoolean(obj)) return 'Boolean';
            else if (L.isDate(obj)) return 'Date';
            else if (L.isArray(obj)) return 'Array';
            else if (L.isFunction(obj)) return 'Function';
            else if (L.isObject(obj)) {
                if (obj.__TYPE__) {
                    //obj.__TYPE__是服务器端对应的类型，如：com.kingdee.youshang.gl.Account
                    return obj.__TYPE__
                }
                else {
                    return 'Object';
                }
            }
            else return 'undefined';
        }

        L.isEmpty = function(v, allowBlank){
            return v === null || v === undefined || ((L.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
        }

        L.isPrimitive = function(v) {
            return L.isString(v) || L.isNumber(v) || L.isBoolean(v);
        }

	Y.mix(Y.Node.prototype, {
		/**
	     * Sets up event handlers to call the passed functions when the mouse is moved into and out of the Element.
	     * @param {Function} overFn The function to call when the mouse enters the Element.
	     * @param {Function} outFn The function to call when the mouse leaves the Element.
	     * @param {Object} scope (optional) The scope (<tt>this</tt> reference) in which the functions are executed. Defaults to the Element's DOM element.
	     * @param {Object} options (optional) Options for the listener. See {@link Ext.util.Observable#addListener the <tt>options</tt> parameter}.
	     * @return {Ext.Element} this
	     */
	    hover : function(overFn, outFn, scope, options){
	        var me = this;
	        Y.on('mouseenter', overFn, me, scope || me, options);
	        Y.on('mouseleave', outFn, me, scope || me, options);
	        return me;
	    },
	    
	    /**
	     * Sets up event handlers to add and remove a css class when the mouse is over this element
	     * @param {String} className
	     * @return {Ext.Element} this
	     */
	    addClassOnOver : function(className){
	        this.hover(
	            function(){
	                this.addClass(className);
	            },
	            function(){
	                this.removeClass(className);
	            }
	        );
	        return this;
	    },

	    /**
	     * Sets up event handlers to add and remove a css class when this element has the focus
	     * @param {String} className
	     * @return {Ext.Element} this
	     */
	    addClassOnFocus : function(className){
	    	var me = this;
		    Y.on("focus", function(){
		        me.addClass(className);
		    }, this);
		    Y.on("blur", function(){
		        me.removeClass(className);
		    }, this);
		    return this;
	    },

	    /**
	     * Sets up event handlers to add and remove a css class when the mouse is down and then up on this element (a click effect)
	     * @param {String} className
	     * @return {Ext.Element} this
	     */
	    addClassOnClick : function(className){
	        var me = this;
	        this.on("mousedown", function(){
	            me.addClass(className);
	            var d = me.get('ownerDocument'),
	            	fn = function(){
		                me.removeClass(className);
		                Y.detach("mouseup", fn, d);
		            };
	            Y.on("mouseup", fn, d);
	        }, this);
	        return this;
	    }
	}, true);
	
}, '3.0.0' ,{requires:['node', 'event-mouseenter']});