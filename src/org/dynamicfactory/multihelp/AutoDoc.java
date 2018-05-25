/**
 * Created by Daniel McEnnis on 5/25/2018
 * <p>
 * Copyright Daniel McEnnis 2015
 */

package org.dynamicfactory.multihelp;

import org.dynamicfactory.FactoryFactory;

import java.io.File;


/**
 * Default Description Google Interview Project
 */
public class AutoDoc extends org.multihelp.file.FileNode {

    protected static AutoDoc document = null;

    private String listID;

    private boolean isInterface = false;

    protected AutoDoc(File root, int place) {
        super(root);
        if (document == null) {
            document = new AutoDoc(root);
        }
    }

    public AutoDoc(File root) {
        super(root);
    }

    @Override
    public void setPage(org.multihelp.HelpViewer viewer) {
        document.setPageInternal(viewer);
    }

    public void setPageInternal(org.multihelp.HelpViewer viewer) {

        // Construct the HTML header

        // Which page are we viewing again?
    }

    @Override
    public void traverseFileSystem(File root, int depth) {
        document.traverseFileSystem(root,depth);
    }

    protected static void traverseFileSystemInternal(File root, int depth) {
        // build the entire tree here

        // Construct a list of all factories
        FactoryFactory base = FactoryFactory.newInstance();
        for(String interfaceName : base.getKnownTypes()){

        }
    }
}
