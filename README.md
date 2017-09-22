What is it?
-----------

This Plugin provides some example code, which should help you to get familiar with the SAP Customer Checkout Plugin development.

To develop plugins for SAP Customer Checkout, you need to download the application from the SAP Service Marketplace download center.
This can be done by logging in with your S-User.

The downloaded Setup-Exe file can be opened with an archiving app (like WinRar). Inside of the exe you will find a folder called "CustomerCheckout".
In this folder the "env.jar" is located. You will need this jar for the plugin development. It has to be added as an external library to the plugin project.
This can either be achieved by adding this jar file directly as external library. Alternatively, if you use maven for building your project, you could also 
add the jar file to your local maven repository.

Once your project setup is complete, you can start developing a basic plugin. Have a look at the WorkshopPlugin class in this repository. This will show the different possibilities
you have, when you want do create a plugin.


Deploy
------

To deploy you plugin, you have to export it as jar file. The jar file needs some special entries in the manifest. Have a look at the manifest included in this example.
Once you created a plugin and exported it to a jar file, you can put it in the Plugin-Folder of your SAP Customer Checkout installation. The plugin folder is located in the subfolder cco\POSPlugins\AP
of your installation. If your plugin needs further external libraries, you must either include them in the jar file, or put them in the cco\POSPlugins\AP or cco\lib folder.
Now run the application and check if your plugin is showing up in the Plugins tab on the configuration page. If it is not showing up, check the console during the application startup.
There you will find some information about the plugins, which should be loaded. Have a look for the message "com.sap.scco.ap.plugin.PluginManager - Starting PluginManager".
Some lines above and below this message, you will see which plugins are found and loaded (or not). You will also see why a plugin was not loaded (e.g. The version numbers did not match). 


Debug
-----

To debug your plugin code, you need to setup a remote debug connection to the SAP Customer Checkout process. To enable remote debugging for the process, you have to add some additional
start parameters. This is done in the run.bat file in your SAP Customer Checkout installation directory. In the run.bat have a look for a line "com.sap.scco.env.Runner". Right before that
line include the following two lines:

-Xdebug ^
-Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=y ^

(The ^ chars at the end are important.)

Now start SAP Customer Checkout. In the console you will now see, that the process waits for a debug connection on port 4000. It waits, because in the second line "suspend=y" was set. You 
could also set this to "suspend=n", if the process should not wait for the connection. In your development environment you can now create a remote debug session. If you use eclipse, just go to
debug configurations and add a new "Remote Java Application". Enter port 4000 and click debug. If the connection is established, you will see that the console output will continue like on a 
normal startup. Now you can add breakpoints in your code. Be aware, that any code changes you make in you plugin code will only be reflected, if you redeploy your plugin to the cco\POSPlugins\AP
folder and restart SAP Customer Checkout.
