package com.zestedesavoir.zdsnotificateur.member.queries;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by gerard on 18/10/2015.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface MyProfile {
}
