package com.zestedesavoir.zdsnotificateur.notifications.queries;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Gerard Paligot
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ListNotification {
}
