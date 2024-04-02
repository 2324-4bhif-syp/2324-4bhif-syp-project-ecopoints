package at.htl.ecopoints.model;


import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class Store {
    ModelMapper<Model> mapper;
    public BehaviorSubject<Model> subject;

    @Inject
    public Store() {
        subject = BehaviorSubject.createDefault(new Model());
        mapper = new ModelMapper(Model.class);
    }

    public void next(Consumer<Model> recipe) {
        var model = mapper.clone(subject.getValue());
        recipe.accept(model);
        subject.onNext(model);
    }
}