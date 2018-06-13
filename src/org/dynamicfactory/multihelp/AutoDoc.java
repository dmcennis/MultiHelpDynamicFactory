/**
 * Created by Daniel McEnnis on 5/25/2018
 * <p>
 * Copyright Daniel McEnnis 2015
 */

package org.dynamicfactory.multihelp;

import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.FactoryFactory;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.multihelp.file.FileNode;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Default Description Google Interview Project
 */
public class AutoDoc extends org.multihelp.file.FileNode {

    protected static AutoDoc document = null;

    private String listID;

    private FileNode parentInterface;

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

        if (isInterface) {
        } else {

        }
    }

    protected void setPageConcrete(org.multihelp.HelpViewer viewer) {

        AbstractFactory factory = FactoryFactory.newInstance().create(listID);

        // extract the interface global documentation and hyperlink

        // extract the combined parameter lists and documentation

        // extract any class specific functions not in the interface

    }

    protected void setPageInterface(org.multihelp.HelpViewer viewer) {

        AbstractFactory factory = FactoryFactory.newInstance().create(listID);

        StringBuffer buffer = new StringBuffer();
        addHeader(buffer);
        // extract the interface level documentation from the factory object
        buffer.append(factory.getDescription());
        buffer.append(factory.getLongDescription());

        // extract and load all function signatures
        TypeVariable[] t = factory.getClass().getTypeParameters();

        if (t.length > 0) {
            Class c = t[0].getClass();
            Method[] methods = c.getDeclaredMethods();
            for(Method method : methods){
                buffer.append("<code>");
                buffer.append(method.getGenericReturnType().getClass().getName());
                buffer.append(" ");
                buffer.append(method.getName());
                buffer.append("(");
                Type[] types = method.getGenericParameterTypes();
                boolean first = true;
                int count='a';
                for(Type type : types){
                    if(first){
                        first = false;
                    }else{
                        buffer.append(", ");
                    }
                    buffer.append(type.getClass().getName());
                    buffer.append(" ");
                    buffer.append((char)count++);
                }
                buffer.append(")</code>");
            }
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Internal error: All factories must provide a concrete type");
        }

        // TODO: Attach separator from the interface

        // list all parameters with their documentation from the factory object
        Properties list = factory.getParameter();
        for(Parameter param :list.get()){
            buffer.append("<p/>"+param.getType()+" of type "+param.getType());
            buffer.append(" ").append(param.getDescription());
            buffer.append(" ").append(param.getLongDescription());
            if(param.getRestrictions() != null){
                buffer.append("Minumum parameter count of ");
                buffer.append(param.getRestrictions().getMinCount()).append("\n");
                buffer.append("Maximum parameter count of ");
                buffer.append(param.getRestrictions().getMaxCount()).append("\n");
                buffer.append(" with a default of ");
                buffer.append(param.get().toString()).append("\n");
            }

        }

        // list hyperlinks to all available implementations
        Vector<FileNode> e = this.children;
        for (FileNode file : e) {
            buffer.append("<href link=\"" + e.toString() + "\"/>");
        }

        addFooter(buffer);
    }

    protected void addHeader(StringBuffer buffer) {
        buffer.append("<html5>");
    }


    protected void addFooter(StringBuffer buffer) {
        buffer.append("</html5>");
    }

    @Override
    public void traverseFileSystem(File root, int depth) {
        // build this FileNode listing the known nterfaces and their short description.


        // build the interface loop
        FactoryFactory factory = FactoryFactory.newInstance();
        for (String thisInterface : FactoryFactory.newInstance().getKnownTypes()) {
            AbstractFactory interfaceFactory = factory.create(thisInterface);
            // construct the interface page

            Collection<String> list = interfaceFactory.getKnownTypes();
            for (String o : list) {
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
