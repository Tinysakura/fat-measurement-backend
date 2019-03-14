package com.cfh.fatmeasurementsbackend.constant;

/**
 * 动物相关常量设置
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public class AnimalConstant {

    /**
     * 动物品种枚举
     * 编号以1开头为猪的品种，2开头为牛的品种， 3开头为羊的品种
     */
    public enum AnimalVarietyEnum {
        /**
         * 大白猪
         */
        Large_White_PIG(101, "大白猪"),
        /**
         * 长白猪
         */
        Landrace_PIG(102, "长白猪"),
        /**
         * 杜洛克猪
         */
        Duroc_PIF(103, "杜洛克猪")
        ;

        Integer code;
        String variety;

        AnimalVarietyEnum(Integer code, String variety) {
            this.code = code;
            this.variety = variety;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getVariety() {
            return variety;
        }

        public void setVariety(String variety) {
            this.variety = variety;
        }
    }

    public enum AnimalSexEnum {
        /**
         * 公
         */
        MALE(0, "公"),

        /**
         * 母
         */
        FEMALE(1, "母"),;

        Integer code;
        String sex;

        AnimalSexEnum(Integer code, String sex) {
            this.code = code;
            this.sex = sex;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}