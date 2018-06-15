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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Default Description Google Interview Project
 */
public class AutoDoc extends org.multihelp.file.FileNode {

    protected static AutoDoc document = null;

    protected String listID;

    protected FileNode parentInterface;

    protected boolean isInterface = false;

    protected String description;

    protected String longDescription;

    protected String[] methods;

    protected String[] returnTypes;

    protected String[][] methodParameters;

    protected String[][] parameters;

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
            for (Method method : methods) {
                buffer.append("<code>");
                buffer.append(method.getGenericReturnType().getClass().getName());
                buffer.append(" ");
                buffer.append(method.getName());
                buffer.append("(");
                Type[] types = method.getGenericParameterTypes();
                boolean first = true;
                int count = 'a';
                for (Type type : types) {
                    if (first) {
                        first = false;
                    } else {
                        buffer.append(", ");
                    }
                    buffer.append(type.getClass().getName());
                    buffer.append(" ");
                    buffer.append((char) count++);
                }
                buffer.append(")</code>");
            }
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Internal error: All factories must provide a concrete type");
        }

        // TODO: Attach separator from the interface

        // list all parameters with their documentation from the factory object
        Properties list = factory.getParameter();
        for (Parameter param : list.get()) {
            buffer.append("<p/>" + param.getType() + " of type " + param.getType());
            buffer.append(" ").append(param.getDescription());
            buffer.append(" ").append(param.getLongDescription());
            if (param.getRestrictions() != null) {
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
        FactoryFactory masterFactory = FactoryFactory.newInstance();
        for (String thisInterface : FactoryFactory.newInstance().getKnownTypes()) {
            AbstractFactory interfaceFactory = masterFactory.create(thisInterface);
            // construct the interface page

            for (String o : (Collection<String>)interfaceFactory.getKnownTypes()) {
                // construct the product pages as children
                AbstractFactory factory = FactoryFactory.newInstance().create(listID);

                AutoDoc interfaceHelpPage = new AutoDoc(root,depth+1);
                // extract the interface level documentation from the factory object
                interfaceHelpPage.description = factory.getDescription();
                interfaceHelpPage.longDescription = factory.getLongDescription();

                // extract and load all function signatures
                TypeVariable[] t = factory.getClass().getTypeParameters();

                if (t.length > 0) {
                    Class c = t[0].getClass();
                    Method[] methods = c.getDeclaredMethods();
                    interfaceHelpPage.methods = new String[methods.length];
                    interfaceHelpPage.returnTypes = new String[methods.length];
                    interfaceHelpPage.methodParameters = new String[methods.length][];
                    for (int i=0;i< methods.length;++i){
                        // TODO handle generic return types
                        interfaceHelpPage.returnTypes[i] =  methods[i].getGenericReturnType().getClass().getName();
                        interfaceHelpPage.methods[i] = methods[i].getName();
                        Type[] types = methods[i].getGenericParameterTypes();
                        interfaceHelpPage.methodParameters[i] = new String[types.length];
                        for (int j=0;j<types.length;++j) {
                            interfaceHelpPage.methodParameters[i][j] = types[j].getClass().getName();
                        }
                    }
                } else {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Internal error: All factories must provide a concrete type");
                }

                // TODO: Attach separator from the interface

                // list all parameters with their documentation from the factory object
                Properties interfaceParameters = factory.getParameter();
                interfaceHelpPage.parameters = new String[interfaceParameters.get().size()][6];
                int i=0;
                for (Parameter param : interfaceParameters.get()) {
                    interfaceHelpPage.parameters[i][0] = param.getType();
                    interfaceHelpPage.parameters[i][1] = param.getParameterClass().getName();
                    interfaceHelpPage.parameters[i][2] = param.getDescription();
                    interfaceHelpPage.parameters[i][3] = param.getLongDescription();
                    interfaceHelpPage.parameters[i][4] = String.valueOf(param.getRestrictions().getMinCount());
                    interfaceHelpPage.parameters[i][5] = String.valueOf(param.getRestrictions().getMaxCount());
                    i++;
                }

                this.add(interfaceHelpPage);
                // list hyperlinks to all available implementations
                for (String type : (Collection<String>)factory.getKnownTypes()){
                    AutoDoc file = new AutoDoc(root,depth+2);
                    file.parent = interfaceHelpPage;
                    AutoDoc concreteHelpPage = new AutoDoc(root,depth+1);
                    // extract the interface level documentation from the factory object
                    concreteHelpPage.description = factory.getDescription();
                    concreteHelpPage.longDescription = factory.getLongDescription();

                    // extract and load all function signatures
                    TypeVariable[] t2 = factory.getClass().getTypeParameters();

                    if (t.length > 0) {
                        Class c = t[0].getClass();
                        Method[] methods = c.getDeclaredMethods();
                        concreteHelpPage.methods = new String[methods.length];
                        concreteHelpPage.returnTypes = new String[methods.length];
                        concreteHelpPage.methodParameters = new String[methods.length][];
                        for (int concreteMethod =0;concreteMethod< methods.length;++concreteMethod){
                            // TODO handle generic return types
                            concreteHelpPage.returnTypes[concreteMethod] =  methods[concreteMethod].getGenericReturnType().getClass().getName();
                            concreteHelpPage.methods[concreteMethod] = methods[concreteMethod].getName();
                            Type[] types = methods[concreteMethod].getGenericParameterTypes();
                            concreteHelpPage.methodParameters[concreteMethod] = new String[types.length];
                            for (int j=0;j<types.length;++j) {
                                concreteHelpPage.methodParameters[concreteMethod][j] = types[j].getClass().getName();
                            }
                        }
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Internal error: All factories must provide a concrete type");
                    }

                    // TODO: Attach separator from the interface

                    // list all parameters with their documentation from the factory object
                    Properties concreteParameters = factory.getParameter();
                    int novel = concreteParameters.get().size()-interfaceParameters.get().size();
                    concreteHelpPage.parameters = new String[novel][6];
                    int index=0;
                    for (Parameter param : interfaceParameters.get()) {
                        if(interfaceParameters.quickCheck(param.getType(),param.getParameterClass())){
                            concreteHelpPage.parameters[index][0] = param.getType();
                            concreteHelpPage.parameters[index][1] = param.getParameterClass().getName();
                            concreteHelpPage.parameters[index][2] = param.getDescription();
                            concreteHelpPage.parameters[index][3] = param.getLongDescription();
                            concreteHelpPage.parameters[index][4] = String.valueOf(param.getRestrictions().getMinCount());
                            concreteHelpPage.parameters[index][5] = String.valueOf(param.getRestrictions().getMaxCount());
                            index++;
                        }
                    }
                    concreteHelpPage.setParent(interfaceHelpPage);
                    interfaceHelpPage.add(concreteHelpPage);
                }
                this.add(interfaceHelpPage);
            }
        }
    }

    protected static void traverseFileSystemInternal(File root, int depth) {
        // build the entire tree here

        // Construct a list of all factories
        FactoryFactory base = FactoryFactory.newInstance();
        for (String interfaceName : base.getKnownTypes()) {
            AbstractFactory type = base.create(interfaceName);

        }
    }


    protected String getHTMLString(Locale l) {
        StringBuffer buffer = new StringBuffer();

        return "";
    }

    protected Document getHTMLDocument(Locale l) {
        HTMLDocument doc = new HTMLDocument();
        try {
            doc.insertString(0,getHTMLString(l),null);
        } catch (BadLocationException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"INTERNAL ERROR: offset of 0 regsitered a bad location exception");
        }
        return doc;
    }

    protected String getText(Locale l) {
        StringBuffer buffer = new StringBuffer();
        return "";
    }

}
