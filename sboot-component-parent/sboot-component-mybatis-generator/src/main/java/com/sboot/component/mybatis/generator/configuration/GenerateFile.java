package com.sboot.component.mybatis.generator.configuration;

import java.io.File;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
public abstract class GenerateFile {

    /**
     * 输出文件
     */
    private File outputDirectory;
    /**
     * 模板
     */
    private String template;
    /**
     * 是否覆盖
     */
    private boolean override;
    /**
     * 前缀
     */
    private String prefix;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 文件扩展名
     */
    private String extension;

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
