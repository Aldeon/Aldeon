package org.aldeon.events;

import java.util.concurrent.Executor;

public interface AsyncCallback<T> extends Callback<T>{
    Executor getExecutor();
}
