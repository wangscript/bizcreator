YUI.add('date-field', function(Y) {
	
	var Lang = Y.Lang,
	    Widget = Y.Widget,
	    Node = Y.Node;

	/* Spinner class constructor */
	function DateField(config) {
		DateField.superclass.constructor.apply(this, arguments);
	}

	/* 
	 * Required NAME static field, to identify the Widget class and 
	 * used as an event prefix, to generate class names etc. (set to the 
	 * class name in camel case). 
	 */
	DateField.NAME = "date-field";
	
	/*
	 * The attribute configuration for the Spinner widget. Attributes can be
	 * defined with default values, get/set functions and validator functions
	 * as with any other class extending Base.
	 */
	DateField.ATTRS = {
		 // The current value of the spinner.
	    value : {
	        value: '',
	        validator : Y.Lang.isString
	    },
	    
	    // The localizable strings for the spinner. This attribute is 
	    // defined by the base Widget class but has an empty value. The
	    // spinner is simply providing a default value for the attribute.
	    strings: {
	        value: {
	            tooltip: "Press the arrow up/down keys for minor increments, page up/down for major increments.",
	            increment: "Increment",
	            decrement: "Decrement"
	        }
	    }
	};
	
	/* Static constant used to identify the classname applied to the spinners value field */
	DateField.INPUT_CLASS = Y.ClassNameManager.getClassName(DateField.NAME, "input");
	
	/* Static constants used to define the markup templates used to create Spinner DOM elements */
	DateField.INPUT_TEMPLATE = '<input type="text" class="' + DateField.INPUT_CLASS + '">';
	DateField.TRIGGER_TEMPLATE = '<img class="yui-form-trigger" src="../assets/s.gif"/>';
	
	/* Spinner extends the base Widget class */
	Y.extend(DateField, Widget, {
	
	    initializer: function() {
	        // Not doing anything special during initialization
	    },
	
	    destructor : function() {
	        
	    },
	
	    renderUI : function() {
	    	var contentBox = this.get("contentBox"),
            input = contentBox.one("." + DateField.INPUT_CLASS),
            strings = this.get("strings");

	        if (!input) {
	            input = Node.create(DateField.INPUT_TEMPLATE);
	            contentBox.appendChild(input);
	        }
	
	        input.set("title", strings.tooltip);
	        this.inputNode = input;
	        
	        var btn = Node.create(DateField.TRIGGER_TEMPLATE);
	        //btn.set("innerHTML", text);
	        //btn.set("title", text);
	        btn.addClass(this.getClassName("trigger"));
	        contentBox.appendChild(btn);
	        
	        this.trigger = btn;
	    },
	
	    bindUI : function() {
	    	this.trigger.addClassOnOver("yui-form-trigger-over");
	    	this.trigger.addClassOnClick("yui-form-trigger-click");
	    	
	    	this.inputNode.on('change', Y.bind(function (e) {
				this.set('value', this.inputNode.get('value'));
			}, this));
			
			this.on('valueChange', Y.bind(function (e) {
				this.inputNode.set('value', e.newVal);
			}, this)); 
	    },
	
	    syncUI : function() {
	        
	    }
	});
	
	Y.DateField = DateField;
	
}, '3.0.0' ,{requires:['event-key', 'widget', 't-calendar', 'yui-ext']});