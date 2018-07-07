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

    protected String listID;

    protected FileNode parentInterface;

    protected boolean isInterface = false;

    protected String description;

    protected String longDescription;

    protected String[] methods;

    protected String[] returnTypes;

    protected String[][] methodParameters;

    protected String[][] parameters;

    public AutoDoc(File root) {
        super(root);
    }

    @Override
    public void setPage(org.multihelp.HelpViewer viewer) {
        viewer.set(getHTMLString(Locale.getDefault()));
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


                AutoDoc interfaceHelpPage = new AutoDoc(root);
                interfaceHelpPage.isInterface=true;
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
                    AutoDoc file = new AutoDoc(root);
                    file.isInterface=false;
                    file.parent = interfaceHelpPage;
                    AutoDoc concreteHelpPage = new AutoDoc(root);
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

    protected String getHTMLStringInterface(Locale l){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        buffer.append("<h1>Interface ").append(listID).append("</h1>");

        // interface
        buffer.append("<p>").append(longDescription);

        buffer.append("<h3>Global Parameters</h3>");
        buffer.append("<ul>");
        for(int interfaceParameterIndex=0;interfaceParameterIndex<parameters.length;interfaceParameterIndex++){
            buffer.append("<li>").append("<h4>").append(parameters[interfaceParameterIndex][0]).append("</h4>");
            buffer.append("<p>").append(parameters[interfaceParameterIndex][1]);
            buffer.append("<p>Must have between ").append(parameters[0][2]).append(" and ").append(parameters[0][3]).append(" number of values");
        }
        buffer.append("</ul>");

        buffer.append("<h3>Methods</h3>");
        buffer.append("<ul>");
        for(int interfaceMethodIndex=0;interfaceMethodIndex<parameters.length;interfaceMethodIndex++){
            buffer.append("<li>").append(returnTypes[interfaceMethodIndex]).append(" ").append(methods[interfaceMethodIndex]).append("(");
            boolean first=true;
            for(int parameterIndex=0;parameterIndex<parameters[interfaceMethodIndex].length;parameterIndex++){
                if(first){
                    first = false;
                }else{
                    buffer.append(", ");
                }
                buffer.append(methodParameters[interfaceMethodIndex][parameterIndex]);
            }
            buffer.append(")");
            buffer.append("</li>");
        }
        buffer.append("</ul>");

        buffer.append("</body></html>");
        return buffer.toString();
    }

    protected String getHTMLStringClass(Locale l){
        AutoDoc document = (AutoDoc)getParent();
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        buffer.append("<h1>Interface ").append(document.listID).append("</h1>");
        buffer.append("<h2>Class ").append(listID).append("</h2>");

        // interface
        buffer.append("<a href=\"../").append(document.listID).append("\"/>");
        buffer.append("<p>").append(document.description);
        buffer.append("<p>").append(longDescription);

        buffer.append("<h3>Interface Global Parameters</h3>");
        buffer.append("<ul>");
        for(int interfaceParameterIndex=0;interfaceParameterIndex<document.parameters.length;interfaceParameterIndex++){
            buffer.append("<li>").append("<h4>").append(document.parameters[interfaceParameterIndex][0]).append("</h4>");
            buffer.append("<p>").append(document.parameters[interfaceParameterIndex][1]);
            buffer.append("<p>Must have between ").append(document.parameters[0][2]).append(" and ").append(document.parameters[0][3]).append(" number of values");
        }
        buffer.append("</ul>");

        buffer.append("<h3>Class Specific Parameters</h3>");
        buffer.append("<ul>");
        for(int classParameterIndex=0;classParameterIndex<document.parameters.length;classParameterIndex++){
            buffer.append("<li>").append("<h4>").append(document.parameters[classParameterIndex][0]).append("</h4>");
            buffer.append("<p>").append(document.parameters[classParameterIndex][1]);
            buffer.append("<p>Must haave between ").append(document.parameters[0][2]).append(" and ").append(document.parameters[0][3]).append(" number of values");
        }
        buffer.append("</ul>");

        buffer.append("<h3>Interface Global Methods</h3>");
        buffer.append("<ul>");
        for(int interfaceMethodIndex=0;interfaceMethodIndex<document.parameters.length;interfaceMethodIndex++){
            buffer.append("<li>").append(document.returnTypes[interfaceMethodIndex]).append(" ").append(document.methods[interfaceMethodIndex]).append("(");
            boolean first=true;
            for(int parameterIndex=0;parameterIndex<document.parameters[interfaceMethodIndex].length;parameterIndex++){
                if(first){
                    first = false;
                }else{
                    buffer.append(", ");
                }
                buffer.append(document.methodParameters[interfaceMethodIndex][parameterIndex]);
            }
            buffer.append(")");
            buffer.append("</li>");
        }
        buffer.append("</ul>");

        buffer.append("<h3>Class Specific Methods</h3>");
        buffer.append("<ul>");
        for(int classMethodIndex=0;classMethodIndex<document.parameters.length;classMethodIndex++){
            buffer.append("<li>").append(document.returnTypes[classMethodIndex]).append(" ").append(document.methods[classMethodIndex]).append("(");
            boolean first=true;
            for(int parameterIndex=0;parameterIndex<document.parameters[classMethodIndex].length;parameterIndex++){
                if(first){
                    first = false;
                }else{
                    buffer.append(", ");
                }
                buffer.append(document.methodParameters[classMethodIndex][parameterIndex]);
            }
            buffer.append(")");
            buffer.append("</li>");
        }
        buffer.append("</ul>");

        buffer.append("</body></html>");
        return buffer.toString();
    }

    protected String getHTMLString(Locale l) {
        if(isInterface){
            return getHTMLStringInterface(l);
        }else{
            return getHTMLStringClass(l);
        }
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

    //TODO: implement command line export version
    protected String getText(Locale l) {
        StringBuffer buffer = new StringBuffer();
        return "";
    }

}
