package com.bizcreator.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldInfo {
	// 名称
    String name() default "";
    // 描述
    String description() default "";
   
    //引用代码, 如为CodeName字段, 则为lovId, 如为entity 字段, 则为qe class name
    String refCode() default "";
    
    //是否pk
    boolean isPk() default false;
    //是否搜索
    boolean isSearch() default false;
    //是否在搜索控件中显示该字段的值
    boolean isDisplay() default false;
    //是否表格列
    boolean isColumn() default true;
    //对于实体字段，是否需要将其字段在表格中展开
    boolean expandable() default false;
    //显示宽度
    int width() default 100;
    
}
