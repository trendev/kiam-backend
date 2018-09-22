/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import java.util.function.Function;

/**
 *
 * @author jsie
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        try {
            return applyThrows(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T t) throws Exception;
}
