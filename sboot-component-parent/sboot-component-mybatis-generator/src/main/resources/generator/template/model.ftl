<#-- package -->
package ${modelPackage};

<#-- imports -->
<#list modelImportTypes as importType>
import ${importType};
</#list>
import lombok.Data;

/**
 * ${tableName} ${remarks!}
 */
@Data
public class ${modelName} extends ${modelExtends} {

    <#-- getId -->
    @Override
    public String getId() {
        return this.${primaryKey.property};
    }

    @Override
    public void setId(String id) {
        this.${primaryKey.property} = id;
    }

<#-- fields -->
<#list columns as column>
    <#if column.remarks?length gt 1>/** ${column.remarks} */</#if>
    private ${column.javaType.simpleName} ${column.property};
</#list>
}