package alex.core;

import org.reactivestreams.Subscriber;

public interface Publisher<T> {
    void subscribe(Subscriber<? super T> var1);
}
