<#-- package -->
package ${repositoryPackage};

<#-- imports -->
<#list repositoryImportTypes as importType>
import ${importType};
</#list>
import org.springframework.stereotype.Repository;

@Repository
public interface ${repositoryName} extends ${repositoryExtends} {


}