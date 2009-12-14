<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <!--script type="text/javascript" src="../extjs/ext-core-3.0/ext-core-debug.js"></script>
    <script type="text/javascript" src="remoting-ext.js"></script-->
    <script type="text/javascript" src="http://yui.yahooapis.com/3.0.0/build/yui/yui-min.js"></script>

    <script type="text/javascript" src="yui-ext.js"></script>
    <script type="text/javascript" src="io-rmi.js"></script>
    <script language="javascript" type="text/javascript">
        /*Ext.onReady(function(){
        	var svc = new BIZ.ServiceProxy('core.userMgr');
        	var user = svc.invoke('findById',['000000000001@rhino']);
        	
        });*/
        YUI().use('yui-ext', 'io-rmi', function(Y){
            var u = Y.rmi("core.userMgr", 'findById',['000000000001@rhino']);
            //Y.log(">>>result: " + u);
        });
    </script>
</head>
<body>
    <h1>Hello World!</h1>
</body>
</html>
