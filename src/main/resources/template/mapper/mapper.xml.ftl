<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.aomygod.mapper.${packageName}.${entityName}Mapper">
    <#assign PKType = "java.lang.Long"/>
    <#assign PKJdbcType = "BIGINT"/>
    <#assign PKName = "id"/>
    <#assign PKPropertyName = "id"/>
    <resultMap id="BaseResultMap" type="com.aomygod.${packageName}.pojo.${entityName}" >
        <#list attrs as a>
        <#if a.columnKey == "PRI">
        <#assign PKType = "${a.javaFullType}"/>
        <#assign PKJdbcType = "${a.dataType}"/>
        <id column="${a.columnName}" property="${a.name}" jdbcType="${a.dataType}" />
        <#assign PKName = "${a.columnName}"/>
        <#assign PKPropertyName = "${a.name}"/>
        <#else>
        <result column="${a.columnName}" property="${a.name}" jdbcType="${a.dataType}" />
        </#if>
        </#list>
    </resultMap>

    <sql id="BaseListColumn" >
        ${listColumn}
    </sql>
    <sql id="Base_Where_Sql">
        <where>
            <foreach collection="params.criteriaList" item="criteria"
                separator="or">
                <if test="criteria.valid">
                    <trim prefix="(" suffix=")" prefixOverrides="and">
                        <foreach collection="criteria.criterions" item="criterion">
                            <choose>
                                <when test="criterion.noValue">
                                    and ${"$"}{criterion.condition}
                                </when>
                                <when test="criterion.singleValue">
                                    and ${"$"}{criterion.condition} ${"#"}{criterion.value}
                                </when>
                                <when test="criterion.betweenValue">
                                    and ${"$"}{criterion.condition} ${"#"}{criterion.value} and
                                    ${"#"}{criterion.secondValue}
                                </when>
                                <when test="criterion.listValue">
                                    and ${"$"}{criterion.condition}
                                    <foreach collection="criterion.value" item="listItem"
                                        open="(" close=")" separator=",">
                                        ${"#"}{listItem}
                                    </foreach>
                                </when>
                            </choose>
                        </foreach>
                    </trim>
                </if>
            </foreach>
        </where>
    </sql>
    <select id="getById" resultMap="BaseResultMap" parameterType="${PKType}" >
        select
        <include refid="BaseListColumn" />
        from ${tableName}
        where del_flag = 0 and ${PKName} = ${"#"}{id, jdbcType=${PKJdbcType}}
    </select>

    <insert id="insert" parameterType="com.aomygod.${packageName}.pojo.${entityName}">
        <selectKey resultType="${PKType}" keyProperty="${PKPropertyName}" order="AFTER" >
                SELECT LAST_INSERT_ID()
        </selectKey>
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides="," >
            <#list attrs as a>
            <if test="${a.name} != null" >
                ${a.columnName},
            </if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides="," >
            <#list attrs as a>
            <if test="${a.name} != null" >
                ${"#"}{${a.name}, jdbcType=${a.dataType}},
            </if>
            </#list>
        </trim>
    </insert>

    <update id="update" parameterType="com.aomygod.${packageName}.pojo.${entityName}" >
        update ${tableName}
        set
        <trim suffixOverrides=",">
            <#list attrs as a>
            <if test="${a.name} != null" >
                ${a.columnName} = ${"#"}{${a.name}, jdbcType=${a.dataType}},
            </if>
            </#list>
        </trim>
        where ${PKName} = ${"#"}{${PKPropertyName},jdbcType=${PKJdbcType}}
    </update>
    
    <update id="updateByWhere" parameterType="java.util.Map" >
        update ${tableName}
        set
        <trim suffixOverrides=",">
            <#list attrs as a>
            <if test="entity.${a.name} != null" >
                ${a.columnName} = ${"#"}{entity.${a.name}, jdbcType=${a.dataType}},
            </if>
            </#list>
        </trim>
        <if test="_parameter != null">
            <include refid="Base_Where_Sql" />
        </if>
    </update>
    
    <select id="count" resultType="java.lang.Integer" parameterType="map" >
        select
            count(1)
        from ${tableName}
        <if test="_parameter != null">
            <include refid="Base_Where_Sql" />
        </if>
    </select>
    
    <select id="listByWhere" resultMap="BaseResultMap" parameterType="map" >
        select
        <include refid="BaseListColumn" />
        from ${tableName}
        <if test="_parameter != null">
            <include refid="Base_Where_Sql" />
        </if>
    </select>
        
    <select id="listByWherePage" resultType="map" parameterType="map" >
        select
        <include refid="BaseListColumn" />
        from ${tableName}
        <if test="_parameter != null">
            <include refid="Base_Where_Sql" />
        </if>
    </select>

    <update id="deleteById" parameterType="${PKType}" >
        update ${tableName} set del_flag = 1
        where ${PKName} = ${"#"}{id,jdbcType=${PKJdbcType}}
    </update>

    <update id="deleteByWhere" parameterType="map">
        update ${tableName} set del_flag = 1
        <if test="_parameter != null">
            <include refid="Base_Where_Sql" />
        </if>
    </update>
</mapper>
