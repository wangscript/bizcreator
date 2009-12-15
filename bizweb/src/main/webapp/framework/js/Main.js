
//	Call the "use" method, passing in "node-menunav".  This will load the
//	script and CSS for the MenuNav Node Plugin and all of the required
//	dependencies.

YUI({base:"../yui/3.0.0/build/", timeout: 10000}).use("node-menunav", function(Y) {

        //	Retrieve the Node instance representing the root menu
        //	(<div id="productsandservices">) and call the "plug" method
        //	passing in a reference to the MenuNav Node Plugin.

        var menu = Y.one("#productsandservices");

        menu.plug(Y.Plugin.NodeMenuNav);

        //	Show the menu now that it is ready

        menu.get("ownerDocument").get("documentElement").removeClass("yui-loading");

});

