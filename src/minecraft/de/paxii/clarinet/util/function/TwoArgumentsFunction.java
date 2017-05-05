package de.paxii.clarinet.util.function;

@FunctionalInterface
public interface TwoArgumentsFunction<R, T, A> {

  /**
   * Applies this function to the given arguments.
   *
   * @param value the first function argument
   * @param otherValue the second function argument
   * @return the function result
   */
  R apply(T value, A otherValue);
}
