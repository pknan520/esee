package com.kelin.mvvmlight.command;

import java.util.concurrent.Callable;

import io.reactivex.functions.Function;

/**
 * Created by kelin on 15-8-4.
 */
public class ResponseCommand<T, R> {

    private Callable<R> execute0;
    private Function<T, R> execute1;

    private Callable<Boolean> canExecute0;

    /**
     * like {@link ReplyCommand},but ResponseCommand can return result when command has executed!
     * @param execute function to execute when event occur.
     */
    public ResponseCommand(Callable<R> execute) {
        this.execute0 = execute;
    }


    public ResponseCommand(Function<T, R> execute) {
        this.execute1 = execute;
    }


    public ResponseCommand(Callable<R> execute, Callable<Boolean> canExecute0) {
        this.execute0 = execute;
        this.canExecute0 = canExecute0;
    }


    public ResponseCommand(Function<T, R> execute, Callable<Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }


    public R execute() throws Exception {
        if (execute0 != null && canExecute0()) {
            return execute0.call();
        }
        return null;
    }

    private boolean canExecute0() throws Exception {
        if (canExecute0 == null) {
            return true;
        }
        return canExecute0.call();
    }


    public R execute(T parameter) throws Exception {
        if (execute1 != null && canExecute0()) {
            return execute1.apply(parameter);
        }
        return null;
    }

}
