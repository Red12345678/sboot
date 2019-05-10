package com.sboot.component.mybatis.generator.configuration;

/**
 * @author tuozq
 * @description: Java文件配置
 * @date 2019/5/9.
 */
public class GenerateJavaFile extends GenerateFile {

    @Override
    public String getExtension() {
        return ".java";
    }

    /**
     * 包名
     */
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


}
