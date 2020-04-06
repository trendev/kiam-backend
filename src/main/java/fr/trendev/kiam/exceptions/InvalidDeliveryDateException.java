/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.exceptions;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public class InvalidDeliveryDateException extends ClientErrorException {

    public InvalidDeliveryDateException(Response.Status status) {
        super(status);
    }

    public InvalidDeliveryDateException(String message, Response.Status status) {
        super(message, status);
    }

    public InvalidDeliveryDateException(int status) {
        super(status);
    }

    public InvalidDeliveryDateException(String message, int status) {
        super(message, status);
    }

    public InvalidDeliveryDateException(Response response) {
        super(response);
    }

    public InvalidDeliveryDateException(String message, Response response) {
        super(message, response);
    }

    public InvalidDeliveryDateException(Response.Status status, Throwable cause) {
        super(status, cause);
    }

    public InvalidDeliveryDateException(String message, Response.Status status,
            Throwable cause) {
        super(message, status, cause);
    }

    public InvalidDeliveryDateException(int status, Throwable cause) {
        super(status, cause);
    }

    public InvalidDeliveryDateException(String message, int status,
            Throwable cause) {
        super(message, status, cause);
    }

    public InvalidDeliveryDateException(Response response, Throwable cause) {
        super(response, cause);
    }

    public InvalidDeliveryDateException(String message, Response response,
            Throwable cause) {
        super(message, response, cause);
    }

}
