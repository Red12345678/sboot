<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${repositoryPackage}.${repositoryName}">
    <resultMap id="BaseResultMap" type="${modelPackage}.${modelName}">
        <id column="${primaryKey.name}" jdbcType="${primaryKey.jdbcType}" property="${primaryKey.property}"/>
        <#list columnsWithoutPrimaryKey as column>
        <result column="${column.name}" jdbcType="${column.jdbcType}" property="${column.property}"/>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        ${columnNames}
    </sql>

    <insert id="insert">
        <selectKey keyProperty="${primaryKey.property}" order="BEFORE"  resultType="${primaryKey.javaType.name}">
            select SEQ_${tableName}.nextval from dual
        </selectKey>
        insert into ${tableName} (${columnNames})
        values (<#list columns as column>${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if></#list>)
    </insert>

    <insert id="insertSelective">
        <selectKey keyProperty="${primaryKey.property}" order="BEFORE"  resultType="${primaryKey.javaType.name}">
            select SEQ_${tableName}.nextval from dual
        </selectKey>
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            ${primaryKey.name},
            <#list columnsWithoutPrimaryKey as column>
            <if test="${column.property} != null">
                ${column.name}<#if column_has_next>,</#if>
            </if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}},
            <#list columnsWithoutPrimaryKey as column>
            <if test="${column.property} != null">
                ${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
            </#list>
        </trim>
    </insert>

    <delete id="deleteByPrimaryKey">
        delete from ${tableName}
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property}}
    </delete>

    <delete id="deleteByPrimaryKeys">
        delete from ${tableName} where ${primaryKey.name} in
        <foreach collection="list" item="item" open="(" close=")" separator=",">
        ${'#\{'}item}
        </foreach>
    </delete>

    <update id="updateByPrimaryKey">
        update ${tableName}
        set <#list columnsWithoutPrimaryKey as column>${column.name}=${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if></#list>
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
    </update>

    <update id="updateByPrimaryKeySelective">
        update ${tableName}
        <set>
        <#list columnsWithoutPrimaryKey as column>
            <if test="${column.property} != null">
                ${column.name}=${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
        </#list>
        </set>
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
    </update>

    <select id="selectByPrimaryKey" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
    </select>

    <select id="selectByPrimaryKeys" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName} where ${primaryKey.name} in
        <foreach collection="list" item="item" open="(" close=")" separator=",">
        ${'#\{'}item}
        </foreach>
    </select>

    <select id="selectAll" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
    </select>

    <select id="selectByEntity" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        <where>
        <#list columnsWithoutPrimaryKey as column>
            <if test="${column.property} != null">
                and ${column.name}=${'#\{'}${column.property},jdbcType=${column.jdbcType}}
            </if>
        </#list>
        </where>
    </select>

    <select id="selectByConditions" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        <where>
            <#list columnsWithoutPrimaryKey as column>
            <if test="${column.property} != null">
                and ${column.name} ${'$\{'}${column.property}}
            </if>
            </#list>
        </where>
        <if test="orderBy != null">
            order by ${'$\{'}orderBy}
        </if>
    </select>

    <insert id="insertBatch">
        insert into ${tableName} (${columnNames})
        (
        <foreach collection="list" item="item" index="index" separator="union all">
            select
            <#list columns as column>${'#\{'}item.${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if></#list>
            from dual
        </foreach>
        )
    </insert>

    <update id="updateBatch">
        <foreach collection="list" item="item" index="index" open="begin" close=";end;" separator=";">
            update ${tableName}
            <set>
            <#list columnsWithoutPrimaryKey as column>
            <if test="item.${column.property} != null">
                ${column.name} = ${'#\{'}item.${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
            </#list>
            </set>
            where ${primaryKey.name} = ${'#\{'}item.${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
        </foreach>
    </update>

    <select id="count" resultType="java.lang.Long">
        select count(*) from ${tableName}
    </select>

    <#if repositoryExtendType.simpleName == "TreeableRepository">
    <!-- ####################### tree-start ##################### -->
    <#assign nodeParentIdColumn="ext_node_parent_id">
    <#assign nodeParentIdProperty="extNodeParentId">
    <#assign nodePathColumn="ext_node_path">
    <#assign nodePathProperty="extNodePath">
    <#assign nodeSortColumn="ext_node_sort">
    <#assign nodeSortProperty="extNodeSort">
    <update id="exchange">
        <if test="source.${primaryKey.property} != target.${primaryKey.property} and source.${nodeParentIdProperty} == target.${nodeParentIdProperty}">
            begin
            update ${tableName} set ${nodeSortColumn} = ${'#\{target.'}${nodeSortProperty}} where ${primaryKey.name} = ${'#\{source.'}${primaryKey.property}};
            update ${tableName} set ${nodeSortColumn} = ${'#\{source.'}${nodeSortProperty}} where ${primaryKey.name} = ${'#\{target.'}${primaryKey.property}};
            end;
        </if>
    </update>

    <update id="move">
        <if test="from.${primaryKey.property} != to.${primaryKey.property} and from.${nodeParentIdProperty} == to.${nodeParentIdProperty}">
            begin
            <if test="from.${nodeSortProperty} > to.${nodeSortProperty}">
                update ${tableName} SET ${nodeSortColumn} = ${nodeSortColumn} + 1
                where ${nodeSortColumn} >= ${'#\{to.'}${nodeSortProperty}}
                and ${nodeParentIdColumn} = ${'#\{from.'}${nodeParentIdProperty}}
                and ${nodeSortColumn} <![CDATA[ < ]]> ${'#\{from.'}${nodeSortProperty}};
                update ${tableName} set ${nodeSortColumn} = ${'#\{to.'}${nodeSortProperty}} where ${primaryKey.name} = ${'#\{from.'}${primaryKey.property}};
            </if>
            <if test="to.${nodeSortProperty} > from.${nodeSortProperty}">
                update ${tableName} set ${nodeSortColumn} = ${nodeSortColumn} - 1
                where ${nodeSortColumn} > ${'#\{from.'}${nodeSortProperty}}
                and ${nodeParentIdColumn} = ${'#\{from.'}${nodeParentIdProperty}}
                and ${nodeSortColumn} <![CDATA[ <= ]]> ${'#\{to.'}${nodeSortProperty}};
                update ${tableName} set ${nodeSortColumn} = ${'#\{to.'}${nodeSortProperty}} where ${primaryKey.name} = ${'#\{from.'}${primaryKey.property}};
            </if>
            end;
        </if>
    </update>
    
    <select id="selectPreviousSibling" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
        and ${nodeSortColumn} = (
            select max(${nodeSortColumn}) from ${tableName}
            where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
            and ${nodeSortColumn} <![CDATA[ < ]]> ${'#\{'}${nodeSortProperty},jdbcType=DECIMAL}
        )
        and ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
    </select>
    
    <select id="selectNextSibling" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
        and ${nodeSortColumn} = (
            select min(${nodeSortColumn}) from ${tableName}
            where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
            and ${nodeSortColumn} > ${'#\{'}${nodeSortProperty},jdbcType=DECIMAL}
        )
        and ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
    </select>
    
    <select id="selectFirstSibling" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
        and ${nodeSortColumn} = (
            select min(${nodeSortColumn}) from ${tableName}
            where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
        )
        and ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
    </select>
    
    <select id="selectLastSibling" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
        and ${nodeSortColumn} = (
            select max(${nodeSortColumn}) from ${tableName}
            where ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
        )
        and ${nodeParentIdColumn} = ${'#\{'}${nodeParentIdProperty},jdbcType=${primaryKey.jdbcType}}
    </select>

    <select id="selectParents" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List" />
        from ${tableName}
        where ${primaryKey.name} in (
            select distinct ${nodeParentIdColumn} from ${tableName}
        )
        order by ${nodeParentIdColumn}, ${nodeSortColumn}
    </select>

    <select id="selectChildren" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List" />
        from ${tableName}
        <where>
            <choose>
                <when test="deep">
                ${nodePathColumn} like '${'$\{target.'}${nodePathProperty}}%'
                    and ${primaryKey.name} != ${'#\{target.'}${primaryKey.property}}
                </when>
                <otherwise>
                ${nodeParentIdColumn} = ${'#\{target.'}${primaryKey.property}}
                </otherwise>
            </choose>
        </where>
        order by ${nodeParentIdColumn}, ${nodeSortColumn}
    </select>

    <select id="selectChildrenByConditions" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        <where>
        ${nodeParentIdColumn} = ${'#\{target.'}${primaryKey.property}}
            <#list columnsWithoutPrimaryKey as column>
                <#if (column.name != nodeParentIdColumn)>
             <if test="params.${column.property} != null">
                 and ${column.name} ${'$\{params.'}${column.property}}
             </if>
                </#if>
            </#list>
        </where>
        order by ${nodeParentIdColumn}, ${nodeSortColumn}
    </select>

    <select id="selectRoot" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${nodeParentIdColumn} is null
    </select>

    <update id="updateChildrenPath">
        update ${tableName}
        set ${nodePathColumn} = concat(${'#\{target.'}${nodePathProperty}},substr(${nodePathColumn},length(${'#\{'}oldPath}) + 1, length(${nodePathColumn})))
        where ${nodeParentIdColumn} = ${'#\{target.'}${primaryKey.property}}
    </update>
    <!-- ####################### tree-end ##################### -->
    </#if>
</mapper>

