package org.mybatis.generator.codegen.mybatis3.javaservice.xmlparser;

public enum ParamConditionEnum {

    /**
     * 1
     */
    IsNull(1, 0),

    /**
     * 2
     */
    IsNotNull(2, 0),

    /**
     * 3
     */
    EqualTo(3, 1),

    /**
     * 4
     */
    NotEqualTo(4, 1),

    /**
     * 5
     */
    GreaterThan(5, 1),

    /**
     * 6
     */
    GreaterThanOrEqualTo(6, 1),

    /**
     * 7
     */
    LessThan(7, 1),

    /**
     * 8
     */
    LessThanOrEqualTo(8, 1),

    /**
     * 9
     */
    In(9, 1),

    /**
     * 10
     */
    NotIn(10, 1),

    /**
     * 11
     */
    Between(11, 2),

    /**
     * 12
     */
    NotBetween(12, 2),
    /**
     * 13
     */
    Like(13, 1),
    /**
     * 14
     */
    NotLike(14, 1);

    private Integer code;

    /**
     * 参数个数
     */
    private Integer inputCount;

    public Integer getCode() {
        return code;
    }

    public Integer getInputCount() {
        return inputCount;
    }

    private ParamConditionEnum(Integer code, Integer inputCount) {
        this.code = code;
        this.inputCount = inputCount;
    }

    public  static ParamConditionEnum getByEnumName(String enumName) {
        ParamConditionEnum[] enums=ParamConditionEnum.class.getEnumConstants();
        for(ParamConditionEnum enumi:enums){
            if(enumi.name().equalsIgnoreCase(enumName)){
                return enumi;
            }
        }
        return null;
    }
    

}
