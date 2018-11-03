/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.objectmapper.backupcfg.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.category.entities.Category;
import fr.trendev.comptandye.client.entities.Client;
import fr.trendev.comptandye.collectivegroup.entities.CollectiveGroup;
import fr.trendev.comptandye.expense.entities.Expense;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.notification.entities.Notification;
import fr.trendev.comptandye.offering.entities.Offering;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import java.util.List;

public abstract class ProfessionalMixin {

    @JsonProperty
    public abstract List<Bill> getBills();

    @JsonProperty
    public abstract List<Individual> getIndividuals();

    @JsonProperty
    public abstract List<Notification> getNotifications();

    @JsonProperty
    public abstract List<Product> getStock();

    @JsonProperty
    public abstract List<CollectiveGroup> getCollectiveGroups();

    @JsonProperty
    public abstract List<Expense> getExpenses();

    @JsonProperty
    public abstract List<Category> getCategories();

    @JsonProperty
    public abstract List<Offering> getOfferings();

    @JsonProperty
    public abstract List<Client> getClients();

    @JsonProperty
    public abstract List<UserGroup> getUserGroups();

}
