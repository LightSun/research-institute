package com.vida.common;

/**
 * the repeat task helper.
 *
 * @param <R> the result type.
 */
public class RepeatTaskHelper<R> {

    private final TaskExecutor<R> executor;
    private final int repeatCount;

    public RepeatTaskHelper(TaskExecutor<R> executor) {
        this(executor, 1);
    }

    public RepeatTaskHelper(TaskExecutor<R> executor, int repeatCount) {
        this.executor = executor;
        this.repeatCount = repeatCount;
    }

    /**
     * execute the task until the task success or repeat count reached
     *
     * @param params the params
     * @return the result
     * @throws Throwable of task execute failed after target repeat count.
     */
    public R execute(Object... params) throws Throwable {
        final TaskExecutor2<R> exe2 = executor instanceof TaskExecutor2 ? (TaskExecutor2) executor : null;
        boolean[] state = new boolean[1];
        R result;
        int left = repeatCount;
        for (; ; ) {
            try {
                if (exe2 != null) {
                    result = exe2.execute(state, params);
                    if (state[0]) {
                        break;
                    } else {
                        left--;
                        if (left < 0) {
                            break;
                        }
                    }
                } else {
                    result = executor.execute(params);
                    break;
                }
            } catch (Throwable t) {
                left--;
                if (left < 0) {
                    throw t;
                }
            }
        }
        return result;
    }

    public interface TaskExecutor<R> {
        R execute(Object... params) throws Throwable;
    }

    public static abstract class TaskExecutor2<R> implements TaskExecutor<R> {

        public abstract R execute(boolean[] resultState, Object... params) throws Throwable;

        @Override
        public R execute(Object... params) throws Throwable {
            return execute(null, params);
        }
    }

}
