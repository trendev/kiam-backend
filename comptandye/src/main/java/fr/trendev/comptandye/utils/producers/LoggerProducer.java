package fr.trendev.comptandye.utils.producers;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class LoggerProducer {

    /**
     * @param injectionPoint
     * @return logger
     */
    @Produces
    public Logger producer(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().
                getName());
    }

}
