// Create new file: src/main/java/com/buizzment/annotation/CurrentUser.java
package com.buizzment.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this")
public @interface CurrentUser {}