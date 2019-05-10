package com.sboot.component.mybatis.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public class CodeGenerator {

    private Configuration templateConfiguration;

    public static CodeGenerator create(Configuration configuration){
        return new CodeGenerator(configuration);
    }

    public CodeGenerator(Configuration configuration){
        this.templateConfiguration = configuration;
    }

    public void generator(Object dataModel, String template, File output){
        Template ftl = this.getTemplate(template);
        if(output.exists()){
            output.delete();
        }else{
            if(!output.getParentFile().exists()){
                output.getParentFile().mkdirs();
            }
        }
        this.process(ftl, dataModel, output);

    }

    private void process(Template ftl, Object dataModel, File output){
        try {
            FileWriter out = new FileWriter(output);
            ftl.process(dataModel, out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private Template getTemplate(String name) {
        try {
            return this.templateConfiguration.getTemplate(name);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

}
