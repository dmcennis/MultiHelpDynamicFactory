/**
 * Created by Daniel McEnnis on 5/25/2018
 * <p>
 * Copyright Daniel McEnnis 2015
 */

package org.dynamicfactory.multihelp;

import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.FactoryFactory;

import java.io.File;
import java.util.Collection;


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

        // create the appropriate factory
        AbstractFactory factory = FactoryFactory.newInstance().create(listID);

        if (isInterface) {
            // extract the interface level documentation from the factory object

            // extract the interface object from the factory object

            // extract and load all function signatures

            // list all parameters with their documentation from the factory object

            // list hyperlinks to all available implementations

        } else {
            // extract the interface global documentation and hyperlink

            // extract the combined parameter lists and documentation

            // extract any class specific functions not in the interface
        }
    }

    @Override
    public void traverseFileSystem(File root, int depth) {
        // build this FileNode listing the known nterfaces and their short description.


        // build the interface loop
        FactoryFactory factory = FactoryFactory.newInstance();
        for(String thisInterface : FactoryFactory.newInstance().getKnownTypes()){
            AbstractFactory interfaceFactory = factory.create(thisInterface);
            // construct the interface page

            Collection<String> list = interfaceFactory.getKnownTypes();
            for(String o : list){
                // construct the product pages as children

            }
        }

        //
    }

    protected static void traverseFileSystemInternal(File root, int depth) {
        // build the entire tree here

        // Construct a list of all factories
        FactoryFactory base = FactoryFactory.newInstance();
        for (String interfaceName : base.getKnownTypes()) {

        }
    }
}
