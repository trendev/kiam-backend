/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

/**
 *
 * @author jsie
 */
public enum BillTypeEnum {
    INDIVIDUAL, CLIENT, COLLECTIVEGROUP;

    public static final String INDIVIDUAL_PREFIX = "IX";
    public static final String CLIENT_PREFIX = "CX";
    public static final String COLLECTIVEGROUP_PREFIX = "CG";

    public static final String INDIVIDUAL_CLTYPE = "individualbill";
    public static final String CLIENT_CLTYPE = "clientbill";
    public static final String COLLECTIVEGROUP_CLTYPE = "collectivegroupbill";
}
