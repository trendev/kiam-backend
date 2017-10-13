package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserAccount_;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
@Named("userAccount")
public class UserAccountFacade extends AbstractFacade<UserAccount, String> {

    @Inject
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(UserAccountFacade.class.
            getName());

    public UserAccountFacade() {
        super(UserAccount.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(String pk) {
        return pk;
    }

    public String getUserAccountType(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<UserAccount> root = cq.from(UserAccount.class);
        cq.select(root.get(UserAccount_.cltype)).where(cb.equal(root.get(
                UserAccount_.email), email));

        return Optional.ofNullable(em.createQuery(cq).getSingleResult())
                .map(Function.identity())
                .orElseGet(() -> {
                    LOG.log(Level.WARNING,
                            "Cannot find the type of the UserAccount with email [{0}]",
                            email);
                    return "";
                });
    }

}
