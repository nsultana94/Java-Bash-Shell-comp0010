package uk.ac.ucl.jsh;

import java.io.IOException;

/**
 * ApplicationFactory Class using the factory pattern to create new Applications.
 * Can return a 'safe' and 'unsafe' version
 * The 'unsafe' version prints its errors to standard output and terminates instead of throwing an error
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */


public class ApplicationFactory {
    /**
     * Creates an application object given an application name
     * @param appName the name of the application to be created
     * @param unsafe {@code true} if unsafe or {@code false} if normal
     * @return Application object optionally with a UnsafeDecorator 
     * @see UnsafeDecorator
     * @see Application
     */

    public Application getApplication(String appName, Boolean unsafe) throws IOException {
        Application app;

        if (appName == null) {
            return null;
        } else if (appName.equalsIgnoreCase("pwd")) {
            app = new Pwd();
        }
        else if (appName.equalsIgnoreCase("ls")) {
            app = new Ls();
        } else if (appName.equalsIgnoreCase("cat")) {
            app = new cat();
        }
        else if (appName.equalsIgnoreCase("echo")) {
            app = new echo();
        }
        else if (appName.equalsIgnoreCase("head")) {
            app = new head();
        } else if (appName.equalsIgnoreCase("tail")) {
            app = new tail();
        } else if (appName.equalsIgnoreCase("grep")) {
            app = new grep();
        } else if (appName.equalsIgnoreCase("cd")) {
            app = new Cd();
        } else if (appName.equalsIgnoreCase("sort")) {
            app = new Sort();
        }

        else if (appName.equalsIgnoreCase("uniq")) {
            app = new Uniq();
        }
        else if(appName.equalsIgnoreCase("cut")){
            app = new Cut();
        }

        else if (appName.equalsIgnoreCase("find")) {
            app = new find();
        } else {
            throw new RuntimeException(appName + ": unknown application");
        }
        return unsafe ? new UnsafeDecorator(app) : app;
    }
}