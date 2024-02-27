package at.htl.ecopoints.model;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class Store {

    public final BehaviorSubject<CarData> subject;
    public final ModelMapper<CarData> mapper;

    @Inject
    public Store() {
        subject = BehaviorSubject.createDefault(new CarData());
        mapper = new ModelMapper<>(CarData.class);
    }

    public void next(Consumer<CarData> recipe) {
        CarData model = mapper.clone(subject.getValue());
        recipe.accept(model);
        subject.onNext(model);
    }
}
