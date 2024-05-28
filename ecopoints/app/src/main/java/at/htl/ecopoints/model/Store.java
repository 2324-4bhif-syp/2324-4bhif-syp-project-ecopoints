package at.htl.ecopoints.model;


import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.ecopoints.util.store.StoreBase;
@Singleton
public class Store extends StoreBase<Model> {
    @Inject
    public Store() {
        super(Model.class, new Model());
    }
}
