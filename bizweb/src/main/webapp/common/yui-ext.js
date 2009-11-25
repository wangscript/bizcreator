YUI.add('yui-ext', function(Y) {
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