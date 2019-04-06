/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import java.util.function.BiFunction;

/**
 *
 * @author jsie
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R> {

    @Override
    public default R apply(T t, U u) {
        try {
            return applyThrows(t, u);
        } catch (FirestoreProxyException ex) {
            throw new RuntimeException(ex);
        }
    }

    public R applyThrows(T t, U u) throws FirestoreProxyException;

}
